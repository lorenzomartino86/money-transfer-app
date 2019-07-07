package com.lmartino.bank.bdd;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",  glue = {"com.lmartino.bank.bdd.steps" })
public class BddTest {
}
