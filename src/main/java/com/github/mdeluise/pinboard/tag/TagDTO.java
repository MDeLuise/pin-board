package com.github.mdeluise.pinboard.tag;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "Tag", description = "Represents a tag.")
public class TagDTO {
    @Schema(description = "ID of the tag.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Name of the tag.", example = "tutorial")
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TagDTO tagDTO = (TagDTO) o;
        return id.equals(tagDTO.id) && name.equals(tagDTO.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
