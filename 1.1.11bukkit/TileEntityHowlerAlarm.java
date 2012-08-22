package nuclearcontrol;

import ic2.api.INetworkClientTileEntityEventListener;
import ic2.api.INetworkDataProvider;
import ic2.api.INetworkUpdateListener;
import ic2.api.IWrenchable;
import ic2.api.NetworkHelper;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Facing;
import net.minecraft.server.ModLoader;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_IC2NuclearControl;

public class TileEntityHowlerAlarm extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, IWrenchable, IRedstoneConsumer, INetworkClientTileEntityEventListener
{
    private static final String DEFAULT_SOUND_NAME = "default";
    private static final float BASE_SOUND_RANGE = 16.0F;
    private static final String SOUND_PREFIX = "ic2nuclearControl.alarm-";
    private boolean init = false;
    private short prevFacing = 0;
    public short facing = 0;
    public int range;
    private int prevRange;
    public boolean powered = false;
    private boolean prevPowered = false;
    public String soundName = "default";
    private String prevSoundName;
    private int updateTicker = 0;
    protected int tickRate = 2;
    private String soundId;

    public TileEntityHowlerAlarm()
    {
        this.range = mod_IC2NuclearControl.alarmRange;
    }

    private void initData()
    {
        if (this.world.isStatic)
        {
            NetworkHelper.requestInitialData(this);
        }
        else
        {
            RedstoneHelper.checkPowered(this.world, this);
        }

        this.init = true;
    }

    public int getRange()
    {
        return this.range;
    }

    public void setRange(int var1)
    {
        this.range = var1;

        if (this.prevRange != var1)
        {
            NetworkHelper.updateTileEntityField(this, "range");
        }

        this.prevRange = this.range;
    }

    public String getSoundName()
    {
        return this.soundName;
    }

    public void setSoundName(String var1)
    {
        this.soundName = var1;

        if (this.prevSoundName != var1)
        {
            NetworkHelper.updateTileEntityField(this, "soundName");
        }

        this.prevSoundName = var1;
    }

    public void onNetworkEvent(EntityHuman var1, int var2)
    {
        this.setRange(var2);
    }

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

    public boolean getPowered()
    {
        return this.powered;
    }

    /**
     * invalidates a tile entity
     */
    public void j()
    {
        if (this.soundId != null)
        {
            SoundHelper.stopAlarm(this.soundId);
            this.soundId = null;
        }

        super.j();
    }

    public void setPowered(boolean var1)
    {
        this.powered = var1;

        if (this.prevPowered != var1)
        {
            if (this.powered)
            {
                if (this.soundId == null)
                {
                    this.soundId = SoundHelper.playAlarm((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D, "ic2nuclearControl.alarm-" + this.soundName, this.getNormalizedRange());
                }
            }
            else if (this.soundId != null)
            {
                SoundHelper.stopAlarm(this.soundId);
                this.soundId = null;
            }

            NetworkHelper.updateTileEntityField(this, "powered");
        }

        this.prevPowered = var1;
    }

    public void setPoweredNoNotify(boolean var1)
    {
        this.powered = var1;

        if (this.prevPowered != var1)
        {
            if (this.powered)
            {
                if (this.soundId == null)
                {
                    this.soundId = SoundHelper.playAlarm((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D, "ic2nuclearControl.alarm-" + this.soundName, this.getNormalizedRange());
                }
            }
            else if (this.soundId != null)
            {
                SoundHelper.stopAlarm(this.soundId);
                this.soundId = null;
            }
        }

        this.prevPowered = var1;
    }

    private float getNormalizedRange()
    {
        return this.world.isStatic ? (float)Math.min(this.range, mod_IC2NuclearControl.SMPMaxAlarmRange) / 16.0F : (float)this.range / 16.0F;
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

    public void onNetworkUpdate(String var1)
    {
        if (var1.equals("facing") && this.prevFacing != this.facing)
        {
            this.world.notify(this.x, this.y, this.z);
            this.prevFacing = this.facing;
        }

        if (var1.equals("powered") && this.prevPowered != this.powered)
        {
            this.setPoweredNoNotify(this.powered);
            this.world.notify(this.x, this.y, this.z);
        }

        if (this.world.isStatic && var1.equals("soundName") && this.prevSoundName != this.soundName)
        {
            if (!mod_IC2NuclearControl.availableAlarms.contains(this.soundName))
            {
                ModLoader.getLogger().log(Level.INFO, String.format("[IC2NuclearControl] Can\'t set sound \'%s\' at %d,%d,%d, using default", new Object[] {this.soundName, Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z)}));
                this.soundName = "default";
            }

            this.prevSoundName = this.soundName;
        }
    }

    public List getNetworkedFields()
    {
        Vector var1 = new Vector(2);
        var1.add("facing");
        var1.add("powered");
        var1.add("range");
        var1.add("soundName");
        return var1;
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

        if (mod_IC2NuclearControl.isClient())
        {
            if (this.tickRate != -1 && this.updateTicker-- > 0)
            {
                return;
            }

            this.updateTicker = this.tickRate;
            this.checkStatus();
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void a(NBTTagCompound var1)
    {
        super.a(var1);
        this.prevFacing = this.facing = var1.getShort("facing");

        if (var1.hasKey("soundName"))
        {
            this.prevSoundName = this.soundName = var1.getString("soundName");
            this.prevRange = this.range = var1.getInt("range");
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void b(NBTTagCompound var1)
    {
        super.b(var1);
        var1.setShort("facing", this.facing);
        var1.setString("soundName", this.soundName);
        var1.setInt("range", this.range);
    }

    protected void checkStatus()
    {
        if (this.powered && (this.soundId == null || !SoundHelper.isPlaying(this.soundId)))
        {
            this.soundId = SoundHelper.playAlarm((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D, "ic2nuclearControl.alarm-" + this.soundName, this.getNormalizedRange());
        }
    }
}
