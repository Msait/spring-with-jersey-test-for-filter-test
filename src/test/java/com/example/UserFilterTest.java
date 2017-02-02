package com.example;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.OutputStreamAppender;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;


@RunWith(MockitoJUnitRunner.class)
public class UserFilterTest extends JerseyTest {

    private ByteArrayOutputStream outputStream;
    private UserFilter userFilter;

    @Override
    protected Application configure() {
        userFilter = new UserFilter();
        ResourceConfig resourceConfig = new ResourceConfig(UserFilter.class, UserTestResource.class);
        ApplicationContext context = new AnnotationConfigApplicationContext(com.example.Application.class);
        resourceConfig.property("contextConfig", context);
        return resourceConfig;
    }


    @Before
    public void setup() throws Exception {
        outputStream = new ByteArrayOutputStream();
        final Logger logger = (Logger) ReflectionTestUtils.getField(userFilter, "logger");

        Context context = (Context) LoggerFactory.getILoggerFactory();

        OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<>();
        appender.setName("OutputStream Appender");
        appender.setContext(context);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%logger{20} - %msg%n");
        encoder.start();

        appender.setEncoder(encoder);
        appender.setOutputStream(outputStream);
        appender.start();

        logger.addAppender(appender);
    }

    @Test
    public void testFilterProcess(){
        final String response = target()
                .path("/user-test/test")
                .request().accept(MediaType.APPLICATION_JSON)
                .header("username", "PILOT")
                .get(String.class);

        Assert.assertEquals("TEST", response);

        final String s = new String(outputStream.toByteArray());

        Assert.assertThat(s, CoreMatchers.containsString("some request"));
        Assert.assertThat(s, CoreMatchers.containsString("some response"));
        Assert.assertThat(s, CoreMatchers.containsString("Process user: PILOT"));
    }

    @Path("/user-test")
    public static class UserTestResource {

        @GET
        @Path("test")
        @Produces(MediaType.APPLICATION_JSON)
        public String test(){
            return "TEST";
        }
    }

}