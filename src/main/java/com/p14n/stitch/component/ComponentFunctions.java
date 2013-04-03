package com.p14n.stitch.component;

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
    public static Map<String,Component> getComponentsFromConfig(Config cfg){

        Map<String,Component> components = new HashMap<String,Component>();
        List<String> componentDefinitions = cfg.getStringList("component.files");

        if(componentDefinitions!=null) for (String componentDefinition : componentDefinitions) {
            Config inner = ConfigFactory.parseResources(componentDefinition);
            for (String key : inner.root().keySet()) {
                ConfigObject val = inner.getObject(key);
                Component c = new Component();
                c.setDescription(val.get("description").toString());
                components.put(key,c);
            }
        }
        return components;
    }
}
