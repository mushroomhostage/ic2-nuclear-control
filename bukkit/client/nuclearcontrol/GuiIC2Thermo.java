// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GuiIC2Thermo.java

package nuclearcontrol;

import abp;
import ic2.api.NetworkHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import vp;
import xd;
import yw;

// Referenced classes of package nuclearcontrol:
//            GuiIC2ThermoSlider, TileEntityIC2Thermo

public class GuiIC2Thermo extends vp
{

    public GuiIC2Thermo(xd world1, int i, int j, int k, yw entityplayer, TileEntityIC2Thermo tileentityic2thermo)
    {
        fSliderValue = 0.0F;
        xSize = 176;
        ySize = 166;
        thermo = tileentityic2thermo;
        world = world1;
    }

    public void c()
    {
        s.clear();
        byte byte0 = -16;
        float f = fSliderValue = (float)(thermo.getHeatLevel() - 500) / 14500F;
        ArrayList arraylist = new ArrayList();
        for(int i = 1; i <= 30; i++)
            arraylist.add(Integer.toString(i * 500));

        s.add(new abp(0, q / 2 - 50, r / 4 + 108 + byte0, 98, 20, "Save setting"));
        s.add(new GuiIC2ThermoSlider(3, q / 2 - 75, r / 4 + 65, arraylist, "Signal at %s heat", f, 0));
    }

    protected void a(abp guibutton)
    {
        if(guibutton.f == 0)
        {
            int i = 500 + (int)Math.floor(14500F * fSliderValue);
            thermo.setHeatLevel(i);
            NetworkHelper.initiateClientTileEntityEvent(thermo, i);
            p.a(null);
            p.g();
        }
        if(guibutton.f == 3)
        {
            GuiIC2ThermoSlider guiic2thermoslider = (GuiIC2ThermoSlider)guibutton;
            fSliderValue = guiic2thermoslider.sliderValue;
        }
    }

    public void a()
    {
        super.a();
    }

    protected void a(int i, int j, int k)
    {
        super.a(i, j, k);
        if(k == 0)
        {
            for(int l = 0; l < s.size(); l++)
            {
                abp guibutton = (abp)s.get(l);
                if(guibutton.c(p, i, j))
                    selectedButton = guibutton;
            }

        }
    }

    protected void b(int i, int j, int k)
    {
        if(selectedButton != null && k == 0)
        {
            if(selectedButton.f == 3 || selectedButton.f == 5)
                a(selectedButton);
            selectedButton.a(i, j);
            selectedButton = null;
        }
    }

    public void a(int i, int j, float f)
    {
        k();
        a(u, "Nuclear Thermal Protection", (q - xSize) / 2, (r - ySize) / 2 - 30, 0xffffff);
        super.a(i, j, f);
    }

    public boolean b()
    {
        return false;
    }

    protected int xSize;
    protected int ySize;
    private abp selectedButton;
    public TileEntityIC2Thermo thermo;
    public float fSliderValue;
    public xd world;
}
