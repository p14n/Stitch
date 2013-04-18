package com.p14n.stitch.cache;

import com.p14n.stitch.Function1;
import com.p14n.stitch.Page;
import com.p14n.stitch.content.Content;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 12/04/2013
 */
public class MemoryRenderCache extends ResponseCache<ContentRequestInfo,
        Page> implements RenderCache {

    public MemoryRenderCache(Function1<ContentRequestInfo, Page> function) {
        super(function);
    }

    @Override
    public Page retrieve(ContentRequestInfo info) {
        return apply(info);
    }

}
