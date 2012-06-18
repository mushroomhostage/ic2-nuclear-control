package nuclearcontrol;

import ic2.api.INetworkDataProvider;
import ic2.api.INetworkUpdateListener;
import ic2.api.IWrenchable;
import ic2.api.NetworkHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Facing;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_IC2NuclearControl;

public class TileEntityInfoPanelExtender extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, IWrenchable, ITextureHelper, IScreenPart
{
    protected boolean init = false;
    private Screen screen = null;
    private short prevFacing = 0;
    public short facing = 0;

    public short getFacing()
    {
        return (short)Facing.OPPOSITE_FACING[this.facing];
    }

    public void setFacing(short var1)
    {
        this.setSide((short)Facing.OPPOSITE_FACING[var1]);
    }

    private void setSide(short var1)
    {
        this.facing = var1;

        if (this.prevFacing != var1)
        {
            NetworkHelper.updateTileEntityField(this, "facing");
        }

        this.prevFacing = var1;
    }

    public void onNetworkUpdate(String var1)
    {
        if (var1.equals("facing") && this.prevFacing != this.facing)
        {
            if (mod_IC2NuclearControl.isClient())
            {
                mod_IC2NuclearControl.screenManager.unregisterScreenPart(this);
                mod_IC2NuclearControl.screenManager.registerInfoPanelExtender(this);
            }

            this.world.notify(this.x, this.y, this.z);
            this.prevFacing = this.facing;
        }
    }

    public List getNetworkedFields()
    {
        ArrayList var1 = new ArrayList(1);
        var1.add("facing");
        return var1;
    }

    protected void initData()
    {
        if (this.world.isStatic)
        {
            NetworkHelper.requestInitialData(this);
        }

        if (mod_IC2NuclearControl.isClient())
        {
            mod_IC2NuclearControl.screenManager.registerInfoPanelExtender(this);
        }

        this.init = true;
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
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void a(NBTTagCompound var1)
    {
        super.a(var1);
        this.prevFacing = this.facing = var1.getShort("facing");
    }

    /**
     * invalidates a tile entity
     */
    public void j()
    {
        super.j();

        if (mod_IC2NuclearControl.isClient())
        {
            mod_IC2NuclearControl.screenManager.unregisterScreenPart(this);
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void b(NBTTagCompound var1)
    {
        super.b(var1);
        var1.setShort("facing", this.facing);
    }

    public boolean wrenchCanSetFacing(EntityHuman var1, int var2)
    {
        return this.getFacing() != var2;
    }

    public float getWrenchDropRate()
    {
        return 1.0F;
    }

    public boolean wrenchCanRemove(EntityHuman var1)
    {
        return true;
    }

    public int modifyTextureIndex(int var1)
    {
        return var1 == 11 && this.screen != null && this.screen.getCore() != null && this.screen.getCore().powered ? var1 + 16 : var1;
    }

    public void setScreen(Screen var1)
    {
        this.screen = var1;
    }

    public Screen getScreen()
    {
        return this.screen;
    }
}
