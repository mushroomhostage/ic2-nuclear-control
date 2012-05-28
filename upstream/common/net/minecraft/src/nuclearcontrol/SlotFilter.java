package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotFilter extends Slot
{
    private final int slotIndex;
    
    public SlotFilter(IInventory inventory, int slotIndex, int x, int y)
    {
        super(inventory, slotIndex, x, y);
        this.slotIndex = slotIndex;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        if(inventory instanceof ISlotItemFilter)
            return ((ISlotItemFilter)inventory).isItemValid(slotIndex, itemStack);
        return super.isItemValid(itemStack);
    }

}
