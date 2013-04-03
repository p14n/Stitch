package com.p14n.stitch.crawler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 04/03/2013
 */
public class CrawlerFunctions {

    public static String constructNameFor(String rootContentUrl,
                                          String thisContentUrl) {
        return thisContentUrl.substring(rootContentUrl.length());
    }
    public static String constructContentUrlFor(String pageUrl, String relativeUrl) {
        int traversals = countTraversals(relativeUrl);
        if(traversals == 0)
            return pageUrl.substring(0,pageUrl.lastIndexOf("/")+1)+removeStartingSlash(relativeUrl);
        return removePathsFromUrl(pageUrl, traversals) +
                relativeUrl.substring(relativeUrl.lastIndexOf("../") + 3);
    }
    public static String removeStartingSlash(String url) {
        if(url == null) return null;
        if(url.startsWith("/")) return url.substring(1);
        return url;
    }
    public static int countTraversals(String url) {
        Map<String,AtomicInteger> counts = new HashMap<String, AtomicInteger>();
        for(String element:url.split("/")){
            if(!counts.containsKey(element))
                counts.put(element,new AtomicInteger(0));
            counts.get(element).incrementAndGet();
        }
        if(!counts.containsKey("..")) return 0;
        return counts.get("..").get();
    }
    public static String removePathsFromUrl(String url,int toRemove) {
        return removePathsFromUrl(url, toRemove, -100);
    }
    public static String removePathsFromUrl(String url,int toRemove,int lastPosition) {
        int currentPosition = (lastPosition == -100)? url.length() : lastPosition;
        if(toRemove<1) return url.substring(0,url.lastIndexOf("/",currentPosition) + 1);
        int nextPosition = url.lastIndexOf("/",currentPosition) - 1;
        return removePathsFromUrl(url, toRemove - 1, nextPosition);
    }
    public static byte[] streamToBytes(InputStream i) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = i.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    public static List<String> getOtherPaths(String content,String tag,String attr) {

        int index = 0;
        List<String> paths = new ArrayList<String>();
        if(content==null) return paths;
        while (index<content.length()-1 && (paths.isEmpty() || index > 0 )){
            index = content.indexOf("<"+tag+" ",index) + 1;
            if(index==0) return paths;

            int tagEnd = content.indexOf(">",index);
            int href = content.indexOf(attr,index);

            if(href > -1 && href < tagEnd){
                int pathStart = content.indexOf("\"",href)+1;
                String path = content.substring(pathStart,content.indexOf("\"",pathStart));

                if(path.indexOf("://") == -1 && !path.startsWith("#"))
                    paths.add(path);
            }
            index = tagEnd;

        }
        return paths;
    }
    public static List<String> getImagesFromCSS(String content) {
        int index = 0;
        List<String> paths = new ArrayList<String>();

        while (index<content.length()-1 && (paths.isEmpty() || index > 0 )){
            index = content.indexOf("url(",index) + 4;
            if(index==3) return paths;

            int tagEnd = content.indexOf(")",index);

            if(tagEnd > index){
                paths.add(content.substring(index, tagEnd).replaceAll("'",""));
            }
            index = tagEnd;
        }
        return paths;
    }
 }
