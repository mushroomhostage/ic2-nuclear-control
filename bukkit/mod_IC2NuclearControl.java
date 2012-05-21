package net.minecraft.server;

import forge.Configuration;
import forge.IGuiHandler;
import forge.MinecraftForge;
import forge.NetworkMod;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import nuclearcontrol.BlockNuclearControlMain;
import nuclearcontrol.ContainerRemoteThermo;
import nuclearcontrol.ItemNuclearControlMain;
import nuclearcontrol.ItemRemoteSensorKit;
import nuclearcontrol.ItemSensorLocationCard;
import nuclearcontrol.ItemToolDigitalThermometer;
import nuclearcontrol.ItemToolThermometer;
import nuclearcontrol.ThermometerVersion;
import nuclearcontrol.TileEntityHowlerAlarm;
import nuclearcontrol.TileEntityIC2Thermo;
import nuclearcontrol.TileEntityIndustrialAlarm;
import nuclearcontrol.TileEntityRemoteThermo;

public class mod_IC2NuclearControl extends NetworkMod implements IGuiHandler
{
    private static final String CONFIG_NUCLEAR_CONTROL = "IC2NuclearControl.cfg";
    private static final String CONFIG_THERMO_BLOCK = "mod_thermo.cfg";
    private static final String CONFIG_THERMOMETER = "IC2Thermometer.cfg";
    public static Item itemToolThermometer;
    public static Item itemToolDigitalThermometer;
    public static Item itemRemoteSensorKit;
    public static Item itemSensorLocationCard;
    public static Block blockNuclearControlMain;
    public static int modelId;
    public static float alarmRange;
    private static mod_IC2NuclearControl instance;

    public static boolean isClient()
    {
        return false;
    }

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
        instance = this;
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
        ModLoader.registerTileEntity(TileEntityHowlerAlarm.class, "IC2HowlerAlarm");
        ModLoader.registerTileEntity(TileEntityIndustrialAlarm.class, "IC2IndustrialAlarm");
        ModLoader.registerTileEntity(TileEntityRemoteThermo.class, "IC2RemoteThermo");
        MinecraftForge.setGuiHandler(this, this);

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
        blockNuclearControlMain = (new BlockNuclearControlMain(getIdFor(var1, "blockNuclearControlMain", 192, true), 0)).c(0.5F).a("blockThermalMonitor").j();
        itemToolThermometer = (new ItemToolThermometer(getIdFor(var1, "itemToolThermometer", 31000, false), 2, ThermometerVersion.ANALOG)).a("ItemToolThermometer");
        itemToolDigitalThermometer = (new ItemToolDigitalThermometer(getIdFor(var1, "itemToolDigitalThermometer", 31001, false), 18, ThermometerVersion.DIGITAL, 1, 80, 80)).a("ItemToolDigitalThermometer");
        itemRemoteSensorKit = (new ItemRemoteSensorKit(getIdFor(var1, "itemRemoteSensorKit", 31002, false), 34)).a("ItemRemoteSensorKit");
        itemSensorLocationCard = (new ItemSensorLocationCard(getIdFor(var1, "itemSensorLocationCard", 31003, false), 50)).a("ItemSensorLocationCard");
    }

    public void registerBlocks()
    {
        ModLoader.registerBlock(blockNuclearControlMain, ItemNuclearControlMain.class);
    }

    public void addRecipes()
    {
        ItemStack var1 = new ItemStack(blockNuclearControlMain, 1, 0);
        Ic2Recipes.addCraftingRecipe(var1, new Object[] {"GGG", "GCG", "GRG", 'G', Items.getItem("reinforcedGlass"), 'R', Item.REDSTONE, 'C', Items.getItem("advancedCircuit")});
        ItemStack var2 = new ItemStack(blockNuclearControlMain, 1, 2);
        Ic2Recipes.addCraftingRecipe(var2, new Object[] {"NNN", "ICI", "IRI", 'I', Item.IRON_INGOT, 'R', Item.REDSTONE, 'N', Block.NOTE_BLOCK, 'C', Items.getItem("electronicCircuit")});
        ItemStack var3 = new ItemStack(blockNuclearControlMain, 1, 1);
        Ic2Recipes.addCraftingRecipe(var3, new Object[] {"GOG", "GHG", "GRG", 'G', Items.getItem("reinforcedGlass"), 'O', new ItemStack(Item.INK_SACK, 1, 14), 'R', Item.REDSTONE, 'H', var2});
        Ic2Recipes.addCraftingRecipe(new ItemStack(blockNuclearControlMain, 1, 3), new Object[] {" F ", " M ", " T ", 'T', var1, 'M', Items.getItem("machine"), 'F', Items.getItem("frequencyTransmitter")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolThermometer, 1), new Object[] {"IG ", "GWG", " GG", 'G', Block.GLASS, 'I', Item.IRON_INGOT, 'W', Items.getItem("waterCell")});
        ItemStack var4 = new ItemStack(itemToolDigitalThermometer, 1);
        Ic2Recipes.addCraftingRecipe(var4, new Object[] {"I  ", "IC ", " GI", 'G', Item.GLOWSTONE_DUST, 'I', Items.getItem("refinedIronIngot"), 'C', Items.getItem("electronicCircuit")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemRemoteSensorKit, 1), new Object[] {"  F", " D ", "P  ", 'P', Item.PAPER, 'D', var4, 'F', Items.getItem("frequencyTransmitter")});
    }

    public static void launchGui(World var0, int var1, int var2, int var3, EntityHuman var4, int var5)
    {
        var4.openGui(instance, var5, var0, var1, var2, var3);
    }

    public static void chatMessage(EntityHuman var0, String var1)
    {
        ((EntityPlayer)var0).netServerHandler.sendPacket(new Packet3Chat(var1));
    }

    public Object getGuiElement(int var1, EntityHuman var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 0:
                return null;

            case 3:
                TileEntity var7 = var3.getTileEntity(var4, var5, var6);
                return new ContainerRemoteThermo(var2, (TileEntityRemoteThermo)var7);

            default:
                return null;
        }
    }

    public String getVersion()
    {
        return "v1.1.6";
    }
}
