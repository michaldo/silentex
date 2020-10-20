package io.github.michaldo.silentex;

import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;

public class SilentExceptionHandler implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final SilentExceptionFilter FILTER;

    static {
        FILTER = new SilentExceptionFilter();
        ((LoggerContext) LoggerFactory.getILoggerFactory()).addTurboFilter(FILTER);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        context.getElement()
                .map(annotatedElement -> annotatedElement.getAnnotation(SilentException.class))
                .map(SilentException::value)
                .ifPresent(FILTER::ignoreException);
    }


    @Override
    public void afterTestExecution(ExtensionContext context)  {
        FILTER.reset();
    }

}
