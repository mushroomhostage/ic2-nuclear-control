package net.minecraft.src.nuclearcontrol;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.ic2.api.NetworkHelper;

import org.lwjgl.opengl.GL11;

public class GuiRemoteThermoSlider extends GuiButton
{
    public float sliderValue;
    public boolean dragging;
    private String label;
    private TileEntityRemoteThermo thermo;

    public GuiRemoteThermoSlider(int id, int x, int y, String label, TileEntityRemoteThermo thermo)
    {
        super(id, x, y, 107, 16, label);
        this.thermo = thermo;
        dragging = false;
        this.label = label;
        sliderValue = (thermo.getHeatLevel()-500)/14500F;
        displayString = String.format(label, getNormalizedHeatLevel());
    }
    
    private int getNormalizedHeatLevel()
    {
        return (500+(int)Math.floor(14500F * sliderValue))/500*500;
    }

    private void setSliderPos(int targetX)
    {
        sliderValue = (float) (targetX - (xPosition + 4)) / (float) (width - 8);
        if (sliderValue < 0.0F)
        {
            sliderValue = 0.0F;
        }
        if (sliderValue > 1.0F)
        {
            sliderValue = 1.0F;
        }
        int newHeatLevel = getNormalizedHeatLevel(); 
        if(thermo.getHeatLevel()!=newHeatLevel){
            thermo.setHeatLevel(newHeatLevel);
            NetworkHelper.initiateClientTileEntityEvent(thermo, newHeatLevel);
        }
        displayString = String.format(label, newHeatLevel);
    }
    
    @Override
    public void drawButton(Minecraft minecraft, int targetX, int targetY) {
        if (drawButton)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/img/GUIRemoteThermo.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (dragging)
            {
                setSliderPos(targetX);
            }
            drawTexturedModalRect(xPosition + (int)(sliderValue * (width-8)), yPosition, 176, 14, 8, 16);
            minecraft.fontRenderer.drawString(displayString, xPosition, yPosition - 12, 0x404040);
        }
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int targetX, int j)
    {
        if (super.mousePressed(minecraft, targetX, j))
        {
            setSliderPos(targetX);
            dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void mouseReleased(int i, int j)
    {
        super.mouseReleased(i, j);
        dragging = false;
    }
}
