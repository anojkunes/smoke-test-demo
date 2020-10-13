package com.ak.project.config;

import lombok.extern.slf4j.Slf4j;
import org.spockframework.runtime.AbstractRunListener;
import org.spockframework.runtime.model.ErrorInfo;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.SpecInfo;

@Slf4j
class LoggingListener extends AbstractRunListener {

    @Override
    public void beforeSpec(SpecInfo spec) {
        log.info("Running pack: \"{}\"", spec.getName());
    }

    @Override
    public void afterSpec(SpecInfo spec) {
        log.info("Finished pack");
    }

    @Override
    public void beforeFeature(FeatureInfo feature) {
        log.info("Running test: \"{}\"", feature.getName());
    }

    @Override
    public void afterFeature(FeatureInfo feature) {
        log.info("Finished test");
    }

    @Override
    public void error(ErrorInfo error) {
        log.error("Test Failed: {}", error.getMethod().getName());
    }
}
