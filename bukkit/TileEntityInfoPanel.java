package nuclearcontrol;

import ic2.api.INetworkClientTileEntityEventListener;
import ic2.api.INetworkDataProvider;
import ic2.api.INetworkUpdateListener;
import ic2.api.IWrenchable;
import ic2.api.Items;
import ic2.api.NetworkHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Facing;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_IC2NuclearControl;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import java.util.List;
import java.util.ArrayList;


public class TileEntityInfoPanel extends TileEntity implements IInventory, ISlotItemFilter, INetworkDataProvider, INetworkUpdateListener, INetworkClientTileEntityEventListener, IWrenchable, IRedstoneConsumer, ITextureHelper, IScreenPart
{
    public static final int DISPLAY_ONOFF = 1;
    public static final int DISPLAY_HEAT = 2;
    public static final int DISPLAY_MAXHEAT = 4;
    public static final int DISPLAY_OUTPUT = 8;
    public static final int DISPLAY_TIME = 16;
    public static final int DISPLAY_MELTING = 32;
    public static final int DISPLAY_DEFAULT = 63;
    public static final int SLOT_CARD = 0;
    public static final int SLOT_UPGRADE = 1;
    private static final int LOCATION_RANGE = 8;
    public int deltaX;
    public int deltaY;
    public int deltaZ;
    private int prevDeltaX;
    private int prevDeltaY;
    private int prevDeltaZ;
    protected int updateTicker;
    protected int tickRate;
    protected boolean init;
    private ItemStack[] inventory = new ItemStack[2];
    private Screen screen = null;
    private boolean prevPowered;
    public boolean powered;
    private int prevDisplaySettings;
    public int displaySettings;
    private int prevHeat;
    public int heat;
    private int prevOutput;
    public int output;
    private boolean prevReactorPowered;
    public boolean reactorPowered;
    private int prevTimeleft;
    public int timeLeft;
    private int prevMaxHeat;
    public int maxHeat;
    private short prevFacing;
    public short facing;
    public List transaction = new ArrayList();

    public void onOpen(CraftHumanEntity crafthumanentity)
    {
        transaction.add(crafthumanentity);
    }

    public void onClose(CraftHumanEntity crafthumanentity)
    {
        transaction.remove(crafthumanentity);
    }

    public List getViewers()
    {
        return transaction;
    }

    public void setMaxStackSize(int i)
    {
    }

    public ItemStack[] getContents()
    {
        return inventory;
    }



    public short getFacing()
    {
        return (short)Facing.OPPOSITE_FACING[this.facing];
    }

    public void setFacing(short var1)
    {
        this.setSide((short)Facing.OPPOSITE_FACING[var1]);
    }

    private void setSide(short var1)
    {
        this.facing = var1;

        if (this.prevFacing != var1)
        {
            NetworkHelper.updateTileEntityField(this, "facing");
        }

        this.prevFacing = var1;
    }

    private void setDeltaX(int var1)
    {
        this.deltaX = var1;

        if (this.prevDeltaX != var1)
        {
            NetworkHelper.updateTileEntityField(this, "deltaX");
        }

        this.prevDeltaX = var1;
    }

    private void setDeltaY(int var1)
    {
        this.deltaY = var1;

        if (this.prevDeltaY != var1)
        {
            NetworkHelper.updateTileEntityField(this, "deltaY");
        }

        this.prevDeltaY = var1;
    }

    private void setDeltaZ(int var1)
    {
        this.deltaZ = var1;

        if (this.prevDeltaZ != var1)
        {
            NetworkHelper.updateTileEntityField(this, "deltaZ");
        }

        this.prevDeltaZ = var1;
    }

    public void setPowered(boolean var1)
    {
        this.powered = var1;

        if (this.prevPowered != var1)
        {
            NetworkHelper.updateTileEntityField(this, "powered");
        }

        this.prevPowered = this.powered;
    }

    public boolean getPowered()
    {
        return this.powered;
    }

    public void setDisplaySettings(int var1)
    {
        this.displaySettings = var1;

        if (this.prevDisplaySettings != var1)
        {
            NetworkHelper.updateTileEntityField(this, "displaySettings");
        }

        this.prevDisplaySettings = this.displaySettings;
    }

    public void setHeat(int var1)
    {
        this.heat = var1;

        if (this.prevHeat != var1)
        {
            NetworkHelper.updateTileEntityField(this, "heat");
        }

        this.prevHeat = this.heat;
    }

    public void setOutput(int var1)
    {
        this.output = var1;

        if (this.prevOutput != var1)
        {
            NetworkHelper.updateTileEntityField(this, "output");
        }

        this.prevOutput = this.output;
    }

    public void setReactorPowered(boolean var1)
    {
        this.reactorPowered = var1;

        if (this.prevReactorPowered != var1)
        {
            NetworkHelper.updateTileEntityField(this, "reactorPowered");
        }

        this.prevReactorPowered = this.reactorPowered;
    }

    public void setTimeLeft(int var1)
    {
        this.timeLeft = var1;

        if (this.prevTimeleft != var1)
        {
            NetworkHelper.updateTileEntityField(this, "timeLeft");
        }

        this.prevTimeleft = this.timeLeft;
    }

    public void setMaxHeat(int var1)
    {
        this.maxHeat = var1;

        if (this.prevMaxHeat != var1)
        {
            NetworkHelper.updateTileEntityField(this, "maxHeat");
        }

        this.prevMaxHeat = this.maxHeat;
    }

    public void onNetworkUpdate(String var1)
    {
        if (var1.equals("facing") && this.prevFacing != this.facing)
        {
            if (mod_IC2NuclearControl.isClient())
            {
                mod_IC2NuclearControl.screenManager.unregisterScreenPart(this);
                mod_IC2NuclearControl.screenManager.registerInfoPanel(this);
            }

            this.world.notify(this.x, this.y, this.z);
            this.prevFacing = this.facing;
        }

        if (var1.equals("powered") && this.prevPowered != this.powered)
        {
            if (this.screen != null)
            {
                this.screen.turnPower(this.powered);
            }
            else
            {
                this.world.notify(this.x, this.y, this.z);
                this.world.v(this.x, this.y, this.z);
            }

            this.prevPowered = this.powered;
        }
    }

    public void onNetworkEvent(EntityHuman var1, int var2)
    {
        this.setDisplaySettings(var2);
    }

    public TileEntityInfoPanel()
    {
        this.prevDeltaX = this.deltaX = 0;
        this.prevDeltaY = this.deltaY = 0;
        this.prevDeltaZ = this.deltaZ = 0;
        this.init = false;
        this.tickRate = -1;
        this.updateTicker = 0;
        this.displaySettings = 63;
        this.prevDisplaySettings = 63;
        this.powered = false;
        this.prevPowered = false;
        this.facing = 0;
        this.prevFacing = 0;
    }

    public List getNetworkedFields()
    {
        ArrayList var1 = new ArrayList(11);
        var1.add("powered");
        var1.add("displaySettings");
        var1.add("heat");
        var1.add("output");
        var1.add("reactorPowered");
        var1.add("timeLeft");
        var1.add("maxHeat");
        var1.add("facing");
        var1.add("deltaX");
        var1.add("deltaY");
        var1.add("deltaZ");
        return var1;
    }

    protected void readData()
    {
        this.update();
        TileEntity var1 = NuclearHelper.getReactorAt(this.world, this.x + this.deltaX, this.y + this.deltaY, this.z + this.deltaZ);

        if (var1 != null)
        {
            if (this.tickRate == -1)
            {
                this.tickRate = NuclearHelper.getReactorTickRate(var1);

                if (this.tickRate == 0)
                {
                    this.tickRate = 1;
                }

                this.updateTicker = this.tickRate;
            }

            this.setHeat(NuclearHelper.getReactorHeat(var1));
            this.setMaxHeat(NuclearHelper.getMaxHeat(var1));
            this.setReactorPowered(NuclearHelper.getReactorIsProducingEnergy(var1));
            this.setOutput(NuclearHelper.getReactorOutput(var1));
            IInventory var2 = (IInventory)var1;
            int var3 = var2.getSize();
            int var4 = 0;
            int var5 = Items.getItem("uraniumCell").id;

            for (int var6 = 0; var6 < var3; ++var6)
            {
                ItemStack var7 = var2.getItem(var6);

                if (var7 != null && var7.id == var5)
                {
                    var4 = Math.max(var4, var7.i() - var7.getData());
                }
            }

            this.setTimeLeft(var4);
        }
    }

    protected void initData()
    {
        if (this.world.isStatic)
        {
            NetworkHelper.requestInitialData(this);
        }
        else
        {
            RedstoneHelper.checkPowered(this.world, this);
        }

        if (mod_IC2NuclearControl.isClient())
        {
            mod_IC2NuclearControl.screenManager.registerInfoPanel(this);
        }

        this.init = true;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void q_()
    {
        if (!this.init)
        {
            this.initData();
        }

        if (!this.world.isStatic)
        {
            if (this.tickRate != -1 && this.updateTicker-- > 0)
            {
                return;
            }

            this.updateTicker = this.tickRate;
            this.readData();
        }

        super.q_();
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void a(NBTTagCompound var1)
    {
        super.a(var1);
        this.prevFacing = this.facing = var1.getShort("facing");
        this.prevDisplaySettings = this.displaySettings = var1.getInt("displaySettings");
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
        super.j();

        if (mod_IC2NuclearControl.isClient())
        {
            mod_IC2NuclearControl.screenManager.unregisterScreenPart(this);
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void b(NBTTagCompound var1)
    {
        super.b(var1);
        var1.setShort("facing", this.facing);
        var1.setInt("displaySettings", this.displaySettings);
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
        return "block.StatusDisplay";
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
        ItemStack var2 = this.inventory[1];

        if (var2 != null && var2.getItem() instanceof ItemRangeUpgrade)
        {
            var1 = var2.count;
        }

        if (this.inventory[0] != null)
        {
            int[] var3 = ItemSensorLocationCard.getCoordinates(this.inventory[0]);

            if (var3 != null)
            {
                this.setDeltaX(var3[0] - this.x);
                this.setDeltaY(var3[1] - this.y);
                this.setDeltaZ(var3[2] - this.z);

                if (var1 > 7)
                {
                    var1 = 7;
                }

                int var4 = 8 * (int)Math.pow(2.0D, (double)var1);

                if (Math.abs(this.deltaX) > var4 || Math.abs(this.deltaY) > var4 || Math.abs(this.deltaZ) > var4)
                {
                    this.setDeltaX(0);
                    this.setDeltaY(0);
                    this.setDeltaZ(0);
                }
            }
            else
            {
                this.setDeltaX(0);
                this.setDeltaY(0);
                this.setDeltaZ(0);
            }
        }
        else
        {
            this.setDeltaX(0);
            this.setDeltaY(0);
            this.setDeltaZ(0);
        }
    }

    public boolean isItemValid(int var1, ItemStack var2)
    {
        switch (var1)
        {
            case 0:
                return var2.getItem() instanceof ItemSensorLocationCard;

            default:
                return var2.getItem() instanceof ItemRangeUpgrade;
        }
    }

    public boolean wrenchCanSetFacing(EntityHuman var1, int var2)
    {
        return this.getFacing() != var2;
    }

    public float getWrenchDropRate()
    {
        return 1.0F;
    }

    public boolean wrenchCanRemove(EntityHuman var1)
    {
        return true;
    }

    public int modifyTextureIndex(int var1)
    {
        return var1 == 11 && this.powered ? var1 + 16 : var1;
    }

    public void setScreen(Screen var1)
    {
        this.screen = var1;
    }

    public Screen getScreen()
    {
        return this.screen;
    }

    public int hashCode()
    {
        boolean var1 = true;
        byte var2 = 1;
        int var3 = 31 * var2 + this.x;
        var3 = 31 * var3 + this.y;
        var3 = 31 * var3 + this.z;
        return var3;
    }

    public boolean equals(Object var1)
    {
        if (this == var1)
        {
            return true;
        }
        else if (var1 == null)
        {
            return false;
        }
        else if (this.getClass() != var1.getClass())
        {
            return false;
        }
        else
        {
            TileEntityInfoPanel var2 = (TileEntityInfoPanel)var1;
            return this.x != var2.x ? false : (this.y != var2.y ? false : (this.z != var2.z ? false : this.world == var2.world));
        }
    }
}
