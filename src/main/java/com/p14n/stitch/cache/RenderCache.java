package com.p14n.stitch.cache;

import com.p14n.stitch.Page;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 12/04/2013
 */
public interface RenderCache {

    public Page retrieve(ContentRequestInfo info) ;
    public boolean isCached(ContentRequestInfo info);
    public boolean isAbleToCacheRequest(ContentRequestInfo r);
    public boolean isAbleToCacheResponse(Page r);

}
