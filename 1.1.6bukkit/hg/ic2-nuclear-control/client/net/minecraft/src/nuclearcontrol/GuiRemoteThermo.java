package net.minecraft.src.nuclearcontrol;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.StatCollector;

public class GuiRemoteThermo extends GuiContainer
{
    private ContainerRemoteThermo container;
    private GuiRemoteThermoSlider slider;
    private String name;

    public GuiRemoteThermo(Container container)
    {
        super(container);
        this.container = (ContainerRemoteThermo)container;
        name = StatCollector.translateToLocal("tile.blockRemoteThermo.name");
    }
    
    @Override
    public void initGui() {
        super.initGui();
        controlList.clear();
        slider = new GuiRemoteThermoSlider(3, guiLeft+23, guiTop + 33, "Signal at %s heat", container.remoteThermo);
        controlList.add(slider);
        
    };

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString(name, (xSize - fontRenderer.getStringWidth(name)) / 2, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int texture = mc.renderEngine.getTexture("/img/GUIRemoteThermo.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
        
        //Charge level progress bar
        int chargeWidth = (int)(76F * container.remoteThermo.energy)/container.remoteThermo.maxStorage;
        if(chargeWidth > 76)
        {
            chargeWidth = 76;
        }

        if (chargeWidth > 0)
        {
            drawTexturedModalRect(left + 52, top + 53, 176, 0, chargeWidth, 14);
        }
        
    }
    
    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int which)
    {
        super.mouseMovedOrUp(mouseX, mouseY, which);
        if((which == 0 || which == 1) && slider.dragging )
        {
            slider.mouseReleased(mouseX, mouseY);
        }
    }

}
