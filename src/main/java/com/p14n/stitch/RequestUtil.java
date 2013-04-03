package com.p14n.stitch;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 03/04/2013
 */
public class RequestUtil {
    public static String contentPathFromRequest(HttpServletRequest req){
        String path = req.getContextPath()+req.getServletPath();
        if(req.getQueryString()!=null||!"".equals(req.getQueryString()))
            return path+"?"+req.getQueryString();
        return path;
    }
}
