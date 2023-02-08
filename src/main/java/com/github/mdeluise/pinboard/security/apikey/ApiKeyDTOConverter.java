package com.github.mdeluise.pinboard.security.apikey;

import com.github.mdeluise.pinboard.common.AbstractDTOConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyDTOConverter extends AbstractDTOConverter<ApiKey, ApiKeyDTO> {
    private final ModelMapper modelMapper;


    @Autowired
    public ApiKeyDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public ApiKey convertFromDTO(ApiKeyDTO apiKeyDTO) {
        return modelMapper.map(apiKeyDTO, ApiKey.class);
    }


    @Override
    public ApiKeyDTO convertToDTO(ApiKey apiKey) {
        return modelMapper.map(apiKey, ApiKeyDTO.class);
    }
}
