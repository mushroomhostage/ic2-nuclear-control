package net.minecraft.server;

import forge.Configuration;
import forge.MinecraftForge;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import nuclearcontrol.ContainerRemoteThermo;
import nuclearcontrol.IC2NuclearControl;
import nuclearcontrol.TileEntityHowlerAlarm;
import nuclearcontrol.TileEntityIC2Thermo;
import nuclearcontrol.TileEntityIndustrialAlarm;
import nuclearcontrol.TileEntityRemoteThermo;

public class mod_IC2NuclearControl extends IC2NuclearControl
{
    private static String allowedAlarms;

    public static boolean isClient()
    {
        return false;
    }

    protected File getConfigFile(String var1)
    {
        return new File(new File("config"), var1);
    }

    public void load()
    {
        instance = this;
        Configuration var1;

        try
        {
            File var2 = this.getConfigFile("IC2NuclearControl.cfg");

            if (!var2.exists())
            {
                var1 = this.importConfig();
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
        alarmRange = (new Integer(var1.getOrCreateIntProperty("alarmRange", "general", 64).value)).intValue();
        maxAlarmRange = (new Integer(var1.getOrCreateIntProperty("maxAlarmRange", "general", 128).value)).intValue();
        allowedAlarms = var1.getOrCreateProperty("allowedAlarms", "general", "default,sci-fi").value.replaceAll(" ", "");
        SMPMaxAlarmRange = 256;
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

    public void onLogin(NetworkManager var1, Packet1Login var2)
    {
        EntityPlayer var3 = ((NetServerHandler)var1.getNetHandler()).getPlayerEntity();
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        DataOutputStream var5 = new DataOutputStream(var4);

        try
        {
            var5.writeInt(maxAlarmRange);
            var5.writeUTF(allowedAlarms);
            Packet250CustomPayload var6 = new Packet250CustomPayload();
            var6.tag = "nuclearControl";
            var6.lowPriority = false;
            var6.data = var4.toByteArray();
            var6.length = var4.size();
            var3.netServerHandler.sendPacket(var6);
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }
    }

    public void onPacketData(NetworkManager var1, String var2, byte[] var3)
    {
        DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var3));

        try
        {
            int var5 = var4.readInt();
            int var6 = var4.readInt();
            int var7 = var4.readInt();
            String var8 = var4.readUTF();
            EntityPlayer var9 = ((NetServerHandler)var1.getNetHandler()).getPlayerEntity();
            TileEntity var10 = var9.world.getTileEntity(var5, var6, var7);

            if (var10 instanceof TileEntityHowlerAlarm)
            {
                ((TileEntityHowlerAlarm)var10).setSoundName(var8);
            }
        }
        catch (IOException var11)
        {
            System.err.println("[IC2NuclearControl] WARNING: Invalid packet: " + var11.getMessage());
        }
    }
}
