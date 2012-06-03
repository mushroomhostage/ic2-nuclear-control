// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileEntityIC2Thermo.java

package nuclearcontrol;

import ady;
import ic2.api.*;
import java.util.List;
import java.util.Vector;
import kw;
import xd;
import yw;

// Referenced classes of package nuclearcontrol:
//            NuclearHelper

public class TileEntityIC2Thermo extends kw
    implements INetworkDataProvider, INetworkUpdateListener, INetworkClientTileEntityEventListener, IWrenchable
{

    public TileEntityIC2Thermo()
    {
        init = false;
        onFire = 0;
        prevOnFire = 0;
        facing = 0;
        prevFacing = 0;
        mappedHeatLevel = 500;
        prevHeatLevel = 500;
        heatLevel = 500;
        updateTicker = 0;
        tickRate = -1;
    }

    public void initData()
    {
        if(i.F)
            NetworkHelper.requestInitialData(this);
        init = true;
    }

    public short getFacing()
    {
        return facing;
    }

    public void setFacing(short f)
    {
        facing = f;
        if(prevFacing != f)
            NetworkHelper.updateTileEntityField(this, "facing");
        prevFacing = f;
    }

    public List getNetworkedFields()
    {
        Vector vector = new Vector(3);
        vector.add("heatLevel");
        vector.add("onFire");
        vector.add("facing");
        return vector;
    }

    public void onNetworkUpdate(String s)
    {
        if(s.equals("heatLevel") && prevHeatLevel != heatLevel)
        {
            i.k(j, k, l);
            prevHeatLevel = heatLevel;
        }
        if(s.equals("facing") && prevFacing != facing)
        {
            i.k(j, k, l);
            prevFacing = facing;
        }
        if(s.equals("onFire") && prevOnFire != onFire)
        {
            i.k(j, k, l);
            i.j(j, k, l, i.a(j, k, l));
            prevOnFire = onFire;
        }
    }

    public void onNetworkEvent(yw entityplayer, int i)
    {
        setHeatLevel(i);
    }

    public void setOnFire(byte f)
    {
        onFire = f;
        if(prevOnFire != f)
            NetworkHelper.updateTileEntityField(this, "onFire");
        prevOnFire = onFire;
    }

    public byte getOnFire()
    {
        return onFire;
    }

    public void setHeatLevel(int h)
    {
        heatLevel = h;
        if(prevHeatLevel != h)
            NetworkHelper.updateTileEntityField(this, "heatLevel");
        prevHeatLevel = heatLevel;
        mappedHeatLevel = (h / 500) * 500;
    }

    public void setHeatLevelWithoutNotify(int h)
    {
        heatLevel = h;
        prevHeatLevel = heatLevel;
        mappedHeatLevel = (h / 500) * 500;
    }

    public int getHeatLevel()
    {
        return heatLevel;
    }

    public void a(ady nbttagcompound)
    {
        super.a(nbttagcompound);
        if(nbttagcompound.c("SliderValue"))
        {
            float sliderValue = nbttagcompound.h("SliderValue");
            int i = 500 + (int)Math.floor(14500F * sliderValue);
            setHeatLevelWithoutNotify(i);
        } else
        if(nbttagcompound.c("heatLevel"))
        {
            int heat = nbttagcompound.f("heatLevel");
            setHeatLevelWithoutNotify(heat);
            prevFacing = facing = nbttagcompound.e("facing");
        }
    }

    public void b(ady nbttagcompound)
    {
        super.b(nbttagcompound);
        nbttagcompound.a("heatLevel", getHeatLevel());
        nbttagcompound.a("facing", facing);
    }

    private void checkStatus()
    {
        kw chamber = NuclearHelper.getReactorChamberAroundCoord(i, j, k, l);
        kw reactor = null;
        if(chamber != null)
            reactor = NuclearHelper.getReactorAroundCoord(i, chamber.j, chamber.k, chamber.l);
        if(reactor == null)
            reactor = NuclearHelper.getReactorAroundCoord(i, j, k, l);
        byte fire;
        if(reactor != null)
        {
            if(tickRate == -1)
            {
                tickRate = NuclearHelper.getReactorTickRate(reactor) / 2;
                if(tickRate == 0)
                    tickRate = 1;
                updateTicker = tickRate;
            }
            int reactorHeat = NuclearHelper.getReactorHeat(reactor);
            if(reactorHeat >= mappedHeatLevel)
                fire = 1;
            else
                fire = 0;
        } else
        {
            fire = -1;
        }
        if(fire != getOnFire())
        {
            setOnFire(fire);
            i.j(j, k, l, i.a(j, k, l));
        }
    }

    public void n_()
    {
        if(!init)
            initData();
        super.n_();
        if(!i.F)
        {
            if(tickRate != -1 && updateTicker-- > 0)
                return;
            updateTicker = tickRate;
            checkStatus();
        }
    }

    public boolean wrenchCanSetFacing(yw entityPlayer, int side)
    {
        return false;
    }

    public boolean wrenchCanRemove(yw entityPlayer)
    {
        return true;
    }

    public float getWrenchDropRate()
    {
        return 1.0F;
    }

    private boolean init;
    private int prevHeatLevel;
    public int heatLevel;
    private int mappedHeatLevel;
    private byte prevOnFire;
    public byte onFire;
    private short prevFacing;
    public short facing;
    private int updateTicker;
    private int tickRate;
}
