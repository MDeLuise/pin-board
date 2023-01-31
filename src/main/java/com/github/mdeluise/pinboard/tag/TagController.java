package com.github.mdeluise.pinboard.tag;

import com.github.mdeluise.pinboard.common.CrudController;
import com.github.mdeluise.pinboard.page.Page;
import com.github.mdeluise.pinboard.page.PageDTO;
import com.github.mdeluise.pinboard.page.PageDTOConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tag")
@Tag(name = "Tag", description = "Endpoints for operations on tags.")
public class TagController implements CrudController<TagDTO, Long> {
    private final TagDTOConverter pageDtoConverter;
    private final TagService tagService;
    private final PageDTOConverter pageDTOConverter;


    @Autowired
    public TagController(TagDTOConverter pageDtoConverter, TagService tagService, PageDTOConverter pageDTOConverter) {
        this.pageDtoConverter = pageDtoConverter;
        this.tagService = tagService;
        this.pageDTOConverter = pageDTOConverter;
    }


    @Operation(
        summary = "Get all the Tags", description = "Get all the Tags."
    )
    @Override
    public ResponseEntity<Collection<TagDTO>> findAll() {
        Set<TagDTO> result =
            tagService.getAll().stream().map(pageDtoConverter::convertToDTO).collect(Collectors.toSet());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Get a single Tag", description = "Get the details of a given Tag, according to the `id` parameter."
    )
    @Override
    public ResponseEntity<TagDTO> find(
        @Parameter(description = "The ID of the Tag on which to perform the operation") Long id) {
        TagDTO result = pageDtoConverter.convertToDTO(tagService.get(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Override
    @Operation(
        summary = "Update a single Tag",
        description = "Update the details of a given Tag, according to the `id` parameter." +
                          "Please note that some fields may be readonly for integrity purposes."
    )
    public ResponseEntity<TagDTO> update(TagDTO updatedEntity, @Parameter(
        description = "The ID of the Tag on which to perform the operation"
    ) Long id) {
        TagDTO result = pageDtoConverter.convertToDTO(
            tagService.update(id, pageDtoConverter.convertFromDTO(updatedEntity)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Delete a single Tag", description = "Delete the given Tag, according to the `id` parameter."
    )
    @Override
    public void remove(@Parameter(description = "The ID of the Tag on which to perform the operation") Long id) {
        tagService.remove(id);
    }


    @Operation(
        summary = "Create a new Tag", description = "Create a new Tag."
    )
    @Override
    public ResponseEntity<TagDTO> save(TagDTO entityToSave) {
        TagDTO result = pageDtoConverter.convertToDTO(tagService.save(pageDtoConverter.convertFromDTO(entityToSave)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Get all pages with the provided Tags",
        description = "Get all pages with the provided Tags, according to the `ids` parameter."
    )
    public ResponseEntity<Collection<PageDTO>> getAllPages(@PathVariable("ids") List<Long> tagIds) {
        Collection<Page> pages = new HashSet<>();
        for (Long tagId : tagIds) {
            pages.addAll(tagService.get(tagId).getPages());
        }
        Set<PageDTO> result = pages.stream().map(pageDTOConverter::convertToDTO).collect(Collectors.toSet());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
