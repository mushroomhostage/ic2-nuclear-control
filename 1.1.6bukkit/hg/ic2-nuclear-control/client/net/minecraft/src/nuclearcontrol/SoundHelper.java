package net.minecraft.src.nuclearcontrol;

import java.lang.reflect.Field;

import net.minecraft.src.Entity;
import net.minecraft.src.GameSettings;
import net.minecraft.src.ModLoader;
import net.minecraft.src.SoundManager;
import net.minecraft.src.SoundPool;
import net.minecraft.src.SoundPoolEntry;

import paulscode.sound.SoundSystem;

public class SoundHelper
{
    //obfuscated properties names
    private static final String INTERNAL_SND_SYSTEM = "a";//sndSystem
    private static final String INTERNAL_OPTIONS = "f";//options
    private static final String INTERNAL_SOUND_POOL_SOUNDS = "b";//soundPoolSounds
    private static final String INTERNAL_LOADED = "g";//loaded

    private static final String SOUND_ID_PREFIX = "ic2NuclearControl_";
    private static final float DEFAULT_RANGE = 16F; 
    
    private static Field sndSystemField = null;
    private static Field gameSettingsField = null;
    private static int internalId = 0;
    
    private static SoundSystem getSndSystem()
    {
        if(sndSystemField == null){
            try
            {
                sndSystemField = SoundManager.class.getDeclaredField(INTERNAL_SND_SYSTEM);//sndSystem
                sndSystemField.setAccessible(true);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            return (SoundSystem) sndSystemField.get(null);
        } 
        catch (Exception e)
        {
           throw new RuntimeException(e);
        } 
    }
    
    private static GameSettings getSettings()
    {
        if(gameSettingsField == null){
            try
            {
                gameSettingsField = SoundManager.class.getDeclaredField(INTERNAL_OPTIONS);//options
                gameSettingsField.setAccessible(true);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            return (GameSettings) gameSettingsField.get(ModLoader.getMinecraftInstance().sndManager);
        } 
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private static SoundPool getSoundPool()
    {
        try
        {
            Field field = SoundManager.class.getDeclaredField(INTERNAL_SOUND_POOL_SOUNDS);//soundPoolSounds
            field.setAccessible(true);
            return (SoundPool) field.get(ModLoader.getMinecraftInstance().sndManager);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private static boolean isLoaded()
    {
        try
        {
            Field field = SoundManager.class.getDeclaredField(INTERNAL_LOADED);//loaded
            field.setAccessible(true);
            return (Boolean) field.get(null);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private static String playSound(String name, float x, float y, float z, float volume)
    {
        GameSettings settings = getSettings(); 
        if (isLoaded() && settings.soundVolume != 0.0F)
        {
            SoundSystem sndSystem = getSndSystem();
            if(sndSystem == null)
                return null;
            SoundPoolEntry sound = getSoundPool().getRandomSoundFromSoundPool(name);

            if (sound != null && volume > 0.0F)
            {
                internalId = (internalId + 1) % 256;
                String soundId = SOUND_ID_PREFIX + internalId;
                float range = DEFAULT_RANGE;

                if (volume > 1.0F)
                {
                    range *= volume;
                }

                sndSystem.newSource(volume > 1.0F, soundId, sound.soundUrl, sound.soundName, false, x, y, z, 2, range);
                sndSystem.setPitch(soundId, 1);

                if (volume > 1.0F)
                {
                    volume = 1.0F;
                }

                sndSystem.setVolume(soundId, volume * settings.soundVolume);
                sndSystem.play(soundId);
                return soundId;
            }
        }
        return null;
    }

    public static String playAlarm(double x, double y, double z, String name, float volume)
    {
        float range = DEFAULT_RANGE;

        if (volume > 1.0F)
        {
            range *= volume;
        }

        Entity person = ModLoader.getMinecraftInstance().renderViewEntity;

        if (person.getDistanceSq(x, y, z) < (double)(range * range))
        {
            return playSound(name, (float)x, (float)y, (float)z, volume);
        }
        return null;
    }
    
    public static boolean isPlaying(String soundId)
    {
        SoundSystem snd = getSndSystem();
        return snd != null && snd.playing(soundId);
    }
    
    public static void stopAlarm(String soundId)
    {
        getSndSystem().stop(soundId);
        getSndSystem().removeSource(soundId);
    }

}
