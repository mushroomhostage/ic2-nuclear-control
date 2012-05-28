package net.minecraft.src.nuclearcontrol;

import java.util.ArrayList;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.forge.ITextureProvider;

public class ItemSensorLocationCard extends Item implements ITextureProvider
{

    public ItemSensorLocationCard(int i, int iconIndex)
    {
        super(i);
        setIconIndex(iconIndex);
        setMaxStackSize(1);
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }
    
    public static int[] getCoordinates(ItemStack itemStack)
    {
        if(!(itemStack.getItem() instanceof ItemSensorLocationCard))
            return null;
        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
        if (nbtTagCompound == null)
        {
            return null;
        }
        int[] coordinates = new int[]{
            nbtTagCompound.getInteger("x"),  
            nbtTagCompound.getInteger("y"),  
            nbtTagCompound.getInteger("z")  
        };
        return coordinates;
    }
    
    public static void setCoordinates(ItemStack itemStack, int x, int y, int z)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        itemStack.setTagCompound(nbttagcompound);
        nbttagcompound.setInteger("x", x);
        nbttagcompound.setInteger("y", y);
        nbttagcompound.setInteger("z", z);
    }

    @Override
    public void addCreativeItems(ArrayList arraylist)
    {
        //should not be created via creative inventory
    }
}
