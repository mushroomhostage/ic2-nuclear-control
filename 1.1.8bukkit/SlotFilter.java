package nuclearcontrol;

import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Slot;

public class SlotFilter extends Slot
{
    private final int slotIndex;

    public SlotFilter(IInventory var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4);
        this.slotIndex = var2;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isAllowed(ItemStack var1)
    {
        return this.inventory instanceof ISlotItemFilter ? ((ISlotItemFilter)this.inventory).isItemValid(this.slotIndex, var1) : super.isAllowed(var1);
    }
}
