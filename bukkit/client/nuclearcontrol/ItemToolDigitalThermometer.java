// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ItemToolDigitalThermometer.java

package nuclearcontrol;

import aan;
import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import yw;

// Referenced classes of package nuclearcontrol:
//            ItemToolThermometer, ThermometerVersion

public class ItemToolDigitalThermometer extends ItemToolThermometer
    implements IElectricItem
{

    public ItemToolDigitalThermometer(int i, int j, ThermometerVersion thermometerversion, int k, int l, int i1)
    {
        super(i, j, thermometerversion);
        g(101);
        tier = k;
        ratio = l;
        transfer = i1;
    }

    public boolean canTakeDamage(aan itemstack, int i)
    {
        i *= 50;
        return ElectricItem.discharge(itemstack, i, 0x7fffffff, true, true) == i;
    }

    public void damage(aan itemstack, int i, yw entityplayer)
    {
        ElectricItem.use(itemstack, 50 * i, entityplayer);
    }

    public boolean canProvideEnergy()
    {
        return false;
    }

    public int getChargedItemId()
    {
        return bQ;
    }

    public int getEmptyItemId()
    {
        return bQ;
    }

    public int getMaxCharge()
    {
        return 12000;
    }

    public int getTier()
    {
        return tier;
    }

    public int getTransferLimit()
    {
        return 250;
    }

    public int tier;
    public int ratio;
    public int transfer;
}
