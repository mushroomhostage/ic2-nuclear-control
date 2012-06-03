package nuclearcontrol;

public class TileEntityIndustrialAlarm extends TileEntityHowlerAlarm implements ITextureHelper
{
    private static final byte[] lightSteps = new byte[] {(byte)0, (byte)7, (byte)15, (byte)7, (byte)0};
    protected byte internalFire = 0;
    public byte lightLevel = 0;

    protected void checkStatus()
    {
        super.checkStatus();
        byte var1 = this.lightLevel;

        if (!this.powered)
        {
            this.lightLevel = 0;
            this.internalFire = 0;
        }
        else
        {
            this.internalFire = (byte)((this.internalFire + 1) % lightSteps.length * 2);
            this.lightLevel = lightSteps[this.internalFire / 2];
        }

        if (this.lightLevel != var1)
        {
            this.world.v(this.x, this.y, this.z);
        }
    }

    public int modifyTextureIndex(int var1)
    {
        switch (this.lightLevel)
        {
            case 7:
                var1 += 16;
                break;

            case 15:
                var1 += 32;
        }

        return var1;
    }
}
