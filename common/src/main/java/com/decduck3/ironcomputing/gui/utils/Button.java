package com.decduck3.ironcomputing.gui.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class Button extends AbstractWidget {
    private final ResourceLocation DFT_RESOURCE;
    private final ResourceLocation HOVER_RESOURCE;
    private final ResourceLocation ON_RESOURCE;

    private int baseX = 0;
    private int baseY = 0;
    private boolean enabled = true;

    public Button(int x, int y, int width, int height, Component message, ResourceLocation dft, ResourceLocation hover, ResourceLocation on) {
        super(x, y, width, height, message);
        this.DFT_RESOURCE = dft;
        this.HOVER_RESOURCE = hover;
        this.ON_RESOURCE = on;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if(!enabled) {
            RenderSystem.setShaderTexture(0, DFT_RESOURCE);

            guiGraphics.blit(DFT_RESOURCE, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
            return;
        }

        if(isHovered()) {
            RenderSystem.setShaderTexture(0, HOVER_RESOURCE);

            guiGraphics.blit(HOVER_RESOURCE, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        } else {
            RenderSystem.setShaderTexture(0, ON_RESOURCE);

            guiGraphics.blit(ON_RESOURCE, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public int getX() {
        return super.getX() + this.baseX;
    }

    @Override
    public int getY() {
        return super.getY() + this.baseY;
    }

    public void setBaseX(int v) {
        this.baseX = v;
    }

    public void setBaseY(int v){
        this.baseY = v;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);

        if(isHovered() && this.enabled){
            click();
        }
    }

    public abstract void click();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
