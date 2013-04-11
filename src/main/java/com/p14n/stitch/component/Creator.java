package com.p14n.stitch.component;

import com.typesafe.config.ConfigValue;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 03/04/2013
 */
public abstract class Creator implements PropertyAcceptor {

    public String[] javascriptDependencies(){
        return new String[]{};
    }
    public String javascript(){
        return null;
    }
    public String html(){
        return null;
    }
    public void set(String key,ConfigValue cfg){
        set(key,cfg.render());
    }
}
