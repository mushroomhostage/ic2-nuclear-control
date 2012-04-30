// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   mod_IC2NuclearControl.java

import forge.*;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import java.io.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import nuclearcontrol.*;

public class mod_IC2NuclearControl extends NetworkMod
{

    public boolean clientSideRequired()
    {
        return true;
    }

    public boolean serverSideRequired()
    {
        return false;
    }

    public mod_IC2NuclearControl()
    {
    }

    private static File getConfigFile(String name)
    {
        return new File(new File(Minecraft.b(), "config"), name);
    }

    private static Configuration importConfig()
        throws IOException
    {
        int blockId = -1;
        int thermoAnalog = -1;
        int thermoDigital = -1;
        File file = getConfigFile("mod_thermo.cfg");
        if(file.exists() && file.canRead())
        {
            Properties props = new Properties();
            props.load(new FileInputStream(file));
            blockId = Integer.parseInt(props.getProperty("thermo_blockid", "175"));
        }
        file = getConfigFile("IC2Thermometer.cfg");
        Configuration configuration;
        if(file.exists() && file.canRead())
        {
            configuration = new Configuration(file);
            configuration.load();
            thermoAnalog = getOldIdFor(configuration, "itemToolThermometer", 31000);
            thermoDigital = getOldIdFor(configuration, "itemToolDigitalThermometer", 31001);
        }
        System.out.println((new StringBuilder()).append("Imported:").append(blockId).append(" ").append(thermoAnalog).append(" ").append(thermoDigital).toString());
        file = getConfigFile("IC2NuclearControl.cfg");
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
        ModLoader.setInGameHook(this, true, false);
        MinecraftForgeClient.preloadTexture("/img/texture_thermo.png");
        Configuration configuration;
        try
        {
            File file = getConfigFile("IC2NuclearControl.cfg");
            if(!file.exists())
            {
                configuration = importConfig();
            } else
            {
                configuration = new Configuration(file);
                configuration.load();
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
            configuration = null;
        }
        initBlocks(configuration);
        registerBlocks();
        addNames();
        TileEntityIC2ThermoRenderer render = new TileEntityIC2ThermoRenderer();
        ModLoader.registerTileEntity(nuclearcontrol/TileEntityIC2Thermo, "IC2Thermo", render);
        if(configuration != null)
            configuration.save();
    }

    public void modsLoaded()
    {
        super.modsLoaded();
        addRecipes();
    }

    private static int getIdFor(Configuration configuration, String name, int i, boolean block)
    {
        if(block)
            return (new Integer(configuration.getOrCreateBlockIdProperty(name, i).value)).intValue();
        try
        {
            return (new Integer(configuration.getOrCreateIntProperty(name, "item", i).value)).intValue();
        }
        catch(Exception exception)
        {
            System.out.println((new StringBuilder()).append("Can't get id for :").append(name).toString());
        }
        return i;
    }

    private static int getOldIdFor(Configuration configuration, String name, int i)
    {
        try
        {
            return (new Integer(configuration.getOrCreateIntProperty(name, "general", i).value)).intValue();
        }
        catch(Exception exception)
        {
            System.out.println((new StringBuilder()).append("Can't get id for :").append(name).toString());
        }
        return i;
    }

    public void initBlocks(Configuration configuration)
    {
        IC2Thermo = (new BlockIC2Thermo(getIdFor(configuration, "blockNuclearControlMain", 192, true), 0)).c(0.5F).a("blockThermalMonitor").k();
        itemToolThermometer = (new ItemToolThermometer(getIdFor(configuration, "itemToolThermometer", 31000, false), 2, ThermometerVersion.ANALOG)).a("ItemToolThermometer");
        itemToolDigitalThermometer = (new ItemToolDigitalThermometer(getIdFor(configuration, "itemToolDigitalThermometer", 31001, false), 18, ThermometerVersion.DIGITAL, 1, 80, 80)).a("ItemToolDigitalThermometer");
    }

    public void registerBlocks()
    {
        ModLoader.registerBlock(IC2Thermo);
    }

    public void addRecipes()
    {
        Ic2Recipes.addCraftingRecipe(new aan(IC2Thermo, 1), new Object[] {
            "GGG", "GCG", "GRG", Character.valueOf('G'), Items.getItem("reinforcedGlass"), Character.valueOf('R'), yr.aC, Character.valueOf('C'), Items.getItem("advancedCircuit")
        });
        Ic2Recipes.addCraftingRecipe(new aan(itemToolThermometer, 1), new Object[] {
            "IG ", "GWG", " GG", Character.valueOf('G'), pb.M, Character.valueOf('I'), yr.o, Character.valueOf('W'), Items.getItem("waterCell")
        });
        Ic2Recipes.addCraftingRecipe(new aan(itemToolDigitalThermometer, 1), new Object[] {
            "I  ", "IC ", " GI", Character.valueOf('G'), yr.aT, Character.valueOf('I'), Items.getItem("refinedIronIngot"), Character.valueOf('C'), Items.getItem("electronicCircuit")
        });
    }

    private static void setPhrase(Configuration configuration, String key, String defaultValue)
    {
        configuration.getOrCreateProperty(key, "locale.en.US", defaultValue);
    }

    public void addNames()
    {
        try
        {
            Configuration configuration = new Configuration(getConfigFile("IC2NuclearControl.lang"));
            configuration.load();
            setPhrase(configuration, "item.ItemToolThermometer.name", "Thermometer");
            setPhrase(configuration, "item.ItemToolDigitalThermometer.name", "Digital Thermometer");
            setPhrase(configuration, "tile.blockThermalMonitor.name", "Thermal Monitor");
            Iterator i$ = configuration.categories.entrySet().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                java.util.Map.Entry category = (java.util.Map.Entry)i$.next();
                String rawLocale = (String)category.getKey();
                if(rawLocale != null && rawLocale.startsWith("locale."))
                {
                    rawLocale = rawLocale.substring(7);
                    System.out.println(rawLocale);
                    String chunks[] = rawLocale.split("\\.");
                    Locale locale;
                    if(chunks.length > 1)
                        locale = new Locale(chunks[0], chunks[1]);
                    else
                        locale = new Locale(chunks[0]);
                    Iterator i$ = ((Map)category.getValue()).values().iterator();
                    while(i$.hasNext()) 
                    {
                        Property property = (Property)i$.next();
                        System.out.println((new StringBuilder()).append(property.name).append(":").append(locale.toString()).append(":").append(new String(property.value.getBytes("8859_1"), "UTF-8")).toString());
                        ModLoader.addLocalization(property.name, locale.toString(), new String(property.value.getBytes("8859_1"), "UTF-8"));
                    }
                }
            } while(true);
            configuration.save();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            System.out.println("[IC2NuclearControl] Error occured while loading IC2NuclearControl.lang");
        }
    }

    public static void launchGui(xd world, int x, int y, int z, yw entityplayer)
    {
        TileEntityIC2Thermo tileentityic2thermo = (TileEntityIC2Thermo)world.b(x, y, z);
        vp guiscreen = new GuiIC2Thermo(world, x, y, z, entityplayer, tileentityic2thermo);
        ModLoader.openGUI(entityplayer, guiscreen);
    }

    public static void chatMessage(yw entityplayer, String message)
    {
        ModLoader.getMinecraftInstance().w.a(message);
    }

    public String getVersion()
    {
        return "v1.1.2";
    }

    private static final String CONFIG_NUCLEAR_CONTROL = "IC2NuclearControl.cfg";
    private static final String CONFIG_NUCLEAR_CONTROL_LANG = "IC2NuclearControl.lang";
    private static final String CONFIG_THERMO_BLOCK = "mod_thermo.cfg";
    private static final String CONFIG_THERMOMETER = "IC2Thermometer.cfg";
    public static yr itemToolThermometer;
    public static yr itemToolDigitalThermometer;
    public static pb IC2Thermo;
}
