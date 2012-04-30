package net.minecraft.src.nuclearcontrol;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.src.ChunkPosition;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.ic2.api.Items;

public class NuclearHelper {
	
    private static String reactorPackage = null;
    private static Class reactorTileEntityClass = null;
    private static Class reactorChamberTileEntityClass = null;
    private static ItemStack reactor = null;
    private static ItemStack chamber = null;
    private static Field heatField = null;
	
    private static String getReactorPackage()
    {
        if (reactorPackage != null)
        {
            return reactorPackage;
        }

        Package rPackage = (net.minecraft.src.ic2.api.Ic2Recipes.class).getPackage();
        String result;

        if (rPackage != null)
        {
            result = rPackage.getName().substring(0, rPackage.getName().lastIndexOf('.'));
        } 
        else
        {
            result = "net.minecraft.src.ic2";
        }
        result += ".common";
        reactorPackage = result;
        return result;
    }

	private static Class getReactorTileEntityClass()
	{
        try
        {
			if(reactorTileEntityClass==null)
				reactorTileEntityClass = Class.forName(getReactorPackage() +".TileEntityNuclearReactor");
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
		return reactorTileEntityClass;
	}
    
	private static Class getReactorChamberTileEntityClass()
	{
        try
        {
			if(reactorChamberTileEntityClass==null)
				reactorChamberTileEntityClass = Class.forName(getReactorPackage() +".TileEntityReactorChamber");
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
		return reactorChamberTileEntityClass;
	}
    
	private static ItemStack getReactor()
	{
		if(reactor == null)
			reactor = Items.getItem("nuclearReactor");
		return reactor;
	}
	
	private static ItemStack getChamber(){
		if(chamber == null)
			chamber = Items.getItem("reactorChamber");
		return chamber;
	}
	
	private static Field getHeatField() throws NoSuchFieldException
	{
		if(heatField == null)
			heatField = getReactorTileEntityClass().getField("heat");
		return heatField;
	}
	
	public static TileEntity getReactorAt(World world, int x, int y, int z) 
	{
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if ((getReactorTileEntityClass().isInstance(entity))
				&& world.getBlockId(x, y, z) == getReactor().itemID
				&& world.getBlockMetadata(x, y, z) == getReactor().getItemDamage()){
			return entity;
		}
		return null;
	}


	public static TileEntity getReactorChamberAt(World world, int x, int y, int z) 
	{
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if ((getReactorChamberTileEntityClass().isInstance(entity))
				&& world.getBlockId(x, y, z) == getChamber().itemID
				&& world.getBlockMetadata(x, y, z) == getChamber().getItemDamage()){
			return entity;
		}
		return null;
	}

	public static TileEntity getReactorAroundCoord(World world, int x, int y, int z) {
		ChunkPosition[] around = { 
				new ChunkPosition(-1, 0, 0),
				new ChunkPosition( 1, 0, 0),
				new ChunkPosition( 0,-1, 0),
				new ChunkPosition( 0, 1, 0),
				new ChunkPosition( 0, 0,-1),
				new ChunkPosition( 0, 0, 1)
		};
		TileEntity ent = null;
		for(int i=0;i<6 && ent == null;i++){
			ChunkPosition delta = around[i]; 
			ent = getReactorAt(world, x+delta.x, y+delta.y, z+delta.z);
		}
		return ent;
	}

	public static TileEntity getReactorChamberAroundCoord(World world, int x, int y, int z) 
	{
		ChunkPosition[] around = { 
				new ChunkPosition(-1, 0, 0),
				new ChunkPosition( 1, 0, 0),
				new ChunkPosition( 0,-1, 0),
				new ChunkPosition( 0, 1, 0),
				new ChunkPosition( 0, 0,-1),
				new ChunkPosition( 0, 0, 1)
		};
		TileEntity ent = null;
		for(int i=0;i<6 && ent == null;i++){
			ChunkPosition delta = around[i]; 
			ent = getReactorChamberAt(world, x+delta.x, y+delta.y, z+delta.z);
		}
		return ent;
	}
	
	public static int getReactorChamberCountAroundCoord(World world, int x, int y, int z) 
	{
		ChunkPosition[] around = { 
				new ChunkPosition(-1, 0, 0),
				new ChunkPosition( 1, 0, 0),
				new ChunkPosition( 0,-1, 0),
				new ChunkPosition( 0, 1, 0),
				new ChunkPosition( 0, 0,-1),
				new ChunkPosition( 0, 0, 1)
		};
		int count = 0;
		TileEntity ent = null;
		for(int i=0;i<6;i++){
			ChunkPosition delta = around[i]; 
			ent = getReactorChamberAt(world, x+delta.x, y+delta.y, z+delta.z);
			if(ent!=null)
				count++;
		}
		return count;
	}
	

	public static int getMaxHeat(TileEntity reactor)
	{
        int cCount = getReactorChamberCountAroundCoord(reactor.worldObj, reactor.xCoord, reactor.yCoord, reactor.zCoord);
        int maxHeat = 10000 + 1000*cCount;
        ItemStack plating = Items.getItem("integratedReactorPlating");
        try
        {
        	Class partypes[] = {Integer.TYPE, Integer.TYPE};
            Method getItem = getReactorTileEntityClass().getMethod("getMatrixCoord", partypes);
            for (int j = 0; j < 6; j++)
            {
                for (int k = 0; k < cCount+3; k++)
                {
                	Object[] params = {new Integer(k), new Integer(j)}; 
                	ItemStack item = (ItemStack)getItem.invoke(reactor, params); 
                    if (item != null && item.itemID == plating.itemID)
                    {
                    	maxHeat += 100;
                    }
                }
            }
            return maxHeat;
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
	}
	
	public static int getReactorHeat(TileEntity reactor)
	{
        try
        {
    		return getHeatField().getInt(reactor);
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
	}
	
	public static int getReactorTickRate(TileEntity reactor)
	{
        try
        {
    		return (Integer)getReactorTileEntityClass().getMethod("tickRate").invoke(reactor);
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
	}
}
