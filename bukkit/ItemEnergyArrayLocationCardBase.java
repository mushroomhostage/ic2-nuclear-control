package nuclearcontrol;

import forge.ITextureProvider;
import ic2.api.IEnergyStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.mod_IC2NuclearControl;
import nuclearcontrol.panel.IPanelDataSource;

public abstract class ItemEnergyArrayLocationCardBase extends Item implements ITextureProvider, IPanelDataSource
{
    public static final int DISPLAY_ENERGY = 1;
    public static final int DISPLAY_FREE = 2;
    public static final int DISPLAY_STORAGE = 4;
    public static final int DISPLAY_EACH = 8;
    public static final int DISPLAY_TOTAL = 16;
    public static final int DISPLAY_PERCENTAGE = 32;
    public static final int CARD_TYPE = 3;

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

    public ItemEnergyArrayLocationCardBase(int var1, int var2)
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

    public static int getCardCount(ItemStack var0)
    {
        if (var0 == null)
        {
            return 0;
        }
        else if (!(var0.getItem() instanceof ItemEnergyArrayLocationCardBase))
        {
            return 0;
        }
        else
        {
            NBTTagCompound var1 = var0.getTag();
            return var1 == null ? 0 : var1.getInt("cardCount");
        }
    }

    public static int[] getCoordinates(ItemStack var0, int var1)
    {
        if (!(var0.getItem() instanceof ItemEnergyArrayLocationCardBase))
        {
            return null;
        }
        else
        {
            NBTTagCompound var2 = var0.getTag();

            if (var2 == null)
            {
                return null;
            }
            else
            {
                int var3 = var2.getInt("cardCount");

                if (var1 >= var3)
                {
                    return null;
                }
                else
                {
                    int[] var4 = new int[] {var2.getInt(String.format("_%dx", new Object[]{Integer.valueOf(var1)})), var2.getInt(String.format("_%dy", new Object[]{Integer.valueOf(var1)})), var2.getInt(String.format("_%dz", new Object[]{Integer.valueOf(var1)}))};
                    return var4;
                }
            }
        }
    }

    public static void setCoordinates(ItemStack var0, int var1, int var2, int var3, int var4)
    {
        NBTTagCompound var5 = getTagCompound(var0);
        var5.setInt(String.format("_%dx", new Object[] {Integer.valueOf(var4)}), var1);
        var5.setInt(String.format("_%dy", new Object[] {Integer.valueOf(var4)}), var2);
        var5.setInt(String.format("_%dz", new Object[] {Integer.valueOf(var4)}), var3);
    }

    public static void initArray(ItemStack var0, Vector var1)
    {
        NBTTagCompound var2 = getTagCompound(var0);
        int var3 = getCardCount(var0);
        Iterator var4 = var1.iterator();

        while (var4.hasNext())
        {
            ItemStack var5 = (ItemStack)var4.next();
            int[] var6 = ItemEnergySensorLocationCard.getCoordinates(var5);

            if (var6 != null)
            {
                var2.setInt(String.format("_%dx", new Object[] {Integer.valueOf(var3)}), var6[0]);
                var2.setInt(String.format("_%dy", new Object[] {Integer.valueOf(var3)}), var6[1]);
                var2.setInt(String.format("_%dz", new Object[] {Integer.valueOf(var3)}), var6[2]);
                ++var3;
            }
        }

        var2.setInt("cardCount", var3);
    }

    public void addCreativeItems(ArrayList var1) {}

    public void update(TileEntityInfoPanel var1, ItemStack var2, int var3)
    {
        NBTTagCompound var4 = getTagCompound(var2);
        int var5 = getCardCount(var2);
        HashMap var6 = new HashMap();

        if (var5 == 0)
        {
            this.setField("activeData", false, var4, var1, var6);
        }
        else
        {
            boolean var7 = false;

            for (int var8 = 0; var8 < var5; ++var8)
            {
                int[] var9 = getCoordinates(var2, var8);
                int var10 = var9[0] - var1.x;
                int var11 = var9[1] - var1.y;
                int var12 = var9[2] - var1.z;

                if (Math.abs(var10) <= var3 && Math.abs(var11) <= var3 && Math.abs(var12) <= var3)
                {
                    IEnergyStorage var13 = EnergyStorageHelper.getStorageAt(var1.world, var9[0], var9[1], var9[2]);

                    if (var13 != null)
                    {
                        this.setField("activeData", true, var4, var1, var6);
                        this.setField(String.format("_%denergy", new Object[] {Integer.valueOf(var8)}), var13.getStored(), var4, var1, var6);
                        this.setField(String.format("_%dmaxStorage", new Object[] {Integer.valueOf(var8)}), var13.getCapacity(), var4, var1, var6);
                        var7 = true;
                    }
                }
            }

            if (!var7)
            {
                this.setField("activeData", false, var4, var1, var6);
            }
        }

        if (!var6.isEmpty())
        {
            mod_IC2NuclearControl.setSensorCardField(var1, var6);
        }
    }

    public int getCardType()
    {
        return 3;
    }

    public abstract void networkUpdate(String var1, int var2, ItemStack var3);

    public abstract List getStringData(int var1, ItemStack var2);

    public abstract List getSettingsList();
}
