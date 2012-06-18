package nuclearcontrol;

import ic2.api.Ic2Recipes;
import ic2.api.Items;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.ChunkPosition;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class NuclearHelper
{
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
        else
        {
            Package var0 = Ic2Recipes.class.getPackage();
            String var1;

            if (var0 != null)
            {
                var1 = var0.getName().substring(0, var0.getName().lastIndexOf(46));
            }
            else
            {
                var1 = "net.minecraft.server.ic2";
            }

            var1 = var1 + ".common";
            reactorPackage = var1;
            return var1;
        }
    }

    private static Class getReactorTileEntityClass()
    {
        try
        {
            if (reactorTileEntityClass == null)
            {
                reactorTileEntityClass = Class.forName(getReactorPackage() + ".TileEntityNuclearReactor");
            }
        }
        catch (Exception var1)
        {
            throw new RuntimeException(var1);
        }

        return reactorTileEntityClass;
    }

    private static Class getReactorChamberTileEntityClass()
    {
        try
        {
            if (reactorChamberTileEntityClass == null)
            {
                reactorChamberTileEntityClass = Class.forName(getReactorPackage() + ".TileEntityReactorChamber");
            }
        }
        catch (Exception var1)
        {
            throw new RuntimeException(var1);
        }

        return reactorChamberTileEntityClass;
    }

    private static ItemStack getReactor()
    {
        if (reactor == null)
        {
            reactor = Items.getItem("nuclearReactor");
        }

        return reactor;
    }

    private static ItemStack getChamber()
    {
        if (chamber == null)
        {
            chamber = Items.getItem("reactorChamber");
        }

        return chamber;
    }

    private static Field getHeatField() throws NoSuchFieldException
    {
        if (heatField == null)
        {
            heatField = getReactorTileEntityClass().getField("heat");
        }

        return heatField;
    }

    public static TileEntity getReactorAt(World var0, int var1, int var2, int var3)
    {
        TileEntity var4 = var0.getTileEntity(var1, var2, var3);
        return getReactorTileEntityClass().isInstance(var4) && var0.getTypeId(var1, var2, var3) == getReactor().id && var0.getData(var1, var2, var3) == getReactor().getData() ? var4 : null;
    }

    public static TileEntity getReactorChamberAt(World var0, int var1, int var2, int var3)
    {
        TileEntity var4 = var0.getTileEntity(var1, var2, var3);
        return getReactorChamberTileEntityClass().isInstance(var4) && var0.getTypeId(var1, var2, var3) == getChamber().id && var0.getData(var1, var2, var3) == getChamber().getData() ? var4 : null;
    }

    public static TileEntity getReactorAroundCoord(World var0, int var1, int var2, int var3)
    {
        ChunkPosition[] var4 = new ChunkPosition[] {new ChunkPosition(-1, 0, 0), new ChunkPosition(1, 0, 0), new ChunkPosition(0, -1, 0), new ChunkPosition(0, 1, 0), new ChunkPosition(0, 0, -1), new ChunkPosition(0, 0, 1)};
        TileEntity var5 = null;

        for (int var6 = 0; var6 < 6 && var5 == null; ++var6)
        {
            ChunkPosition var7 = var4[var6];
            var5 = getReactorAt(var0, var1 + var7.x, var2 + var7.y, var3 + var7.z);
        }

        return var5;
    }

    public static TileEntity getReactorChamberAroundCoord(World var0, int var1, int var2, int var3)
    {
        ChunkPosition[] var4 = new ChunkPosition[] {new ChunkPosition(-1, 0, 0), new ChunkPosition(1, 0, 0), new ChunkPosition(0, -1, 0), new ChunkPosition(0, 1, 0), new ChunkPosition(0, 0, -1), new ChunkPosition(0, 0, 1)};
        TileEntity var5 = null;

        for (int var6 = 0; var6 < 6 && var5 == null; ++var6)
        {
            ChunkPosition var7 = var4[var6];
            var5 = getReactorChamberAt(var0, var1 + var7.x, var2 + var7.y, var3 + var7.z);
        }

        return var5;
    }

    public static int getReactorChamberCountAroundCoord(World var0, int var1, int var2, int var3)
    {
        ChunkPosition[] var4 = new ChunkPosition[] {new ChunkPosition(-1, 0, 0), new ChunkPosition(1, 0, 0), new ChunkPosition(0, -1, 0), new ChunkPosition(0, 1, 0), new ChunkPosition(0, 0, -1), new ChunkPosition(0, 0, 1)};
        int var5 = 0;
        TileEntity var6 = null;

        for (int var7 = 0; var7 < 6; ++var7)
        {
            ChunkPosition var8 = var4[var7];
            var6 = getReactorChamberAt(var0, var1 + var8.x, var2 + var8.y, var3 + var8.z);

            if (var6 != null)
            {
                ++var5;
            }
        }

        return var5;
    }

    public static int getMaxHeat(TileEntity var0)
    {
        int var1 = getReactorChamberCountAroundCoord(var0.world, var0.x, var0.y, var0.z);
        int var2 = 10000 + 1000 * var1;
        ItemStack var3 = Items.getItem("integratedReactorPlating");

        try
        {
            Class[] var4 = new Class[] {Integer.TYPE, Integer.TYPE};
            Method var5 = getReactorTileEntityClass().getMethod("getMatrixCoord", var4);

            for (int var6 = 0; var6 < 6; ++var6)
            {
                for (int var7 = 0; var7 < var1 + 3; ++var7)
                {
                    Object[] var8 = new Object[] {new Integer(var7), new Integer(var6)};
                    ItemStack var9 = (ItemStack)var5.invoke(var0, var8);

                    if (var9 != null && var9.id == var3.id)
                    {
                        var2 += 100;
                    }
                }
            }

            return var2;
        }
        catch (Exception var10)
        {
            throw new RuntimeException(var10);
        }
    }

    public static int getReactorHeat(TileEntity var0)
    {
        try
        {
            return getHeatField().getInt(var0);
        }
        catch (Exception var2)
        {
            throw new RuntimeException(var2);
        }
    }

    public static int getReactorTickRate(TileEntity var0)
    {
        try
        {
            return ((Integer)getReactorTileEntityClass().getMethod("tickRate", new Class[0]).invoke(var0, new Object[0])).intValue();
        }
        catch (Exception var2)
        {
            throw new RuntimeException(var2);
        }
    }
}
