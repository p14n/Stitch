package com.p14n.stitch.crawler;

import com.p14n.stitch.content.Content;
import com.p14n.stitch.content.ContentWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static com.p14n.stitch.crawler.CrawlerFunctions.*;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 06/03/2013
 */
public class Crawler {
    private String startPage, root = null;
    ContentWriter writer;

    public Crawler(ContentWriter writer, String root, String startPage) {
        this.startPage = startPage;
        this.root = root;
        this.writer = writer;
    }

    public Crawler(ContentWriter writer, String root) {
        this(writer, root, "index.html");
    }

    Logger syslog = LoggerFactory.getLogger(Crawler.class);

    public void fetch(Writer log) {
        fetchFrom(root + startPage, new HashSet<String>(), log);
    }

    public void fetchFrom(String url, Set<String> visited, Writer log) {

        if (visited.contains(url)) return;

        visited.add(url);

        String name = url;
        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            name = constructNameFor(root, url);

            info(log, "Response code " + responseCode + " from " + name);
            if (responseCode != 200) {
                writer.putContent(new Content(name, responseCode));
                info(log, "Saved missing content " + name);
                return;
            }

            String contentType = conn.getContentType().toLowerCase();
            String encoding = conn.getContentEncoding();
            byte[] bytes = streamToBytes(conn.getInputStream());
            writer.putContent(new Content(name, contentType, encoding, bytes, responseCode));
            info(log, "Saved content type " + contentType);

            if (contentType.indexOf("text/html") > -1) {
                info(log, "Parsing " + name + " for links");
                String page = new String(bytes, (encoding == null)? "UTF-8": encoding);
                parseAndFetch(page, "a", "href", url, visited, log);
                parseAndFetch(page, "img", "src", url, visited, log);
                parseAndFetch(page, "link", "href", url, visited, log);
            } else if (contentType.indexOf("css") > -1) {
                info(log, "Parsing " + name + " for images");
                String page = new String(bytes, (encoding == null)? "UTF-8":encoding);
                for(String path:getImagesFromCSS(page)){
                    fetchFrom(constructContentUrlFor(url, path), visited, log);
                }
            }
        } catch (FileNotFoundException e) {
            error(log,"File not saved " + name,e);
        } catch (MalformedURLException e) {
            error(log,"Invalid url " + url,e);
        } catch (ProtocolException e) {
            error(log,"Invalid url " + url,e);
        } catch (IOException e) {
            error(log,"Could not read url " + url,e);
        }
    }

    private void info(Writer log, String text) {
        try {
            log.write(text);
        } catch (IOException e1) {
            syslog.error("Could not write to log", e1);
        }
        syslog.info(text);
    }
    private void error(Writer log, String text,Throwable e) {
        try {
            log.write(text);
        } catch (IOException e1) {
            syslog.error("Could not write to log", e1);
        }
        syslog.error(text,e);
    }

    private void parseAndFetch(String page,String tag,String attr,String parentUrl,Set<String> visited,Writer log) {
        for(String path:getOtherPaths(page, tag, attr)){
            fetchFrom(constructContentUrlFor(parentUrl, path), visited, log);
        }
    }

}
