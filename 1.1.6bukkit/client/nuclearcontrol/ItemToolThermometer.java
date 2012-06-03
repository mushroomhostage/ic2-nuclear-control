// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ItemToolThermometer.java

package nuclearcontrol;

import aan;
import forge.ITextureProvider;
import ic2.api.ElectricItem;
import kw;
import mod_IC2NuclearControl;
import xd;
import yr;
import yw;

// Referenced classes of package nuclearcontrol:
//            NuclearHelper, ThermometerVersion

public class ItemToolThermometer extends yr
    implements ITextureProvider
{

    public ItemToolThermometer(int i, int j, ThermometerVersion thermometerversion)
    {
        super(i);
        e(j);
        g(102);
        f(1);
        thermometerVersion = thermometerversion;
    }

    public boolean canTakeDamage(aan itemstack, int i)
    {
        return true;
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public boolean onItemUseFirst(aan itemstack, yw entityplayer, xd world, int x, int y, int z, int l)
    {
        if(!canTakeDamage(itemstack, 2))
            return false;
        kw reactor = NuclearHelper.getReactorAt(world, x, y, z);
        if(reactor == null)
        {
            kw chamber = NuclearHelper.getReactorChamberAt(world, x, y, z);
            if(chamber != null)
                reactor = NuclearHelper.getReactorAroundCoord(world, x, y, z);
        }
        if(reactor != null)
        {
            if(!world.F)
            {
                messagePlayer(entityplayer, reactor);
                damage(itemstack, 1, entityplayer);
            }
            return true;
        } else
        {
            return false;
        }
    }

    public void messagePlayer(yw entityplayer, kw reactor)
    {
        int heat = NuclearHelper.getReactorHeat(reactor);
        static class _cls1
        {

            static final int $SwitchMap$net$minecraft$src$nuclearcontrol$ThermometerVersion[];

            static 
            {
                $SwitchMap$net$minecraft$src$nuclearcontrol$ThermometerVersion = new int[ThermometerVersion.values().length];
                try
                {
                    $SwitchMap$net$minecraft$src$nuclearcontrol$ThermometerVersion[ThermometerVersion.ANALOG.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$net$minecraft$src$nuclearcontrol$ThermometerVersion[ThermometerVersion.DIGITAL.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
            }
        }

        switch(_cls1..SwitchMap.net.minecraft.src.nuclearcontrol.ThermometerVersion[thermometerVersion.ordinal()])
        {
        case 1: // '\001'
            mod_IC2NuclearControl.chatMessage(entityplayer, (new StringBuilder()).append("Hull heat: ").append(heat).toString());
            break;

        case 2: // '\002'
            int maxHeat = NuclearHelper.getMaxHeat(reactor);
            mod_IC2NuclearControl.chatMessage(entityplayer, (new StringBuilder()).append("Hull heat: ").append(heat).append(" (Water evaporate: ").append((maxHeat * 50) / 100).append(" / melting: ").append((maxHeat * 85) / 100).append(")").toString());
            break;
        }
    }

    public void damage(aan itemstack, int i, yw entityplayer)
    {
        switch(_cls1..SwitchMap.net.minecraft.src.nuclearcontrol.ThermometerVersion[thermometerVersion.ordinal()])
        {
        case 1: // '\001'
            itemstack.a(10, entityplayer);
            break;

        case 2: // '\002'
            ElectricItem.use(itemstack, 50 * i, entityplayer);
            break;
        }
    }

    public ThermometerVersion thermometerVersion;
}
