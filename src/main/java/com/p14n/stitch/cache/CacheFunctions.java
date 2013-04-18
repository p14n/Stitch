package com.p14n.stitch.cache;

import com.p14n.stitch.Function1;
import com.p14n.stitch.Page;
import com.p14n.stitch.component.Component;
import com.p14n.stitch.component.ComponentFunctions;
import com.p14n.stitch.content.Content;
import com.p14n.stitch.content.ContentRepository;
import com.p14n.stitch.settings.SettingsRepository;

import java.util.Map;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 18/04/2013
 */
public class CacheFunctions {

    public static Function1<ContentRequestInfo,Page> pageRetrievalFunction(
            final ContentRepository contentRepository,
            final Map<String,Component> componentMap,
            final SettingsRepository settings){

        return (ContentRequestInfo info) -> {
            Content c = contentRepository.getContent(info.getPath());
            return ComponentFunctions.stitch(c,componentMap,settings);
        };
    }
}
