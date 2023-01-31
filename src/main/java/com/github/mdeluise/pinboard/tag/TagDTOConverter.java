package com.github.mdeluise.pinboard.tag;

import com.github.mdeluise.pinboard.common.AbstractDTOConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagDTOConverter extends AbstractDTOConverter<Tag, TagDTO> {
    private final ModelMapper modelMapper;


    @Autowired
    public TagDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public Tag convertFromDTO(TagDTO dto) {
        return modelMapper.map(dto, Tag.class);
    }


    @Override
    public TagDTO convertToDTO(Tag data) {
        return modelMapper.map(data, TagDTO.class);
    }
}
