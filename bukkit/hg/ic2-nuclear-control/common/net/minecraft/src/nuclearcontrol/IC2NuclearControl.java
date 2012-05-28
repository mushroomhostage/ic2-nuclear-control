package net.minecraft.src.nuclearcontrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.IGuiHandler;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.ic2.api.Ic2Recipes;
import net.minecraft.src.ic2.api.Items;

public abstract class IC2NuclearControl extends NetworkMod implements IGuiHandler
{
    protected static final String CONFIG_NUCLEAR_CONTROL = "IC2NuclearControl.cfg";
    protected static final String CONFIG_THERMO_BLOCK = "mod_thermo.cfg";
    protected static final String CONFIG_THERMOMETER = "IC2Thermometer.cfg";

    public static Item itemToolThermometer;
    public static Item itemToolDigitalThermometer;
    public static Item itemRemoteSensorKit;
    public static Item itemSensorLocationCard;
    public static Item itemRangeUpgrade;
    public static Block blockNuclearControlMain;
    public static int modelId;
    public static float alarmRange;
    protected static IC2NuclearControl instance;

    
    @Override
    abstract public Object getGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);

    @Override
    public String getVersion()
    {
        return "v1.1.6";
    }

    protected void addRecipes()
    {
        ItemStack thermalMonitor = new ItemStack(blockNuclearControlMain, 1, BlockNuclearControlMain.DAMAGE_THERMAL_MONITOR);
        Ic2Recipes.addCraftingRecipe(thermalMonitor, new Object[]
                {
                    "GGG", "GCG", "GRG", 
                        Character.valueOf('G'), Items.getItem("reinforcedGlass"), 
                        Character.valueOf('R'), Item.redstone, 
                        Character.valueOf('C'), Items.getItem("advancedCircuit")
                });
        ItemStack howler = new ItemStack(blockNuclearControlMain, 1, BlockNuclearControlMain.DAMAGE_HOWLER_ALARM);
        Ic2Recipes.addCraftingRecipe(howler, new Object[]
                {
                    "NNN", "ICI", "IRI", 
                        Character.valueOf('I'), Item.ingotIron, 
                        Character.valueOf('R'), Item.redstone, 
                        Character.valueOf('N'), Block.music, 
                        Character.valueOf('C'), Items.getItem("electronicCircuit")
                });

        ItemStack industrialAlarm = new ItemStack(blockNuclearControlMain, 1, BlockNuclearControlMain.DAMAGE_INDUSTRIAL_ALARM);
        Ic2Recipes.addCraftingRecipe(industrialAlarm, new Object[]
                {
                    "GOG", "GHG", "GRG", 
                        Character.valueOf('G'), Items.getItem("reinforcedGlass"), 
                        Character.valueOf('O'), new ItemStack(Item.dyePowder, 1, 14), 
                        Character.valueOf('R'), Item.redstone, 
                        Character.valueOf('H'), howler 
                });

        Ic2Recipes.addCraftingRecipe(new ItemStack(blockNuclearControlMain, 1, BlockNuclearControlMain.DAMAGE_REMOTE_THERMO), new Object[] 
                {
                    " F ", " M ", " T ", 
                        Character.valueOf('T'), thermalMonitor, 
                        Character.valueOf('M'), Items.getItem("machine"), 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolThermometer, 1), new Object[] 
                {
                    "IG ", "GWG", " GG", 
                        Character.valueOf('G'), Block.glass, 
                        Character.valueOf('I'), Item.ingotIron, 
                        Character.valueOf('W'), Items.getItem("waterCell")
                });
        ItemStack digitalThermometer = new ItemStack(itemToolDigitalThermometer, 1);
        Ic2Recipes.addCraftingRecipe(digitalThermometer, new Object[] 
                {
                    "I  ", "IC ", " GI", 
                        Character.valueOf('G'), Item.lightStoneDust, 
                        Character.valueOf('I'), Items.getItem("refinedIronIngot"), 
                        Character.valueOf('C'), Items.getItem("electronicCircuit")
                });
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemRemoteSensorKit, 1), new Object[] 
                {
                    "  F", " D ", "P  ", 
                        Character.valueOf('P'), Item.paper, 
                        Character.valueOf('D'), digitalThermometer, 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemRangeUpgrade, 1), new Object[] 
                {
                    "   ", "CFC", "   ", 
                        Character.valueOf('C'), Items.getItem("insulatedCopperCableItem"), 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Ic2Recipes.addShapelessCraftingRecipe(new ItemStack(Items.getItem("electronicCircuit").getItem(), 2),  
                itemSensorLocationCard );
    }
    
    protected static int getIdFor(Configuration configuration, String name, int i, boolean block)
    {
        try
        {
            if (block)
                return new Integer(configuration.getOrCreateBlockIdProperty(name, i).value).intValue();
            else
                return new Integer(configuration.getOrCreateIntProperty(name, "item", i).value).intValue();
        } 
        catch (Exception exception)
        {
            System.out.println("Can't get id for :" + name);
        }

        return i;
    }

    protected void initBlocks(Configuration configuration)
    {
        blockNuclearControlMain = new BlockNuclearControlMain(getIdFor(configuration, "blockNuclearControlMain", 192, true), 0)
                .setBlockName("blockThermalMonitor");
        itemToolThermometer = new ItemToolThermometer(
                getIdFor(configuration, "itemToolThermometer", 31000, false), 
                2, ThermometerVersion.ANALOG)
                .setItemName("ItemToolThermometer");
        itemToolDigitalThermometer = new ItemToolDigitalThermometer(
                getIdFor(configuration, "itemToolDigitalThermometer", 31001, false),
                18, ThermometerVersion.DIGITAL, 1, 80, 80)
                .setItemName("ItemToolDigitalThermometer");
        itemRemoteSensorKit = new ItemRemoteSensorKit(
                getIdFor(configuration, "itemRemoteSensorKit", 31002, false),34)
                .setItemName("ItemRemoteSensorKit");
        itemSensorLocationCard = new ItemSensorLocationCard(
                getIdFor(configuration, "itemSensorLocationCard", 31003, false), 50)
                .setItemName("ItemSensorLocationCard");
        itemRangeUpgrade = new ItemRangeUpgrade(
                getIdFor(configuration, "itemRangeUpgrade", 31004, false), 66)
                .setItemName("ItemRangeUpgrade");
    }
    
    abstract protected File getConfigFile(String name);

    protected Configuration importConfig() throws IOException
    {
        int blockId = -1;
        int thermoAnalog = -1;
        int thermoDigital = -1;
        Configuration configuration;
        
        File file = getConfigFile(CONFIG_THERMO_BLOCK);
        if(file.exists() && file.canRead())
        {
            Properties props = new Properties();
            props.load(new FileInputStream(file));
            blockId = Integer.parseInt(props.getProperty("thermo_blockid", "192"));
        }

        file = getConfigFile(CONFIG_THERMOMETER);
        if(file.exists() && file.canRead())
        {
            configuration = new Configuration(file);
            configuration.load();
            thermoAnalog = getOldIdFor(configuration, "itemToolThermometer", 31000);
            thermoDigital = getOldIdFor(configuration, "itemToolDigitalThermometer", 31001);
        }

        file = getConfigFile(CONFIG_NUCLEAR_CONTROL);
        configuration = new Configuration(file);
        configuration.load();
        if(thermoAnalog != -1)
            getIdFor(configuration, "itemToolThermometer", thermoAnalog, false);
        if(thermoDigital != -1)
            getIdFor(configuration, "itemToolDigitalThermometer", thermoDigital, false);
        if(blockId != -1)
            getIdFor(configuration, "blockNuclearControlMain", blockId, true);
        configuration.save();
        return configuration;

    }

    @Override
    public boolean clientSideRequired()
    {
        return true;
    }

    @Override
    public boolean serverSideRequired()
    {
        return false;
    }
    
    @Override
    public void modsLoaded()
    {
        super.modsLoaded();
        addRecipes();
    }    

    private int getOldIdFor(Configuration configuration, String name, int i)
    {
        try
        {
            return new Integer(configuration.getOrCreateIntProperty(name, "general", i).value).intValue();
        } 
        catch (Exception exception)
        {
            System.out.println("Can't get id for :" + name);
        }

        return i;
    }
    
    public static void launchGui(World world, int x, int y, int z, EntityPlayer entityplayer, int blockType)
    {
        entityplayer.openGui(instance, blockType, world, x, y, z);
    }

    public void registerBlocks()
    {
        ModLoader.registerBlock(blockNuclearControlMain, ItemNuclearControlMain.class);
    }

    @Override
    abstract public void load();
}
