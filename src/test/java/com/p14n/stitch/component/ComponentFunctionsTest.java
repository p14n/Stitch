package com.p14n.stitch.component;

import junit.framework.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 16/04/2013
 */

public class ComponentFunctionsTest {
    @Test
    public void shouldMergeHtmlProperties() {

        Map<String, String> props = new HashMap<>();
        props.put("my.value", "ONE");
        props.put("your.value", "TWO");
        String result = ComponentFunctions.merge("<div><span id='aa'>${my.value}</span><span>${your" +
                ".value}</span><span>${your" +
                ".value}</span><span>${a.value}</span>", props);
        Assert.assertEquals("<div><span id='aa'>ONE</span><span>TWO</span><span>TWO</span><span>${a" +
                ".value}</span>", result);
    }

    @Test
    public void shouldMergeWithoutProperties() {

        String result = ComponentFunctions.merge("<div><span id='aa'></span></div>", null);
        Assert.assertEquals("<div><span id='aa'></span></div>", result);
    }

    @Test
    public void shouldAddJsAndCssToHead() {
        Document doc = Jsoup.parse("<html></html>");
        ComponentFunctions.addToHead(doc,
                Arrays.asList(new String[]{"myfile.js"}),
                Arrays.asList(new String[]{"myfile.css"})
        );
        doc.outputSettings().prettyPrint(false);
        Assert.assertEquals("" +
                "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"myfile.css\" />" +
                "<script type=\"text/javascript\" src=\"myfile.js\"></script></head><body></body></html>", doc.outerHtml());
    }
}
