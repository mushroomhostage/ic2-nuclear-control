package net.minecraft.server;

import forge.Configuration;
import forge.NetworkMod;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import nuclearcontrol.BlockIC2Thermo;
import nuclearcontrol.ItemToolDigitalThermometer;
import nuclearcontrol.ItemToolThermometer;
import nuclearcontrol.ThermometerVersion;
import nuclearcontrol.TileEntityIC2Thermo;

public class mod_IC2NuclearControl extends NetworkMod
{
    private static final String CONFIG_NUCLEAR_CONTROL = "IC2NuclearControl.cfg";
    private static final String CONFIG_THERMO_BLOCK = "mod_thermo.cfg";
    private static final String CONFIG_THERMOMETER = "IC2Thermometer.cfg";
    public static Item itemToolThermometer;
    public static Item itemToolDigitalThermometer;
    public static Block IC2Thermo;

    public boolean clientSideRequired()
    {
        return true;
    }

    public boolean serverSideRequired()
    {
        return false;
    }

    private static File getConfigFile(String var0)
    {
        return new File(new File("config"), var0);
    }

    private static Configuration importConfig() throws IOException
    {
        int var0 = -1;
        int var1 = -1;
        int var2 = -1;
        File var4 = getConfigFile("mod_thermo.cfg");

        if (var4.exists() && var4.canRead())
        {
            Properties var5 = new Properties();
            var5.load(new FileInputStream(var4));
            var0 = Integer.parseInt(var5.getProperty("thermo_blockid", "192"));
        }

        var4 = getConfigFile("IC2Thermometer.cfg");
        Configuration var3;

        if (var4.exists() && var4.canRead())
        {
            var3 = new Configuration(var4);
            var3.load();
            var1 = getOldIdFor(var3, "itemToolThermometer", 31000);
            var2 = getOldIdFor(var3, "itemToolDigitalThermometer", 31001);
        }

        var4 = getConfigFile("IC2NuclearControl.cfg");
        var3 = new Configuration(var4);
        var3.load();

        if (var1 != -1)
        {
            getIdFor(var3, "itemToolThermometer", var1, false);
        }

        if (var2 != -1)
        {
            getIdFor(var3, "itemToolDigitalThermometer", var2, false);
        }

        if (var0 != -1)
        {
            getIdFor(var3, "blockNuclearControlMain", var0, true);
        }

        var3.save();
        return var3;
    }

    public void load()
    {
        Configuration var1;

        try
        {
            File var2 = getConfigFile("IC2NuclearControl.cfg");

            if (!var2.exists())
            {
                var1 = importConfig();
            }
            else
            {
                var1 = new Configuration(var2);
                var1.load();
            }
        }
        catch (IOException var3)
        {
            var3.printStackTrace();
            var1 = null;
        }

        ModLoader.setInGameHook(this, true, false);
        this.initBlocks(var1);
        this.registerBlocks();
        ModLoader.registerTileEntity(TileEntityIC2Thermo.class, "IC2Thermo");

        if (var1 != null)
        {
            var1.save();
        }
    }

    public void modsLoaded()
    {
        super.modsLoaded();
        this.addRecipes();
    }

    private static int getIdFor(Configuration var0, String var1, int var2, boolean var3)
    {
        try
        {
            return var3 ? (new Integer(var0.getOrCreateBlockIdProperty(var1, var2).value)).intValue() : (new Integer(var0.getOrCreateIntProperty(var1, "item", var2).value)).intValue();
        }
        catch (Exception var5)
        {
            System.out.println("Can\'t get id for :" + var1);
            return var2;
        }
    }

    private static int getOldIdFor(Configuration var0, String var1, int var2)
    {
        try
        {
            return (new Integer(var0.getOrCreateIntProperty(var1, "general", var2).value)).intValue();
        }
        catch (Exception var4)
        {
            System.out.println("Can\'t get id for :" + var1);
            return var2;
        }
    }

    public void initBlocks(Configuration var1)
    {
        IC2Thermo = (new BlockIC2Thermo(getIdFor(var1, "blockNuclearControlMain", 192, true), 0)).c(0.5F).a("blockThermalMonitor").j();
        itemToolThermometer = (new ItemToolThermometer(getIdFor(var1, "itemToolThermometer", 31000, false), 2, ThermometerVersion.ANALOG)).a("ItemToolThermometer");
        itemToolDigitalThermometer = (new ItemToolDigitalThermometer(getIdFor(var1, "itemToolDigitalThermometer", 31001, false), 18, ThermometerVersion.DIGITAL, 1, 80, 80)).a("ItemToolDigitalThermometer");
    }

    public void registerBlocks()
    {
        ModLoader.registerBlock(IC2Thermo);
    }

    public void addRecipes()
    {
        Ic2Recipes.addCraftingRecipe(new ItemStack(IC2Thermo, 1), new Object[] {"GGG", "GCG", "GRG", 'G', Items.getItem("reinforcedGlass"), 'R', Item.REDSTONE, 'C', Items.getItem("advancedCircuit")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolThermometer, 1), new Object[] {"IG ", "GWG", " GG", 'G', Block.GLASS, 'I', Item.IRON_INGOT, 'W', Items.getItem("waterCell")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolDigitalThermometer, 1), new Object[] {"I  ", "IC ", " GI", 'G', Item.GLOWSTONE_DUST, 'I', Items.getItem("refinedIronIngot"), 'C', Items.getItem("electronicCircuit")});
    }

    public static void launchGui(World var0, int var1, int var2, int var3, EntityHuman var4) {}

    public static void chatMessage(EntityHuman var0, String var1)
    {
        ((EntityPlayer)var0).netServerHandler.sendPacket(new Packet3Chat(var1));
    }

    public String getVersion()
    {
        return "v1.1.2";
    }
}
