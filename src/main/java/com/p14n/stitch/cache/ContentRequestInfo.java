package com.p14n.stitch.cache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 12/04/2013
 */
public class ContentRequestInfo {
    String key,path;
    HttpServletRequest req;
    HttpServletResponse res;

    public String getKey() {
        return key;
    }

    public String getPath() {
        return path;
    }

    public HttpServletRequest getReq() {
        return req;
    }

    public HttpServletResponse getRes() {
        return res;
    }

    public ContentRequestInfo(String key, String path, HttpServletRequest req, HttpServletResponse res) {
        this.key = key;
        this.path = path;
        this.req = req;
        this.res = res;
    }
}
