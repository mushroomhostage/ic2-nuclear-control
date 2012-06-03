package nuclearcontrol;

import forge.ITextureProvider;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_IC2NuclearControl;

public class ItemRemoteSensorKit extends Item implements ITextureProvider
{
    public ItemRemoteSensorKit(int var1, int var2)
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
            TileEntity var8 = NuclearHelper.getReactorAt(var3, var4, var5, var6);

            if (var8 == null)
            {
                TileEntity var9 = NuclearHelper.getReactorChamberAt(var3, var4, var5, var6);

                if (var9 != null)
                {
                    var8 = NuclearHelper.getReactorAroundCoord(var3, var4, var5, var6);
                }
            }

            if (var8 != null)
            {
                ItemStack var10 = new ItemStack(mod_IC2NuclearControl.itemSensorLocationCard, 1, 0);
                ItemSensorLocationCard.setCoordinates(var10, var8.x, var8.y, var8.z);
                var2.inventory.items[var2.inventory.itemInHandIndex] = var10;

                if (!var3.isStatic)
                {
                    mod_IC2NuclearControl.chatMessage(var2, "Remote Sensor mounted, Sensor Location Card received");
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
