package nuclearcontrol;

import forge.ITextureProvider;
import java.util.ArrayList;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class ItemSensorLocationCard extends Item implements ITextureProvider
{
    public ItemSensorLocationCard(int var1, int var2)
    {
        super(var1);
        this.d(var2);
        this.e(1);
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public static int[] getCoordinates(ItemStack var0)
    {
        if (!(var0.getItem() instanceof ItemSensorLocationCard))
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
        NBTTagCompound var4 = new NBTTagCompound();
        var0.setTag(var4);
        var4.setInt("x", var1);
        var4.setInt("y", var2);
        var4.setInt("z", var3);
    }

    public void addCreativeItems(ArrayList var1) {}
}
