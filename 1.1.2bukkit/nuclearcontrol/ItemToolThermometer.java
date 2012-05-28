// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ItemToolThermometer.java

package nuclearcontrol;

import forge.ITextureProvider;
import ic2.api.ElectricItem;
import net.minecraft.server.*;

// Referenced classes of package nuclearcontrol:
//            NuclearHelper, ThermometerVersion

public class ItemToolThermometer extends Item
    implements ITextureProvider
{

    public ItemToolThermometer(int i, int j, ThermometerVersion thermometerversion)
    {
        super(i);
        d(j);
        setMaxDurability(102);
        e(1);
        thermometerVersion = thermometerversion;
    }

    public boolean canTakeDamage(ItemStack itemstack, int i)
    {
        return true;
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l)
    {
        if(!canTakeDamage(itemstack, 2))
            return false;
        TileEntity tileentity = NuclearHelper.getReactorAt(world, i, j, k);
        if(tileentity == null)
        {
            TileEntity tileentity1 = NuclearHelper.getReactorChamberAt(world, i, j, k);
            if(tileentity1 != null)
                tileentity = NuclearHelper.getReactorAroundCoord(world, i, j, k);
        }
        if(tileentity != null)
        {
            if(!world.isStatic)
            {
                messagePlayer(entityhuman, tileentity);
                damage(itemstack, 1, entityhuman);
            }
            return true;
        } else
        {
            return false;
        }
    }

    public void messagePlayer(EntityHuman entityhuman, TileEntity tileentity)
    {
        int i = NuclearHelper.getReactorHeat(tileentity);
        static class _cls1
        {

            static final int $SwitchMap$nuclearcontrol$ThermometerVersion[];

            static 
            {
                $SwitchMap$nuclearcontrol$ThermometerVersion = new int[ThermometerVersion.values().length];
                try
                {
                    $SwitchMap$nuclearcontrol$ThermometerVersion[ThermometerVersion.ANALOG.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$nuclearcontrol$ThermometerVersion[ThermometerVersion.DIGITAL.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
            }
        }

        switch(_cls1..SwitchMap.nuclearcontrol.ThermometerVersion[thermometerVersion.ordinal()])
        {
        case 1: // '\001'
            mod_IC2NuclearControl.chatMessage(entityhuman, (new StringBuilder()).append("Hull heat: ").append(i).toString());
            break;

        case 2: // '\002'
            int j = NuclearHelper.getMaxHeat(tileentity);
            mod_IC2NuclearControl.chatMessage(entityhuman, (new StringBuilder()).append("Hull heat: ").append(i).append(" (Water evaporate: ").append((j * 50) / 100).append(" / melting: ").append((j * 85) / 100).append(")").toString());
            break;
        }
    }

    public void damage(ItemStack itemstack, int i, EntityHuman entityhuman)
    {
        switch(_cls1..SwitchMap.nuclearcontrol.ThermometerVersion[thermometerVersion.ordinal()])
        {
        case 1: // '\001'
            itemstack.damage(10, entityhuman);
            break;

        case 2: // '\002'
            ElectricItem.use(itemstack, 50 * i, entityhuman);
            break;
        }
    }

    public ThermometerVersion thermometerVersion;
}
