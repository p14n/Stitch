package com.p14n.stitch.component;

import com.p14n.stitch.StitchException;
import com.p14n.stitch.settings.SettingsRepository;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 03/04/2013
 */
public class ComponentFunctions {
    public static Map<String, Component> getComponentsFromConfig(Config cfg) {

        Map<String, Component> components = new HashMap<String, Component>();
        List<String> componentDefinitionFiles = cfg.getStringList("component.files");

        if (componentDefinitionFiles != null) for (String componentDefinition : componentDefinitionFiles) {
            Config inner = ConfigFactory.parseResources(componentDefinition);
            addComponentsFromConfigToMap(cfg,components,inner);
        }

        addComponentsFromConfigToMap(cfg,components,cfg);
        return components;
    }
    private static void addComponentsFromConfigToMap(Config topLevelConfig,Map<String, Component> components,
                                                     Config cfg){
        List<? extends Config> componentDefinitions = cfg.getConfigList("components");
        if (componentDefinitions != null) for (Config compDef : componentDefinitions) {
            Component c = componentFromConfig(topLevelConfig,compDef);
            components.put(c.getName(),c);
        }
    }

    private static Component componentFromConfig(Config topLevelConfig,Config cfg){
        Component c = new Component();
        c.setDescription(cfg.getString("description"));
        c.setName(cfg.getString("name"));
        Config creatorValues = cfg.getConfig("component");
        String creatorClass = topLevelConfig.getString("defaults.component.class");
        if(creatorValues.hasPath("class")){
            creatorClass = creatorValues.getString("class");
        }
        try {
            c.setCreator((Creator) Class.forName(creatorClass).newInstance());
        } catch (Exception e) {
            throw new StitchException("Unable to create class "+creatorClass+" for component "+c.getName(),e);
        }
        for(Map.Entry<String,ConfigValue> entry:creatorValues.entrySet()){
            c.getCreator().set(entry.getKey(),entry.getValue());
        }
        return c;
    }

    public static String stitch(String content,Map<String,Component> componentMap,SettingsRepository settings ){
        return null;
    }

}
