package com.p14n.stitch;

import com.p14n.stitch.component.Component;
import com.p14n.stitch.component.ComponentFunctions;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;

import java.util.Map;
import static org.junit.Assert.*;
/**
 * Created by Dean Pehrsson-Chapman
 * Date: 03/04/2013
 */
public class ComponentConfigTest {
    @Test
    public void shouldFindAndAddComponents() {
        Map<String, Component> comps = ComponentFunctions.getComponentsFromConfig(ConfigFactory.load("applicationtest"));
        assertTrue(comps.containsKey("client.search"));
        assertTrue(comps.containsKey("client.search2"));
        assertTrue(comps.containsKey("client.search3"));
    }
}
