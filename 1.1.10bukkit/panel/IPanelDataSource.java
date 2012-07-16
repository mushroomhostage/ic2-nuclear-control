package nuclearcontrol.panel;

import java.util.List;
import net.minecraft.server.ItemStack;
import nuclearcontrol.TileEntityInfoPanel;

public interface IPanelDataSource
{
    void update(TileEntityInfoPanel var1, ItemStack var2, int var3);

    void networkUpdate(String var1, int var2, ItemStack var3);

    List getStringData(int var1, ItemStack var2);

    List getSettingsList();

    int getCardType();
}
