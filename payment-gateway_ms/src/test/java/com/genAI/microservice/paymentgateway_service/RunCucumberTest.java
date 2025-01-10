package com.genAI.microservice.paymentgateway_service;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.genAI.microservice.paymentgateway_service.steps",
        plugin = {"pretty", "html:target/cucumber"}
)
public class RunCucumberTest {
}
