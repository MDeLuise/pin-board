package com.github.mdeluise.pinboard.integration.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mdeluise.pinboard.authentication.payload.request.LoginRequest;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


public class AuthenticationSteps {
    final String authPath = "/authentication";
    final MockMvc mockMvc;
    final StepData stepData;
    final int port;
    final ObjectMapper objectMapper;


    public AuthenticationSteps(@Value("${server.port}") int port, MockMvc mockMvc, StepData stepData,
                               ObjectMapper objectMapper) {
        this.port = port;
        this.mockMvc = mockMvc;
        this.stepData = stepData;
        this.objectMapper = objectMapper;
    }


    @Given("login with username {string} and password {string}")
    public void theClientLoginWithUsernameAndPassword(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.post(String.format("http://localhost:%s%s/login", port, authPath))
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(objectMapper.writeValueAsString(loginRequest)));
        stepData.setResultActions(result);
        if (result.andReturn().getResponse().getStatus() == 200) {
            String cookie = result.andReturn().getResponse().getHeader("Set-Cookie");
            stepData.setCookie(cookie.split("=")[0], cookie.split("=")[1].split(";")[0]);
        }
    }
}
