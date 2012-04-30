package net.minecraft.src.nuclearcontrol;

import java.util.ArrayList;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_IC2NuclearControl;
import net.minecraft.src.forge.ITextureProvider;

public class BlockIC2Thermo extends BlockContainer implements ITextureProvider
{

    public BlockIC2Thermo(int i, int j)
    {
        super(i, Material.iron);
    }

    public boolean isBlockNormalCube(World world, int i, int j, int k)
    {
        return false;
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }
    
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
    	for (int face = 0; face < 6; face++){
    		int side = Facing.faceToSide[face];
    		if(world.isBlockSolidOnSide(x + Facing.offsetsXForSide[side], 
    									y + Facing.offsetsYForSide[side], 
    									z + Facing.offsetsZForSide[side], face))
    			return true;
    	}
    	return false;
    }
    
    /**
     * Called when a block is placed using an item. Used often for taking the facing and figuring out how to position
     * the item.
     */
    public void onBlockPlaced(World world, int x, int y, int z, int face)
    {
        int side = Facing.faceToSide[face];

        if(world.isBlockSolidOnSide(x + Facing.offsetsXForSide[side], 
				y + Facing.offsetsYForSide[side], 
				z + Facing.offsetsZForSide[side], face))
        {
            TileEntity tileentity = world.getBlockTileEntity(x, y, z);
            if(tileentity instanceof TileEntityIC2Thermo)
            {
            	((TileEntityIC2Thermo)tileentity).setFacing((short)side);
            }
        }
        
    }

    /**
     * Called whenever the block is added into the world.
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
    	for (int face = 0; face < 6; face++){
    		int side = Facing.faceToSide[face];
    		if(world.isBlockSolidOnSide(x + Facing.offsetsXForSide[side], 
    									y + Facing.offsetsYForSide[side], 
    									z + Facing.offsetsZForSide[side], face))
    		{
                TileEntity tileentity = world.getBlockTileEntity(x, y, z);
                if(tileentity instanceof TileEntityIC2Thermo)
                {
                	((TileEntityIC2Thermo)tileentity).setFacing((short)side);
                }
                break;
    		}
    	}
        dropBlockIfCantStay(world, x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbor)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        int side = 0;
        TileEntity tileentity = world.getBlockTileEntity(x, y, z);
        if(tileentity instanceof TileEntityIC2Thermo)
        {
        	side = ((TileEntityIC2Thermo)tileentity).getFacing();
        }
		if(!world.isBlockSolidOnSide(x + Facing.offsetsXForSide[side], 
				y + Facing.offsetsYForSide[side], 
				z + Facing.offsetsZForSide[side], Facing.faceToSide[side]))
		{
			if(!world.isRemote){
				dropBlockAsItem(world, x, y, z, metadata, 0);
			}
            world.setBlockWithNotify(x, y, z, 0);
		}
    }

    /**
     * Tests if the block can remain at its current location and will drop as an item if it is unable to stay. Returns
     * True if it can stay and False if it drops. Args: world, x, y, z
     */
    private boolean dropBlockIfCantStay(World world, int x, int y, int z)
    {
        if (!canPlaceBlockAt(world, x, y, z))
        {
            if (world.getBlockId(x, y, z) == blockID)
            {
                dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockWithNotify(x, y, z, 0);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setBlockBoundsForItemRender()
    {
        float baseX1 = 0.0625F;
        float baseY1 = 0F;
        float baseZ1 = 0.0625F;
        
        float baseX2 = 0.9375F;
        float baseY2 = 0.4375F;
        float baseZ2 = 0.9375F; 
        setBlockBounds(baseX1, baseY1, baseZ1, baseX2, baseY2, baseZ2);
    }
    
    
    /**
     * Updates the blocks bounds based on its current state.
     */
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        float baseX1 = 0.0625F;
        float baseY1 = 0F;
        float baseZ1 = 0.0625F;
        
        float baseX2 = 0.9375F;
        float baseY2 = 0.4375F;
        float baseZ2 = 0.9375F; 

        float tmp;
        
        int side = 0;
        TileEntity tileentity = blockAccess.getBlockTileEntity(x, y, z);
        if(tileentity instanceof TileEntityIC2Thermo)
        {
        	side = ((TileEntityIC2Thermo)tileentity).getFacing();
        }
        switch (side)
        {
            case 1:
            	baseY1 = 1 - baseY1;
            	baseY2 = 1 - baseY2;
                break;

            case 2:
            	tmp = baseY1;
            	baseY1 = baseZ1;
            	baseZ1 = tmp;

            	tmp = baseY2;
            	baseY2 = baseZ2;
            	baseZ2 = tmp;
                break;

            case 3:
            	tmp = baseY1;
            	baseY1 = baseZ1;
            	baseZ1 = 1 - tmp;

            	tmp = baseY2;
            	baseY2 = baseZ2;
            	baseZ2 = 1 - tmp;
                break;

            case 4:
            	tmp = baseY1;
            	baseY1 = baseX1;
            	baseX1 = tmp;

            	tmp = baseY2;
            	baseY2 = baseX2;
            	baseX2 = tmp;
                break;

            case 5:
            	tmp = baseY1;
            	baseY1 = baseX1;
            	baseX1 = 1 - tmp;

            	tmp = baseY2;
            	baseY2 = baseX2;
            	baseX2 = 1 - tmp;
                break;
        }
        setBlockBounds( Math.min(baseX1, baseX2), Math.min(baseY1, baseY2), Math.min(baseZ1, baseZ2), 
        				Math.max(baseX1, baseX2), Math.max(baseY1, baseY2), Math.max(baseZ1, baseZ2) );
    }

    public String getInvName()
    {
        return "IC2 Thermo";
    }

    public boolean canProvidePower()
    {
        return true;
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer)
    {
        if (entityplayer.isSneaking())
        {
            return false;
        }
        mod_IC2NuclearControl.launchGui(world, x, y, z, entityplayer);
        return true;
    }

    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
    {
        return false;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public boolean isPoweringTo(IBlockAccess iblockaccess, int x, int y, int z, int direction)
    {
        int targetX = x;
        int targetY = y;
        int targetZ = z;
        switch (direction) {
		case 0:
			targetY++;
			break;
		case 1:
			targetY--;
			break;
		case 2:
			targetZ++;
			break;
		case 3:
			targetZ--;
			break;
		case 4:
			targetX++;
			break;
		case 5:
			targetX--;
			break;
		}
        TileEntity tileentity = iblockaccess.getBlockTileEntity(targetX, targetY, targetZ);
        if (tileentity!=null && (NuclearHelper.getReactorAt(tileentity.worldObj, targetX, targetY, targetZ)!=null || 
    		NuclearHelper.getReactorChamberAt(tileentity.worldObj, targetX, targetY, targetZ)!=null))
        {
            return false;
        }
        tileentity = iblockaccess.getBlockTileEntity(x, y, z);
        if(tileentity instanceof TileEntityIC2Thermo)
        	return ((TileEntityIC2Thermo)tileentity).getOnFire() == 1;
        else
        	return false;
    }

    private static final int[][] sideMapping = 
    	{
			{1, 0, 17, 17, 17, 17},
			{0, 1, 17, 17, 17, 17},
			{17, 17, 1, 0, 33, 33},
			{17, 17, 0, 1, 33, 33},
			{33, 33, 33, 33, 1, 0},
			{33, 33, 33, 33, 0, 1}
    	};
    
    public int getBlockTextureFromSideAndMetadata(int side, int metadata)
    {
    	int texture = sideMapping[0][side];
		return blockIndexInTexture+texture;
    }
    
    public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side)
    {
        TileEntity tileentity = iblockaccess.getBlockTileEntity(x, y, z);
        boolean isThermo = tileentity instanceof TileEntityIC2Thermo;
        int metaSide = 0;
        if(isThermo)
        {
        	metaSide = ((TileEntityIC2Thermo)tileentity).getFacing();
        }
        int texture = sideMapping[metaSide][side];
    	if(texture!=0 || !isThermo)
    		return blockIndexInTexture+texture;
    	byte fireState = ((TileEntityIC2Thermo)tileentity).getOnFire();
    	switch (fireState)
        {
            case 1:
                texture = 16;
                break;
            case 0:
                texture = 0;
                break;
            default:
                texture = 32;
                break;
        }
	    return blockIndexInTexture + texture;
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntityIC2Thermo();
    }

    public TileEntity getBlockEntity(int i)
    {
        return getBlockEntity();
    }

    @Override
    public void addCreativeItems(ArrayList arraylist)
    {
        arraylist.add(new ItemStack(this));
    }

}
