// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileEntityIC2ThermoRenderer.java

package nuclearcontrol;

import aar;
import kw;
import nl;
import org.lwjgl.opengl.GL11;

// Referenced classes of package nuclearcontrol:
//            TileEntityIC2Thermo

public class TileEntityIC2ThermoRenderer extends aar
{

    public TileEntityIC2ThermoRenderer()
    {
    }

    public void a(kw tileEntity, double x, double y, double z, 
            float f)
    {
        boolean isThermo = tileEntity instanceof TileEntityIC2Thermo;
        if(isThermo)
        {
            GL11.glPushMatrix();
            TileEntityIC2Thermo thermo = (TileEntityIC2Thermo)tileEntity;
            short side = thermo.getFacing();
            float var12 = 0.01666667F;
            int heat = (thermo.getHeatLevel() / 500) * 500;
            String text = Integer.toString(heat);
            GL11.glTranslatef((float)x, (float)y, (float)z);
            switch(side)
            {
            case 1: // '\001'
                GL11.glTranslatef(1.0F, 1.0F, 0.0F);
                GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                break;

            case 2: // '\002'
                GL11.glTranslatef(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                break;

            case 3: // '\003'
                GL11.glTranslatef(1.0F, 1.0F, 1.0F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                break;

            case 4: // '\004'
                GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                break;

            case 5: // '\005'
                GL11.glTranslatef(1.0F, 1.0F, 0.0F);
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                break;
            }
            GL11.glTranslatef(0.5F, 0.4377F, 0.6875F);
            nl fontRenderer = a();
            GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(var12, -var12, var12);
            GL11.glDepthMask(false);
            fontRenderer.b(text, -fontRenderer.a(text) / 2, -fontRenderer.b, 0);
            GL11.glDepthMask(true);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
