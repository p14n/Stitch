package com.p14n.stitch.cache;

import com.p14n.stitch.Page;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 15/04/2013
 */
public class FrontCache extends MemoryRenderCache {
    public FrontCache(final RenderCache subCache){
        super((ContentRequestInfo info) -> {
            return subCache.retrieve(info);
        });
    }

    @Override
    public boolean isAbleToCacheResponse(Page p) {
        return !p.getContent().isRenderOnServer();
    }
}
