package nuclearcontrol;

import ic2.api.Direction;
import ic2.api.ElectricItem;
import ic2.api.EnergyNet;
import ic2.api.IElectricItem;
import ic2.api.IEnergySink;
import ic2.api.Items;
import ic2.api.NetworkHelper;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntity;

public class TileEntityRemoteThermo extends TileEntityIC2Thermo implements IInventory, IEnergySink, ISlotItemFilter
{
    public static final int SLOT_CHARGER = 0;
    public static final int SLOT_CARD = 1;
    private static final int BASE_PACKET_SIZE = 32;
    private static final int BASE_STORAGE = 600;
    private static final int STORAGE_PER_UPGRADE = 10000;
    private static final int ENERGY_SU_BATTERY = 1000;
    private static final int LOCATION_RANGE = 8;
    private int deltaX = 0;
    private int deltaY = 0;
    private int deltaZ = 0;
    private int prevMaxStorage;
    public int maxStorage = 600;
    public int prevMaxPacketSize;
    public int maxPacketSize = 32;
    private int prevTier;
    public int tier = 1;
    private int prevEnergy = 0;
    public int energy = 0;
    private boolean addedToEnergyNet = false;
    private ItemStack[] inventory = new ItemStack[5];

    public List getNetworkedFields()
    {
        List var1 = super.getNetworkedFields();
        var1.add("maxStorage");
        var1.add("energy");
        var1.add("tier");
        var1.add("maxPacketSize");
        return var1;
    }

    protected void checkStatus()
    {
        if (!this.addedToEnergyNet)
        {
            EnergyNet.getForWorld(this.world).addTileEntity(this);
            this.addedToEnergyNet = true;
        }

        this.update();
        int var1;

        if (this.energy > 0)
        {
            TileEntity var2 = NuclearHelper.getReactorAt(this.world, this.x + this.deltaX, this.y + this.deltaY, this.z + this.deltaZ);

            if (var2 != null)
            {
                if (this.tickRate == -1)
                {
                    this.tickRate = NuclearHelper.getReactorTickRate(var2) / 2;

                    if (this.tickRate == 0)
                    {
                        this.tickRate = 1;
                    }

                    this.updateTicker = this.tickRate;
                }

                int var3 = NuclearHelper.getReactorHeat(var2);
                var1 = var3;
            }
            else
            {
                var1 = -1;
            }
        }
        else
        {
            var1 = -2;
        }

        if (var1 != this.getOnFire())
        {
            this.setOnFire(var1);
            this.world.applyPhysics(this.x, this.y, this.z, this.world.getTypeId(this.x, this.y, this.z));
        }
    }

    public int getEnergy()
    {
        return this.energy;
    }

    public void setEnergy(int var1)
    {
        this.energy = var1;

        if (this.energy != this.prevEnergy)
        {
            NetworkHelper.updateTileEntityField(this, "energy");
        }

        this.prevEnergy = this.energy;
    }

    public void setTier(int var1)
    {
        this.tier = var1;

        if (this.tier != this.prevTier)
        {
            NetworkHelper.updateTileEntityField(this, "tier");
        }

        this.prevTier = this.tier;
    }

    public void setMaxPacketSize(int var1)
    {
        this.maxPacketSize = var1;

        if (this.maxPacketSize != this.prevMaxPacketSize)
        {
            NetworkHelper.updateTileEntityField(this, "maxPacketSize");
        }

        this.prevMaxPacketSize = this.maxPacketSize;
    }

    public void setMaxStorage(int var1)
    {
        this.maxStorage = var1;

        if (this.maxStorage != this.prevMaxStorage)
        {
            NetworkHelper.updateTileEntityField(this, "maxStorage");
        }

        this.prevMaxStorage = this.maxStorage;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void q_()
    {
        if (!this.world.isStatic)
        {
            if (this.energy > 0)
            {
                --this.energy;
            }

            if (this.inventory[0] != null && this.energy < this.maxStorage && this.inventory[0] != null)
            {
                if (this.inventory[0].getItem() instanceof IElectricItem)
                {
                    IElectricItem var1 = (IElectricItem)this.inventory[0].getItem();

                    if (var1.canProvideEnergy())
                    {
                        int var2 = ElectricItem.discharge(this.inventory[0], this.maxStorage - this.energy, this.tier, false, false);
                        this.energy += var2;
                    }
                }
                else if (this.inventory[0].id == Items.getItem("suBattery").id && (1000 <= this.maxStorage - this.energy || this.energy == 0))
                {
                    --this.inventory[0].count;

                    if (this.inventory[0].count <= 0)
                    {
                        this.inventory[0] = null;
                    }

                    this.energy += 1000;

                    if (this.energy > this.maxStorage)
                    {
                        this.energy = this.maxStorage;
                    }
                }
            }

            this.setEnergy(this.energy);
        }

        super.q_();
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void a(NBTTagCompound var1)
    {
        super.a(var1);
        this.energy = var1.getInt("energy");
        NBTTagList var2 = var1.getList("Items");
        this.inventory = new ItemStack[this.getSize()];

        for (int var3 = 0; var3 < var2.size(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.get(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.inventory.length)
            {
                this.inventory[var5] = ItemStack.a(var4);
            }
        }

        this.update();
    }

    /**
     * invalidates a tile entity
     */
    public void j()
    {
        if (!this.world.isStatic && this.addedToEnergyNet)
        {
            EnergyNet.getForWorld(this.world).removeTileEntity(this);
            this.addedToEnergyNet = false;
        }

        super.j();
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void b(NBTTagCompound var1)
    {
        super.b(var1);
        var1.setInt("energy", this.energy);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.inventory.length; ++var3)
        {
            if (this.inventory[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.inventory[var3].save(var4);
                var2.add(var4);
            }
        }

        var1.set("Items", var2);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSize()
    {
        return this.inventory.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getItem(int var1)
    {
        return this.inventory[var1];
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack splitStack(int var1, int var2)
    {
        if (this.inventory[var1] != null)
        {
            ItemStack var3;

            if (this.inventory[var1].count <= var2)
            {
                var3 = this.inventory[var1];
                this.inventory[var1] = null;
                return var3;
            }
            else
            {
                var3 = this.inventory[var1].a(var2);

                if (this.inventory[var1].count == 0)
                {
                    this.inventory[var1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack splitWithoutUpdate(int var1)
    {
        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int var1, ItemStack var2)
    {
        this.inventory[var1] = var2;

        if (var2 != null && var2.count > this.getMaxStackSize())
        {
            var2.count = this.getMaxStackSize();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getName()
    {
        return "block.RemoteThermo";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getMaxStackSize()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean a(EntityHuman var1)
    {
        return this.world.getTileEntity(this.x, this.y, this.z) == this && var1.e((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D) <= 64.0D;
    }

    public void f() {}

    public void g() {}

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void update()
    {
        super.update();
        int var1 = 0;
        int var2 = 0;

        if (this.inventory[1] != null)
        {
            int[] var3 = ItemSensorLocationCard.getCoordinates(this.inventory[1]);

            if (var3 != null)
            {
                this.deltaX = var3[0] - this.x;
                this.deltaY = var3[1] - this.y;
                this.deltaZ = var3[2] - this.z;

                if (Math.abs(this.deltaX) > 8 || Math.abs(this.deltaY) > 8 || Math.abs(this.deltaZ) > 8)
                {
                    this.deltaX = this.deltaY = this.deltaZ = 0;
                }
            }
            else
            {
                this.deltaX = 0;
                this.deltaY = 0;
                this.deltaZ = 0;
            }
        }
        else
        {
            this.deltaX = 0;
            this.deltaY = 0;
            this.deltaZ = 0;
        }

        for (int var5 = 2; var5 < 5; ++var5)
        {
            ItemStack var4 = this.inventory[var5];

            if (var4 != null)
            {
                if (var4.doMaterialsMatch(Items.getItem("transformerUpgrade")))
                {
                    var1 += var4.count;
                }
                else if (var4.doMaterialsMatch(Items.getItem("energyStorageUpgrade")))
                {
                    var2 += var4.count;
                }
            }
        }

        var1 = Math.min(var1, 4);

        if (this.world != null && !this.world.isStatic)
        {
            this.tier = var1 + 1;
            this.setTier(this.tier);
            this.maxPacketSize = 32 * (int)Math.pow(4.0D, (double)var1);
            this.setMaxPacketSize(this.maxPacketSize);
            this.maxStorage = 600 + 10000 * var2;
            this.setMaxStorage(this.maxStorage);

            if (this.energy > this.maxStorage)
            {
                this.energy = this.maxStorage;
            }

            this.setEnergy(this.energy);
        }
    }

    public boolean acceptsEnergyFrom(TileEntity var1, Direction var2)
    {
        return true;
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public boolean demandsEnergy()
    {
        return this.energy <= this.maxStorage - this.maxPacketSize || this.energy == 0;
    }

    public int injectEnergy(Direction var1, int var2)
    {
        if (var2 > this.maxPacketSize)
        {
            this.world.setTypeId(this.x, this.y, this.z, 0);
            this.world.explode((Entity)null, (double)this.x, (double)this.y, (double)this.z, 0.8F);
            return 0;
        }
        else
        {
            this.energy += var2;
            int var3 = 0;

            if (this.energy > this.maxStorage)
            {
                var3 = this.energy - this.maxStorage;
                this.energy = this.maxStorage;
            }

            this.setEnergy(this.energy);
            return var3;
        }
    }

    public boolean isItemValid(int var1, ItemStack var2)
    {
        switch (var1)
        {
            case 0:
                if (var2.id == Items.getItem("suBattery").id)
                {
                    return true;
                }
                else
                {
                    if (var2.getItem() instanceof IElectricItem)
                    {
                        IElectricItem var3 = (IElectricItem)var2.getItem();

                        if (var3.canProvideEnergy() && var3.getTier() <= this.tier)
                        {
                            return true;
                        }
                    }

                    return false;
                }

            case 1:
                return var2.getItem() instanceof ItemSensorLocationCard;

            default:
                return var2.doMaterialsMatch(Items.getItem("transformerUpgrade")) || var2.doMaterialsMatch(Items.getItem("energyStorageUpgrade"));
        }
    }

    public boolean wrenchCanSetFacing(EntityHuman var1, int var2)
    {
        return this.getFacing() != var2;
    }

    public int modifyTextureIndex(int var1)
    {
        return var1;
    }
}
