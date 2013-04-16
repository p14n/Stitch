package com.p14n.stitch;

import com.p14n.stitch.component.Component;
import com.p14n.stitch.content.Content;

import java.util.List;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 15/04/2013
 */
public class Page {

    Content content;
    List<Component> components;
    String rendered;


    public Page(Content content) {
        this.content = content;
    }

    public List<Component> getComponents() {
        return components;
    }
    public String render(){
        return rendered;
    }

    public boolean hasClientRenderOnlyComponents() {
        if(components==null)return false;
        for(Component c:components){
            if(c.isRenderOnClientOnly()){
                return true;
            }
        }
        return false;
    }

    public Content getContent() {
        return content;
    }
}
