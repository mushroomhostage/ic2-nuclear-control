package nuclearcontrol;

import ic2.api.Ic2Recipes;
import java.lang.reflect.Field;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class EnergyStorageHelper
{
    private static String storagePackage = null;
    private static Class storageTileEntityClass = null;
    private static Field energyField = null;
    private static Field outputField = null;
    private static Field maxStorageField = null;

    private static String getStoragePackage()
    {
        if (storagePackage != null)
        {
            return storagePackage;
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
            storagePackage = var1;
            return var1;
        }
    }

    private static Class getStorageTileEntityClass()
    {
        try
        {
            if (storageTileEntityClass == null)
            {
                storageTileEntityClass = Class.forName(getStoragePackage() + ".TileEntityElectricBlock");
            }
        }
        catch (Exception var1)
        {
            throw new RuntimeException(var1);
        }

        return storageTileEntityClass;
    }

    private static Field getEnergyField() throws NoSuchFieldException
    {
        if (energyField == null)
        {
            energyField = getStorageTileEntityClass().getField("energy");
        }

        return energyField;
    }

    private static Field getOutputField() throws NoSuchFieldException
    {
        if (outputField == null)
        {
            outputField = getStorageTileEntityClass().getField("output");
        }

        return outputField;
    }

    private static Field getMaxStorageField() throws NoSuchFieldException
    {
        if (maxStorageField == null)
        {
            maxStorageField = getStorageTileEntityClass().getField("maxStorage");
        }

        return maxStorageField;
    }

    public static TileEntity getStorageAt(World var0, int var1, int var2, int var3)
    {
        if (var0 == null)
        {
            return null;
        }
        else
        {
            TileEntity var4 = var0.getTileEntity(var1, var2, var3);
            return var4 != null && getStorageTileEntityClass().isInstance(var4) ? var4 : null;
        }
    }

    public static int getStorageEnergy(TileEntity var0)
    {
        try
        {
            return getEnergyField().getInt(var0);
        }
        catch (Exception var2)
        {
            throw new RuntimeException(var2);
        }
    }

    public static int getStorageOutput(TileEntity var0)
    {
        try
        {
            return getOutputField().getInt(var0);
        }
        catch (Exception var2)
        {
            throw new RuntimeException(var2);
        }
    }

    public static int getStorageMaxStorage(TileEntity var0)
    {
        try
        {
            return getMaxStorageField().getInt(var0);
        }
        catch (Exception var2)
        {
            throw new RuntimeException(var2);
        }
    }
}
