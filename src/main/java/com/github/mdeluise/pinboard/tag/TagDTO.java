package com.github.mdeluise.pinboard.tag;

import io.swagger.v3.oas.annotations.media.Schema;

public class TagDTO {
    @Schema(description = "ID of the tag.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Name of the tag.", example = "Tutorial")
    private String name;
}
