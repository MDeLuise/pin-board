package com.github.mdeluise.pinboard.security.apikey;

import io.swagger.v3.oas.annotations.media.Schema;

public class ApiKeyDTO {
    @Schema(description = "ID of the API Key.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the API Key.")
    private String name;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
