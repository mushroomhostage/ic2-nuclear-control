package nuclearcontrol;

import ic2.api.IEnergyStorage;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class EnergyStorageHelper
{
    public static IEnergyStorage getStorageAt(World var0, int var1, int var2, int var3)
    {
        if (var0 == null)
        {
            return null;
        }
        else
        {
            TileEntity var4 = var0.getTileEntity(var1, var2, var3);
            return var4 != null && var4 instanceof IEnergyStorage ? (IEnergyStorage)var4 : null;
        }
    }
}
