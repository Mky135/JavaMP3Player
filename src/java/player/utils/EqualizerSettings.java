package player.utils;

public enum EqualizerSettings
{
    manuel("Manuel"),
    acoustic("Acoustic", 5, 5, 4, 1.5, 2, 2, 4, 4.5, 4, 2),
    bassBooster("Bass Booster", 6, 4.5, 3.5, 3, 1.5),
    bassReducer("Bass Reducer", bassBooster),
    classical("Classical", 5, 4, 3, 3, -1, -1, 0, 3, 4, 4.5),
    dance("Dance", 4, 7, 5, 0, 2, 4, 5, 4.5, 4, 0),
    deep("Deep", 5, 4, 2, 1.5, 3, 3, 1.5, -1.5, -3, -4.5),
    electronic("Electronic", 4.5, 4, 1.5, 0, -1.5, 3, 1, 1.5, 4.5, 5),
    flat("Flat", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    hiphop("Hip-Hop", 5, 4.5, 1.5, 3, 0, 0, 1.5, 0, 2, 3),
    jazz("Jazz", 4, 3, 1.5, 3, -1.5, -1.5, 0, 1.5, 3, 4),
    loudness("Loudness", 6, 4.5, 0, 0, -1.5, 0, -1, -5, 5.5, 1.5),
    pop("Pop", -1.5, -1, 0, 2, 4, 4, 2, 0, -1, -1.5),
    rock("Rock", 5, 4.5, 3, 1.5, 0, -1, 1, 3, 4, 4.5),
    smallSpeakers("Small Speakers", 6, 4.5, 4, 3, 1.5, 0, -1, -2, -3, -4),
    trebleBooster("Treble Booster", 0, 0, 0, 0, 0, 1.5, 3, 4, 4.5, 6),
    trebleReducer("Treble Reducer", trebleBooster),
    vocalBooster("Vocal Booster", -1, -3, -3, 1.5, 4, 4, 3, 1.5, 0, -1.5);

    String name;
    double[] gains = new double[10];

    EqualizerSettings(String name)
    {
        this.name = name;
    }

    EqualizerSettings(String name, EqualizerSettings equalizerSettings)
    {
        this.name = name;
        gains = equalizerSettings.invertedGains();
    }

    EqualizerSettings(String name, double... gains)
    {
        this.name = name;

        for(int i = 0; i < gains.length; i++)
        {
            this.gains[i] = gains[i];
        }
    }

    public double[] invertedGains()
    {
        double[] iGains = new double[10];

        for(int i = 0; i < iGains.length; i++)
        {
            iGains[i] = -gains[i];
        }

        return iGains;
    }

    public double[] getGains()
    {
        return gains;
    }

    public void setGains(double[] gains)
    {
        this.gains = gains;
    }

    public String getName()
    {
        return name;
    }
}
