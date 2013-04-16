package com.p14n.stitch.settings;

import java.util.Map;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 16/04/2013
 */
public interface SettingsRepository {
    public Map<String,String> getSettings(String componentId);
}
