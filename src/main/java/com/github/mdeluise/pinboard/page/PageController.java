package com.github.mdeluise.pinboard.page;

import com.github.mdeluise.pinboard.common.CrudController;
import com.github.mdeluise.pinboard.page.body.PageBodyDTO;
import com.github.mdeluise.pinboard.page.body.PageBodyDTOConverter;
import com.github.mdeluise.pinboard.page.body.PageBodyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/page")
@Tag(name = "Page", description = "Endpoints for operations on pages.")
public class PageController implements CrudController<PageDTO, Long> {
    private final PageDTOConverter pageDtoConverter;
    private final PageService pageService;
    private final PageBodyService pageBodyService;
    private final PageBodyDTOConverter pageBodyDtoConverter;


    @Autowired
    public PageController(PageDTOConverter pageDtoConverter, PageService pageService, PageBodyService pageBodyService,
                          PageBodyDTOConverter pageBodyDtoConverter) {
        this.pageDtoConverter = pageDtoConverter;
        this.pageService = pageService;
        this.pageBodyService = pageBodyService;
        this.pageBodyDtoConverter = pageBodyDtoConverter;
    }


    @Operation(
        summary = "Get all the Pages",
        description = "Get all the Pages."
    )
    @Override
    public ResponseEntity<Collection<PageDTO>> findAll() {
        Set<PageDTO> result =
            pageService.getAll().stream().map(pageDtoConverter::convertToDTO).collect(Collectors.toSet());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Get a single Page",
        description = "Get the details of a given Page, according to the `id` parameter."
    )
    @Override
    public ResponseEntity<PageDTO> find(
        @Parameter(description = "The ID of the Page on which to perform the operation") Long id) {
        PageDTO result = pageDtoConverter.convertToDTO(pageService.get(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Override
    @Operation(
        summary = "Update a single Page",
        description = "Update the details of a given Page, according to the `id` parameter." +
                          "Please note that some fields may be readonly for integrity purposes."
    )
    public ResponseEntity<PageDTO> update(
        PageDTO updatedEntity,
        @Parameter(description = "The ID of the Page on which to perform the operation") Long id) {
        PageDTO result = pageDtoConverter.convertToDTO(
            pageService.update(id, pageDtoConverter.convertFromDTO(updatedEntity)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Delete a single Page",
        description = "Delete the given Page, according to the `id` parameter."
    )
    @Override
    public void remove(@Parameter(description = "The ID of the Page on which to perform the operation") Long id) {
        pageService.remove(id);
    }


    @Operation(
        summary = "Create a new Page",
        description = "Create a new Page."
    )
    @Override
    public ResponseEntity<PageDTO> save(PageDTO entityToSave) {
        PageDTO result = pageDtoConverter.convertToDTO(pageService.save(pageDtoConverter.convertFromDTO(entityToSave)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Get the Page body",
        description = "Get the body of the given Page, according to the `id` parameter"
    )
    @GetMapping("/{id}/body")
    public ResponseEntity<PageBodyDTO> getThePageBody(@PathVariable("id") Long pageId) {
        Page page = pageService.get(pageId);
        PageBodyDTO result = pageBodyDtoConverter.convertToDTO(pageBodyService.get(page.getId()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
