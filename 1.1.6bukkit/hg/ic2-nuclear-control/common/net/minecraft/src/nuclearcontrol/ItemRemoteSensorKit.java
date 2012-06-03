package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_IC2NuclearControl;
import net.minecraft.src.forge.ITextureProvider;

public class ItemRemoteSensorKit extends Item implements ITextureProvider
{

    public ItemRemoteSensorKit(int i, int iconIndex)
    {
        super(i);
        setIconIndex(iconIndex);
        setMaxStackSize(1);
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int l)
    {
        if(entityPlayer==null)
            return false;
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
            ItemStack sensorLocationCard = new ItemStack(mod_IC2NuclearControl.itemSensorLocationCard, 1, 0);
            ItemSensorLocationCard.setCoordinates(sensorLocationCard, reactor.xCoord, reactor.yCoord, reactor.zCoord);
            entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = sensorLocationCard;
        	if(!world.isRemote)
        	{
        	    mod_IC2NuclearControl.chatMessage(entityPlayer, "Remote Sensor mounted, Sensor Location Card received");
        	}
        	return true;
        }
        return false;

    }

}
