package nuclearcontrol;

import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;

public class ItemNuclearControlMain extends ItemBlock
{
    public ItemNuclearControlMain(int var1)
    {
        super(var1);
        this.setMaxDurability(0);
        this.a(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int filterData(int var1)
    {
        return var1;
    }

    public String a(ItemStack var1)
    {
        switch (var1.getData())
        {
            case 0:
                return "tile.blockThermalMonitor";

            case 1:
                return "tile.blockIndustrialAlarm";

            case 2:
                return "tile.blockHowlerAlarm";

            case 3:
                return "tile.blockRemoteThermo";

            default:
                return "";
        }
    }
}
