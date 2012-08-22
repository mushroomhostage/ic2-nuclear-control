package nuclearcontrol;

import forge.ITextureProvider;
import java.util.List;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import nuclearcontrol.panel.IPanelDataSource;

public class ItemTimeCard extends Item implements ITextureProvider, IPanelDataSource
{
    public static final int CARD_TYPE = 1;

    public ItemTimeCard(int var1, int var2)
    {
        super(var1);
        this.d(var2);
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public void update(TileEntityInfoPanel var1, ItemStack var2, int var3) {}

    public void networkUpdate(String var1, int var2, ItemStack var3) {}

    public List getStringData(int var1, ItemStack var2)
    {
        return null;
    }

    public List getSettingsList()
    {
        return null;
    }

    public int getCardType()
    {
        return 1;
    }
}
