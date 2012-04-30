// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NuclearHelper.java

package nuclearcontrol;

import aan;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import kw;
import qo;
import xd;

public class NuclearHelper
{

    public NuclearHelper()
    {
    }

    private static String getReactorPackage()
    {
        if(reactorPackage != null)
            return reactorPackage;
        Package rPackage = ic2/api/Ic2Recipes.getPackage();
        String result;
        if(rPackage != null)
            result = rPackage.getName().substring(0, rPackage.getName().lastIndexOf('.'));
        else
            result = "net.minecraft.src.ic2";
        result = (new StringBuilder()).append(result).append(".common").toString();
        reactorPackage = result;
        return result;
    }

    private static Class getReactorTileEntityClass()
    {
        try
        {
            if(reactorTileEntityClass == null)
                reactorTileEntityClass = Class.forName((new StringBuilder()).append(getReactorPackage()).append(".TileEntityNuclearReactor").toString());
        }
        catch(Exception exception)
        {
            throw new RuntimeException(exception);
        }
        return reactorTileEntityClass;
    }

    private static Class getReactorChamberTileEntityClass()
    {
        try
        {
            if(reactorChamberTileEntityClass == null)
                reactorChamberTileEntityClass = Class.forName((new StringBuilder()).append(getReactorPackage()).append(".TileEntityReactorChamber").toString());
        }
        catch(Exception exception)
        {
            throw new RuntimeException(exception);
        }
        return reactorChamberTileEntityClass;
    }

    private static aan getReactor()
    {
        if(reactor == null)
            reactor = Items.getItem("nuclearReactor");
        return reactor;
    }

    private static aan getChamber()
    {
        if(chamber == null)
            chamber = Items.getItem("reactorChamber");
        return chamber;
    }

    private static Field getHeatField()
        throws NoSuchFieldException
    {
        if(heatField == null)
            heatField = getReactorTileEntityClass().getField("heat");
        return heatField;
    }

    public static kw getReactorAt(xd world, int x, int y, int z)
    {
        kw entity = world.b(x, y, z);
        if(getReactorTileEntityClass().isInstance(entity) && world.a(x, y, z) == getReactor().c && world.e(x, y, z) == getReactor().i())
            return entity;
        else
            return null;
    }

    public static kw getReactorChamberAt(xd world, int x, int y, int z)
    {
        kw entity = world.b(x, y, z);
        if(getReactorChamberTileEntityClass().isInstance(entity) && world.a(x, y, z) == getChamber().c && world.e(x, y, z) == getChamber().i())
            return entity;
        else
            return null;
    }

    public static kw getReactorAroundCoord(xd world, int x, int y, int z)
    {
        qo around[] = {
            new qo(-1, 0, 0), new qo(1, 0, 0), new qo(0, -1, 0), new qo(0, 1, 0), new qo(0, 0, -1), new qo(0, 0, 1)
        };
        kw ent = null;
        for(int i = 0; i < 6 && ent == null; i++)
        {
            qo delta = around[i];
            ent = getReactorAt(world, x + delta.a, y + delta.b, z + delta.c);
        }

        return ent;
    }

    public static kw getReactorChamberAroundCoord(xd world, int x, int y, int z)
    {
        qo around[] = {
            new qo(-1, 0, 0), new qo(1, 0, 0), new qo(0, -1, 0), new qo(0, 1, 0), new qo(0, 0, -1), new qo(0, 0, 1)
        };
        kw ent = null;
        for(int i = 0; i < 6 && ent == null; i++)
        {
            qo delta = around[i];
            ent = getReactorChamberAt(world, x + delta.a, y + delta.b, z + delta.c);
        }

        return ent;
    }

    public static int getReactorChamberCountAroundCoord(xd world, int x, int y, int z)
    {
        qo around[] = {
            new qo(-1, 0, 0), new qo(1, 0, 0), new qo(0, -1, 0), new qo(0, 1, 0), new qo(0, 0, -1), new qo(0, 0, 1)
        };
        int count = 0;
        kw ent = null;
        for(int i = 0; i < 6; i++)
        {
            qo delta = around[i];
            ent = getReactorChamberAt(world, x + delta.a, y + delta.b, z + delta.c);
            if(ent != null)
                count++;
        }

        return count;
    }

    public static int getMaxHeat(kw reactor)
    {
        int cCount = getReactorChamberCountAroundCoord(reactor.i, reactor.j, reactor.k, reactor.l);
        int maxHeat = 10000 + 1000 * cCount;
        aan plating = Items.getItem("integratedReactorPlating");
        try
        {
            Class partypes[] = {
                Integer.TYPE, Integer.TYPE
            };
            Method getItem = getReactorTileEntityClass().getMethod("getMatrixCoord", partypes);
            for(int j = 0; j < 6; j++)
            {
                for(int k = 0; k < cCount + 3; k++)
                {
                    Object params[] = {
                        new Integer(k), new Integer(j)
                    };
                    aan item = (aan)getItem.invoke(reactor, params);
                    if(item != null && item.c == plating.c)
                        maxHeat += 100;
                }

            }

            return maxHeat;
        }
        catch(Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    public static int getReactorHeat(kw reactor)
    {
        try
        {
            return getHeatField().getInt(reactor);
        }
        catch(Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    public static int getReactorTickRate(kw reactor)
    {
        try
        {
            return ((Integer)getReactorTileEntityClass().getMethod("tickRate", new Class[0]).invoke(reactor, new Object[0])).intValue();
        }
        catch(Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    private static String reactorPackage = null;
    private static Class reactorTileEntityClass = null;
    private static Class reactorChamberTileEntityClass = null;
    private static aan reactor = null;
    private static aan chamber = null;
    private static Field heatField = null;

}
