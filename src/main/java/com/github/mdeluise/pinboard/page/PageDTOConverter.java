package com.github.mdeluise.pinboard.page;

import com.github.mdeluise.pinboard.common.AbstractDTOConverter;
import com.github.mdeluise.pinboard.list.PageList;
import com.github.mdeluise.pinboard.list.PageListService;
import com.github.mdeluise.pinboard.tag.Tag;
import com.github.mdeluise.pinboard.tag.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PageDTOConverter extends AbstractDTOConverter<Page, PageDTO> {
    private final ModelMapper modelMapper;
    private final TagService tagService;
    private final PageListService pageListService;


    @Autowired
    public PageDTOConverter(ModelMapper modelMapper, TagService tagService, PageListService pageListService) {
        this.modelMapper = modelMapper;
        this.tagService = tagService;
        this.pageListService = pageListService;
    }


    @Override
    public Page convertFromDTO(PageDTO dto) {
        Page toReturn = modelMapper.map(dto, Page.class);
        toReturn.setTags(dto.getTagsName().stream().map(tagService::getByName).collect(Collectors.toSet()));
        toReturn.setLists(dto.getListsName().stream().map(pageListService::getByName).collect(Collectors.toSet()));
        return toReturn;
    }


    @Override
    public PageDTO convertToDTO(Page data) {
        PageDTO toReturn = modelMapper.map(data, PageDTO.class);
        toReturn.setListsName(data.getLists().stream().map(PageList::getName).collect(Collectors.toSet()));
        toReturn.setTagsName(data.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        return toReturn;
    }
}
