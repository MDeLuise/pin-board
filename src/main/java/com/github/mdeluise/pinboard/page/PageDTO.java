package com.github.mdeluise.pinboard.page;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

@Schema(name = "Page", description = "Represents a page.")
public class PageDTO {
    @Schema(description = "ID of the page.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(
        description = "URL of the page.",
        example = "https://en.wikipedia.org/wiki/The_Hitchhiker%27s_Guide_to_the_Galaxy_(novel)"
    )
    private String url;
    @Schema(
        description = "URL of the header image of the page.",
        example = "https://upload.wikimedia.org/wikipedia/en/b/bd/H2G2_UK_front_cover.jpg",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String headerImgUrl;
    @Schema(
        description = "Title of the page.",
        example = "The Hitchhiker's Guide to the Galaxy (novel)",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String title;
    @Schema(
        description = "Names of the page's tags",
        example = "[\"tutorial\",\"long\"]"
    )
    private Set<String> tagsName = new HashSet<>();
    @Schema(
        description = "Names of the lists that contains the page",
        example = "[\"learning\",\"work\"]"
    )
    private Set<String> listsName = new HashSet<>();


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getHeaderImgUrl() {
        return headerImgUrl;
    }


    public void setHeaderImgUrl(String headerImgUrl) {
        this.headerImgUrl = headerImgUrl;
    }


    public Set<String> getTagsName() {
        return tagsName;
    }


    public void setTagsName(Set<String> tagsName) {
        this.tagsName = tagsName;
    }


    public Set<String> getListsName() {
        return listsName;
    }


    public void setListsName(Set<String> listsName) {
        this.listsName = listsName;
    }
}
