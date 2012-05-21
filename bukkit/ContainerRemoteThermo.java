package nuclearcontrol;

import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Slot;

public class ContainerRemoteThermo extends Container
{
    TileEntityRemoteThermo remoteThermo;
    EntityHuman player;

    public ContainerRemoteThermo(EntityHuman var1, TileEntityRemoteThermo var2)
    {
        this.remoteThermo = var2;
        this.player = var1;
        this.a(new SlotFilter(var2, 0, 24, 52));
        this.a(new SlotFilter(var2, 1, 152, 8));
        this.a(new SlotFilter(var2, 2, 152, 26));
        this.a(new SlotFilter(var2, 3, 152, 44));
        this.a(new SlotFilter(var2, 4, 152, 62));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.a(new Slot(var1.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.a(new Slot(var1.inventory, var3, 8 + var3 * 18, 142));
        }
    }

    public boolean b(EntityHuman var1)
    {
        return this.remoteThermo.a(this.player);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack a(int var1)
    {
        Slot var2 = (Slot)this.e.get(var1);

        if (var2 != null)
        {
            ItemStack var3 = var2.getItem();

            if (var3 != null)
            {
                if (var1 >= this.remoteThermo.getSize())
                {
                    return null;
                }

                this.a(var3, this.remoteThermo.getSize(), this.e.size(), false);

                if (var3.count == 0)
                {
                    var2.set((ItemStack)null);
                }
                else
                {
                    var2.d();
                }
            }
        }

        return null;
    }
}
