package com.p14n.stitch;

import com.p14n.stitch.cache.ContentCacheKey;
import com.p14n.stitch.cache.ContentRequestInfo;
import com.p14n.stitch.cache.FrontCache;
import com.p14n.stitch.cache.RenderCache;
import com.p14n.stitch.cache.ServerRenderCache;
import com.p14n.stitch.component.Component;
import com.p14n.stitch.component.ComponentFunctions;
import com.p14n.stitch.content.Content;
import com.p14n.stitch.content.ContentRepository;
import com.p14n.stitch.content.FileContentStore;
import com.p14n.stitch.settings.SettingsRepository;
import com.typesafe.config.ConfigFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 02/04/2013
 */
public class PageBuilder {

    RenderCache frontCache;
    RenderCache serverRenderCache;
    ContentRepository contentRepository;
    SettingsRepository settingsRepository;
    ContentCacheKey frontKey;
    Map<String, Component> components;

    public PageBuilder() {
        contentRepository = createContentRepository();
        settingsRepository  = createSettingsRepository();
        serverRenderCache = createServerCache(contentRepository);
        frontCache = createFrontCache(serverRenderCache);
        components = ComponentFunctions.getComponentsFromConfig(ConfigFactory.load());
    }

    private ContentRepository createContentRepository() {
        return new FileContentStore();
    }

    private SettingsRepository createSettingsRepository() {
        return null;
    }

    public RenderCache createFrontCache(RenderCache subCache) {
        return new FrontCache(subCache);
    }

    public RenderCache createServerCache(ContentRepository contentRepository) {
        return new ServerRenderCache(contentRepository);
    }

    public String stitch(Content c) {
        Page p = new Page(c);


        ////parse for component
        ////load component settings
        ////load component content
        ////stitch component content and settings
        ////stitch content and components
        return null;
    }

  //  Set<String> cannotFrontCache = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
  //  Set<String> cannotServerCache = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    /*private Content retrieveContent(RenderCache cache, ContentRequestInfo info, Set<String> cannotCache,
                                    Function1<Content, Boolean> ableToCache) {
        if (!cache.isCached(info)) {
            if (cannotCache.contains(info.getKey())) {
                return contentRepository.getContent(info);
            }
            Content c = contentRepository.getContent(info);
            if (!cache.ableToCache(c)) {
                cannotCache.add(info.getKey());
                return c;
            }

        }
        return cache.retrieve(info);
    } */

    //front cache can cache if the page is not server render
    //server cache can cache if the page has no client render only

    public String buildPageFromRequest(HttpServletRequest req) {
        String requestPath = RequestUtil.contentPathFromRequest(req);
        String key = frontKey.generate(requestPath, req);
        ContentRequestInfo info = new ContentRequestInfo(key, requestPath, req, null);
        return frontCache.retrieve(info).render();
        //Get from cache
        /*if (!frontCache.isCached(info)) {

            if (cannotFrontCache.contains(key)) {
                //if not there, check server side
                if (!serverRenderCache.isCached(info)) {
                    if (!cannotServerCache.contains(key)) {
                        //Page p = sti
                        if (!serverRenderCache.ableToCache()) {

                        }
                        return null;
                    }

                }
            } else {
                Content c = contentRepository.getContent(info);
                if (!frontCache.ableToCache(c)) {
                    cannotFrontCache.add(key);
                }
                return frontCache.retrieve(info).render();
            }
        } else {
            return frontCache.retrieve(info).render();
        }

        //get page from cache
        //if not there, check server side cache
        ///if not there
        ////get content from store

        ////if some components are server-side
        ////cache page in server side cache
        ////otherwise cache in page cache
        ///if there
        /////parse for component
        /////load component settings
        /////load component content
        /////stitch component content and settings
        /////stitch content and components
        //return page

        return null;          */
    }
}
