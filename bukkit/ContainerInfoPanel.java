package nuclearcontrol;

import net.minecraft.server.*;

public class ContainerInfoPanel extends Container
{
    public TileEntityInfoPanel panel;
    public EntityHuman player;

    public ContainerInfoPanel(EntityHuman var1, TileEntityInfoPanel var2)
    {
        this.panel = var2;
        this.player = var1;
        this.a(new SlotFilter(var2, 0, 8, 18));
        this.a(new SlotFilter(var2, 1, 8, 36));
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

    public EntityHuman getPlayer()
    {
        return player;
    }

    public IInventory getInventory()
    {
        return panel;
    }


    public boolean b(EntityHuman var1)
    {
        return this.panel.a(this.player);
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
                int var4 = var3.count;

                if (var1 < this.panel.getSize())
                {
                    this.a(var3, this.panel.getSize(), this.e.size(), false);

                    if (var3.count == 0)
                    {
                        var2.set((ItemStack)null);
                    }
                    else
                    {
                        var2.d();

                        if (var4 != var3.count)
                        {
                            return var3;
                        }
                    }
                }
                else
                {
                    for (int var5 = 0; var5 < this.panel.getSize(); ++var5)
                    {
                        if (this.panel.isItemValid(var5, var3))
                        {
                            ItemStack var6 = this.panel.getItem(var5);

                            if (var6 == null)
                            {
                                Slot var7 = (Slot)this.e.get(var5);
                                var7.set(var3);
                                var2.set((ItemStack)null);
                                break;
                            }

                            if (var3.isStackable() && var3.doMaterialsMatch(var6))
                            {
                                this.a(var3, var5, var5 + 1, false);

                                if (var3.count == 0)
                                {
                                    var2.set((ItemStack)null);
                                }
                                else
                                {
                                    var2.d();

                                    if (var4 != var3.count)
                                    {
                                        return var3;
                                    }
                                }

                                break;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
