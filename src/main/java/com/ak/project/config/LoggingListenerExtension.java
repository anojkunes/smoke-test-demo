package com.ak.project.config;

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.model.SpecInfo;

public class LoggingListenerExtension extends AbstractAnnotationDrivenExtension<TestLogging> {

    private final LoggingListener listener = new LoggingListener();

    @Override
    public void visitSpecAnnotation(TestLogging annotation, SpecInfo spec) {
        spec.addListener(listener);
    }
}
