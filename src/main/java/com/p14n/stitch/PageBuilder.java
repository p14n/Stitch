package com.p14n.stitch;

import com.p14n.stitch.cache.CacheFunctions;
import com.p14n.stitch.cache.ContentCacheKey;
import com.p14n.stitch.cache.ContentRequestInfo;
import com.p14n.stitch.cache.MemoryRenderCache;
import com.p14n.stitch.cache.RenderCache;
import com.p14n.stitch.component.Component;
import com.p14n.stitch.component.ComponentFunctions;
import com.p14n.stitch.content.ContentRepository;
import com.p14n.stitch.content.FileContentStore;
import com.p14n.stitch.settings.SettingsRepository;
import com.typesafe.config.ConfigFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.p14n.stitch.component.ComponentFunctions.stitch;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 02/04/2013
 */
public class PageBuilder {

    RenderCache frontCache;
    ContentRepository contentRepository;
    SettingsRepository settingsRepository;
    ContentCacheKey frontKey;
    Map<String, Component> components;
    ServerRenderer serverRenderer;

    public PageBuilder() {
        contentRepository = createContentRepository();
        settingsRepository  = createSettingsRepository();
        frontCache = createFrontCache();
        components = ComponentFunctions.getComponentsFromConfig(ConfigFactory.load());
    }

    private ContentRepository createContentRepository() {
        return new FileContentStore();
    }

    private SettingsRepository createSettingsRepository() {
        return null;
    }

    public RenderCache createFrontCache() {
        return new MemoryRenderCache(CacheFunctions.pageRetrievalFunction(contentRepository,components,settingsRepository));
    }

    public String buildPageFromRequest(HttpServletRequest req) {

        String requestPath = RequestUtil.contentPathFromRequest(req);
        String key = frontKey.generate(requestPath, req);
        ContentRequestInfo info = new ContentRequestInfo(key, requestPath, req, null);
        Page p = frontCache.retrieve(info);
        if(p.isServerRender()){
            String rendered = serverRenderer.render(p.stitched());
            if(p.hasClientRenderOnlyComponents()){
               return stitch(rendered,p.getClientComponents(),settingsRepository);
            }
            return rendered;
        }
        return p.stitched();

     }


}
