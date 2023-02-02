package com.github.mdeluise.pinboard.list;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "PageList", description = "Represents a page list.")
public class PageListDTO {
    @Schema(description = "ID of the list.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Name of the list.", example = "Computer science tutorial")
    private String name;
    @Schema(description = "Description of the list.", example = "Youtube tutorial from my favorite programmers", nullable = true)
    private String description;


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


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageListDTO that = (PageListDTO) o;
        return id.equals(that.id) && name.equals(that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
