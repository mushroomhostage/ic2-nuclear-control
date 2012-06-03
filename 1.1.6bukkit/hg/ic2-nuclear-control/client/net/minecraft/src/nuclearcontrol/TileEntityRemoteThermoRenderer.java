package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.Block;
import net.minecraft.src.Facing;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;

public class TileEntityRemoteThermoRenderer extends TileEntitySpecialRenderer
{

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
    {
        boolean isThermo = tileEntity instanceof TileEntityIC2Thermo;
        if(isThermo)
        {
            GL11.glPushMatrix();
            GL11.glPolygonOffset( -1, -1 );
            GL11.glEnable ( GL11.GL_POLYGON_OFFSET_FILL );
            TileEntityIC2Thermo thermo = (TileEntityIC2Thermo)tileEntity;
            short side = (short)Facing.faceToSide[thermo.getFacing()];
            float var12 = 0.016666668F;
            int heat = (thermo.getHeatLevel() / 500) * 500;
            String text = Integer.toString(heat);
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ModLoader.getMinecraftInstance().renderEngine.getTexture("/img/texture_thermo.png"));
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            switch (side)
            {
                case 0:
                    tessellator.setNormal(0, -1, 0);
                    break;
                case 1:
                    tessellator.setNormal(0, 1, 0);
                    GL11.glTranslatef(1, 1, 0);
                    GL11.glRotatef(180, 1, 0, 0);
                    GL11.glRotatef(180, 0, 1, 0);
                    break;
                case 2:
                    tessellator.setNormal(0, 0, -1);
                    GL11.glTranslatef(0, 1, 0);
                    GL11.glRotatef(0, 0, 1, 0);
                    GL11.glRotatef(90, 1, 0, 0);
                    break;
                case 3:
                    tessellator.setNormal(0, 0, 1);
                    GL11.glTranslatef(1, 1, 1);
                    GL11.glRotatef(180, 0, 1, 0);
                    GL11.glRotatef(90, 1, 0, 0);
                    break;
                case 4:
                    tessellator.setNormal(-1, 0, 0);
                    GL11.glTranslatef(0, 1, 1);
                    GL11.glRotatef(90, 0, 1, 0);
                    GL11.glRotatef(90, 1, 0, 0);
                    break;
                case 5:
                    tessellator.setNormal(1, 0, 0);
                    GL11.glTranslatef(1, 1, 0);
                    GL11.glRotatef(-90, 0, 1, 0);
                    GL11.glRotatef(90, 1, 0, 0);
                    break;

            }
            GL11.glTranslatef(0.5F, 1F, 0.4375F);
            GL11.glRotatef(-90, 1, 0, 0);

            Block block = Block.blocksList[thermo.worldObj.getBlockId(thermo.xCoord, thermo.yCoord, thermo.zCoord)];
            if(block==null)
            {
                block = Block.stone;
            }
            int fire = thermo.getOnFire();
            if(fire > -2)
            {
                tessellator.setBrightness(block.getMixedBrightnessForBlock(thermo.worldObj, thermo.xCoord, thermo.yCoord, thermo.zCoord));
                tessellator.setColorOpaque_F(1, 1, 1);
                double left = -0.4375;
                double top = -0.375;
                double width = 0.875;
                double height = 0.25;
                double u = 161D/256;
                double v;
                double middle;
                if(fire == -1)
                {
                    v = 16D/256;
                    middle = width;
                }
                else
                {
                    double heatLevel = ((double)thermo.getOnFire())/thermo.getHeatLevel();
                    if(heatLevel > 1)
                        heatLevel = 1;
                    middle = heatLevel * width;
                    v = 20D/256;
                }
                
                tessellator.addVertexWithUV(left,        top,        0, u,           v);
                tessellator.addVertexWithUV(left+middle, top,        0, u+middle/16, v);
                tessellator.addVertexWithUV(left+middle, top+height, 0, u+middle/16, v+height/16);
                tessellator.addVertexWithUV(left,        top+height, 0, u,           v+height/16);
                
                if(middle!=width)
                {
                    v = 24D/256;
                    tessellator.addVertexWithUV(left+middle, top,        0, u,          v);
                    tessellator.addVertexWithUV(left+width,  top,        0, u+width/16, v);
                    tessellator.addVertexWithUV(left+width,  top+height, 0, u+width/16, v+height/16);
                    tessellator.addVertexWithUV(left+middle, top+height, 0, u,          v+height/16);
                }
            }
            tessellator.draw();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            FontRenderer fontRenderer = this.getFontRenderer();
            GL11.glScalef(var12, - var12, var12);
            GL11.glDepthMask(false);
            fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, -fontRenderer.FONT_HEIGHT, 0);
            GL11.glDepthMask(true);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL );
            GL11.glPopMatrix();
            
        }
        
    }

}
