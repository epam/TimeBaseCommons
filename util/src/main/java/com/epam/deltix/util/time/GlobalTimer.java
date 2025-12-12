package com.epam.deltix.util.time;

import java.util.Timer;

/**
 * Java Timer class uses dedicated thread to schedule timer tasks. To avoid multiplying these threads you can use this singleton.
 */
public class GlobalTimer {

    public static final Timer INSTANCE = new Timer("Global Timer", true);
}
