package nuclearcontrol;

import forge.ITextureProvider;
import ic2.api.IWrenchable;
import java.util.ArrayList;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Facing;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_IC2NuclearControl;

public class BlockNuclearControlMain extends BlockContainer implements ITextureProvider
{
    public static final int DAMAGE_THERMAL_MONITOR = 0;
    public static final int DAMAGE_INDUSTRIAL_ALARM = 1;
    public static final int DAMAGE_HOWLER_ALARM = 2;
    public static final int DAMAGE_REMOTE_THERMO = 3;
    public static final int DAMAGE_MAX = 3;
    public static final float[][] blockSize = new float[][] {{0.0625F, 0.0F, 0.0625F, 0.9375F, 0.4375F, 0.9375F}, {0.125F, 0.0F, 0.125F, 0.875F, 0.4375F, 0.875F}, {0.125F, 0.0F, 0.125F, 0.875F, 0.4375F, 0.875F}, {0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F}};
    private static final boolean[] solidBlockRequired = new boolean[] {true, true, true, false};
    private static final byte[][][] sideMapping = new byte[][][] {{{(byte)1, (byte)0, (byte)17, (byte)17, (byte)17, (byte)17}, {(byte)0, (byte)1, (byte)17, (byte)17, (byte)17, (byte)17}, {(byte)17, (byte)17, (byte)1, (byte)0, (byte)33, (byte)33}, {(byte)17, (byte)17, (byte)0, (byte)1, (byte)33, (byte)33}, {(byte)33, (byte)33, (byte)33, (byte)33, (byte)1, (byte)0}, {(byte)33, (byte)33, (byte)33, (byte)33, (byte)0, (byte)1}}, {{(byte)4, (byte)3, (byte)5, (byte)5, (byte)5, (byte)5}, {(byte)3, (byte)4, (byte)5, (byte)5, (byte)5, (byte)5}, {(byte)5, (byte)5, (byte)4, (byte)3, (byte)6, (byte)6}, {(byte)5, (byte)5, (byte)3, (byte)4, (byte)6, (byte)6}, {(byte)6, (byte)6, (byte)6, (byte)6, (byte)4, (byte)3}, {(byte)6, (byte)6, (byte)6, (byte)6, (byte)3, (byte)4}}, {{(byte)8, (byte)7, (byte)9, (byte)9, (byte)9, (byte)9}, {(byte)7, (byte)8, (byte)9, (byte)9, (byte)9, (byte)9}, {(byte)9, (byte)9, (byte)8, (byte)7, (byte)10, (byte)10}, {(byte)9, (byte)9, (byte)7, (byte)8, (byte)10, (byte)10}, {(byte)10, (byte)10, (byte)10, (byte)10, (byte)8, (byte)7}, {(byte)10, (byte)10, (byte)10, (byte)10, (byte)7, (byte)8}}, {{(byte)23, (byte)25, (byte)24, (byte)24, (byte)24, (byte)24}, {(byte)25, (byte)23, (byte)24, (byte)24, (byte)24, (byte)24}, {(byte)24, (byte)24, (byte)23, (byte)25, (byte)24, (byte)24}, {(byte)24, (byte)24, (byte)25, (byte)23, (byte)24, (byte)24}, {(byte)24, (byte)24, (byte)24, (byte)24, (byte)23, (byte)25}, {(byte)24, (byte)24, (byte)24, (byte)24, (byte)25, (byte)23}}};

    public BlockNuclearControlMain(int var1, int var2)
    {
        super(var1, var2, Material.ORE);
    }

    /**
     * The type of render function that is called for this block
     */
    public int c()
    {
        return mod_IC2NuclearControl.modelId;
    }

    public boolean isBlockNormalCube(World var1, int var2, int var3, int var4)
    {
        return false;
    }

    public String getTextureFile()
    {
        return "/img/texture_thermo.png";
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean b()
    {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean a()
    {
        return false;
    }

    public boolean canPlaceBlockAtlocal(World var1, int var2, int var3, int var4)
    {
        for (int var5 = 0; var5 < 6; ++var5)
        {
            int var6 = Facing.OPPOSITE_FACING[var5];

            if (var1.isBlockSolidOnSide(var2 + Facing.b[var6], var3 + Facing.c[var6], var4 + Facing.d[var6], var5))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Called when a block is placed using an item. Used often for taking the facing and figuring out how to position
     * the item. Args: x, y, z, facing
     */
    public void postPlace(World var1, int var2, int var3, int var4, int var5)
    {
        int var6 = Facing.OPPOSITE_FACING[var5];
        int var7 = var1.getData(var2, var3, var4);

        if (var7 > 3)
        {
            var7 = 0;
        }

        if (!solidBlockRequired[var7] || var1.isBlockSolidOnSide(var2 + Facing.b[var6], var3 + Facing.c[var6], var4 + Facing.d[var6], var5))
        {
            TileEntity var8 = var1.getTileEntity(var2, var3, var4);

            if (var8 instanceof IWrenchable)
            {
                ((IWrenchable)var8).setFacing((short)var5);
            }
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onPlace(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getData(var2, var3, var4);

        if (var5 > 3)
        {
            var5 = 0;
        }

        if (solidBlockRequired[var5])
        {
            for (int var6 = 0; var6 < 6; ++var6)
            {
                int var7 = Facing.OPPOSITE_FACING[var6];

                if (var1.isBlockSolidOnSide(var2 + Facing.b[var7], var3 + Facing.c[var7], var4 + Facing.d[var7], var6))
                {
                    TileEntity var8 = var1.getTileEntity(var2, var3, var4);

                    if (var8 instanceof IWrenchable)
                    {
                        ((IWrenchable)var8).setFacing((short)var6);
                    }

                    break;
                }
            }
        }

        this.dropBlockIfCantStay(var1, var2, var3, var4);
    }

    /**
     * Called whenever the block is removed.
     */
    public void remove(World var1, int var2, int var3, int var4)
    {
        TileEntity var5 = var1.getTileEntity(var2, var3, var4);

        if (var5 instanceof TileEntityHowlerAlarm)
        {
            ((TileEntityHowlerAlarm)var5).setPowered(false);
        }

        if (!var1.isStatic && var5 instanceof IInventory)
        {
            IInventory var6 = (IInventory)var5;
            float var7 = 0.7F;

            for (int var8 = 0; var8 < var6.getSize(); ++var8)
            {
                ItemStack var9 = var6.getItem(var8);

                if (var9 != null)
                {
                    double var10 = (double)(var1.random.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
                    double var12 = (double)(var1.random.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
                    double var14 = (double)(var1.random.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
                    EntityItem var16 = new EntityItem(var1, (double)var2 + var10, (double)var3 + var12, (double)var4 + var14, var9);
                    var16.pickupDelay = 10;
                    var1.addEntity(var16);
                    var6.setItem(var8, (ItemStack)null);
                }
            }
        }

        super.remove(var1, var2, var3, var4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World var1, int var2, int var3, int var4, int var5)
    {
        int var6 = 0;
        TileEntity var7 = var1.getTileEntity(var2, var3, var4);

        if (var7 instanceof IWrenchable)
        {
            var6 = Facing.OPPOSITE_FACING[((IWrenchable)var7).getFacing()];
        }

        int var8 = var1.getData(var2, var3, var4);

        if (solidBlockRequired[Math.min(var8, solidBlockRequired.length - 1)] && !var1.isBlockSolidOnSide(var2 + Facing.b[var6], var3 + Facing.c[var6], var4 + Facing.d[var6], Facing.OPPOSITE_FACING[var6]))
        {
            if (!var1.isStatic)
            {
                this.b(var1, var2, var3, var4, var8, 0);
            }

            var1.setTypeId(var2, var3, var4, 0);
        }
        else
        {
            RedstoneHelper.checkPowered(var1, var7);
        }

        super.doPhysics(var1, var2, var3, var4, var5);
    }

    private boolean dropBlockIfCantStay(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getData(var2, var3, var4);

        if (!solidBlockRequired[Math.min(var5, solidBlockRequired.length - 1)])
        {
            return true;
        }
        else if (!this.canPlaceBlockAtlocal(var1, var2, var3, var4))
        {
            if (var1.getTypeId(var2, var3, var4) == this.id)
            {
                this.b(var1, var2, var3, var4, var5, 0);
                var1.setTypeId(var2, var3, var4, 0);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void f()
    {
        this.a(blockSize[0][0], blockSize[0][1], blockSize[0][2], blockSize[0][3], blockSize[0][4], blockSize[0][5]);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void updateShape(IBlockAccess var1, int var2, int var3, int var4)
    {
        int var5 = var1.getData(var2, var3, var4);

        if (var5 > 3)
        {
            var5 = 0;
        }

        float var6 = blockSize[var5][0];
        float var7 = blockSize[var5][1];
        float var8 = blockSize[var5][2];
        float var9 = blockSize[var5][3];
        float var10 = blockSize[var5][4];
        float var11 = blockSize[var5][5];
        int var13 = 0;
        TileEntity var14 = var1.getTileEntity(var2, var3, var4);

        if (var14 instanceof IWrenchable)
        {
            var13 = Facing.OPPOSITE_FACING[((IWrenchable)var14).getFacing()];
        }

        float var12;

        switch (var13)
        {
            case 1:
                var7 = 1.0F - var7;
                var10 = 1.0F - var10;
                break;

            case 2:
                var12 = var7;
                var7 = var8;
                var8 = var12;
                var12 = var10;
                var10 = var11;
                var11 = var12;
                break;

            case 3:
                var12 = var7;
                var7 = var8;
                var8 = 1.0F - var12;
                var12 = var10;
                var10 = var11;
                var11 = 1.0F - var12;
                break;

            case 4:
                var12 = var7;
                var7 = var6;
                var6 = var12;
                var12 = var10;
                var10 = var9;
                var9 = var12;
                break;

            case 5:
                var12 = var7;
                var7 = var6;
                var6 = 1.0F - var12;
                var12 = var10;
                var10 = var9;
                var9 = 1.0F - var12;
        }

        this.a(Math.min(var6, var9), Math.min(var7, var10), Math.min(var8, var11), Math.max(var6, var9), Math.max(var7, var10), Math.max(var8, var11));
    }

    public String getInvName()
    {
        return "IC2 Thermo";
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean isPowerSource()
    {
        return true;
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5)
    {
        int var6 = var1.getData(var2, var3, var4);

        if (var5.isSneaking())
        {
            return false;
        }
        else
        {
            switch (var6)
            {
                case 0:
                case 3:
                    mod_IC2NuclearControl.launchGui(var1, var2, var3, var4, var5, var6);
                    return true;

                default:
                    return false;
            }
        }
    }

    /**
     * Is this block indirectly powering the block on the specified side
     */
    public boolean d(World var1, int var2, int var3, int var4, int var5)
    {
        return false;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB e(World var1, int var2, int var3, int var4)
    {
        this.updateShape(var1, var2, var3, var4);
        return super.e(var1, var2, var3, var4);
    }

    /**
     * Is this block powering the block on the specified side
     */
    public boolean a(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        TileEntity var6 = var1.getTileEntity(var2, var3, var4);

        if (!(var6 instanceof TileEntityIC2Thermo))
        {
            return false;
        }
        else
        {
            int var7 = var2;
            int var8 = var3;
            int var9 = var4;

            switch (var5)
            {
                case 0:
                    var8 = var3 + 1;
                    break;

                case 1:
                    var8 = var3 - 1;
                    break;

                case 2:
                    var9 = var4 + 1;
                    break;

                case 3:
                    var9 = var4 - 1;
                    break;

                case 4:
                    var7 = var2 + 1;
                    break;

                case 5:
                    var7 = var2 - 1;
            }

            TileEntity var10 = var1.getTileEntity(var7, var8, var9);

            if (var10 != null && (NuclearHelper.getReactorAt(var6.world, var7, var8, var9) != null || NuclearHelper.getReactorChamberAt(var6.world, var7, var8, var9) != null))
            {
                return false;
            }
            else if (var6 instanceof TileEntityRemoteThermo)
            {
                TileEntityRemoteThermo var11 = (TileEntityRemoteThermo)var6;
                return var11.getOnFire() >= var11.getHeatLevel();
            }
            else
            {
                return ((TileEntityIC2Thermo)var6).getOnFire() > 0;
            }
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int a(int var1, int var2)
    {
        if (var2 > 3)
        {
            var2 = 0;
        }

        byte var3 = sideMapping[var2][0][var1];
        return var3;
    }

    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        TileEntity var6 = var1.getTileEntity(var2, var3, var4);
        int var7 = 0;

        if (var6 instanceof IWrenchable)
        {
            var7 = Facing.OPPOSITE_FACING[((IWrenchable)var6).getFacing()];
        }

        int var8 = var1.getData(var2, var3, var4);

        if (var8 > 3)
        {
            var8 = 0;
        }

        int var9 = sideMapping[var8][var7][var5];

        if (var6 instanceof ITextureHelper)
        {
            var9 = ((ITextureHelper)var6).modifyTextureIndex(var9);
        }

        return var9;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return null;
    }

    public TileEntity getBlockEntity(int var1)
    {
        switch (var1)
        {
            case 0:
                return new TileEntityIC2Thermo();

            case 1:
                return new TileEntityIndustrialAlarm();

            case 2:
                return new TileEntityHowlerAlarm();

            case 3:
                return new TileEntityRemoteThermo();

            default:
                return null;
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    protected int getDropData(int var1)
    {
        return var1 > 0 && var1 <= 3 ? var1 : 0;
    }

    public boolean isBlockSolidOnSide(World var1, int var2, int var3, int var4, int var5)
    {
        int var6 = var1.getData(var2, var3, var4);
        return !solidBlockRequired[var6];
    }

    public int getLightValue(IBlockAccess var1, int var2, int var3, int var4)
    {
        TileEntity var5 = var1.getTileEntity(var2, var3, var4);
        return var5 instanceof TileEntityIndustrialAlarm ? ((TileEntityIndustrialAlarm)var5).lightLevel : lightEmission[this.id];
    }

    public void addCreativeItems(ArrayList var1)
    {
        var1.add(new ItemStack(this, 1, 0));
        var1.add(new ItemStack(this, 1, 1));
        var1.add(new ItemStack(this, 1, 2));
        var1.add(new ItemStack(this, 1, 3));
    }
}
