package com.p14n.stitch.cache;

import com.p14n.stitch.content.Content;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 12/04/2013
 */
public interface ContentCacheKey {

    public String generate(String path,HttpServletRequest req);
    public boolean uncacheable(Content c);

}
