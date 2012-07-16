package nuclearcontrol;

import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class Screen
{
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    private int coreX;
    private int coreY;
    private int coreZ;
    public World coreWorld;
    private boolean powered = false;

    public TileEntityInfoPanel getCore()
    {
        TileEntity var1 = this.coreWorld.getTileEntity(this.coreX, this.coreY, this.coreZ);
        return var1 != null && var1 instanceof TileEntityInfoPanel ? (TileEntityInfoPanel)var1 : null;
    }

    public void setCore(TileEntityInfoPanel var1)
    {
        this.coreWorld = var1.world;
        this.coreX = var1.x;
        this.coreY = var1.y;
        this.coreZ = var1.z;
        this.powered = var1.powered;
    }

    public boolean isBlockNearby(TileEntity var1)
    {
        int var2 = var1.x;
        int var3 = var1.y;
        int var4 = var1.z;
        return var2 == this.minX - 1 && var3 >= this.minY && var3 <= this.maxY && var4 >= this.minZ && var4 <= this.maxZ || var2 == this.maxX + 1 && var3 >= this.minY && var3 <= this.maxY && var4 >= this.minZ && var4 <= this.maxZ || var2 >= this.minX && var2 <= this.maxX && var3 == this.minY - 1 && var4 >= this.minZ && var4 <= this.maxZ || var2 >= this.minX && var2 <= this.maxX && var3 == this.maxY + 1 && var4 >= this.minZ && var4 <= this.maxZ || var2 >= this.minX && var2 <= this.maxX && var3 >= this.minY && var3 <= this.maxY && var4 == this.minZ - 1 || var2 >= this.minX && var2 <= this.maxX && var3 >= this.minY && var3 <= this.maxY && var4 == this.maxZ + 1;
    }

    public boolean isBlockPartOf(TileEntity var1)
    {
        int var2 = var1.x;
        int var3 = var1.y;
        int var4 = var1.z;
        return var2 >= this.minX && var2 <= this.maxX && var3 >= this.minY && var3 <= this.maxY && var4 >= this.minZ && var4 <= this.maxZ;
    }

    public void init()
    {
        for (int var1 = this.minX; var1 <= this.maxX; ++var1)
        {
            for (int var2 = this.minY; var2 <= this.maxY; ++var2)
            {
                for (int var3 = this.minZ; var3 <= this.maxZ; ++var3)
                {
                    TileEntity var4 = this.coreWorld.getTileEntity(var1, var2, var3);

                    if (var4 != null && var4 instanceof IScreenPart)
                    {
                        ((IScreenPart)var4).setScreen(this);

                        if (this.powered)
                        {
                            this.coreWorld.notify(var1, var2, var3);
                            this.coreWorld.v(var1, var2, var3);
                        }
                    }
                }
            }
        }
    }

    public void destroy()
    {
        for (int var1 = this.minX; var1 <= this.maxX; ++var1)
        {
            for (int var2 = this.minY; var2 <= this.maxY; ++var2)
            {
                for (int var3 = this.minZ; var3 <= this.maxZ; ++var3)
                {
                    TileEntity var4 = this.coreWorld.getTileEntity(var1, var2, var3);

                    if (var4 != null && var4 instanceof IScreenPart)
                    {
                        ((IScreenPart)var4).setScreen((Screen)null);

                        if (this.powered)
                        {
                            this.coreWorld.notify(var1, var2, var3);
                            this.coreWorld.v(var1, var2, var3);
                        }
                    }
                }
            }
        }
    }

    public void turnPower(boolean var1)
    {
        if (this.powered != var1)
        {
            this.powered = var1;

            for (int var2 = this.minX; var2 <= this.maxX; ++var2)
            {
                for (int var3 = this.minY; var3 <= this.maxY; ++var3)
                {
                    for (int var4 = this.minZ; var4 <= this.maxZ; ++var4)
                    {
                        this.coreWorld.notify(var2, var3, var4);
                        this.coreWorld.v(var2, var3, var4);
                    }
                }
            }
        }
    }
}
