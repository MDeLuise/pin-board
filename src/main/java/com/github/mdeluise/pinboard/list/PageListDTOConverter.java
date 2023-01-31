package com.github.mdeluise.pinboard.list;

import com.github.mdeluise.pinboard.common.AbstractDTOConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageListDTOConverter extends AbstractDTOConverter<PageList, PageListDTO> {
    private final ModelMapper modelMapper;


    @Autowired
    public PageListDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public PageList convertFromDTO(PageListDTO dto) {
        return modelMapper.map(dto, PageList.class);
    }


    @Override
    public PageListDTO convertToDTO(PageList data) {
        return modelMapper.map(data, PageListDTO.class);
    }
}
