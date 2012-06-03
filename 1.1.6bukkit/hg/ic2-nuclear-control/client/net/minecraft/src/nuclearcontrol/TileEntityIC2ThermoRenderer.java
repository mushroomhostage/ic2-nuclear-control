package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.Facing;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;

public class TileEntityIC2ThermoRenderer extends TileEntitySpecialRenderer
{

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
    {
        boolean isThermo = tileEntity instanceof TileEntityIC2Thermo;
        if(isThermo)
        {
            GL11.glPushMatrix();
            TileEntityIC2Thermo thermo = (TileEntityIC2Thermo)tileEntity;
            short side = (short)Facing.faceToSide[thermo.getFacing()];
            float var12 = 0.016666668F;
            int heat = (thermo.getHeatLevel() / 500) * 500;
            String text = Integer.toString(heat);
            GL11.glTranslatef((float)x, (float)y, (float)z);
            switch (side)
            {
                case 0:
                    break;
                case 1:
                    GL11.glTranslatef(1, 1, 0);
                    GL11.glRotatef(180, 1, 0, 0);
                    GL11.glRotatef(180, 0, 1, 0);
                    break;
                case 2:
                    GL11.glTranslatef(0, 1, 0);
                    GL11.glRotatef(0, 0, 1, 0);
                    GL11.glRotatef(90, 1, 0, 0);
                    break;
                case 3:
                    GL11.glTranslatef(1, 1, 1);
                    GL11.glRotatef(180, 0, 1, 0);
                    GL11.glRotatef(90, 1, 0, 0);
                    break;
                case 4:
                    GL11.glTranslatef(0, 1, 1);
                    GL11.glRotatef(90, 0, 1, 0);
                    GL11.glRotatef(90, 1, 0, 0);
                    break;
                case 5:
                    GL11.glTranslatef(1, 1, 0);
                    GL11.glRotatef(-90, 0, 1, 0);
                    GL11.glRotatef(90, 1, 0, 0);
                    break;

            }
            GL11.glTranslatef(0.5F, 0.4375F, 0.6875F);
            
            FontRenderer fontRenderer = this.getFontRenderer();
            GL11.glRotatef(-90, 1, 0, 0);
            GL11.glScalef(var12, - var12, var12);
            GL11.glPolygonOffset( -1, -1 );
            GL11.glEnable ( GL11.GL_POLYGON_OFFSET_FILL );
            fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, -fontRenderer.FONT_HEIGHT, 0);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL );
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
        
    }

}
