package com.github.mdeluise.pinboard.page.body;

import com.github.mdeluise.pinboard.common.AbstractDTOConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageBodyDTOConverter extends AbstractDTOConverter<PageBody, PageBodyDTO> {
    private final ModelMapper modelMapper;


    @Autowired
    public PageBodyDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public PageBody convertFromDTO(PageBodyDTO dto) {
        return modelMapper.map(dto, PageBody.class);
    }


    @Override
    public PageBodyDTO convertToDTO(PageBody data) {
        return modelMapper.map(data, PageBodyDTO.class);
    }
}
