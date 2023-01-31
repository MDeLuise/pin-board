package com.github.mdeluise.pinboard.page;

import com.github.mdeluise.pinboard.common.AbstractDTOConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageDTOConverter extends AbstractDTOConverter<Page, PageDTO> {
    private final ModelMapper modelMapper;


    @Autowired
    public PageDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public Page convertFromDTO(PageDTO dto) {
        return modelMapper.map(dto, Page.class);
    }


    @Override
    public PageDTO convertToDTO(Page data) {
        return modelMapper.map(data, PageDTO.class);
    }
}
