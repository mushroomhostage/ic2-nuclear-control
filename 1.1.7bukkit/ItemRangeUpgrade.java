package nuclearcontrol;

import forge.ITextureProvider;
import net.minecraft.server.Item;

public class ItemRangeUpgrade extends Item implements ITextureProvider
{
    public ItemRangeUpgrade(int var1, int var2)
    {
        super(var1);
        this.d(var2);
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }
}
