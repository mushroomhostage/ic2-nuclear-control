package net.minecraft.src.nuclearcontrol;

import java.util.ArrayList;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ic2.api.ElectricItem;
import net.minecraft.src.ic2.api.IElectricItem;

public class ItemToolDigitalThermometer extends ItemToolThermometer
    implements IElectricItem
{

    public int tier;
    public int ratio;
    public int transfer;

    public ItemToolDigitalThermometer(int i, int j, ThermometerVersion thermometerversion, int k, int l, int i1)
    {
        super(i, j, thermometerversion);
        setMaxDamage(101);
        tier = k;
        ratio = l;
        transfer = i1;
    }

    public boolean canTakeDamage(ItemStack itemstack, int i)
    {
        i *= 50;
        return ElectricItem.discharge(itemstack, i, 0x7fffffff, true, true) == i;
    }
    
    public void damage(ItemStack itemstack, int i, EntityPlayer entityplayer)
    {
        ElectricItem.use(itemstack, 50 * i, entityplayer);
    }
    
	@Override
	public boolean canProvideEnergy() {
		return false;
	}

	@Override
	public int getChargedItemId() {
		return shiftedIndex;
	}

	@Override
	public int getEmptyItemId() {
		return shiftedIndex;
	}

	@Override
	public int getMaxCharge() {
		return 12000;
	}

	@Override
	public int getTier() {
		return tier;
	}

	@Override
	public int getTransferLimit() {
		return 250;
	}

	@Override
    public void addCreativeItems(ArrayList arraylist)
    {
        ItemStack itemstack = new ItemStack(this, 1);
        ElectricItem.charge(itemstack, 0x7fffffff, 0x7fffffff, true, false);
        arraylist.add(itemstack);
        arraylist.add(new ItemStack(this, 1, getMaxDamage()));
    }

}
