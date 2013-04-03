package com.p14n.stitch.component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 03/04/2013
 */
public class HtmlComponentCreator extends Creator implements PropertyAcceptor {
    Map<String,String> props = new HashMap<String,String>();
    @Override
    public void set(String key, String val) {
        props.put(key,val);
    }

    @Override
    public String[] javascriptDependencies() {
        String deps = props.get("dependencies");
        if(deps==null)
            return super.javascriptDependencies();
        return deps.split(",");
    }

    @Override
    public String javascript() {
        String script = props.get("script");
        return script;
    }

    @Override
    public String html() {
        String html = props.get("html");
        return html;
    }
}
