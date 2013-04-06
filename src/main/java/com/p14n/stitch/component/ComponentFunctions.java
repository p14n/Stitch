package com.p14n.stitch.component;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;

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
            for (String key : inner.root().keySet()) {
                String searchKey = key;
                if (key.indexOf('.') > -1) searchKey = "\"" + key + "\"";
                ConfigObject val = inner.getObject(searchKey);
                Component c = new Component();
                c.setDescription(val.get("description").render());
                components.put(key, c);
            }
        }

        List<? extends Config> componentDefinitions = cfg.getConfigList("components");
        if (componentDefinitions != null) for (Config compDef : componentDefinitions) {
            Component c = new Component();
            c.setDescription(compDef.getString("description"));
            components.put(compDef.getString("name"),c);
        }
        return components;
    }
}
