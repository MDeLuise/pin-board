package com.github.mdeluise.pinboard.list;

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
@RequestMapping("/list")
@Tag(name = "List", description = "Endpoints for operations on lists.")
public class PageListController implements CrudController<PageListDTO, Long> {
    private final PageListDTOConverter pageListDtoConverter;
    private final PageListService pageListService;
    private final PageDTOConverter pageDTOConverter;


    @Autowired
    public PageListController(PageListDTOConverter pageListDtoConverter, PageListService pageListService,
                              PageDTOConverter pageDTOConverter) {
        this.pageListDtoConverter = pageListDtoConverter;
        this.pageListService = pageListService;
        this.pageDTOConverter = pageDTOConverter;
    }


    @Operation(
        summary = "Get all the Lists",
        description = "Get all the Lists."
    )
    @Override
    public ResponseEntity<Collection<PageListDTO>> findAll() {
        Set<PageListDTO> result =
            pageListService.getAll().stream().map(pageListDtoConverter::convertToDTO).collect(Collectors.toSet());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Get a single List",
        description = "Get the details of a given List, according to the `id` parameter."
    )
    @Override
    public ResponseEntity<PageListDTO> find(
        @Parameter(description = "The ID of the List on which to perform the operation") Long id) {
        PageListDTO result = pageListDtoConverter.convertToDTO(pageListService.get(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Override
    @Operation(
        summary = "Update a single List",
        description = "Update the details of a given List, according to the `id` parameter." +
                          "Please note that some fields may be readonly for integrity purposes."
    )
    public ResponseEntity<PageListDTO> update(
        PageListDTO updatedEntity,
        @Parameter(description = "The ID of the List on which to perform the operation") Long id) {
        PageListDTO result = pageListDtoConverter.convertToDTO(
            pageListService.update(id, pageListDtoConverter.convertFromDTO(updatedEntity)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Delete a single List",
        description = "Delete the given List, according to the `id` parameter."
    )
    @Override
    public void remove(@Parameter(description = "The ID of the List on which to perform the operation") Long id) {
        pageListService.remove(id);
    }


    @Operation(
        summary = "Create a new List",
        description = "Create a new List."
    )
    @Override
    public ResponseEntity<PageListDTO> save(PageListDTO entityToSave) {
        PageListDTO result =
            pageListDtoConverter.convertToDTO(pageListService.save(pageListDtoConverter.convertFromDTO(entityToSave)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Get all pages in the provided Lists",
        description = "Get all pages in the provided Lists, according to the `listIds` parameter."
    )
    @GetMapping("/{listIds}/pages")
    public ResponseEntity<Collection<PageDTO>> getAllPages(@PathVariable("listIds") List<Long> listIds) {
        Collection<Page> pages = new HashSet<>();
        for (Long listId : listIds) {
            pages.addAll(pageListService.get(listId).getPages());
        }
        Set<PageDTO> result = pages.stream().map(pageDTOConverter::convertToDTO).collect(Collectors.toSet());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Add Pages to lists",
        description = "Add Pages to Lists, according to the `pageIds` and `listIds` parameters."
    )
    @PostMapping("/{listId}/add-page/{pageId}")
    public ResponseEntity<String> addPageToList(
        @PathVariable("listId") List<Long> listIds, @PathVariable("pageId") List<Long> pageIds) {
        pageListService.addPagesToLists(listIds, pageIds);
        return new ResponseEntity<>("Page(s) successfully added to the list(s).", HttpStatus.OK);
    }


    @Operation(
        summary = "Remove Pages from lists",
        description = "Remove Pages from Lists, according to the `pageIds` and `listIds` parameters."
    )
    @PostMapping("/{listId}/remove-page/{pageId}")
    public ResponseEntity<String> removePageFromList(
        @PathVariable("listId") List<Long> listIds, @PathVariable("pageId") List<Long> pageIds) {
        pageListService.removePagesFromLists(listIds, pageIds);
        return new ResponseEntity<>("Page(s) successfully removed from the list(s).", HttpStatus.OK);
    }
}
