package com.p14n.stitch.component;

import com.p14n.stitch.Page;
import com.p14n.stitch.StitchException;
import com.p14n.stitch.content.Content;
import com.p14n.stitch.settings.SettingsRepository;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            addComponentsFromConfigToMap(cfg, components, inner);
        }

        addComponentsFromConfigToMap(cfg, components, cfg);
        return components;
    }

    private static void addComponentsFromConfigToMap(Config topLevelConfig, Map<String, Component> components,
                                                     Config cfg) {
        List<? extends Config> componentDefinitions = cfg.getConfigList("components");
        if (componentDefinitions != null) for (Config compDef : componentDefinitions) {
            Component c = componentFromConfig(topLevelConfig, compDef);
            components.put(c.getName(), c);
        }
    }

    private static Component componentFromConfig(Config topLevelConfig, Config cfg) {
        Component c = new Component();
        c.setDescription(cfg.getString("description"));
        c.setName(cfg.getString("name"));
        Config creatorValues = cfg.getConfig("component");
        String creatorClass = topLevelConfig.getString("defaults.component.class");
        if (creatorValues.hasPath("class")) {
            creatorClass = creatorValues.getString("class");
        }
        try {
            c.setCreator((Creator) Class.forName(creatorClass).newInstance());
        } catch (Exception e) {
            throw new StitchException("Unable to create class " + creatorClass + " for component " + c.getName(), e);
        }
        for (Map.Entry<String, ConfigValue> entry : creatorValues.entrySet()) {
            c.getCreator().set(entry.getKey(), entry.getValue());
        }
        return c;
    }

    public static String stitch(String html, Map<String, Component> componentMap,
                                 SettingsRepository settings) {
        return stitch(Jsoup.parse(html),componentMap,settings,false,null);
    }

    private static String stitch(Document doc, Map<String, Component> componentMap,
                                 SettingsRepository settings, boolean serverRender, Map<String, Component> clientComponents) {

        doc.outputSettings().prettyPrint(false);
        List<String> js = new ArrayList<>();
        List<String> css = new ArrayList<>();

        for (Element e : doc.getElementsByAttribute("data-stitch")) {
            String componentId = e.attr("data-stitch");
            Component c = componentMap.get(componentId);
            if (c != null) {
                boolean renderLater = serverRender && c.isRenderOnClientOnly();
                if (!renderLater) {
                    Map<String, String> componentSettings = settings.getSettings(componentId);
                    css.addAll(Arrays.asList(c.getCreator().cssDependencies()));
                    js.addAll(Arrays.asList(c.getCreator().javascriptDependencies()));
                    replace(e, c, componentSettings);
                } else {
                    clientComponents.put(c.getName(), c);
                }
            } else {
                e.replaceWith(new Comment("Component " + componentId + " not found", ""));
            }
        }
        addToHead(doc, js, css);

        return doc.outerHtml();


    }

    public static Page stitch(Content content, Map<String, Component> componentMap, SettingsRepository settings) {

        Document doc = contentToDocument(content);
        Map<String, Component> clientComponents = new HashMap<>();

        boolean serverRender = content.isRenderOnServer();

        stitch(doc, componentMap, settings, serverRender, clientComponents);

        if (clientComponents.isEmpty()) clientComponents = null;

        if (serverRender) {
            return Page.serverRenderPage(doc.outerHtml(), content.getEncoding(), clientComponents);
        }
        return Page.clientRenderPage(doc.outerHtml(), content.getEncoding());

    }

    private static Document contentToDocument(Content content) {
        try {
            return Jsoup.parse(new String(content.getContent(), content.getEncoding()));
        } catch (UnsupportedEncodingException e) {
            throw new StitchException("Could not decode content with path " + content.getPath(), e);
        }
    }


    public static void replace(Element e, Component c, Map<String, String> componentSettings) {

        e.after(merge(c.getCreator().html(), componentSettings));
        String componentId = c.getName();
        //e.remove();
        e.replaceWith(new Comment("Component " + componentId, ""));

    }

    public static void addToHead(Document doc, List<String> js, List<String> css) {
        if (!(js.isEmpty() && css.isEmpty())) {
            Set<String> done = new HashSet<>();
            Element head = doc.head();
            for (String c : css) {
                if (!done.contains(c)) {
                    done.add(c);
                    head.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + c + "\">");
                }
            }
            for (String j : js) {
                if (!done.contains(j)) {
                    done.add(j);
                    head.append("<script type=\"text/javascript\" src=\"" + j +
                            "\"></script>");
                }
            }
        }
    }

    public static String merge(String html, Map<String, String> componentSettings) {
        StringBuffer newHtml = new StringBuffer();
        int start = html.indexOf("${");
        int index = 0;
        while (start > -1) {
            newHtml.append(html.substring(index, start));
            int end = html.indexOf('}', start);
            if (end > -1) {
                String prop = html.substring(start + 2, end);
                String val = componentSettings.get(prop);
                if (val == null) val = "${" + prop + "}";
                newHtml.append(val);
                index = end + 1;
            }
            start = html.indexOf("${", index);
        }
        newHtml.append(html.substring(index, html.length()));
        return newHtml.toString();
    }

}
