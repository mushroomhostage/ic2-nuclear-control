package net.minecraft.src.nuclearcontrol;

import java.util.List;
import java.util.Vector;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Facing;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_IC2NuclearControl;
import net.minecraft.src.ic2.api.INetworkDataProvider;
import net.minecraft.src.ic2.api.INetworkUpdateListener;
import net.minecraft.src.ic2.api.IWrenchable;
import net.minecraft.src.ic2.api.NetworkHelper;

public class TileEntityHowlerAlarm extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, IWrenchable, IRedstoneConsumer
{
    private boolean init;
    private short prevFacing;
    public short facing;
    private int updateTicker;
    protected int tickRate;
    public boolean powered;
    public boolean prevPowered;
    private String soundId;
    
    public TileEntityHowlerAlarm()
    {
        facing = 0;
        prevFacing = 0;
        init = false;
        tickRate = 2;
        updateTicker = 0;
        powered = false;
        prevPowered = false;
    }

    private void initData()
    {
        if(worldObj.isRemote){
            NetworkHelper.requestInitialData(this);
        }
        else
        {
            RedstoneHelper.checkPowered(worldObj, this);
        }
        init = true;
    }
    
    @Override
    public short getFacing()
    {
        return (short)Facing.faceToSide[facing];
    }
    
    @Override
    public void setFacing(short f)
    {
        setSide((short)Facing.faceToSide[f]);
    
    }

    private void setSide(short f)
    {
        facing = f;

        if (prevFacing != f)
        {
            NetworkHelper.updateTileEntityField(this, "facing");
        }

        prevFacing = f;
    }

    @Override
    public boolean getPowered()
    {
        return powered;
    }
    
    @Override
    public void invalidate()
    {
        if(soundId != null)
        {
            SoundHelper.stopAlarm(soundId);
            soundId = null;
        }
        super.invalidate();
    }

    @Override
    public void setPowered(boolean value)
    {
        powered = value;

        if (prevPowered != value)
        {
            if(powered)
            {
                if(soundId == null)
                    soundId = SoundHelper.playAlarm(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "ic2nuclearControl.alarm", mod_IC2NuclearControl.alarmRange);
            }
            else
            {
                if(soundId != null)
                {
                    SoundHelper.stopAlarm(soundId);
                    soundId = null;
                }
                    
            }
            NetworkHelper.updateTileEntityField(this, "powered");
        }

        prevPowered = value;
    }
    
    public void setPoweredNoNotify(boolean value)
    {
        powered = value;

        if (prevPowered != value)
        {
            if(powered)
            {
                if(soundId == null)
                    soundId = SoundHelper.playAlarm(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "ic2nuclearControl.alarm", mod_IC2NuclearControl.alarmRange);
            }
            else
            {
                if(soundId != null)
                {
                    SoundHelper.stopAlarm(soundId);
                    soundId = null;
                }
                    
            }
        }
        prevPowered = value;
    }
    
    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return false;
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public float getWrenchDropRate()
    {
        return 1;
    }

    @Override
    public void onNetworkUpdate(String field)
    {
        if (field.equals("facing") && prevFacing != facing)
        {
            worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
            prevFacing = facing;
        }
        if (field.equals("powered") && prevPowered != powered)
        {
            setPoweredNoNotify(powered);
            worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public List<String> getNetworkedFields()
    {
        Vector<String> vector = new Vector<String>(2);
        vector.add("facing");
        vector.add("powered");
        return vector;
    }
    
    @Override
    public void updateEntity()
    {
        if (!init)
        {
            initData();
        }
        super.updateEntity();
        if (mod_IC2NuclearControl.isClient())
        {
            if (tickRate != -1 && updateTicker-- > 0)
                return;
            updateTicker = tickRate;
            checkStatus();
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        prevFacing = facing =  nbttagcompound.getShort("facing");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("facing", facing);
    }
    
    protected void checkStatus()
    {
        if(powered && (soundId==null || !SoundHelper.isPlaying(soundId))){
            soundId = SoundHelper.playAlarm(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "ic2nuclearControl.alarm", mod_IC2NuclearControl.alarmRange);
        }
    }

}
