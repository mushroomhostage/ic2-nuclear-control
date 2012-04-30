// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ThermometerVersion.java

package nuclearcontrol;


public final class ThermometerVersion extends Enum
{

    public static ThermometerVersion[] values()
    {
        return (ThermometerVersion[])$VALUES.clone();
    }

    public static ThermometerVersion valueOf(String name)
    {
        return (ThermometerVersion)Enum.valueOf(nuclearcontrol/ThermometerVersion, name);
    }

    private ThermometerVersion(String s, int i)
    {
        super(s, i);
    }

    public static final ThermometerVersion ANALOG;
    public static final ThermometerVersion DIGITAL;
    private static final ThermometerVersion $VALUES[];

    static 
    {
        ANALOG = new ThermometerVersion("ANALOG", 0);
        DIGITAL = new ThermometerVersion("DIGITAL", 1);
        $VALUES = (new ThermometerVersion[] {
            ANALOG, DIGITAL
        });
    }
}
