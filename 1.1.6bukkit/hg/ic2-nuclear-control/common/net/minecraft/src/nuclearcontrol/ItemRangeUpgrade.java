package net.minecraft.src.nuclearcontrol;

import net.minecraft.src.Item;
import net.minecraft.src.forge.ITextureProvider;

public class ItemRangeUpgrade extends Item implements ITextureProvider
{

    public ItemRangeUpgrade(int i, int iconIndex)
    {
        super(i);
        setIconIndex(iconIndex);
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

}
