package com.github.mdeluise.pinboard.tag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import com.github.mdeluise.pinboard.page.PageDTOConverter;
import com.github.mdeluise.pinboard.security.apikey.ApiKeyFilter;
import com.github.mdeluise.pinboard.security.apikey.ApiKeyRepository;
import com.github.mdeluise.pinboard.security.apikey.ApiKeyService;
import com.github.mdeluise.pinboard.security.jwt.JwtTokenFilter;
import com.github.mdeluise.pinboard.security.jwt.JwtTokenUtil;
import com.github.mdeluise.pinboard.security.jwt.JwtWebUtil;
import com.github.mdeluise.pinboard.tag.Tag;
import com.github.mdeluise.pinboard.tag.TagController;
import com.github.mdeluise.pinboard.tag.TagDTO;
import com.github.mdeluise.pinboard.tag.TagDTOConverter;
import com.github.mdeluise.pinboard.tag.TagService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(TagController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "ADMIN")
public class TagControllerTest {
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @MockBean
    JwtTokenUtil jwtTokenUtil;
    @MockBean
    JwtWebUtil jwtWebUtil;
    @MockBean
    ApiKeyFilter apiKeyFilter;
    @MockBean
    ApiKeyService apiKeyService;
    @MockBean
    ApiKeyRepository apiKeyRepository;
    @MockBean
    TagService tagService;
    @MockBean
    PageDTOConverter pageDTOConverter;
    @MockBean
    TagDTOConverter tagDTOConverter;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;


    @Test
    void whenGetTags_ShouldReturnTags() throws Exception {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("tag1");
        TagDTO tagDTO1 = new TagDTO();
        tagDTO1.setId(1L);
        tagDTO1.setName("tag1");
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("tag2");
        TagDTO tagDTO2 = new TagDTO();
        tagDTO2.setId(2L);
        tagDTO2.setName("tag2");
        Mockito.when(tagService.getAll(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Sort.Direction.class)))
               .thenReturn(new PageImpl<>(List.of(tag1, tag2)));
        Mockito.when(tagDTOConverter.convertToDTO(tag1)).thenReturn(tagDTO1);
        Mockito.when(tagDTOConverter.convertToDTO(tag2)).thenReturn(tagDTO2);

        mockMvc.perform(MockMvcRequestBuilders.get("/tag")).andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)));
    }


    @Test
    void whenGetTag_ShouldReturnTag() throws Exception {
        Tag tag = new Tag();
        tag.setId(0L);
        tag.setName("tag0");
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(0L);
        tagDTO.setName("tag0");
        Mockito.when(tagService.get(0L)).thenReturn(tag);
        Mockito.when(tagDTOConverter.convertToDTO(tag)).thenReturn(tagDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/tag/0")).andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tag0"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }


    @Test
    void whenGetNotExistingTag_shouldError() throws Exception {
        Mockito.when(tagService.get(0L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/tag/0"))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenDeleteTag_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(tagService).remove(0L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tag/0")).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void whenDeleteNonExistingTag_shouldError() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(tagService).remove(0L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tag/0"))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenUpdateTag_shouldReturnUpdated() throws Exception {
        Tag updated = new Tag();
        updated.setId(0L);
        updated.setName("tag1");
        TagDTO updatedDTO = new TagDTO();
        updatedDTO.setId(0L);
        updatedDTO.setName("tag1");
        Mockito.when(tagService.update(0L, updated)).thenReturn(updated);
        Mockito.when(tagDTOConverter.convertToDTO(updated)).thenReturn(updatedDTO);
        Mockito.when(tagDTOConverter.convertFromDTO(updatedDTO)).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put("/tag/0").content(
                                                  objectMapper.writeValueAsString(tagDTOConverter.convertToDTO(updated)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tag1"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }


    @Test
    void whenUpdateNonExistingTag_shouldError() throws Exception {
        Tag updated = new Tag();
        updated.setId(1L);
        updated.setName("tag1");
        TagDTO updatedDTO = new TagDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("tag1");
        Mockito.doThrow(EntityNotFoundException.class).when(tagService).update(0L, updated);
        Mockito.when(tagDTOConverter.convertToDTO(updated)).thenReturn(updatedDTO);
        Mockito.when(tagDTOConverter.convertFromDTO(updatedDTO)).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put("/tag/0").content(
                                                  objectMapper.writeValueAsString(tagDTOConverter.convertToDTO(updated)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenCreateTag_shouldReturnTag() throws Exception {
        Tag created = new Tag();
        created.setId(0L);
        created.setName("tag1");
        TagDTO createdDTO = new TagDTO();
        createdDTO.setId(0L);
        createdDTO.setName("tag1");
        Mockito.when(tagService.save(created)).thenReturn(created);
        Mockito.when(tagDTOConverter.convertToDTO(created)).thenReturn(createdDTO);
        Mockito.when(tagDTOConverter.convertFromDTO(createdDTO)).thenReturn(created);

        mockMvc.perform(MockMvcRequestBuilders.post("/tag").content(
                                                  objectMapper.writeValueAsString(tagDTOConverter.convertToDTO(created)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tag1"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }

}
