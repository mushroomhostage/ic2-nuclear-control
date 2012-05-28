package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerRemoteThermo extends Container
{
    TileEntityRemoteThermo remoteThermo;
    EntityPlayer player;

    public ContainerRemoteThermo(EntityPlayer player, TileEntityRemoteThermo remoteThermo)
    {
        super();
        
        this.remoteThermo = remoteThermo;
        this.player = player; 
        
        //energy charger
        addSlot(new SlotFilter(remoteThermo, 0, 24, 52));
        
        //upgrades
        addSlot(new SlotFilter(remoteThermo, 1, 152, 8));
        addSlot(new SlotFilter(remoteThermo, 2, 152, 26));
        addSlot(new SlotFilter(remoteThermo, 3, 152, 44));
        addSlot(new SlotFilter(remoteThermo, 4, 152, 62));

        //inventory
        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlot(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlot(new Slot(player.inventory, j, 8 + j * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return remoteThermo.isUseableByPlayer(player);
    }
    
    @Override
    public ItemStack transferStackInSlot(int slotId)
    {
        Slot slot = (Slot)this.inventorySlots.get(slotId);
        if(slot!=null){
            ItemStack items = slot.getStack();
            if(items!=null)
            {
                if(slotId < remoteThermo.getSizeInventory())//moving from thermo to inventory
                {
                    mergeItemStack(items, remoteThermo.getSizeInventory(), inventorySlots.size(), false);
                    if (items.stackSize == 0)
                    {
                        slot.putStack((ItemStack)null);
                    }
                    else
                    {
                        slot.onSlotChanged();
                    }
                }
                else//moving from inventory to thermo
                {
                    return null;// not supported yet
                }
            }
        }
        return null;
    }
    
}
