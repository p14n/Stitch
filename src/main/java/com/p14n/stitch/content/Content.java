package com.p14n.stitch.content;

import java.util.Arrays;

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
    boolean renderOnServer;

    public Content(String path, String contentType, String encoding, byte[] content, int status) {
        this.path = path;
        this.contentType = contentType;
        this.encoding = encoding;
        this.content = content;
        this.status = status;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content1 = (Content) o;

        if (status != content1.status) return false;
        if (!Arrays.equals(content, content1.content)) return false;
        if (contentType != null ? !contentType.equals(content1.contentType) : content1.contentType != null)
            return false;
        if (encoding != null ? !encoding.equals(content1.encoding) : content1.encoding != null) return false;
        if (meta != null ? !meta.equals(content1.meta) : content1.meta != null) return false;
        if (path != null ? !path.equals(content1.path) : content1.path != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Content{" +
                "path='" + path + '\'' +
                ", contentType='" + contentType + '\'' +
                ", meta='" + meta + '\'' +
                ", encoding='" + encoding + '\'' +
                ", status=" + status +
                ", content is " + (content == null ? "" : "not") + " null" +
                '}';
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (meta != null ? meta.hashCode() : 0);
        result = 31 * result + (encoding != null ? encoding.hashCode() : 0);
        result = 31 * result + (content != null ? Arrays.hashCode(content) : 0);
        result = 31 * result + status;
        return result;
    }


    public boolean isRenderOnServer() {
        return renderOnServer;
    }

    public void setRenderOnServer(boolean renderOnServer) {
        this.renderOnServer = renderOnServer;
    }
}
