package nuclearcontrol;

import net.minecraft.server.ItemStack;

public interface ISlotItemFilter
{
    boolean isItemValid(int var1, ItemStack var2);
}
