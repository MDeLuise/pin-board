package com.github.mdeluise.pinboard.page.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mdeluise.pinboard.TestEnvironment;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import com.github.mdeluise.pinboard.page.Page;
import com.github.mdeluise.pinboard.page.PageController;
import com.github.mdeluise.pinboard.page.PageDTO;
import com.github.mdeluise.pinboard.page.PageDTOConverter;
import com.github.mdeluise.pinboard.page.PageService;
import com.github.mdeluise.pinboard.page.body.PageBodyDTOConverter;
import com.github.mdeluise.pinboard.page.body.PageBodyService;
import com.github.mdeluise.pinboard.security.ApplicationSecurityConfig;
import com.github.mdeluise.pinboard.security.jwt.JwtTokenFilter;
import com.github.mdeluise.pinboard.security.jwt.JwtTokenUtil;
import com.github.mdeluise.pinboard.security.jwt.JwtWebUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(PageController.class)
@Import(
    {
        JwtTokenFilter.class,
        JwtTokenUtil.class,
        JwtWebUtil.class,
        TestEnvironment.class,
        ApplicationSecurityConfig.class,
        ModelMapper.class
    }
)
@WithMockUser(roles = "ADMIN")
public class PageControllerTest {
    @MockBean
    PageService pageService;
    @MockBean
    PageDTOConverter pageDTOConverter;
    @MockBean
    PageBodyService pageBodyService;
    @MockBean
    PageBodyDTOConverter pageBodyDTOConverter;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void whenGetPages_ShouldReturnPages() throws Exception {
        Page page1 = new Page();
        page1.setId(1L);
        page1.setUrl("page1");
        PageDTO pageDTO1 = new PageDTO();
        pageDTO1.setId(1L);
        pageDTO1.setUrl("page1");
        Page page2 = new Page();
        page2.setId(2L);
        page2.setUrl("page2");
        PageDTO pageDTO2 = new PageDTO();
        pageDTO2.setId(2L);
        pageDTO2.setUrl("page2");
        Mockito.when(pageService.getAll()).thenReturn(List.of(page1, page2));
        Mockito.when(pageDTOConverter.convertToDTO(page1)).thenReturn(pageDTO1);
        Mockito.when(pageDTOConverter.convertToDTO(page2)).thenReturn(pageDTO2);

        mockMvc.perform(MockMvcRequestBuilders.get("/page")).andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }


    @Test
    void whenGetPage_ShouldReturnPage() throws Exception {
        Page page = new Page();
        page.setId(0L);
        page.setUrl("page0");
        PageDTO pageDTO = new PageDTO();
        pageDTO.setId(0L);
        pageDTO.setUrl("page0");
        Mockito.when(pageService.get(0L)).thenReturn(page);
        Mockito.when(pageDTOConverter.convertToDTO(page)).thenReturn(pageDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/page/0")).andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("page0"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }


    @Test
    void whenGetNotExistingPage_shouldError() throws Exception {
        Mockito.when(pageService.get(0L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/page/0"))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenDeletePage_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(pageService).remove(0L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/page/0")).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void whenDeleteNonExistingPage_shouldError() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(pageService).remove(0L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/page/0"))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenUpdatePage_shouldReturnUpdated() throws Exception {
        Page updated = new Page();
        updated.setId(0L);
        updated.setUrl("page1");
        PageDTO updatedDTO = new PageDTO();
        updatedDTO.setId(0L);
        updatedDTO.setUrl("page1");
        Mockito.when(pageService.update(0L, updated)).thenReturn(updated);
        Mockito.when(pageDTOConverter.convertToDTO(updated)).thenReturn(updatedDTO);
        Mockito.when(pageDTOConverter.convertFromDTO(updatedDTO)).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put("/page/0").content(
                                                  objectMapper.writeValueAsString(pageDTOConverter.convertToDTO(updated)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("page1"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }


    @Test
    void whenUpdateNonExistingPage_shouldError() throws Exception {
        Page updated = new Page();
        updated.setId(1L);
        updated.setUrl("page1");
        PageDTO updatedDTO = new PageDTO();
        updatedDTO.setId(1L);
        updatedDTO.setUrl("page1");
        Mockito.doThrow(EntityNotFoundException.class).when(pageService).update(0L, updated);
        Mockito.when(pageDTOConverter.convertToDTO(updated)).thenReturn(updatedDTO);
        Mockito.when(pageDTOConverter.convertFromDTO(updatedDTO)).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put("/page/0").content(
                                                  objectMapper.writeValueAsString(pageDTOConverter.convertToDTO(updated)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenCreatePage_shouldReturnPage() throws Exception {
        Page created = new Page();
        created.setId(0L);
        created.setUrl("page1");
        PageDTO createdDTO = new PageDTO();
        createdDTO.setId(0L);
        createdDTO.setUrl("page1");
        Mockito.when(pageService.save(created)).thenReturn(created);
        Mockito.when(pageDTOConverter.convertToDTO(created)).thenReturn(createdDTO);
        Mockito.when(pageDTOConverter.convertFromDTO(createdDTO)).thenReturn(created);

        mockMvc.perform(MockMvcRequestBuilders.post("/page").content(
                                                  objectMapper.writeValueAsString(pageDTOConverter.convertToDTO(created)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("page1"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }

}
