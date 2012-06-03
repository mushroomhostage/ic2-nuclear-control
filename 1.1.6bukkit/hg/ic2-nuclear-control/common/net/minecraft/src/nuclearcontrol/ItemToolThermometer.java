package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_IC2NuclearControl;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.ic2.api.ElectricItem;

public class ItemToolThermometer extends Item implements ITextureProvider
{

    public ThermometerVersion thermometerVersion;

    public ItemToolThermometer(int i, int j, ThermometerVersion thermometerversion)
    {
        super(i);
        setIconIndex(j);
        setMaxDamage(102);
        setMaxStackSize(1);
        thermometerVersion = thermometerversion;
    }

    public boolean canTakeDamage(ItemStack itemstack, int i)
    {
        return true;
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int l)
    {
        if (!canTakeDamage(itemstack, 2))
        {
            return false;
        }
        TileEntity reactor = NuclearHelper.getReactorAt(world, x, y, z);
        if (reactor == null)
        {
        	TileEntity chamber = NuclearHelper.getReactorChamberAt(world, x, y, z);
        	if(chamber!=null)
        	{
        		reactor = NuclearHelper.getReactorAroundCoord(world, x, y, z);
        	}
        }
        
        if(reactor != null)
        {
        	if(!world.isRemote)
        	{
                messagePlayer(entityplayer, reactor);
                damage(itemstack, 1, entityplayer);
        	}
        	return true;
        }
        return false;

    }

    public void messagePlayer(EntityPlayer entityplayer, TileEntity reactor)
    {
        int heat = NuclearHelper.getReactorHeat(reactor);
        switch(thermometerVersion)
        {
        case ANALOG: 
        	mod_IC2NuclearControl.chatMessage(entityplayer, "Hull heat: " + heat);
            break;

        case DIGITAL: 
            int maxHeat = NuclearHelper.getMaxHeat(reactor);
            mod_IC2NuclearControl.chatMessage(entityplayer, "Hull heat: " + heat + " (Water evaporate: " +((maxHeat * 50) / 100) + 
            		" / melting: "+ ((maxHeat * 85) / 100) + ")");
            break;
        }
    }

    public void damage(ItemStack itemstack, int i, EntityPlayer entityplayer)
    {
        switch(thermometerVersion)
        {
        case ANALOG: 
            itemstack.damageItem(10, entityplayer);
            break;

        case DIGITAL: 
            ElectricItem.use(itemstack, 50*i, entityplayer);
            break;
        }
    }

}
