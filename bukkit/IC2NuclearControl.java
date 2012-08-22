package nuclearcontrol;

import forge.Configuration;
import forge.IConnectionHandler;
import forge.IGuiHandler;
import forge.IPacketHandler;
import forge.MessageManager;
import forge.MinecraftForge;
import forge.NetworkMod;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import net.minecraft.server.Block;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.World;

public abstract class IC2NuclearControl extends NetworkMod implements IGuiHandler, IConnectionHandler, IPacketHandler
{
    protected static final int PACKET_ALARM = 1;
    protected static final int PACKET_SENSOR = 2;
    public static final String LOG_PREFIX = "[IC2NuclearControl] ";
    public static final String MSG_PREFIX = "ic2:nc:c7518eb6:";
    public static final String NETWORK_CHANNEL_NAME = "nuclearControl";
    protected static final String CONFIG_NUCLEAR_CONTROL = "IC2NuclearControl.cfg";
    protected static final String CONFIG_THERMO_BLOCK = "mod_thermo.cfg";
    protected static final String CONFIG_THERMOMETER = "IC2Thermometer.cfg";
    public static int IC2WrenchId;
    public static int IC2ElectricWrenchId;
    public static Item itemToolThermometer;
    public static Item itemToolDigitalThermometer;
    public static Item itemRemoteSensorKit;
    public static Item itemEnergySensorKit;
    public static Item itemSensorLocationCard;
    public static Item itemEnergySensorLocationCard;
    public static Item itemEnergyArrayLocationCard;
    public static Item itemTimeCard;
    public static Item itemRangeUpgrade;
    public static Block blockNuclearControlMain;
    public static int modelId;
    public static int alarmRange;
    protected static IC2NuclearControl instance;
    public static int SMPMaxAlarmRange;
    public static int maxAlarmRange;
    public static List availableAlarms;
    public static int remoteThermalMonitorEnergyConsumption;
    public static ScreenManager screenManager = new ScreenManager();
    public static int screenRefreshPeriod;

    public abstract Object getGuiElement(int var1, EntityHuman var2, World var3, int var4, int var5, int var6);

    public String getVersion()
    {
        return "v1.1.11";
    }

    protected void addRecipes()
    {
        ItemStack var1 = new ItemStack(blockNuclearControlMain, 1, 0);
        Ic2Recipes.addCraftingRecipe(var1, new Object[] {"GGG", "GCG", "GRG", 'G', Items.getItem("reinforcedGlass"), 'R', Item.REDSTONE, 'C', Items.getItem("advancedCircuit")});
        ItemStack var2 = new ItemStack(blockNuclearControlMain, 1, 2);
        Ic2Recipes.addCraftingRecipe(var2, new Object[] {"NNN", "ICI", "IRI", 'I', Item.IRON_INGOT, 'R', Item.REDSTONE, 'N', Block.NOTE_BLOCK, 'C', Items.getItem("electronicCircuit")});
        ItemStack var3 = new ItemStack(blockNuclearControlMain, 1, 1);
        Ic2Recipes.addCraftingRecipe(var3, new Object[] {"GOG", "GHG", "GRG", 'G', Items.getItem("reinforcedGlass"), 'O', new ItemStack(Item.INK_SACK, 1, 14), 'R', Item.REDSTONE, 'H', var2});
        Ic2Recipes.addCraftingRecipe(new ItemStack(blockNuclearControlMain, 1, 3), new Object[] {"F", "M", "T", 'T', var1, 'M', Items.getItem("machine"), 'F', Items.getItem("frequencyTransmitter")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(blockNuclearControlMain, 1, 4), new Object[] {"PPP", "LCL", "IRI", 'P', Block.THIN_GLASS, 'L', new ItemStack(Item.INK_SACK, 1, 10), 'I', new ItemStack(Item.INK_SACK, 1, 0), 'R', Item.REDSTONE, 'C', Items.getItem("electronicCircuit")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(blockNuclearControlMain, 1, 5), new Object[] {"PPP", "WLW", "WWW", 'P', Block.THIN_GLASS, 'L', new ItemStack(Item.INK_SACK, 1, 10), 'W', Block.WOOD});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolThermometer, 1), new Object[] {"IG ", "GWG", " GG", 'G', Block.GLASS, 'I', Item.IRON_INGOT, 'W', Items.getItem("waterCell")});
        ItemStack var4 = new ItemStack(itemToolDigitalThermometer, 1);
        Ic2Recipes.addCraftingRecipe(var4, new Object[] {"I  ", "IC ", " GI", 'G', Item.GLOWSTONE_DUST, 'I', Items.getItem("refinedIronIngot"), 'C', Items.getItem("electronicCircuit")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemRemoteSensorKit, 1), new Object[] {"  F", " D ", "P  ", 'P', Item.PAPER, 'D', var4, 'F', Items.getItem("frequencyTransmitter")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemEnergySensorKit, 1), new Object[] {"  F", " D ", "P  ", 'P', Item.PAPER, 'D', Items.getItem("ecMeter"), 'F', Items.getItem("frequencyTransmitter")});
        Ic2Recipes.addCraftingRecipe(new ItemStack(itemRangeUpgrade, 1), new Object[] {"CFC", 'C', Items.getItem("insulatedCopperCableItem"), 'F', Items.getItem("frequencyTransmitter")});
        Ic2Recipes.addShapelessCraftingRecipe(new ItemStack(itemTimeCard, 1), new Object[] {Items.getItem("electronicCircuit"), Item.WATCH});
        Ic2Recipes.addShapelessCraftingRecipe(new ItemStack(Items.getItem("electronicCircuit").getItem(), 2), new Object[] {itemSensorLocationCard});
        Ic2Recipes.addShapelessCraftingRecipe(new ItemStack(Items.getItem("electronicCircuit").getItem(), 2), new Object[] {itemEnergySensorLocationCard});
        CraftingManager.getInstance().getRecipies().add(new StorageArrayRecipe());
    }

    protected static int getIdFor(Configuration var0, String var1, int var2, boolean var3)
    {
        try
        {
            return var3 ? (new Integer(var0.getOrCreateBlockIdProperty(var1, var2).value)).intValue() : (new Integer(var0.getOrCreateIntProperty(var1, "item", var2).value)).intValue();
        }
        catch (Exception var5)
        {
            ModLoader.getLogger().log(Level.WARNING, "[IC2NuclearControl] Can\'t get id for:" + var1);
            return var2;
        }
    }

    protected void initBlocks(Configuration var1)
    {
        blockNuclearControlMain = (new BlockNuclearControlMain(getIdFor(var1, "blockNuclearControlMain", 192, true), 0)).a("blockThermalMonitor");
        itemToolThermometer = (new ItemToolThermometer(getIdFor(var1, "itemToolThermometer", 31000, false), 2, ThermometerVersion.ANALOG)).a("ItemToolThermometer");
        itemToolDigitalThermometer = (new ItemToolDigitalThermometer(getIdFor(var1, "itemToolDigitalThermometer", 31001, false), 18, ThermometerVersion.DIGITAL, 1, 80, 80)).a("ItemToolDigitalThermometer");
        itemRemoteSensorKit = (new ItemRemoteSensorKit(getIdFor(var1, "itemRemoteSensorKit", 31002, false), 34)).a("ItemRemoteSensorKit");
        itemSensorLocationCard = (new ItemSensorLocationCard(getIdFor(var1, "itemSensorLocationCard", 31003, false), 50)).a("ItemSensorLocationCard");
        itemRangeUpgrade = (new ItemRangeUpgrade(getIdFor(var1, "itemRangeUpgrade", 31004, false), 66)).a("ItemRangeUpgrade");
        itemTimeCard = (new ItemTimeCard(getIdFor(var1, "itemTimeCard", 31005, false), 48)).a("ItemTimeCard");
        itemEnergySensorKit = (new ItemEnergySensorKit(getIdFor(var1, "itemEnergySensorKit", 31006, false), 65)).a("ItemEnergySensorKit");
        itemEnergySensorLocationCard = (new ItemEnergySensorLocationCard(getIdFor(var1, "itemEnergySensorLocationCard", 31007, false), 49)).a("ItemEnergySensorLocationCard");
        itemEnergyArrayLocationCard = (new ItemEnergyArrayLocationCard(getIdFor(var1, "itemEnergyArrayLocationCard", 31008, false), 51)).a("ItemEnergyArrayLocationCard");
    }

    protected abstract File getConfigFile(String var1);

    protected Configuration importConfig() throws IOException
    {
        int var1 = -1;
        int var2 = -1;
        int var3 = -1;
        File var5 = this.getConfigFile("mod_thermo.cfg");

        if (var5.exists() && var5.canRead())
        {
            Properties var6 = new Properties();
            var6.load(new FileInputStream(var5));
            var1 = Integer.parseInt(var6.getProperty("thermo_blockid", "192"));
        }

        var5 = this.getConfigFile("IC2Thermometer.cfg");
        Configuration var4;

        if (var5.exists() && var5.canRead())
        {
            var4 = new Configuration(var5);
            var4.load();
            var2 = this.getOldIdFor(var4, "itemToolThermometer", 31000);
            var3 = this.getOldIdFor(var4, "itemToolDigitalThermometer", 31001);
        }

        var5 = this.getConfigFile("IC2NuclearControl.cfg");
        var4 = new Configuration(var5);
        var4.load();

        if (var2 != -1)
        {
            getIdFor(var4, "itemToolThermometer", var2, false);
        }

        if (var3 != -1)
        {
            getIdFor(var4, "itemToolDigitalThermometer", var3, false);
        }

        if (var1 != -1)
        {
            getIdFor(var4, "blockNuclearControlMain", var1, true);
        }

        var4.save();
        return var4;
    }

    public boolean clientSideRequired()
    {
        return true;
    }

    public boolean serverSideRequired()
    {
        return false;
    }

    public void modsLoaded()
    {
        super.modsLoaded();
        IC2WrenchId = Items.getItem("wrench").id;
        IC2ElectricWrenchId = Items.getItem("electricWrench").id;
        MinecraftForge.registerConnectionHandler(this);
        this.addRecipes();
    }

    private int getOldIdFor(Configuration var1, String var2, int var3)
    {
        try
        {
            return (new Integer(var1.getOrCreateIntProperty(var2, "general", var3).value)).intValue();
        }
        catch (Exception var5)
        {
            ModLoader.getLogger().log(Level.WARNING, "[IC2NuclearControl] Can\'t get id for:" + var2);
            return var3;
        }
    }

    public static void launchGui(World var0, int var1, int var2, int var3, EntityHuman var4, int var5)
    {
        var4.openGui(instance, var5, var0, var1, var2, var3);
    }

    public void registerBlocks()
    {
        ModLoader.registerBlock(blockNuclearControlMain, ItemNuclearControlMain.class);
    }

    public void onConnect(NetworkManager var1)
    {
        MessageManager.getInstance().registerChannel(var1, this, "nuclearControl");
    }

    public void onDisconnect(NetworkManager var1, String var2, Object[] var3) {}

    public void onLogin(NetworkManager var1, Packet1Login var2) {}

    public abstract void load();

    public String getPriorities()
    {
        return "after:mod_IC2";
    }
}
