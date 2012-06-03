package nuclearcontrol;

import net.minecraft.server.Block;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class RedstoneHelper
{
    private static boolean isPoweredWire(World var0, int var1, int var2, int var3)
    {
        return var0.getTypeId(var1, var2, var3) == Block.REDSTONE_WIRE.id && Block.byId[Block.REDSTONE_WIRE.id].a(var0, var1, var2, var3, 1);
    }

    public static void checkPowered(World var0, TileEntity var1)
    {
        if (var0 != null && var1 != null && var1 instanceof IRedstoneConsumer)
        {
            boolean var2 = var0.isBlockIndirectlyPowered(var1.x, var1.y, var1.z) || isPoweredWire(var0, var1.x + 1, var1.y, var1.z) || isPoweredWire(var0, var1.x - 1, var1.y, var1.z) || isPoweredWire(var0, var1.x, var1.y, var1.z + 1) || isPoweredWire(var0, var1.x, var1.y, var1.z - 1);
            ((IRedstoneConsumer)var1).setPowered(var2);
        }
    }
}
