package com.p14n.stitch.content;

import com.p14n.stitch.StitchException;
import com.p14n.stitch.cache.ContentRequestInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 09/04/2013
 */
public class FileContentStore implements ContentRepository {

    private volatile String version;
    private AtomicInteger idCounter;
    private File versionStore;
    private Properties contentProperties;
    private Map<String, String> pathIdMap;

    @Override
    public synchronized void addContent(Content content) {

        String id = String.valueOf(idCounter.incrementAndGet());
        pathIdMap.put(content.getPath(), id);
        saveContent(content, id);
    }

    private void saveContent(Content content, String id) {
        contentProperties.put(id + ".path", content.getPath());
        contentProperties.put(id + ".encoding", content.getEncoding());
        contentProperties.put(id + ".status", String.valueOf(content.getStatus()));
        contentProperties.put(id + ".type", content.getContentType());

        saveContentProperties();

        try (FileOutputStream outcontent = new FileOutputStream(new File(versionStore, id + ".content"));
             FileOutputStream outmeta = new FileOutputStream(new File(versionStore, id + ".meta"))) {

            outcontent.write(content.getContent());
            outcontent.flush();
            if (content.getMeta() != null) {
                outmeta.write(content.getMeta().getBytes("UTF-8"));
                outmeta.flush();
            }

        } catch (IOException e) {
            throw new StitchException("Could not save content at path " + content.getPath(), e);
        }

    }

    private void saveContentProperties() {
        try (FileOutputStream out = new FileOutputStream(new File(versionStore, "content.properties"))) {

            contentProperties.store(out, "Content properties");

        } catch (IOException e) {
            throw new StitchException("Could not save content properties", e);
        }

    }

    @Override
    public synchronized void updateContent(Content content) {
        saveContent(content, pathIdMap.get(content.getPath()));
    }

    @Override
    public synchronized void deleteContent(Content content) {
        String id = pathIdMap.get(content.getPath());
        contentProperties.remove(id + ".path");
        contentProperties.remove(id + ".encoding");
        contentProperties.remove(id + ".status");
        contentProperties.remove(id + ".type");
        saveContentProperties();
        new File(id + ".content").delete();
        new File(id + ".meta").delete();
    }

    @Override
    public synchronized void initialize(Map<String, String> settings) {
        String rootPath = settings.get("root");
        try {

            File rootFile = new File(rootPath);
            Properties rootProps = new Properties();
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            File props = new File(rootFile, "database.properties");
            if (!props.exists()) {
                version = "1";
                rootFile.mkdirs();
                rootProps.put("current.version", version);
                FileOutputStream stream = new FileOutputStream(props);
                rootProps.store(stream, "Top level storage properties");
                stream.flush();
                ;
                stream.close();
            } else {
                rootProps.load(new FileInputStream(props));
            }
            versionStore = new File(rootFile, rootProps.getProperty("current.version"));
            contentProperties = new Properties();
            idCounter = new AtomicInteger(1);
            pathIdMap = new HashMap<>();
            if (!versionStore.exists()) {
                versionStore.mkdir();
            } else {
                contentProperties.load(new FileReader(new File(versionStore, "content.properties")));
                for (Map.Entry<Object, Object> entry : contentProperties.entrySet()) {
                    String key = entry.getKey().toString();
                    if (key.endsWith(".path")) {
                        String id = key.substring(0, key.indexOf(".path"));
                        int idAsInt = Integer.parseInt(id);
                        if (idAsInt > idCounter.get()) {
                            idCounter.set(idAsInt);
                        }
                        pathIdMap.put(id,
                                entry.getValue().toString());
                    }
                }
            }

        } catch (Exception e) {
            throw new StitchException("Unable to initialize " + FileContentStore.class.getName() + " at " + rootPath, e);
        }
    }

    @Override
    public synchronized List<Content> getContent() {
        List<Content> contents = new ArrayList<Content>();
        for(Map.Entry<String,String> entry:pathIdMap.entrySet()){
            contents.add(getContentById(entry.getKey(),entry.getValue()));
        }
        return contents;
    }

    @Override
    public synchronized Content getContent(ContentRequestInfo info) {
        String id = pathIdMap.get(info.getPath());
        return getContentById(id,info.getPath());
    }
    public synchronized Content getContentById(String id,String path) {
        if (id != null) {
            int status = contentProperties.containsKey(id + ".status") ?
                    Integer.parseInt((String) contentProperties.get(id + ".status")) : 0;
            Content c = new Content(path,
                    contentProperties.getProperty(id + ".type"),
                    contentProperties.getProperty(id + ".encoding"),
                    read(id+".content"),
                    status);
            return c;

        }
        return null;
    }

    private byte[] read(String name) {
        File file = new File(versionStore, name);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (file.exists()) {
            try {
                FileInputStream reader = new FileInputStream(file);
                byte[] word = new byte[2048];
                int read = 0;
                while ((read = reader.read(word)) > 0) {
                    stream.write(word, 0, read);
                }
                stream.flush();
                return stream.toByteArray();
            } catch (IOException e) {
                throw new StitchException("Unable to read content", e);
            }

        }
        return null;
    }
}
