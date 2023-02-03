package com.github.mdeluise.pinboard.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

/**
 * Class to use spring application context while running cucumber
 */
@CucumberContextConfiguration
@SpringBootTest
@ComponentScan(basePackages="com.github.mdeluise.pinboard")
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class CucumberSpringContextConfiguration {
}
