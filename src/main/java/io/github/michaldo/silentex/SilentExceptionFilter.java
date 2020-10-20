package io.github.michaldo.silentex;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import static ch.qos.logback.core.spi.FilterReply.DENY;
import static ch.qos.logback.core.spi.FilterReply.NEUTRAL;

public class SilentExceptionFilter extends TurboFilter {

    private final ThreadLocal<Class<? extends Exception>> exceptionToIgnore = ThreadLocal.withInitial(() -> null);


    void ignoreException(Class<? extends Exception> exceptionClass) {
        exceptionToIgnore.set(exceptionClass);
    }

    void reset() {
        exceptionToIgnore.remove();
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        if (t == null || exceptionToIgnore.get() == null) {
            return NEUTRAL;
        }
        return exceptionToIgnore.get().isAssignableFrom(t.getClass()) ? DENY : NEUTRAL;
    }

}
