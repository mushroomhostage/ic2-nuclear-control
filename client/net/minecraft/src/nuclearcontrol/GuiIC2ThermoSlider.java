package net.minecraft.src.nuclearcontrol;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

import org.lwjgl.opengl.GL11;

public class GuiIC2ThermoSlider extends GuiButton
{
    public float sliderValue;
    public boolean dragging;
    private ArrayList<String> values;
    private String label;
    public int type;

    public GuiIC2ThermoSlider(int i, int j, int k, ArrayList<String> arraylist, String s, float f, int l)
    {
        super(i, j, k, 150, 20, s);
        sliderValue = 1.0F;
        dragging = false;
        values = arraylist;
        label = s;
        sliderValue = f;
        type = l;
        int i1 = values.size() - 1;
        displayString = String.format(label, new Object[]
                {
                    values.get((int)Math.floor((float)i1 * sliderValue))
                });
    }

    protected int getHoverState(boolean flag)
    {
        return 0;
    }

    protected void mouseDragged(Minecraft minecraft, int i, int j)
    {
        super.mouseDragged(minecraft, i, j);
        if (!drawButton)
        {
            return;
        }
        if (dragging)
        {
            sliderValue = (float)(i - (xPosition + 4)) / (float)(width - 8);
            if (sliderValue < 0.0F)
            {
                sliderValue = 0.0F;
            }
            if (sliderValue > 1.0F)
            {
                sliderValue = 1.0F;
            }
            int k = values.size() - 1;
            displayString = String.format(label, new Object[]
                    {
                        values.get((int)Math.floor((float)k * sliderValue))
                    });
            if (type != 0);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)), yPosition, 0, 66, 4, 20);
        drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)) + 4, yPosition, 196, 66, 4, 20);
    }

    public boolean mousePressed(Minecraft minecraft, int i, int j)
    {
        if (super.mousePressed(minecraft, i, j))
        {
            sliderValue = (float)(i - (xPosition + 4)) / (float)(width - 8);
            if (sliderValue < 0.0F)
            {
                sliderValue = 0.0F;
            }
            if (sliderValue > 1.0F)
            {
                sliderValue = 1.0F;
            }
            int k = values.size() - 1;
            displayString = String.format(label, new Object[]
                    {
                        values.get((int)Math.floor((float)k * sliderValue))
                    });
            dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void mouseReleased(int i, int j)
    {
        super.mouseReleased(i, j);
        dragging = false;
    }
}
