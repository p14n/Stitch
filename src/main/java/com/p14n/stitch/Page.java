package com.p14n.stitch;

import com.p14n.stitch.component.Component;
import com.p14n.stitch.content.Content;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 15/04/2013
 */
public class Page {

    final String stitched,encoding;
    final Map<String, Component> clientComponents;
    final boolean serverRender;

    private Page(boolean serverRender,String stitched, String encoding, Map<String, Component> clientComponents) {
        this.serverRender=serverRender;
        this.stitched = stitched;
        this.encoding = encoding;
        this.clientComponents = clientComponents;
    }

    public String encoding() {
        return encoding;
    }

    public String stitched() {
        return stitched;
    }

    public boolean hasClientRenderOnlyComponents() {
        return clientComponents!=null&&!clientComponents.isEmpty();
    }

    public Map<String, Component> getClientComponents() {
        return clientComponents;
    }
    public static Page serverRenderPage(String stitched, String encoding, Map<String, Component> clientComponents){
        return new Page(true,stitched,encoding,clientComponents);
    }
    public static Page clientRenderPage(String stitched, String encoding){
        return new Page(false,stitched,encoding,null);
    }

    public boolean isServerRender() {
        return serverRender;
    }
}
