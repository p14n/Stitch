package com.p14n.stitch.content;

import java.util.List;
import java.util.Map;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 06/03/2013
 */
public interface ContentRepository {

    public void addContent(Content content);
    public void updateContent(Content content);
    public void deleteContent(Content content);
    public List<Content> getContent();
    public Content getContent(String path);

    public void initialize(Map<String, String> settings);
}
