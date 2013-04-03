package com.p14n.stitch;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 02/04/2013
 */
public class PageBuilder {

    public static String buildPageFromRequest(HttpServletRequest req){
        String requestPath = RequestUtil.contentPathFromRequest(req);

        //get page from cache
         //if not there, check server side cache
          //if not there
           //get content from store
           //parse for component
           //load component settings
           //load component content
           //stitch component content and settings
           //stitch content and components
           //if some components are server-side
            //cache page in server side cache
            //otherwise cache in page cache
          //if there
           //parse for component
           //load component settings
           //load component content
           //stitch component content and settings
           //stitch content and components
        //return page

        return null;
    }
}
