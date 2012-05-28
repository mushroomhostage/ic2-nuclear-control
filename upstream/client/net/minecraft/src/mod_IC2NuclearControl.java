package net.minecraft.src;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.forge.Property;
import net.minecraft.src.nuclearcontrol.BlockNuclearControlMain;
import net.minecraft.src.nuclearcontrol.ContainerRemoteThermo;
import net.minecraft.src.nuclearcontrol.GuiIC2Thermo;
import net.minecraft.src.nuclearcontrol.GuiRemoteThermo;
import net.minecraft.src.nuclearcontrol.IC2NuclearControl;
import net.minecraft.src.nuclearcontrol.TileEntityIC2Thermo;
import net.minecraft.src.nuclearcontrol.TileEntityIC2ThermoRenderer;
import net.minecraft.src.nuclearcontrol.TileEntityRemoteThermo;
import net.minecraft.src.nuclearcontrol.TileEntityRemoteThermoRenderer;

import org.lwjgl.opengl.GL11;

public class mod_IC2NuclearControl extends IC2NuclearControl
{
    private static final String CONFIG_NUCLEAR_CONTROL_LANG = "IC2NuclearControl.lang";

    public static boolean isClient()
    {
        return true;
    }
    
    @Override
    protected File getConfigFile(String name)
    {
    	return new File(new File(Minecraft.getMinecraftDir(), "config"), name);
    }

    @Override
    public void load()
    {
        instance = this;
        ModLoader.setInGameHook(this, true, false);

        MinecraftForgeClient.preloadTexture("/img/texture_thermo.png");
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

        initBlocks(configuration);
        registerBlocks();
        addNames();
        importSound(configuration);
        TileEntityIC2ThermoRenderer renderThermalMonitor = new TileEntityIC2ThermoRenderer();
        TileEntityRemoteThermoRenderer renderRemoteThermo = new TileEntityRemoteThermoRenderer();
        
        ModLoader.registerTileEntity(net.minecraft.src.nuclearcontrol.TileEntityIC2Thermo.class, "IC2Thermo", renderThermalMonitor);
        ModLoader.registerTileEntity(net.minecraft.src.nuclearcontrol.TileEntityHowlerAlarm.class, "IC2HowlerAlarm");
        ModLoader.registerTileEntity(net.minecraft.src.nuclearcontrol.TileEntityIndustrialAlarm.class, "IC2IndustrialAlarm");
        ModLoader.registerTileEntity(net.minecraft.src.nuclearcontrol.TileEntityRemoteThermo.class, "IC2RemoteThermo", renderRemoteThermo);
        modelId = ModLoader.getUniqueBlockModelID(this, true);
        MinecraftForge.setGuiHandler(this, this);
        if(configuration!=null)
        {
        	configuration.save();
        }
    }
    
    private void importSound(Configuration configuration)
    {
        File soundDir =  new File(new File(Minecraft.getMinecraftDir(), "resources"), "newsound");
        File ncSoundDir = new File(soundDir, "ic2nuclearControl");
        if(!ncSoundDir.exists()){
            ncSoundDir.mkdir();
        }
        File alarmFile = new File(ncSoundDir, "alarm.ogg");
        if(!alarmFile.exists()){
            try
            {
                if(!alarmFile.createNewFile() || !alarmFile.canWrite())
                    return;
                InputStream input = getClass().getResourceAsStream("/sound/nuclear-alarm.ogg");
                FileOutputStream output = new FileOutputStream(alarmFile);
                byte[] buf = new byte[8192];
                while (true) {
                  int length = input.read(buf);
                  if (length < 0)
                    break;
                  output.write(buf, 0, length);
                }
                input.close();
                output.close();
            } catch (IOException e)
            {
                System.out.println("[IC2NuclearControl] can't import sound file");
            }
        }
        alarmRange = new Float(configuration.getOrCreateIntProperty("alarmRange", Configuration.CATEGORY_GENERAL, 64).value).floatValue() / 16F;
        ModLoader.getMinecraftInstance().sndManager.addSound("ic2nuclearControl/alarm.ogg", alarmFile);
    }
    
    @Override
    public void renderInvBlock(RenderBlocks render, Block block, int metadata, int model)
    {
        if(model == modelId){
            float[] size = BlockNuclearControlMain.blockSize[metadata];
            block.setBlockBounds(size[0], size[1], size[2], size[3], size[4], size[5]);
            Tessellator tesselator = Tessellator.instance;
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, -1.0F, 0.0F);
            render.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 1.0F, 0.0F);
            render.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 0.0F, -1.0F);
            render.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 0.0F, 1.0F);
            render.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(-1.0F, 0.0F, 0.0F);
            render.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(1.0F, 0.0F, 0.0F);
            render.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, metadata));
            tesselator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }
    
    @Override
    public boolean renderWorldBlock(RenderBlocks render, IBlockAccess blockAccess, int x, int y, int z, Block block, int model)
    {
       if(model == modelId){
           render.renderStandardBlock(block, x, y, z);
       }
       return false;
    }

    private static void setPhrase(Configuration configuration, String key, String defaultValue)
    {
        configuration.getOrCreateProperty(key, "locale.en.US", defaultValue);
    }
    
    public void addNames()
    {
        try
        {
            Configuration configuration = new Configuration(getConfigFile(CONFIG_NUCLEAR_CONTROL_LANG));
            configuration.load();
            setPhrase(configuration, "item.ItemToolThermometer.name","Thermometer");
            setPhrase(configuration, "item.ItemToolDigitalThermometer.name", "Digital Thermometer");
            setPhrase(configuration, "item.ItemRemoteSensorKit.name", "Remote Sensor Kit");
            setPhrase(configuration, "item.ItemSensorLocationCard.name", "Sensor Location Card");
            setPhrase(configuration, "item.ItemRangeUpgrade.name", "Range Upgrade");
            setPhrase(configuration, "tile.blockThermalMonitor.name", "Thermal Monitor");
            setPhrase(configuration, "tile.blockIndustrialAlarm.name", "Industrial Alarm");
            setPhrase(configuration, "tile.blockHowlerAlarm.name", "Howler Alarm");
            setPhrase(configuration, "tile.blockRemoteThermo.name", "Remote Thermal Monitor");
            
            for(Map.Entry<String, Map<String, Property>> category : configuration.categories.entrySet())
            {
                String rawLocale = category.getKey(); 
                if(rawLocale == null || !rawLocale.startsWith("locale."))
                    continue;
                rawLocale = rawLocale.substring(7);
                String[] chunks = rawLocale.split("\\.");
                Locale locale;
                if(chunks.length>1)
                    locale = new Locale(chunks[0], chunks[1]);
                else
                    locale = new Locale(chunks[0]);
                
                for(Property property : category.getValue().values())
                {
                    ModLoader.addLocalization(property.name, locale.toString(),  new String(property.value.getBytes("8859_1"),"UTF-8"));
                }
            
            }
            configuration.save();
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            System.out.println("[IC2NuclearControl] Error occured while loading "+CONFIG_NUCLEAR_CONTROL_LANG);
        }
    }

    public static void chatMessage(EntityPlayer entityplayer, String message)
    {
    	ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(message);
    }
    
    @Override
    public Object getGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity= world.getBlockTileEntity(x, y, z);
        switch (ID)
        {
            case BlockNuclearControlMain.DAMAGE_THERMAL_MONITOR:
                return new GuiIC2Thermo(world, x, y, z, player, (TileEntityIC2Thermo)tileEntity);
            case BlockNuclearControlMain.DAMAGE_REMOTE_THERMO:
                ContainerRemoteThermo container = new ContainerRemoteThermo(player, (TileEntityRemoteThermo)tileEntity);
                return new GuiRemoteThermo(container);
            default:
                return null;
        }
    }
    
}
