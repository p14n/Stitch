package com.p14n.stitch.content;

import com.p14n.stitch.cache.ContentRequestInfo;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 09/04/2013
 */
public class FileContentStoreTest {

    @Before
    public void setup(){
        Map<String,String> settings = new HashMap<String,String>();
        settings.put("root","./database");
        File root = new File("./database");
        if(root.exists()) deleteDirectoryContents(root);
        store = new FileContentStore();
        store.initialize(settings);
    }
    FileContentStore store;

    private void deleteDirectoryContents(File root) {
        for(File child:root.listFiles()){
            if(child.isDirectory()){
                deleteDirectoryContents(child);
            }
            child.delete();
        }
    }

    @Test
    public void shouldCreateAndRetrieveContent(){
        String path = "my test path/ something";
        Content c = new Content(path,"text/html","utf8","<span>hi</span>".getBytes(),200);

        store.addContent(c);;
        Content c2 = store.getContent(new ContentRequestInfo(null,path,null,null));
        Assert.assertEquals(c,c2);
    }
}
