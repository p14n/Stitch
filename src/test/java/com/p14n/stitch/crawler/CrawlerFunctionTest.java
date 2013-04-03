package com.p14n.stitch.crawler;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.p14n.stitch.crawler.CrawlerFunctions.*;
import static org.junit.Assert.*;
public class CrawlerFunctionTest {

    @Test public void shouldGiveMeaningfulNameToPageAtRootLevel() {
        assertEquals("index.html",constructNameFor("http://a.com/html/",
                "http://a.com/html/index.html"));
    }
    @Test public void shouldCountTraversals(){
        assertEquals(2 , countTraversals("../../index.html"));
    }
    @Test
    public void shouldFindImageSourcePath(){
        List<String> expected  = new ArrayList<String>();
        expected.add("images/test.gif");
        assertEquals(expected,getOtherPaths("<span class=\"hi\"/><img src=\"images/test.gif\"></img>", "img", "src"));
    }
    @Test
    public void shouldFindLinkPath(){
        List<String> expected  = new ArrayList<String>();
        expected.add("html/other");
        assertEquals(expected,getOtherPaths("<span class=\"hi\"/><a href=\"html/other\"></a>","a","href"));
    }
    @Test
    public void shouldFindImagesFromCss(){
        List<String> expected  = new ArrayList<String>();
        expected.add("images/test.gif");
        expected.add("images/test2.gif");
        assertEquals(expected,
        getImagesFromCSS("url(images/test.gif) url('images/test2.gif')" ));
    }

}
