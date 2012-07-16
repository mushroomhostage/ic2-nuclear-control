package nuclearcontrol;

import forge.ITextureProvider;
import ic2.api.Items;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_IC2NuclearControl;
import nuclearcontrol.panel.IPanelDataSource;

public abstract class ItemSensorLocationCardBase extends Item implements ITextureProvider, IPanelDataSource
{
    protected static final String HINT_TEMPLATE = "x: %d, y: %d, z: %d";
    public static final int DISPLAY_ONOFF = 1;
    public static final int DISPLAY_HEAT = 2;
    public static final int DISPLAY_MAXHEAT = 4;
    public static final int DISPLAY_OUTPUT = 8;
    public static final int DISPLAY_TIME = 16;
    public static final int DISPLAY_MELTING = 32;
    public static final int CARD_TYPE = 0;

    protected void setField(String var1, int var2, NBTTagCompound var3, TileEntityInfoPanel var4, Map var5)
    {
        if (var3.hasKey(var1))
        {
            int var6 = var3.getInt(var1);

            if (var6 != var2)
            {
                var5.put(var1, Integer.valueOf(var2));
            }
        }

        var3.setInt(var1, var2);
    }

    protected void setField(String var1, boolean var2, NBTTagCompound var3, TileEntityInfoPanel var4, Map var5)
    {
        this.setField(var1, var2 ? 1 : 0, var3, var4, var5);
    }

    public ItemSensorLocationCardBase(int var1, int var2)
    {
        super(var1);
        this.d(var2);
        this.e(1);
        this.canRepair = false;
    }

    public boolean g()
    {
        return true;
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    protected static NBTTagCompound getTagCompound(ItemStack var0)
    {
        NBTTagCompound var1 = var0.getTag();

        if (var1 == null)
        {
            var1 = new NBTTagCompound();
            var0.setTag(var1);
        }

        return var1;
    }

    public static int[] getCoordinates(ItemStack var0)
    {
        if (!(var0.getItem() instanceof ItemSensorLocationCardBase))
        {
            return null;
        }
        else
        {
            NBTTagCompound var1 = var0.getTag();

            if (var1 == null)
            {
                return null;
            }
            else
            {
                int[] var2 = new int[] {var1.getInt("x"), var1.getInt("y"), var1.getInt("z")};
                return var2;
            }
        }
    }

    public static void setCoordinates(ItemStack var0, int var1, int var2, int var3)
    {
        NBTTagCompound var4 = getTagCompound(var0);
        var4.setInt("x", var1);
        var4.setInt("y", var2);
        var4.setInt("z", var3);
    }

    public void addCreativeItems(ArrayList var1) {}

    public void update(TileEntityInfoPanel var1, ItemStack var2, int var3)
    {
        NBTTagCompound var4 = getTagCompound(var2);
        int[] var5 = getCoordinates(var2);
        HashMap var6 = new HashMap();

        if (var5 == null)
        {
            this.setField("activeData", false, var4, var1, var6);
        }
        else
        {
            int var7 = var5[0] - var1.x;
            int var8 = var5[1] - var1.y;
            int var9 = var5[2] - var1.z;

            if (Math.abs(var7) <= var3 && Math.abs(var8) <= var3 && Math.abs(var9) <= var3)
            {
                TileEntity var10 = NuclearHelper.getReactorAt(var1.world, var5[0], var5[1], var5[2]);

                if (var10 != null)
                {
                    this.setField("activeData", true, var4, var1, var6);
                    this.setField("heat", NuclearHelper.getReactorHeat(var10), var4, var1, var6);
                    this.setField("maxHeat", NuclearHelper.getMaxHeat(var10), var4, var1, var6);
                    this.setField("reactorPowered", NuclearHelper.getReactorIsProducingEnergy(var10), var4, var1, var6);
                    this.setField("output", NuclearHelper.getReactorOutput(var10), var4, var1, var6);
                    IInventory var11 = (IInventory)var10;
                    int var12 = var11.getSize();
                    int var13 = 0;
                    int var14 = Items.getItem("uraniumCell").id;

                    for (int var15 = 0; var15 < var12; ++var15)
                    {
                        ItemStack var16 = var11.getItem(var15);

                        if (var16 != null && var16.id == var14)
                        {
                            var13 = Math.max(var13, var16.i() - var16.getData());
                        }
                    }

                    this.setField("timeLeft", var13, var4, var1, var6);
                }
                else
                {
                    this.setField("activeData", false, var4, var1, var6);
                }
            }
            else
            {
                this.setField("activeData", false, var4, var1, var6);
            }

            if (!var6.isEmpty())
            {
                mod_IC2NuclearControl.setSensorCardField(var1, var6);
            }
        }
    }

    public int getCardType()
    {
        return 0;
    }

    public abstract void networkUpdate(String var1, int var2, ItemStack var3);

    public abstract List getStringData(int var1, ItemStack var2);

    public abstract List getSettingsList();
}
