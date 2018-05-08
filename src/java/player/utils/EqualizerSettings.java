package player.utils;

public enum EqualizerSettings
{
    manuel("Manuel"),
    acoustic("Acoustic", 5, 5, 4, 1.5, 2, 2, 4, 4.5, 4),
    bassBooster("Bass Booster", 6, 4.5, 3.5, 3, 1.5),
    bassReducer("Bass Reducer", bassBooster),
    flat("Flat", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

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
