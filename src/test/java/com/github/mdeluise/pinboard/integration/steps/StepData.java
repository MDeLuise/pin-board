package com.github.mdeluise.pinboard.integration.steps;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

@Component
public class StepData {
    private ResultActions resultActions;
    private Optional<Cookie> cookie = Optional.empty();


    public ResultActions getResultActions() {
        return resultActions;
    }


    public void setResultActions(ResultActions resultActions) {
        this.resultActions = resultActions;
    }


    public Optional<Cookie> getCookie() {
        return cookie;
    }


    public void setCookie(String name, String value) {
        this.cookie = Optional.of(new Cookie(name, value));
    }
}
