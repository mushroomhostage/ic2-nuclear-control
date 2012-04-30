// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GuiIC2ThermoSlider.java

package nuclearcontrol;

import abp;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiIC2ThermoSlider extends abp
{

    public GuiIC2ThermoSlider(int i, int j, int k, ArrayList arraylist, String s, float f, int l)
    {
        super(i, j, k, 150, 20, s);
        sliderValue = 1.0F;
        dragging = false;
        values = arraylist;
        label = s;
        sliderValue = f;
        type = l;
        int i1 = values.size() - 1;
        e = String.format(label, new Object[] {
            values.get((int)Math.floor((float)i1 * sliderValue))
        });
    }

    protected int a(boolean flag)
    {
        return 0;
    }

    protected void b(Minecraft minecraft, int i, int j)
    {
        super.b(minecraft, i, j);
        if(!this.i)
            return;
        if(dragging)
        {
            sliderValue = (float)(i - (c + 4)) / (float)(a - 8);
            if(sliderValue < 0.0F)
                sliderValue = 0.0F;
            if(sliderValue > 1.0F)
                sliderValue = 1.0F;
            int k = values.size() - 1;
            e = String.format(label, new Object[] {
                values.get((int)Math.floor((float)k * sliderValue))
            });
            if(type == 0);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        b(c + (int)(sliderValue * (float)(a - 8)), d, 0, 66, 4, 20);
        b(c + (int)(sliderValue * (float)(a - 8)) + 4, d, 196, 66, 4, 20);
    }

    public boolean c(Minecraft minecraft, int i, int j)
    {
        if(super.c(minecraft, i, j))
        {
            sliderValue = (float)(i - (c + 4)) / (float)(a - 8);
            if(sliderValue < 0.0F)
                sliderValue = 0.0F;
            if(sliderValue > 1.0F)
                sliderValue = 1.0F;
            int k = values.size() - 1;
            e = String.format(label, new Object[] {
                values.get((int)Math.floor((float)k * sliderValue))
            });
            dragging = true;
            return true;
        } else
        {
            return false;
        }
    }

    public void a(int i, int j)
    {
        super.a(i, j);
        dragging = false;
    }

    public float sliderValue;
    public boolean dragging;
    private ArrayList values;
    private String label;
    public int type;
}
