package com.github.mdeluise.pinboard.page.body;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public class PageBodyDTO implements Serializable {
    @Schema(description = "ID of the body.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(
        description = "Content of the body.",
        example = "<html>...</html>",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String content;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }
}
