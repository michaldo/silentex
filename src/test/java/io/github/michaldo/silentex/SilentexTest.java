package io.github.michaldo.silentex;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SilentexTest {

    private float div(int a, int b) {

        org.slf4j.Logger logger = LoggerFactory.getLogger("div");
        try {
            logger.info("Start");
            return a / b;
        } catch (ArithmeticException e) {
            logger.warn("Ex", e);
            return 42;
        }
    }


    @Test
    @SilentException(ArithmeticException.class)
    @Order(1)
    void testWithSilentAnnotation() {
        // given
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("div")).addAppender(appender);

        // when
        div(4,0);

        // then
        assertEquals(appender.list.size(), 1);
        assertEquals("Start", appender.list.get(0).getMessage(), "'Start' logged, 'Ex' dropped");
    }


    @Test
    @Order(2)
    void testWithoutSilentAnnotation() {
        // given
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("div")).addAppender(appender);

        // when
        div(4,0);

        // then
        assertEquals(appender.list.size(), 2, "'Start' logged, 'Ex logged");
    }


}
