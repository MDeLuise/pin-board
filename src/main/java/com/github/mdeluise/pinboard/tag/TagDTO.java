package com.github.mdeluise.pinboard.tag;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Tag", description = "Represents a tag.")
public class TagDTO {
    @Schema(description = "ID of the tag.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Name of the tag.", example = "Tutorial")
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
