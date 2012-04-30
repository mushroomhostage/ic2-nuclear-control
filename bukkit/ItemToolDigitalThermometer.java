package nuclearcontrol;

import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;

public class ItemToolDigitalThermometer extends ItemToolThermometer implements IElectricItem
{
    public int tier;
    public int ratio;
    public int transfer;

    public ItemToolDigitalThermometer(int var1, int var2, ThermometerVersion var3, int var4, int var5, int var6)
    {
        super(var1, var2, var3);
        this.setMaxDurability(101);
        this.tier = var4;
        this.ratio = var5;
        this.transfer = var6;
    }

    public boolean canTakeDamage(ItemStack var1, int var2)
    {
        var2 *= 50;
        return ElectricItem.discharge(var1, var2, Integer.MAX_VALUE, true, true) == var2;
    }

    public void damage(ItemStack var1, int var2, EntityHuman var3)
    {
        ElectricItem.use(var1, 50 * var2, var3);
    }

    public boolean canProvideEnergy()
    {
        return false;
    }

    public int getChargedItemId()
    {
        return this.id;
    }

    public int getEmptyItemId()
    {
        return this.id;
    }

    public int getMaxCharge()
    {
        return 12000;
    }

    public int getTier()
    {
        return this.tier;
    }

    public int getTransferLimit()
    {
        return 250;
    }
}
