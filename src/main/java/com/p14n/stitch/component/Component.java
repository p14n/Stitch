package com.p14n.stitch.component;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 03/04/2013
 */
public class Component {
    String name;
    String description;

    Creator creator;

    public void setRenderOnClientOnly(boolean renderOnClientOnly) {
        this.renderOnClientOnly = renderOnClientOnly;
    }

    private boolean renderOnClientOnly;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public boolean isRenderOnClientOnly() {
        return renderOnClientOnly;
    }
}
