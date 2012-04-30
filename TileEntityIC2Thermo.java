package nuclearcontrol;

import ic2.api.INetworkClientTileEntityEventListener;
import ic2.api.INetworkDataProvider;
import ic2.api.INetworkUpdateListener;
import ic2.api.IWrenchable;
import ic2.api.NetworkHelper;
import java.util.List;
import java.util.Vector;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class TileEntityIC2Thermo extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, INetworkClientTileEntityEventListener, IWrenchable
{
    private boolean init = false;
    private int prevHeatLevel = 500;
    public int heatLevel = 500;
    private int mappedHeatLevel = 500;
    private byte prevOnFire = 0;
    public byte onFire = 0;
    private short prevFacing = 0;
    public short facing = 0;
    private int updateTicker = 0;
    private int tickRate = -1;

    public void initData()
    {
        if (this.world.isStatic)
        {
            NetworkHelper.requestInitialData(this);
        }

        this.init = true;
    }

    public short getFacing()
    {
        return this.facing;
    }

    public void setFacing(short var1)
    {
        this.facing = var1;

        if (this.prevFacing != var1)
        {
            NetworkHelper.updateTileEntityField(this, "facing");
        }

        this.prevFacing = var1;
    }

    public List getNetworkedFields()
    {
        Vector var1 = new Vector(3);
        var1.add("heatLevel");
        var1.add("onFire");
        var1.add("facing");
        return var1;
    }

    public void onNetworkUpdate(String var1)
    {
        if (var1.equals("heatLevel") && this.prevHeatLevel != this.heatLevel)
        {
            this.world.notify(this.x, this.y, this.z);
            this.prevHeatLevel = this.heatLevel;
        }

        if (var1.equals("facing") && this.prevFacing != this.facing)
        {
            this.world.notify(this.x, this.y, this.z);
            this.prevFacing = this.facing;
        }

        if (var1.equals("onFire") && this.prevOnFire != this.onFire)
        {
            this.world.notify(this.x, this.y, this.z);
            this.world.applyPhysics(this.x, this.y, this.z, this.world.getTypeId(this.x, this.y, this.z));
            this.prevOnFire = this.onFire;
        }
    }

    public void onNetworkEvent(EntityHuman var1, int var2)
    {
        this.setHeatLevel(var2);
    }

    public void setOnFire(byte var1)
    {
        this.onFire = var1;

        if (this.prevOnFire != var1)
        {
            NetworkHelper.updateTileEntityField(this, "onFire");
        }

        this.prevOnFire = this.onFire;
    }

    public byte getOnFire()
    {
        return this.onFire;
    }

    public void setHeatLevel(int var1)
    {
        this.heatLevel = var1;

        if (this.prevHeatLevel != var1)
        {
            NetworkHelper.updateTileEntityField(this, "heatLevel");
        }

        this.prevHeatLevel = this.heatLevel;
        this.mappedHeatLevel = var1 / 500 * 500;
    }

    public void setHeatLevelWithoutNotify(int var1)
    {
        this.heatLevel = var1;
        this.prevHeatLevel = this.heatLevel;
        this.mappedHeatLevel = var1 / 500 * 500;
    }

    public int getHeatLevel()
    {
        return this.heatLevel;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void a(NBTTagCompound var1)
    {
        super.a(var1);

        if (var1.hasKey("SliderValue"))
        {
            float var2 = var1.getFloat("SliderValue");
            int var3 = 500 + (int)Math.floor((double)(14500.0F * var2));
            this.setHeatLevelWithoutNotify(var3);
        }
        else if (var1.hasKey("heatLevel"))
        {
            int var4 = var1.getInt("heatLevel");
            this.setHeatLevelWithoutNotify(var4);
            this.prevFacing = this.facing = var1.getShort("facing");
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void b(NBTTagCompound var1)
    {
        super.b(var1);
        var1.setInt("heatLevel", this.getHeatLevel());
        var1.setShort("facing", this.facing);
    }

    private void checkStatus()
    {
        TileEntity var2 = NuclearHelper.getReactorChamberAroundCoord(this.world, this.x, this.y, this.z);
        TileEntity var3 = null;

        if (var2 != null)
        {
            var3 = NuclearHelper.getReactorAroundCoord(this.world, var2.x, var2.y, var2.z);
        }

        if (var3 == null)
        {
            var3 = NuclearHelper.getReactorAroundCoord(this.world, this.x, this.y, this.z);
        }

        byte var1;

        if (var3 != null)
        {
            if (this.tickRate == -1)
            {
                this.tickRate = NuclearHelper.getReactorTickRate(var3) / 2;

                if (this.tickRate == 0)
                {
                    this.tickRate = 1;
                }

                this.updateTicker = this.tickRate;
            }

            int var4 = NuclearHelper.getReactorHeat(var3);

            if (var4 >= this.mappedHeatLevel)
            {
                var1 = 1;
            }
            else
            {
                var1 = 0;
            }
        }
        else
        {
            var1 = -1;
        }

        if (var1 != this.getOnFire())
        {
            this.setOnFire(var1);
            this.world.applyPhysics(this.x, this.y, this.z, this.world.getTypeId(this.x, this.y, this.z));
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void q_()
    {
        if (!this.init)
        {
            this.initData();
        }

        super.q_();

        if (!this.world.isStatic)
        {
            if (this.tickRate != -1 && this.updateTicker-- > 0)
            {
                return;
            }

            this.updateTicker = this.tickRate;
            this.checkStatus();
        }
    }

    public boolean wrenchCanSetFacing(EntityHuman var1, int var2)
    {
        return false;
    }

    public boolean wrenchCanRemove(EntityHuman var1)
    {
        return true;
    }

    public float getWrenchDropRate()
    {
        return 1.0F;
    }
}
