package net.minecraft.src.nuclearcontrol;

public class TileEntityIndustrialAlarm extends TileEntityHowlerAlarm implements ITextureHelper
{
    private static final byte[] lightSteps = {0, 7, 15, 7, 0};

    protected byte internalFire;
    public byte lightLevel;

    public TileEntityIndustrialAlarm()
    {
        super();
        internalFire = 0;
        lightLevel = 0;
    }

    @Override
    protected void checkStatus()
    {
        super.checkStatus();
        int light = lightLevel;
        if(!powered){
            lightLevel = 0;
            internalFire = 0;
        }
        else
        {
            internalFire = (byte)((internalFire + 1) % lightSteps.length*2);
            lightLevel = lightSteps[internalFire/2];
        }
        if(lightLevel!=light)
            worldObj.updateAllLightTypes(xCoord, yCoord, zCoord);
    }

    @Override
    public int modifyTextureIndex(int texture)
    {
        switch(lightLevel)
        {
            case 7: 
                texture +=16;
                break;
            case 15: 
                texture += 32;
                break;
        }
        return texture;
    }
    
}
