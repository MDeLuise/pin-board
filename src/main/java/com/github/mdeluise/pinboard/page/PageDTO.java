package com.github.mdeluise.pinboard.page;

import io.swagger.v3.oas.annotations.media.Schema;

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
}
