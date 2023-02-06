package com.github.mdeluise.pinboard.page;

import com.github.mdeluise.pinboard.common.AbstractCrudController;
import com.github.mdeluise.pinboard.common.EntityBucket;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
@Tag(name = "Page", description = "Endpoints for operations on pages.")
public class PageController extends AbstractCrudController<Page, PageDTO, Long> {
    private final PageBodyService pageBodyService;
    private final PageBodyDTOConverter pageBodyDtoConverter;


    @Autowired
    public PageController(PageDTOConverter pageDtoConverter, PageService pageService, PageBodyService pageBodyService,
                          PageBodyDTOConverter pageBodyDtoConverter) {
        super(pageService, pageDtoConverter);
        this.pageBodyService = pageBodyService;
        this.pageBodyDtoConverter = pageBodyDtoConverter;
    }


    @Operation(
        summary = "Get all the Pages",
        description = "Get all the Pages."
    )
    @Override
    public ResponseEntity<EntityBucket<PageDTO>> findAll(
        @RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortBy) {
        return super.findAll(pageNo, pageSize, sortBy);
    }


    @Operation(
        summary = "Get a single Page",
        description = "Get the details of a given Page, according to the `id` parameter."
    )
    @Override
    public ResponseEntity<PageDTO> find(
        @Parameter(description = "The ID of the Page on which to perform the operation") Long id) {
        return super.find(id);
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
        return super.update(updatedEntity, id);
    }


    @Operation(
        summary = "Delete a single Page",
        description = "Delete the given Page, according to the `id` parameter."
    )
    @Override
    public void remove(@Parameter(description = "The ID of the Page on which to perform the operation") Long id) {
        super.remove(id);
    }


    @Operation(
        summary = "Create a new Page",
        description = "Create a new Page."
    )
    @Override
    public ResponseEntity<PageDTO> save(PageDTO entityToSave) {
        return super.save(entityToSave);
    }


    @Operation(
        summary = "Get the Page body",
        description = "Get the body of the given Page, according to the `id` parameter"
    )
    @GetMapping("/{id}/body")
    public ResponseEntity<PageBodyDTO> getThePageBody(@PathVariable("id") Long pageId) {
        Page page = service.get(pageId);
        PageBodyDTO result = pageBodyDtoConverter.convertToDTO(pageBodyService.get(page.getId()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(
        summary = "Get the Page based on title",
        description = "Get the Pages starting with the given title, according to the `title` parameter"
    )
    @GetMapping("/title/{title}")
    public ResponseEntity<org.springframework.data.domain.Page<PageDTO>> getThePageBody(
        @RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortBy,
        @PathVariable("title") String title) {
        org.springframework.data.domain.Page<PageDTO> result =
            ((PageService) service).getPagesWithTitleLike(pageNo, pageSize, sortBy, title)
                                   .map(abstractDTOConverter::convertToDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
