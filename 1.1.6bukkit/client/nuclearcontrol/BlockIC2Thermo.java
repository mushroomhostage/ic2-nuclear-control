// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BlockIC2Thermo.java

package nuclearcontrol;

import aan;
import acn;
import agy;
import ali;
import forge.ITextureProvider;
import java.util.ArrayList;
import kw;
import mod_IC2NuclearControl;
import qs;
import wu;
import xd;
import yw;

// Referenced classes of package nuclearcontrol:
//            TileEntityIC2Thermo, NuclearHelper

public class BlockIC2Thermo extends agy
    implements ITextureProvider
{

    public BlockIC2Thermo(int i, int j)
    {
        super(i, acn.f);
    }

    public boolean isBlockNormalCube(xd world, int i, int j, int l)
    {
        return false;
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    public boolean b()
    {
        return false;
    }

    public boolean a()
    {
        return false;
    }

    public boolean e(xd world, int x, int y, int z)
    {
        for(int face = 0; face < 6; face++)
        {
            int side = qs.a[face];
            if(world.isBlockSolidOnSide(x + qs.b[side], y + qs.c[side], z + qs.d[side], face))
                return true;
        }

        return false;
    }

    public void c(xd world, int x, int y, int z, int face)
    {
        int side = qs.a[face];
        if(world.isBlockSolidOnSide(x + qs.b[side], y + qs.c[side], z + qs.d[side], face))
        {
            kw tileentity = world.b(x, y, z);
            if(tileentity instanceof TileEntityIC2Thermo)
                ((TileEntityIC2Thermo)tileentity).setFacing((short)side);
        }
    }

    public void a(xd world, int x, int y, int z)
    {
        int face = 0;
        do
        {
            if(face >= 6)
                break;
            int side = qs.a[face];
            if(world.isBlockSolidOnSide(x + qs.b[side], y + qs.c[side], z + qs.d[side], face))
            {
                kw tileentity = world.b(x, y, z);
                if(tileentity instanceof TileEntityIC2Thermo)
                    ((TileEntityIC2Thermo)tileentity).setFacing((short)side);
                break;
            }
            face++;
        } while(true);
        dropBlockIfCantStay(world, x, y, z);
    }

    public void a(xd world, int x, int y, int z, int neighbor)
    {
        int metadata = world.e(x, y, z);
        int side = 0;
        kw tileentity = world.b(x, y, z);
        if(tileentity instanceof TileEntityIC2Thermo)
            side = ((TileEntityIC2Thermo)tileentity).getFacing();
        if(!world.isBlockSolidOnSide(x + qs.b[side], y + qs.c[side], z + qs.d[side], qs.a[side]))
        {
            if(!world.F)
                a(world, x, y, z, metadata, 0);
            world.g(x, y, z, 0);
        }
    }

    private boolean dropBlockIfCantStay(xd world, int x, int y, int z)
    {
        if(!e(world, x, y, z))
        {
            if(world.a(x, y, z) == bO)
            {
                a(world, x, y, z, world.e(x, y, z), 0);
                world.g(x, y, z, 0);
            }
            return false;
        } else
        {
            return true;
        }
    }

    public void h()
    {
        float baseX1 = 0.0625F;
        float baseY1 = 0.0F;
        float baseZ1 = 0.0625F;
        float baseX2 = 0.9375F;
        float baseY2 = 0.4375F;
        float baseZ2 = 0.9375F;
        a(baseX1, baseY1, baseZ1, baseX2, baseY2, baseZ2);
    }

    public void a(ali blockAccess, int x, int y, int z)
    {
        float baseX1 = 0.0625F;
        float baseY1 = 0.0F;
        float baseZ1 = 0.0625F;
        float baseX2 = 0.9375F;
        float baseY2 = 0.4375F;
        float baseZ2 = 0.9375F;
        int side = 0;
        kw tileentity = blockAccess.b(x, y, z);
        if(tileentity instanceof TileEntityIC2Thermo)
            side = ((TileEntityIC2Thermo)tileentity).getFacing();
        switch(side)
        {
        case 1: // '\001'
        {
            baseY1 = 1.0F - baseY1;
            baseY2 = 1.0F - baseY2;
            break;
        }

        case 2: // '\002'
        {
            float tmp = baseY1;
            baseY1 = baseZ1;
            baseZ1 = tmp;
            tmp = baseY2;
            baseY2 = baseZ2;
            baseZ2 = tmp;
            break;
        }

        case 3: // '\003'
        {
            float tmp = baseY1;
            baseY1 = baseZ1;
            baseZ1 = 1.0F - tmp;
            tmp = baseY2;
            baseY2 = baseZ2;
            baseZ2 = 1.0F - tmp;
            break;
        }

        case 4: // '\004'
        {
            float tmp = baseY1;
            baseY1 = baseX1;
            baseX1 = tmp;
            tmp = baseY2;
            baseY2 = baseX2;
            baseX2 = tmp;
            break;
        }

        case 5: // '\005'
        {
            float tmp = baseY1;
            baseY1 = baseX1;
            baseX1 = 1.0F - tmp;
            tmp = baseY2;
            baseY2 = baseX2;
            baseX2 = 1.0F - tmp;
            break;
        }
        }
        a(Math.min(baseX1, baseX2), Math.min(baseY1, baseY2), Math.min(baseZ1, baseZ2), Math.max(baseX1, baseX2), Math.max(baseY1, baseY2), Math.max(baseZ1, baseZ2));
    }

    public String getInvName()
    {
        return "IC2 Thermo";
    }

    public boolean g()
    {
        return true;
    }

    public boolean b(xd world, int x, int y, int z, yw entityplayer)
    {
        if(entityplayer.V())
        {
            return false;
        } else
        {
            mod_IC2NuclearControl.launchGui(world, x, y, z, entityplayer);
            return true;
        }
    }

    public boolean e(xd world, int i, int j, int i1, int j1)
    {
        return false;
    }

    public wu c(xd world, int x, int y, int z)
    {
        a(world, x, y, z);
        return super.c(world, x, y, z);
    }

    public boolean b(ali iblockaccess, int x, int y, int z, int direction)
    {
        int targetX = x;
        int targetY = y;
        int targetZ = z;
        switch(direction)
        {
        case 0: // '\0'
            targetY++;
            break;

        case 1: // '\001'
            targetY--;
            break;

        case 2: // '\002'
            targetZ++;
            break;

        case 3: // '\003'
            targetZ--;
            break;

        case 4: // '\004'
            targetX++;
            break;

        case 5: // '\005'
            targetX--;
            break;
        }
        kw tileentity = iblockaccess.b(targetX, targetY, targetZ);
        if(tileentity != null && (NuclearHelper.getReactorAt(tileentity.i, targetX, targetY, targetZ) != null || NuclearHelper.getReactorChamberAt(tileentity.i, targetX, targetY, targetZ) != null))
            return false;
        tileentity = iblockaccess.b(x, y, z);
        if(tileentity instanceof TileEntityIC2Thermo)
            return ((TileEntityIC2Thermo)tileentity).getOnFire() == 1;
        else
            return false;
    }

    public int a(int side, int metadata)
    {
        int texture = sideMapping[0][side];
        return bN + texture;
    }

    public int d(ali iblockaccess, int x, int y, int z, int side)
    {
        kw tileentity = iblockaccess.b(x, y, z);
        boolean isThermo = tileentity instanceof TileEntityIC2Thermo;
        int metaSide = 0;
        if(isThermo)
            metaSide = ((TileEntityIC2Thermo)tileentity).getFacing();
        int texture = sideMapping[metaSide][side];
        if(texture != 0 || !isThermo)
            return bN + texture;
        byte fireState = ((TileEntityIC2Thermo)tileentity).getOnFire();
        switch(fireState)
        {
        case 1: // '\001'
            texture = 16;
            break;

        case 0: // '\0'
            texture = 0;
            break;

        default:
            texture = 32;
            break;
        }
        return bN + texture;
    }

    public kw u_()
    {
        return new TileEntityIC2Thermo();
    }

    public kw getBlockEntity(int i)
    {
        return u_();
    }

    public void addCreativeItems(ArrayList arraylist)
    {
        arraylist.add(new aan(this));
    }

    private static final int sideMapping[][] = {
        {
            1, 0, 17, 17, 17, 17
        }, {
            0, 1, 17, 17, 17, 17
        }, {
            17, 17, 1, 0, 33, 33
        }, {
            17, 17, 0, 1, 33, 33
        }, {
            33, 33, 33, 33, 1, 0
        }, {
            33, 33, 33, 33, 0, 1
        }
    };

}
