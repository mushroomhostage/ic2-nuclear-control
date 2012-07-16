package nuclearcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.Facing;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_IC2NuclearControl;

public class ScreenManager
{
    private final Map screens = new HashMap();
    private final Map unusedPanels = new HashMap();

    private int getWorldKey(World var1)
    {
        return var1.getWorldData().g() == 0 ? var1.worldProvider.dimension : var1.getWorldData().g();
    }

    private boolean isValidExtender(World var1, int var2, int var3, int var4, int var5)
    {
        if (var1.getTypeId(var2, var3, var4) != mod_IC2NuclearControl.blockNuclearControlMain.id)
        {
            return false;
        }
        else
        {
            TileEntity var6 = var1.getTileEntity(var2, var3, var4);
            return !(var6 instanceof TileEntityInfoPanelExtender) ? false : (((TileEntityInfoPanelExtender)var6).facing != var5 ? false : ((IScreenPart)var6).getScreen() == null);
        }
    }

    private void updateScreenBound(Screen var1, int var2, int var3, int var4)
    {
        if (var2 != 0 || var3 != 0 || var4 != 0)
        {
            boolean var5 = var2 + var3 + var4 < 0;
            int var6 = var5 ? 1 : -1;
            World var7 = var1.coreWorld;

            for (int var8 = 0; var8 < 20; ++var8)
            {
                int var9;
                int var11;
                int var13;

                if (var5)
                {
                    var9 = var1.minX + var2;
                    var11 = var1.minY + var3;
                    var13 = var1.minZ + var4;
                }
                else
                {
                    var9 = var1.maxX + var2;
                    var11 = var1.maxY + var3;
                    var13 = var1.maxZ + var4;
                }

                int var10 = var2 != 0 ? 0 : var1.maxX - var1.minX;
                int var12 = var3 != 0 ? 0 : var1.maxY - var1.minY;
                int var14 = var4 != 0 ? 0 : var1.maxZ - var1.minZ;
                boolean var15 = true;

                for (int var16 = 0; var16 <= var10 && var15; ++var16)
                {
                    for (int var17 = 0; var17 <= var12 && var15; ++var17)
                    {
                        for (int var18 = 0; var18 <= var14 && var15; ++var18)
                        {
                            var15 = var1.getCore() != null && this.isValidExtender(var7, var9 + var6 * var16, var11 + var6 * var17, var13 + var6 * var18, var1.getCore().facing);
                        }
                    }
                }

                if (!var15)
                {
                    break;
                }

                if (var5)
                {
                    var1.minX += var2;
                    var1.minY += var3;
                    var1.minZ += var4;
                }
                else
                {
                    var1.maxX += var2;
                    var1.maxY += var3;
                    var1.maxZ += var4;
                }
            }
        }
    }

    private Screen tryBuildFromPanel(TileEntityInfoPanel var1)
    {
        Screen var2 = new Screen();
        var2.maxX = var2.minX = var1.x;
        var2.maxY = var2.minY = var1.y;
        var2.maxZ = var2.minZ = var1.z;
        var2.setCore(var1);
        int var3 = Facing.b[var1.facing] != 0 ? 0 : -1;
        int var4 = Facing.c[var1.facing] != 0 ? 0 : -1;
        int var5 = Facing.d[var1.facing] != 0 ? 0 : -1;
        this.updateScreenBound(var2, var3, 0, 0);
        this.updateScreenBound(var2, -var3, 0, 0);
        this.updateScreenBound(var2, 0, var4, 0);
        this.updateScreenBound(var2, 0, -var4, 0);
        this.updateScreenBound(var2, 0, 0, var5);
        this.updateScreenBound(var2, 0, 0, -var5);

        if (var2.minX == var2.maxX && var2.minY == var2.maxY && var2.minZ == var2.maxZ)
        {
            return null;
        }
        else
        {
            var2.init();
            return var2;
        }
    }

    private void destroyScreen(Screen var1)
    {
        ((List)this.screens.get(Integer.valueOf(this.getWorldKey(var1.coreWorld)))).remove(var1);
        var1.destroy();
    }

    public void unregisterScreenPart(TileEntity var1)
    {
        if (this.screens.containsKey(Integer.valueOf(this.getWorldKey(var1.world))))
        {
            if (this.unusedPanels.containsKey(Integer.valueOf(this.getWorldKey(var1.world))))
            {
                if (var1 instanceof IScreenPart)
                {
                    IScreenPart var2 = (IScreenPart)var1;
                    Screen var3 = var2.getScreen();

                    if (var3 == null)
                    {
                        if (var1 instanceof TileEntityInfoPanel && ((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var1.world)))).contains(var1))
                        {
                            ((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var1.world)))).remove(var1);
                        }
                    }
                    else
                    {
                        TileEntityInfoPanel var4 = var3.getCore();
                        this.destroyScreen(var3);
                        boolean var5 = var1 instanceof TileEntityInfoPanel;

                        if (!var5 && var4 != null)
                        {
                            Screen var6 = this.tryBuildFromPanel(var4);

                            if (var6 == null)
                            {
                                ((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var4.world)))).add(var4);
                            }
                            else
                            {
                                ((List)this.screens.get(Integer.valueOf(this.getWorldKey(var4.world)))).add(var6);
                            }
                        }
                    }
                }
            }
        }
    }

    public void registerInfoPanel(TileEntityInfoPanel var1)
    {
        if (!this.screens.containsKey(Integer.valueOf(this.getWorldKey(var1.world))))
        {
            this.screens.put(Integer.valueOf(this.getWorldKey(var1.world)), new ArrayList());
        }

        if (!this.unusedPanels.containsKey(Integer.valueOf(this.getWorldKey(var1.world))))
        {
            this.unusedPanels.put(Integer.valueOf(this.getWorldKey(var1.world)), new ArrayList());
        }

        Iterator var2 = ((List)this.screens.get(Integer.valueOf(this.getWorldKey(var1.world)))).iterator();

        while (var2.hasNext())
        {
            Screen var3 = (Screen)var2.next();

            if (var3.isBlockPartOf(var1))
            {
                this.destroyScreen(var3);
                break;
            }
        }

        Screen var4 = this.tryBuildFromPanel(var1);

        if (var4 != null)
        {
            ((List)this.screens.get(Integer.valueOf(this.getWorldKey(var1.world)))).add(var4);
        }
        else
        {
            ((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var1.world)))).add(var1);
        }
    }

    public void registerInfoPanelExtender(TileEntityInfoPanelExtender var1)
    {
        if (!this.screens.containsKey(Integer.valueOf(this.getWorldKey(var1.world))))
        {
            this.screens.put(Integer.valueOf(this.getWorldKey(var1.world)), new ArrayList());
        }

        if (!this.unusedPanels.containsKey(Integer.valueOf(this.getWorldKey(var1.world))))
        {
            this.unusedPanels.put(Integer.valueOf(this.getWorldKey(var1.world)), new ArrayList());
        }

        ArrayList var2 = new ArrayList();
        ArrayList var3 = new ArrayList();
        Iterator var4 = ((List)this.screens.get(Integer.valueOf(this.getWorldKey(var1.world)))).iterator();
        Screen var5;

        while (var4.hasNext())
        {
            var5 = (Screen)var4.next();

            if (var5.isBlockNearby(var1) && var5.getCore() != null && var1.facing == var5.getCore().facing)
            {
                var2.add(var5.getCore());
                var3.add(var5);
            }
            else if (var5.isBlockPartOf(var1))
            {
                return;
            }
        }

        var4 = var3.iterator();

        while (var4.hasNext())
        {
            var5 = (Screen)var4.next();
            this.destroyScreen(var5);
        }

        var4 = ((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var1.world)))).iterator();
        TileEntityInfoPanel var7;

        while (var4.hasNext())
        {
            var7 = (TileEntityInfoPanel)var4.next();

            if ((var7.x == var1.x && var7.y == var1.y && (var7.z == var1.z + 1 || var7.z == var1.z - 1) || var7.x == var1.x && (var7.y == var1.y + 1 || var7.y == var1.y - 1) && var7.z == var1.z || (var7.x == var1.x + 1 || var7.x == var1.x - 1) && var7.y == var1.y && var7.z == var1.z) && var1.facing == var7.facing)
            {
                var2.add(var7);
            }
        }

        var4 = var2.iterator();

        while (var4.hasNext())
        {
            var7 = (TileEntityInfoPanel)var4.next();
            Screen var6 = this.tryBuildFromPanel(var7);

            if (var6 != null)
            {
                ((List)this.screens.get(Integer.valueOf(this.getWorldKey(var1.world)))).add(var6);

                if (((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var1.world)))).contains(var7))
                {
                    ((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var1.world)))).remove(var7);
                }
            }
            else if (!((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var1.world)))).contains(var7))
            {
                ((List)this.unusedPanels.get(Integer.valueOf(this.getWorldKey(var1.world)))).add(var7);
            }
        }
    }
}
