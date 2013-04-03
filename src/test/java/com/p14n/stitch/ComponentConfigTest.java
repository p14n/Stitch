package com.p14n.stitch;

import com.p14n.stitch.component.ComponentFunctions;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 03/04/2013
 */
public class ComponentConfigTest {
    @Test
    public void shouldFindAndAddComponents(){
        ComponentFunctions.getComponentsFromConfig(ConfigFactory.load("applicationtest"));
    }
}
