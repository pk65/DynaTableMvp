package com.google.gwt.sample.dynatablemvp.server;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( "classpath:spring-config-eclipselink.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SchoolCalendarServiceEclipseLinkTest extends SchoolCalendarServiceCheck {
}
