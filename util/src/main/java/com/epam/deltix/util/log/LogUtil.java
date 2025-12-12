package com.epam.deltix.util.log;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;

/**
 * Created by Alex Karpovich on 4/16/2018.
 */
public class LogUtil {
    public static final String   LOGGER_NAME        = "deltix.util";
    public static final Log      LOGGER             = LogFactory.getLog(LOGGER_NAME);
}
