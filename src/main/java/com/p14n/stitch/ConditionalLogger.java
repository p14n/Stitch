package com.p14n.stitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 03/04/2013
 */
public class ConditionalLogger {

    Logger log;

    public ConditionalLogger(Class logClass) {
        log = LoggerFactory.getLogger(logClass);
    }

    public void ifInfo(Function0<String> f){
        if(log.isInfoEnabled()){
            log.info(f.apply());
        }
    }
    public void ifDebug(Function0<String> f){
        if(log.isDebugEnabled()){
            log.debug(f.apply());
        }
    }
    public void error(String text,Throwable error){
        log.error(text,error);
    }
}
