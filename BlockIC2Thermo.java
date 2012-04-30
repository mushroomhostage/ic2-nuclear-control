package nuclearcontrol;

import forge.ITextureProvider;
import java.util.ArrayList;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Facing;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_IC2NuclearControl;

public class BlockIC2Thermo extends BlockContainer implements ITextureProvider
{
    private static final int[][] sideMapping = new int[][] {{1, 0, 17, 17, 17, 17}, {0, 1, 17, 17, 17, 17}, {17, 17, 1, 0, 33, 33}, {17, 17, 0, 1, 33, 33}, {33, 33, 33, 33, 1, 0}, {33, 33, 33, 33, 0, 1}};

    public BlockIC2Thermo(int var1, int var2)
    {
        super(var1, Material.ORE);
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

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlace(World var1, int var2, int var3, int var4)
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

        if (var1.isBlockSolidOnSide(var2 + Facing.b[var6], var3 + Facing.c[var6], var4 + Facing.d[var6], var5))
        {
            TileEntity var7 = var1.getTileEntity(var2, var3, var4);

            if (var7 instanceof TileEntityIC2Thermo)
            {
                ((TileEntityIC2Thermo)var7).setFacing((short)var6);
            }
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onPlace(World var1, int var2, int var3, int var4)
    {
        for (int var5 = 0; var5 < 6; ++var5)
        {
            int var6 = Facing.OPPOSITE_FACING[var5];

            if (var1.isBlockSolidOnSide(var2 + Facing.b[var6], var3 + Facing.c[var6], var4 + Facing.d[var6], var5))
            {
                TileEntity var7 = var1.getTileEntity(var2, var3, var4);

                if (var7 instanceof TileEntityIC2Thermo)
                {
                    ((TileEntityIC2Thermo)var7).setFacing((short)var6);
                }

                break;
            }
        }

        this.dropBlockIfCantStay(var1, var2, var3, var4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World var1, int var2, int var3, int var4, int var5)
    {
        int var6 = var1.getData(var2, var3, var4);
        short var7 = 0;
        TileEntity var8 = var1.getTileEntity(var2, var3, var4);

        if (var8 instanceof TileEntityIC2Thermo)
        {
            var7 = ((TileEntityIC2Thermo)var8).getFacing();
        }

        if (!var1.isBlockSolidOnSide(var2 + Facing.b[var7], var3 + Facing.c[var7], var4 + Facing.d[var7], Facing.OPPOSITE_FACING[var7]))
        {
            if (!var1.isStatic)
            {
                this.b(var1, var2, var3, var4, var6, 0);
            }

            var1.setTypeId(var2, var3, var4, 0);
        }
    }

    private boolean dropBlockIfCantStay(World var1, int var2, int var3, int var4)
    {
        if (!this.canPlace(var1, var2, var3, var4))
        {
            if (var1.getTypeId(var2, var3, var4) == this.id)
            {
                this.b(var1, var2, var3, var4, var1.getData(var2, var3, var4), 0);
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
        float var1 = 0.0625F;
        float var2 = 0.0F;
        float var3 = 0.0625F;
        float var4 = 0.9375F;
        float var5 = 0.4375F;
        float var6 = 0.9375F;
        this.a(var1, var2, var3, var4, var5, var6);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void updateShape(IBlockAccess var1, int var2, int var3, int var4)
    {
        float var5 = 0.0625F;
        float var6 = 0.0F;
        float var7 = 0.0625F;
        float var8 = 0.9375F;
        float var9 = 0.4375F;
        float var10 = 0.9375F;
        short var12 = 0;
        TileEntity var13 = var1.getTileEntity(var2, var3, var4);

        if (var13 instanceof TileEntityIC2Thermo)
        {
            var12 = ((TileEntityIC2Thermo)var13).getFacing();
        }

        float var11;

        switch (var12)
        {
            case 1:
                var6 = 1.0F - var6;
                var9 = 1.0F - var9;
                break;

            case 2:
                var11 = var6;
                var6 = var7;
                var7 = var11;
                var11 = var9;
                var9 = var10;
                var10 = var11;
                break;

            case 3:
                var11 = var6;
                var6 = var7;
                var7 = 1.0F - var11;
                var11 = var9;
                var9 = var10;
                var10 = 1.0F - var11;
                break;

            case 4:
                var11 = var6;
                var6 = var5;
                var5 = var11;
                var11 = var9;
                var9 = var8;
                var8 = var11;
                break;

            case 5:
                var11 = var6;
                var6 = var5;
                var5 = 1.0F - var11;
                var11 = var9;
                var9 = var8;
                var8 = 1.0F - var11;
        }

        this.a(Math.min(var5, var8), Math.min(var6, var9), Math.min(var7, var10), Math.max(var5, var8), Math.max(var6, var9), Math.max(var7, var10));
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
        if (var5.isSneaking())
        {
            return false;
        }
        else
        {
            mod_IC2NuclearControl.launchGui(var1, var2, var3, var4, var5);
            return true;
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
        int var6 = var2;
        int var7 = var3;
        int var8 = var4;

        switch (var5)
        {
            case 0:
                var7 = var3 + 1;
                break;

            case 1:
                var7 = var3 - 1;
                break;

            case 2:
                var8 = var4 + 1;
                break;

            case 3:
                var8 = var4 - 1;
                break;

            case 4:
                var6 = var2 + 1;
                break;

            case 5:
                var6 = var2 - 1;
        }

        TileEntity var9 = var1.getTileEntity(var6, var7, var8);

        if (var9 != null && (NuclearHelper.getReactorAt(var9.world, var6, var7, var8) != null || NuclearHelper.getReactorChamberAt(var9.world, var6, var7, var8) != null))
        {
            return false;
        }
        else
        {
            var9 = var1.getTileEntity(var2, var3, var4);
            return var9 instanceof TileEntityIC2Thermo ? ((TileEntityIC2Thermo)var9).getOnFire() == 1 : false;
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int a(int var1, int var2)
    {
        int var3 = sideMapping[0][var1];
        return this.textureId + var3;
    }

    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        TileEntity var6 = var1.getTileEntity(var2, var3, var4);
        boolean var7 = var6 instanceof TileEntityIC2Thermo;
        short var8 = 0;

        if (var7)
        {
            var8 = ((TileEntityIC2Thermo)var6).getFacing();
        }

        int var9 = sideMapping[var8][var5];

        if (var9 == 0 && var7)
        {
            byte var10 = ((TileEntityIC2Thermo)var6).getOnFire();
            byte var11;

            switch (var10)
            {
                case 0:
                    var11 = 0;
                    break;

                case 1:
                    var11 = 16;
                    break;

                default:
                    var11 = 32;
            }

            return this.textureId + var11;
        }
        else
        {
            return this.textureId + var9;
        }
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return new TileEntityIC2Thermo();
    }

    public TileEntity getBlockEntity(int var1)
    {
        return this.a_();
    }

    public void addCreativeItems(ArrayList var1)
    {
        var1.add(new ItemStack(this));
    }
}
