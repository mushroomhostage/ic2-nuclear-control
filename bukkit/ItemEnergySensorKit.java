package nuclearcontrol;

import forge.ITextureProvider;
import ic2.api.IEnergyStorage;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.mod_IC2NuclearControl;

public class ItemEnergySensorKit extends Item implements ITextureProvider
{
    public ItemEnergySensorKit(int var1, int var2)
    {
        super(var1);
        this.d(var2);
        this.e(1);
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public boolean onItemUseFirst(ItemStack var1, EntityHuman var2, World var3, int var4, int var5, int var6, int var7)
    {
        if (var2 == null)
        {
            return false;
        }
        else
        {
            IEnergyStorage var8 = EnergyStorageHelper.getStorageAt(var3, var4, var5, var6);

            if (var8 == null)
            {
                return false;
            }
            else
            {
                ItemStack var9 = new ItemStack(mod_IC2NuclearControl.itemEnergySensorLocationCard, 1, 0);
                ItemSensorLocationCardBase.setCoordinates(var9, var4, var5, var6);
                var2.inventory.items[var2.inventory.itemInHandIndex] = var9;

                if (!var3.isStatic)
                {
                    mod_IC2NuclearControl.chatMessage(var2, "ic2:nc:c7518eb6:SensorKit");
                }

                return true;
            }
        }
    }
}
