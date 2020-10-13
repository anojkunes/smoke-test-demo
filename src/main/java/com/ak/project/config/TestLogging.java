package com.ak.project.config;

import org.spockframework.runtime.extension.ExtensionAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// https://stackoverflow.com/questions/25967241/does-spock-have-test-event-listeners
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtensionAnnotation(LoggingListenerExtension.class)
public @interface TestLogging {
}
