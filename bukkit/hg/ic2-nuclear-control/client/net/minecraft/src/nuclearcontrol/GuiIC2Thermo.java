package net.minecraft.src.nuclearcontrol;

import java.util.ArrayList;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.World;
import net.minecraft.src.ic2.api.NetworkHelper;

public class GuiIC2Thermo extends GuiScreen
{
    protected int xSize;
    protected int ySize;
    private GuiButton selectedButton;
    public TileEntityIC2Thermo thermo;
    public float fSliderValue;
    public World world;

    public GuiIC2Thermo(World world1, int i, int j, int k, EntityPlayer entityplayer, TileEntityIC2Thermo tileentityic2thermo)
    {
        fSliderValue = 0.0F;
        xSize = 176;
        ySize = 166;
        thermo = tileentityic2thermo;
        world = world1;
    }

    public void initGui()
    {
        controlList.clear();
        byte byte0 = -16;
        float f = fSliderValue = (thermo.getHeatLevel()-500)/14500F;
        ArrayList<String> arraylist = new ArrayList<String>();
        for (int i = 1; i <= 30; i++)
        {
            arraylist.add(Integer.toString(i * 500));
        }

        controlList.add(new GuiButton(0, width / 2 - 50, height / 4 + 108 + byte0, 98, 20, "Save setting"));
        controlList.add(new GuiIC2ThermoSlider(3, width / 2 - 75, height / 4 + 65, arraylist, "Signal at %s heat", f, 0));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.id == 0)
        {
            int i = 500+(int)Math.floor(14500F * fSliderValue);
            thermo.setHeatLevel(i);
            NetworkHelper.initiateClientTileEntityEvent(thermo, i);
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
        if (guibutton.id == 3)
        {
            GuiIC2ThermoSlider guiic2thermoslider = (GuiIC2ThermoSlider)guibutton;
            fSliderValue = guiic2thermoslider.sliderValue;
        }
    }

    public void updateScreen()
    {
        super.updateScreen();
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        if (k == 0)
        {
            for (int l = 0; l < controlList.size(); l++)
            {
                GuiButton guibutton = (GuiButton)controlList.get(l);
                if (guibutton.mousePressed(mc, i, j))
                {
                    selectedButton = guibutton;
                }
            }
        }
    }

    protected void mouseMovedOrUp(int i, int j, int k)
    {
        if (selectedButton != null && k == 0)
        {
            if (selectedButton.id == 3 || selectedButton.id == 5)
            {
                actionPerformed(selectedButton);
            }
            selectedButton.mouseReleased(i, j);
            selectedButton = null;
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Nuclear Thermal Protection", (width - xSize) / 2, (height - ySize) / 2 - 30, 0xffffff);
        super.drawScreen(i, j, f);
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
