package com.p14n.stitch.cache;

import com.p14n.stitch.Page;
import com.p14n.stitch.content.Content;
import com.p14n.stitch.content.ContentRepository;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 15/04/2013
 */
public class ServerRenderCache extends MemoryRenderCache {
    public ServerRenderCache(ContentRepository contentRepository){
        super((ContentRequestInfo info) -> {
            Content c = contentRepository.getContent(info);
            Page p = new Page(c);
            return p;
        });
    }

    @Override
    public boolean isAbleToCacheResponse(Page p) {
        return !p.hasClientRenderOnlyComponents();
    }

}
