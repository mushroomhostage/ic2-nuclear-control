package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class RedstoneHelper
{

    private static boolean isPoweredWire(World world, int x, int y, int z)
    {
        return world.getBlockId(x, y, z) == Block.redstoneWire.blockID &&
                Block.blocksList[Block.redstoneWire.blockID].isPoweringTo(world, x, y, z, 1);
    }
    
    public static void checkPowered(World world, TileEntity tileentity)
    {
        if(world != null && tileentity!=null && tileentity instanceof IRedstoneConsumer)
        {
            boolean powered = world.isBlockIndirectlyGettingPowered(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord) ||
                    isPoweredWire(world, tileentity.xCoord+1, tileentity.yCoord, tileentity.zCoord) ||
                    isPoweredWire(world, tileentity.xCoord-1, tileentity.yCoord, tileentity.zCoord) ||
                    isPoweredWire(world, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord+1) ||
                    isPoweredWire(world, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord-1);
            ((IRedstoneConsumer)tileentity).setPowered(powered);        
        }
    }

}
