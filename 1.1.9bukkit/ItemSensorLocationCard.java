package nuclearcontrol;

import forge.ITextureProvider;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class ItemSensorLocationCard extends Item implements ITextureProvider
{
    private static final String HINT_TEMPLATE = "x: %d, y: %d, z: %d";

    public ItemSensorLocationCard(int var1, int var2)
    {
        super(var1);
        this.d(var2);
        this.e(1);
    }

    public boolean g()
    {
        return true;
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

    public void addInformation(ItemStack var1, List var2)
    {
        int[] var3 = getCoordinates(var1);

        if (var3 != null)
        {
            String var4 = String.format("x: %d, y: %d, z: %d", new Object[] {Integer.valueOf(var3[0]), Integer.valueOf(var3[1]), Integer.valueOf(var3[2])});
            var2.add(var4);
        }
    }

    public void addCreativeItems(ArrayList var1) {}
}
