package net.minecraft.src.nuclearcontrol;

import java.util.List;
import java.util.Vector;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Facing;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.ic2.api.INetworkClientTileEntityEventListener;
import net.minecraft.src.ic2.api.INetworkDataProvider;
import net.minecraft.src.ic2.api.INetworkUpdateListener;
import net.minecraft.src.ic2.api.IWrenchable;
import net.minecraft.src.ic2.api.NetworkHelper;

public class TileEntityIC2Thermo extends TileEntity implements 
        INetworkDataProvider, INetworkUpdateListener, 
        INetworkClientTileEntityEventListener, IWrenchable,
        ITextureHelper
{
    protected boolean init;
    private int prevHeatLevel;
    public int heatLevel;
    private int mappedHeatLevel;
    private int prevOnFire;
    public int onFire;
    private short prevFacing;
    public short facing;

    protected int updateTicker;
    protected int tickRate;

    public TileEntityIC2Thermo()
    {
        init = false;
        onFire = 0;
        prevOnFire = 0;
        facing = 0;
        prevFacing = 0;
        mappedHeatLevel = 500;
        prevHeatLevel = 500;
        heatLevel = 500;
        updateTicker = 0;
        tickRate = -1;
    }

    protected void initData()
    {
    	if(worldObj.isRemote){
    		NetworkHelper.requestInitialData(this);
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
    public List<String> getNetworkedFields()
    {
        Vector<String> vector = new Vector<String>(3);
        vector.add("heatLevel");
        vector.add("onFire");
        vector.add("facing");
        return vector;
    }
    
    @Override
    public void onNetworkUpdate(String field)
    {
        if (field.equals("heatLevel") && prevHeatLevel != heatLevel)
        {
            worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
            prevHeatLevel = heatLevel;
        }
        if (field.equals("facing") && prevFacing != facing)
        {
            worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
            prevFacing = facing;
        }
        if (field.equals("onFire") && prevOnFire != onFire)
        {
            worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
            prevOnFire = onFire;
        }
    }

    public void onNetworkEvent(EntityPlayer entityplayer, int i)
    {
        setHeatLevel(i);
    }
    
    public void setOnFire(int f)
    {
        onFire = f;
        if (prevOnFire != f)
        {
            NetworkHelper.updateTileEntityField(this, "onFire");
        }
        prevOnFire = onFire;
    }
    
    public int getOnFire()
    {
        return onFire;
    }
    
    public void setHeatLevel(int h)
    {
        heatLevel = h;
        if (prevHeatLevel != h)
        {
            NetworkHelper.updateTileEntityField(this, "heatLevel");
        }
        prevHeatLevel = heatLevel;
        mappedHeatLevel = (h / 500) * 500;
    }    

    public void setHeatLevelWithoutNotify(int h)
    {
    	heatLevel = h;
        prevHeatLevel = heatLevel;
        mappedHeatLevel = (h/500)*500;
    }
    
    public int getHeatLevel()
    {
    	return heatLevel;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        if(nbttagcompound.hasKey("SliderValue"))
        {//old mods backward compatibility
            float sliderValue = nbttagcompound.getFloat("SliderValue");
            int i = 500+(int)Math.floor(14500F * sliderValue);
            setHeatLevelWithoutNotify(i);
        }
        else
        if(nbttagcompound.hasKey("heatLevel")){
        	int heat = nbttagcompound.getInteger("heatLevel");
        	setHeatLevelWithoutNotify(heat);
        	prevFacing = facing =  nbttagcompound.getShort("facing");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("heatLevel", getHeatLevel());
        nbttagcompound.setShort("facing", facing);
    }

    protected void checkStatus()
    {
    	byte fire;
        TileEntity chamber = NuclearHelper.getReactorChamberAroundCoord(worldObj, xCoord, yCoord, zCoord);
        TileEntity reactor = null;
        if(chamber != null){
        	reactor = NuclearHelper.getReactorAroundCoord(worldObj, chamber.xCoord, chamber.yCoord, chamber.zCoord);
        }
        if(reactor == null){
        	reactor = NuclearHelper.getReactorAroundCoord(worldObj, xCoord, yCoord, zCoord);
        }
        if(reactor != null){
        	if(tickRate == -1)
        	{
        		tickRate = NuclearHelper.getReactorTickRate(reactor) / 2;
        		if(tickRate == 0)
    				tickRate = 1;
        		updateTicker = tickRate;
        	}
        	int reactorHeat = NuclearHelper.getReactorHeat(reactor);
            if (reactorHeat >= mappedHeatLevel)
            {
                fire = 1;
            } 
            else
            {
                fire = 0;
            }
        }
        else
        {
            fire = -1;
        }
        if(fire != getOnFire()){
        	setOnFire(fire);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        }
    }

    @Override
    public void updateEntity()
    {
        if (!init)
        {
            initData();
        }
        super.updateEntity();
        if (!worldObj.isRemote)
        {
            if (tickRate != -1 && updateTicker-- > 0)
                return;
            updateTicker = tickRate;
            checkStatus();
        }

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
    public int modifyTextureIndex(int texture)
    {
        if(texture != 0)
            return texture;
        int fireState = getOnFire();
        switch (fireState)
        {
            case 1:
                texture = 16;
                break;
            case 0:
                texture = 0;
                break;
            default:
                texture = 32;
                break;
        }
        return texture;
    }
}
