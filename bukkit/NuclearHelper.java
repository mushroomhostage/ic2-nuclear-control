package nuclearcontrol;

import ic2.api.IReactor;
import ic2.api.IReactorChamber;
import net.minecraft.server.ChunkCoordinates;
import net.minecraft.server.ChunkPosition;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class NuclearHelper
{
    public static IReactor getReactorAt(World var0, int var1, int var2, int var3)
    {
        if (var0 == null)
        {
            return null;
        }
        else
        {
            TileEntity var4 = var0.getTileEntity(var1, var2, var3);
            return var4 instanceof IReactor ? (IReactor)var4 : null;
        }
    }

    public static IReactorChamber getReactorChamberAt(World var0, int var1, int var2, int var3)
    {
        if (var0 == null)
        {
            return null;
        }
        else
        {
            TileEntity var4 = var0.getTileEntity(var1, var2, var3);
            return var4 instanceof IReactorChamber ? (IReactorChamber)var4 : null;
        }
    }

    public static IReactor getReactorAroundCoord(World var0, int var1, int var2, int var3)
    {
        if (var0 == null)
        {
            return null;
        }
        else
        {
            ChunkPosition[] var4 = new ChunkPosition[] {new ChunkPosition(-1, 0, 0), new ChunkPosition(1, 0, 0), new ChunkPosition(0, -1, 0), new ChunkPosition(0, 1, 0), new ChunkPosition(0, 0, -1), new ChunkPosition(0, 0, 1)};
            IReactor var5 = null;

            for (int var6 = 0; var6 < 6 && var5 == null; ++var6)
            {
                ChunkPosition var7 = var4[var6];
                var5 = getReactorAt(var0, var1 + var7.x, var2 + var7.y, var3 + var7.z);
            }

            return var5;
        }
    }

    public static IReactorChamber getReactorChamberAroundCoord(World var0, int var1, int var2, int var3)
    {
        if (var0 == null)
        {
            return null;
        }
        else
        {
            ChunkPosition[] var4 = new ChunkPosition[] {new ChunkPosition(-1, 0, 0), new ChunkPosition(1, 0, 0), new ChunkPosition(0, -1, 0), new ChunkPosition(0, 1, 0), new ChunkPosition(0, 0, -1), new ChunkPosition(0, 0, 1)};
            IReactorChamber var5 = null;

            for (int var6 = 0; var6 < 6 && var5 == null; ++var6)
            {
                ChunkPosition var7 = var4[var6];
                var5 = getReactorChamberAt(var0, var1 + var7.x, var2 + var7.y, var3 + var7.z);
            }

            return var5;
        }
    }

    public static boolean isProducing(IReactor var0)
    {
        ChunkCoordinates var1 = var0.getPosition();
        return !var0.getWorld().isBlockIndirectlyPowered(var1.x, var1.y, var1.z);
    }
}
