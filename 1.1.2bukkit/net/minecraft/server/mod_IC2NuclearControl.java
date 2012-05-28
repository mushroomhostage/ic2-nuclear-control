package net.minecraft.server;

import forge.Configuration;
import forge.NetworkMod;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.World;
import nuclearcontrol.BlockIC2Thermo;
import nuclearcontrol.ItemToolDigitalThermometer;
import nuclearcontrol.ItemToolThermometer;
import nuclearcontrol.ThermometerVersion;
import nuclearcontrol.TileEntityIC2Thermo;

public class mod_IC2NuclearControl extends NetworkMod {

   private static final String CONFIG_NUCLEAR_CONTROL = "IC2NuclearControl.cfg";
   private static final String CONFIG_THERMO_BLOCK = "mod_thermo.cfg";
   private static final String CONFIG_THERMOMETER = "IC2Thermometer.cfg";
   public static Item itemToolThermometer;
   public static Item itemToolDigitalThermometer;
   public static Block IC2Thermo;


   public boolean clientSideRequired() {
      return true;
   }

   public boolean serverSideRequired() {
      return false;
   }

   private static File getConfigFile(String s) {
      return new File(new File("config"), s);
   }

   private static Configuration importConfig() throws IOException {
      int i = -1;
      int j = -1;
      int k = -1;
      File file = getConfigFile("mod_thermo.cfg");
      if(file.exists() && file.canRead()) {
         Properties configuration1 = new Properties();
         configuration1.load(new FileInputStream(file));
         i = Integer.parseInt(configuration1.getProperty("thermo_blockid", "192"));
      }

      file = getConfigFile("IC2Thermometer.cfg");
      Configuration configuration11;
      if(file.exists() && file.canRead()) {
         configuration11 = new Configuration(file);
         configuration11.load();
         j = getOldIdFor(configuration11, "itemToolThermometer", 31000);
         k = getOldIdFor(configuration11, "itemToolDigitalThermometer", 31001);
      }

      file = getConfigFile("IC2NuclearControl.cfg");
      configuration11 = new Configuration(file);
      configuration11.load();
      if(j != -1) {
         getIdFor(configuration11, "itemToolThermometer", j, false);
      }

      if(k != -1) {
         getIdFor(configuration11, "itemToolDigitalThermometer", k, false);
      }

      if(i != -1) {
         getIdFor(configuration11, "blockNuclearControlMain", i, true);
      }

      configuration11.save();
      return configuration11;
   }

   public void load() {
      Configuration configuration;
      try {
         File ioexception = getConfigFile("IC2NuclearControl.cfg");
         if(!ioexception.exists()) {
            configuration = importConfig();
         } else {
            configuration = new Configuration(ioexception);
            configuration.load();
         }
      } catch (IOException var3) {
         var3.printStackTrace();
         configuration = null;
      }

      ModLoader.setInGameHook(this, true, false);
      this.initBlocks(configuration);
      this.registerBlocks();
      ModLoader.registerTileEntity(TileEntityIC2Thermo.class, "IC2Thermo");
      if(configuration != null) {
         configuration.save();
      }

   }

   public void modsLoaded() {
      super.modsLoaded();
      this.addRecipes();
   }

   private static int getIdFor(Configuration configuration, String s, int i, boolean flag) {
      try {
         return flag?(new Integer(configuration.getOrCreateBlockIdProperty(s, i).value)).intValue():(new Integer(configuration.getOrCreateIntProperty(s, "item", i).value)).intValue();
      } catch (Exception var5) {
         System.out.println("Can\'t get id for :" + s);
         return i;
      }
   }

   private static int getOldIdFor(Configuration configuration, String s, int i) {
      try {
         return (new Integer(configuration.getOrCreateIntProperty(s, "general", i).value)).intValue();
      } catch (Exception var4) {
         System.out.println("Can\'t get id for :" + s);
         return i;
      }
   }

   public void initBlocks(Configuration configuration) {
      IC2Thermo = (new BlockIC2Thermo(getIdFor(configuration, "blockNuclearControlMain", 192, true), 0)).c(0.5F).a("blockThermalMonitor").j();
      itemToolThermometer = (new ItemToolThermometer(getIdFor(configuration, "itemToolThermometer", 31000, false), 2, ThermometerVersion.ANALOG)).a("ItemToolThermometer");
      itemToolDigitalThermometer = (new ItemToolDigitalThermometer(getIdFor(configuration, "itemToolDigitalThermometer", 31001, false), 18, ThermometerVersion.DIGITAL, 1, 80, 80)).a("ItemToolDigitalThermometer");
   }

   public void registerBlocks() {
      ModLoader.registerBlock(IC2Thermo);
   }

   public void addRecipes() {
      Ic2Recipes.addCraftingRecipe(new ItemStack(IC2Thermo, 1), new Object[]{"GGG", "GCG", "GRG", Character.valueOf('G'), Items.getItem("reinforcedGlass"), Character.valueOf('R'), Item.REDSTONE, Character.valueOf('C'), Items.getItem("advancedCircuit")});
      Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolThermometer, 1), new Object[]{"IG ", "GWG", " GG", Character.valueOf('G'), Block.GLASS, Character.valueOf('I'), Item.IRON_INGOT, Character.valueOf('W'), Items.getItem("waterCell")});
      Ic2Recipes.addCraftingRecipe(new ItemStack(itemToolDigitalThermometer, 1), new Object[]{"I  ", "IC ", " GI", Character.valueOf('G'), Item.GLOWSTONE_DUST, Character.valueOf('I'), Items.getItem("refinedIronIngot"), Character.valueOf('C'), Items.getItem("electronicCircuit")});
   }

   public static void launchGui(World world, int i, int j, int k, EntityHuman entityhuman) {}

   public static void chatMessage(EntityHuman entityhuman, String s) {
      ((EntityPlayer)entityhuman).netServerHandler.sendPacket(new Packet3Chat(s));
   }

   public String getVersion() {
      return "v1.1.2";
   }
}
