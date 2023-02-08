package com.github.mdeluise.pinboard.tag;

import com.github.mdeluise.pinboard.common.AbstractCrudController;
import com.github.mdeluise.pinboard.common.EntityBucket;
import com.github.mdeluise.pinboard.page.Page;
import com.github.mdeluise.pinboard.page.PageDTO;
import com.github.mdeluise.pinboard.page.PageDTOConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tag")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "Endpoints for operations on tags.")
public class TagController extends AbstractCrudController<Tag, TagDTO, Long> {
    private final PageDTOConverter pageDTOConverter;


    @Autowired
    public TagController(TagDTOConverter tagDTOConverter, TagService tagService, PageDTOConverter pageDTOConverter) {
        super(tagService, tagDTOConverter);
        this.pageDTOConverter = pageDTOConverter;
    }


    @Operation(
        summary = "Get all the Tags", description = "Get all the Tags."
    )
    @Override
    public ResponseEntity<EntityBucket<TagDTO>> findAll(Integer pageNo, Integer pageSize, String sortBy,
                                                        Sort.Direction sortDir) {
        return super.findAll(pageNo, pageSize, sortBy, sortDir);
    }


    @Operation(
        summary = "Get a single Tag", description = "Get the details of a given Tag, according to the `id` parameter."
    )
    @Override
    public ResponseEntity<TagDTO> find(
        @Parameter(description = "The ID of the Tag on which to perform the operation") Long id) {
        return super.find(id);
    }


    @Override
    @Operation(
        summary = "Update a single Tag",
        description = "Update the details of a given Tag, according to the `id` parameter." +
                          "Please note that some fields may be readonly for integrity purposes."
    )
    public ResponseEntity<TagDTO> update(
        TagDTO updatedEntity,
        @Parameter(description = "The ID of the Tag on which to perform the operation") Long id) {
        return super.update(updatedEntity, id);
    }


    @Operation(
        summary = "Delete a single Tag", description = "Delete the given Tag, according to the `id` parameter."
    )
    @Override
    public void remove(@Parameter(description = "The ID of the Tag on which to perform the operation") Long id) {
        super.remove(id);
    }


    @Operation(
        summary = "Create a new Tag", description = "Create a new Tag."
    )
    @Override
    public ResponseEntity<TagDTO> save(TagDTO entityToSave) {
        return super.save(entityToSave);
    }


    @Operation(
        summary = "Get all pages with the provided Tags",
        description = "Get all pages with the provided Tags, according to the `tagIds` parameter."
    )
    @GetMapping("/{tagIds}/pages")
    public ResponseEntity<Collection<PageDTO>> getAllPages(@PathVariable("tagIds") List<Long> tagIds) {
        Collection<Page> pages = new HashSet<>();
        for (Long tagId : tagIds) {
            pages.addAll(service.get(tagId).getPages());
        }
        Set<PageDTO> result = pages.stream().map(pageDTOConverter::convertToDTO).collect(Collectors.toSet());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Add Tags to Pages",
        description = "Add Tags to Pages, according to the `tagIds` and `pageIds` parameters."
    )
    @PostMapping("/{tagIds}/add-to-page/{pageIds}")
    public ResponseEntity<String> addTagsToPages(
        @PathVariable("tagIds") List<Long> tagId, @PathVariable("pageIds") List<Long> pageIds) {
        ((TagService) service).addTagsToPages(tagId, pageIds);
        return new ResponseEntity<>("Tag(s) successfully added to page(s).", HttpStatus.OK);
    }


    @Operation(
        summary = "Remove Tags from Pages",
        description = "Remove Tags from Pages, according to the `tagIds` and `pageIds` parameters."
    )
    @PostMapping("/{tagIds}/remove-from-page/{pageIds}")
    public ResponseEntity<String> removeTagsFromPages(
        @PathVariable("tagIds") List<Long> tagId, @PathVariable("pageIds") List<Long> pageIds) {
        ((TagService) service).removeTagsFromPages(tagId, pageIds);
        return new ResponseEntity<>("Tag(s) successfully removed from page(s).", HttpStatus.OK);
    }
}
