package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.ItemStack;

public interface ISlotItemFilter
{
    boolean isItemValid(int slotIndex, ItemStack itemStack);
}
