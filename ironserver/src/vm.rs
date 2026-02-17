use tokio::io::stdout;
use wasmtime::{
    Config, Engine, ResourceLimiter, Store,
    component::{Component, Linker},
};
use wasmtime_wasi::{ResourceTable, WasiCtx, WasiCtxView, WasiView};

use crate::bindgen::Vm;

struct VMState {
    ctx: WasiCtx,
    table: ResourceTable,
    limiter: VMLimiter,
}

impl WasiView for VMState {
    fn ctx(&mut self) -> WasiCtxView<'_> {
        WasiCtxView {
            ctx: &mut self.ctx,
            table: &mut self.table,
        }
    }
}

struct VMLimiter;

impl ResourceLimiter for VMLimiter {
    fn memory_growing(
        &mut self,
        current: usize,
        desired: usize,
        maximum: Option<usize>,
    ) -> anyhow::Result<bool> {
        Ok(true)
    }

    fn table_growing(
        &mut self,
        current: usize,
        desired: usize,
        maximum: Option<usize>,
    ) -> anyhow::Result<bool> {
        Ok(true)
    }
}

async fn create_vm(
    value: Vec<u8>,
    engine: &Engine,
    linker: &mut Linker<VMState>,
) -> Result<(Vm, Store<VMState>), anyhow::Error> {
    let component = Component::from_binary(engine, &value)?;

    let mut store = Store::new(
        engine,
        VMState {
            ctx: WasiCtx::builder().stdout(stdout()).build(),
            table: ResourceTable::new(),
            limiter: VMLimiter {},
        },
    );
    store.limiter(|v| &mut v.limiter);

    let vm = Vm::instantiate_async(&mut store, &component, &linker).await?;
    Ok((vm, store))
}

async fn run_vm() -> Result<(), anyhow::Error> {
    let mut config = Config::new();
    config.async_support(true);
    config.wasm_component_model(true);
    config.wasm_component_model_async(true);
    let engine = Engine::new(&config)?;
    let mut linker: Linker<VMState> = Linker::new(&engine);
    wasmtime_wasi::p2::add_to_linker_async(&mut linker)?;

    let (vm, mut store) = create_vm(vec![0u8], &engine, &mut linker).await?;

    let main_result = store
        .run_concurrent(async |accessor| -> wasmtime::Result<_> {
            vm.call_main(accessor).await?;

            Ok(())
        })
        .await??;

    Ok(())
}
