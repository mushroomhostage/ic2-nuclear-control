package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.ic2.api.Ic2Recipes;
import net.minecraft.src.ic2.api.Items;
import net.minecraft.src.nuclearcontrol.BlockIC2Thermo;
import net.minecraft.src.nuclearcontrol.ItemToolDigitalThermometer;
import net.minecraft.src.nuclearcontrol.ItemToolThermometer;
import net.minecraft.src.nuclearcontrol.ThermometerVersion;

public class mod_IC2NuclearControl extends NetworkMod
{
    private static final String CONFIG_NUCLEAR_CONTROL = "IC2NuclearControl.cfg";
    private static final String CONFIG_THERMO_BLOCK = "mod_thermo.cfg";
    private static final String CONFIG_THERMOMETER = "IC2Thermometer.cfg";

    public static Item itemToolThermometer;
    public static Item itemToolDigitalThermometer;
    public static Block IC2Thermo;

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

    public mod_IC2NuclearControl()
    {
    }

    private static File getConfigFile(String name)
    {
    	return new File(new File("config"), name);
    }
    
    private static Configuration importConfig() throws IOException
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

    public void load()
    {
    	Configuration configuration;
        try
        {
        	File file = getConfigFile(CONFIG_NUCLEAR_CONTROL);
        	if(!file.exists()){
        		configuration = importConfig();
        	}
        	else
        	{
        		configuration = new Configuration(file);
                configuration.load();
        	}
        	
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            configuration = null;
        }
        ModLoader.setInGameHook(this, true, false);
        initBlocks(configuration);
        registerBlocks();
        ModLoader.registerTileEntity(net.minecraft.src.nuclearcontrol.TileEntityIC2Thermo.class, "IC2Thermo");
        if(configuration!=null)
        {
        	configuration.save();
        }
    }

    @Override
    public void modsLoaded()
    {
    	super.modsLoaded();
        addRecipes();
    }

    private static int getIdFor(Configuration configuration, String name, int i, boolean block)
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

    private static int getOldIdFor(Configuration configuration, String name, int i)
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
    
    public void initBlocks(Configuration configuration)
    {
        IC2Thermo = new BlockIC2Thermo(getIdFor(configuration, "blockNuclearControlMain", 192, true), 0)
				.setHardness(0.5F)
				.setBlockName("blockThermalMonitor")
				.setRequiresSelfNotify();
		itemToolThermometer = new ItemToolThermometer(
				getIdFor(configuration, "itemToolThermometer", 31000, false), 
				2, ThermometerVersion.ANALOG)
				.setItemName("ItemToolThermometer");
		itemToolDigitalThermometer = new ItemToolDigitalThermometer(
				getIdFor(configuration, "itemToolDigitalThermometer", 31001, false),
				18, ThermometerVersion.DIGITAL, 1, 80, 80)
				.setItemName("ItemToolDigitalThermometer");

    }

    public void registerBlocks()
    {
        ModLoader.registerBlock(IC2Thermo);
    }

    public void addRecipes()
    {
        Ic2Recipes.addCraftingRecipe(new ItemStack(IC2Thermo, 1), new Object[]
                {
                    "GGG", "GCG", "GRG", 
                    	Character.valueOf('G'), Items.getItem("reinforcedGlass"), 
                    	Character.valueOf('R'), Item.redstone, 
                    	Character.valueOf('C'), Items.getItem("advancedCircuit")
                });
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolThermometer, 1), new Object[] 
        		{
            		"IG ", "GWG", " GG", 
            			Character.valueOf('G'), Block.glass, 
            			Character.valueOf('I'), Item.ingotIron, 
            			Character.valueOf('W'), Items.getItem("waterCell")
    			});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolDigitalThermometer, 1), new Object[] 
        		{
            		"I  ", "IC ", " GI", 
            			Character.valueOf('G'), Item.lightStoneDust, 
            			Character.valueOf('I'), Items.getItem("refinedIronIngot"), 
            			Character.valueOf('C'), Items.getItem("electronicCircuit")
        		});
    }
    
    public static void launchGui(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
    }

    public static void chatMessage(EntityPlayer entityplayer, String message)
    {
        ((EntityPlayerMP) entityplayer).playerNetServerHandler.sendPacket(new Packet3Chat(message));
    }

    public String getVersion()
    {
        return "v1.1.2";
    }
}
