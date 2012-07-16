package nuclearcontrol;

import ic2.api.Items;
import java.util.Vector;
import net.minecraft.server.CraftingRecipe;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class StorageArrayRecipe implements CraftingRecipe
{
    public org.bukkit.inventory.ShapelessRecipe toBukkitRecipe() 
    {
        // TODO: try to return a real recipe wrapper, like IC2 v1.97 r1 does?
        return null;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean a(InventoryCrafting var1)
    {
        return this.b(var1) != null;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack b(InventoryCrafting var1)
    {
        int var2 = var1.getSize();
        boolean var3 = false;
        int var4 = 0;
        int var5 = 0;
        ItemStack var6 = null;
        Vector var7 = new Vector();
        int var8;
        ItemStack var9;

        for (var8 = 0; var8 < var2; ++var8)
        {
            var9 = var1.getItem(var8);

            if (var9 != null)
            {
                if (var9.getItem() instanceof ItemEnergySensorLocationCard)
                {
                    var7.add(var9);
                    ++var4;
                }
                else
                {
                    if (!(var9.getItem() instanceof ItemEnergyArrayLocationCard))
                    {
                        var3 = true;
                        break;
                    }

                    var6 = var9;
                    ++var5;
                }
            }
        }

        if (var3)
        {
            return null;
        }
        else if (var4 >= 2 && var4 <= 6 && var5 == 0)
        {
            ItemStack var10 = new ItemStack(IC2NuclearControl.itemEnergyArrayLocationCard, 1, 0);
            ItemEnergyArrayLocationCard.initArray(var10, var7);
            return var10;
        }
        else
        {
            if (var4 == 0 && var5 == 1)
            {
                var8 = ItemEnergyArrayLocationCard.getCardCount(var6);

                if (var8 > 0)
                {
                    return new ItemStack(Items.getItem("electronicCircuit").getItem(), 2 * var8, 0);
                }
            }
            else if (var5 == 1 && var4 > 0)
            {
                var8 = ItemEnergyArrayLocationCard.getCardCount(var6);

                if (var8 + var4 <= 6)
                {
                    var9 = new ItemStack(IC2NuclearControl.itemEnergyArrayLocationCard, 1, 0);
                    var9.setTag((NBTTagCompound)var6.getTag().clone());
                    ItemEnergyArrayLocationCard.initArray(var9, var7);
                    return var9;
                }
            }

            return null;
        }
    }

    /**
     * Returns the size of the recipe area
     */
    public int a()
    {
        return 2;
    }

    public ItemStack b()
    {
        return null;
    }
}
