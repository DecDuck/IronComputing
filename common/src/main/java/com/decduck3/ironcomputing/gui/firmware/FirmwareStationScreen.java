package com.decduck3.ironcomputing.gui.firmware;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.gui.utils.Button;
import com.decduck3.ironcomputing.network.IronComputingNetwork;
import com.decduck3.ironcomputing.network.ROMUpload;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FirmwareStationScreen extends AbstractContainerScreen<FirmwareStationMenu> {

    private static final ResourceLocation BACKGROUND = ResourceLocation.tryBuild(IronComputing.MOD_ID, "textures/gui/firmwarestation/firmwarestation.png");

    private final Button UPLOAD_BUTTON = new Button(98, 35, 16, 16, Component.literal("Upload firmware"), //.
            ResourceLocation.tryBuild(IronComputing.MOD_ID, "textures/gui/components/upload_waiting.png"), // default
            ResourceLocation.tryBuild(IronComputing.MOD_ID, "textures/gui/components/upload_hover.png"), // hover
            ResourceLocation.tryBuild(IronComputing.MOD_ID, "textures/gui/components/upload_ready.png") // on
    ) {
        @Override
        public void click() {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Select your ROM file", "bin", "rom");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(null);
            if(returnVal != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();
            byte[] contents;
            try {
                contents = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Failed to open file");
                dialog.setContentPane(new JTextArea(e.toString()));
                dialog.setVisible(true);
                return;
            }

            ROMUpload.uploadContent(contents);
        }
    };

    private int UI_X_OFFSET = 0;
    private int UI_Y_OFFSET = 0;

    public FirmwareStationScreen(FirmwareStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        addWidget(UPLOAD_BUTTON);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        guiGraphics.blit(BACKGROUND, this.UI_X_OFFSET, this.UI_Y_OFFSET, 0, 0, imageWidth, imageHeight, 256, 256);

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
        UPLOAD_BUTTON.setEnabled(!this.menu.getRom().isEmpty());
        UPLOAD_BUTTON.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();

        this.UI_X_OFFSET = (width - imageWidth) / 2;
        this.UI_Y_OFFSET = (height - imageHeight) / 2;
        UPLOAD_BUTTON.setBaseX(this.UI_X_OFFSET);
        UPLOAD_BUTTON.setBaseY(this.UI_Y_OFFSET);
    }
}
