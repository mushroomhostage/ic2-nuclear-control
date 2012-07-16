package nuclearcontrol;

import net.minecraft.server.*;

public class ContainerRemoteThermo extends Container
{
    public TileEntityRemoteThermo remoteThermo;
    private EntityHuman player;
    private int lastEnergy = -1;

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

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void a()
    {
        super.a();
        int var1 = this.remoteThermo.energy;

        for (int var2 = 0; var2 < this.listeners.size(); ++var2)
        {
            ICrafting var3 = (ICrafting)this.listeners.get(var2);

            if (this.lastEnergy != var1)
            {
                var3.setContainerData(this, 0, var1);
            }
        }

        this.lastEnergy = var1;
    }

    public void updateProgressBar(int var1, int var2)
    {
        if (var1 == 0)
        {
            this.remoteThermo.setEnergy(var2);
        }
    }
    
    public EntityHuman getPlayer()
    {
        return player;
    }

    public IInventory getInventory()
    {
        return remoteThermo;
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
                int var4 = var3.count;

                if (var1 < this.remoteThermo.getSize())
                {
                    this.a(var3, this.remoteThermo.getSize(), this.e.size(), false);

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
                    for (int var5 = 0; var5 < this.remoteThermo.getSize(); ++var5)
                    {
                        if (this.remoteThermo.isItemValid(var5, var3))
                        {
                            ItemStack var6 = this.remoteThermo.getItem(var5);

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
