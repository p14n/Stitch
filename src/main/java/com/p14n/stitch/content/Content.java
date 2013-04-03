package com.p14n.stitch.content;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 06/03/2013
 */
public class Content {

    String path;
    String contentType;
    String meta;
    String encoding;
    byte[] content;
    int status;

    public String getPath() {
        return path;
    }

    public String getContentType() {
        return contentType;
    }

    public String getEncoding() {
        return encoding;
    }

    public byte[] getContent() {
        return content;
    }

    public int getStatus() {
        return status;
    }

    public Content(String path, int status) {
        this.path = path;
        this.status = status;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Content(String path, String contentType, String encoding, byte[] content, int status) {
        this.path = path;
        this.contentType = contentType;
        this.encoding = encoding;
        this.content = content;
        this.status = status;
    }


}
