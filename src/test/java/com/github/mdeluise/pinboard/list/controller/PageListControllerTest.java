package com.github.mdeluise.pinboard.list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mdeluise.pinboard.TestEnvironment;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import com.github.mdeluise.pinboard.list.PageList;
import com.github.mdeluise.pinboard.list.PageListController;
import com.github.mdeluise.pinboard.list.PageListDTO;
import com.github.mdeluise.pinboard.list.PageListDTOConverter;
import com.github.mdeluise.pinboard.list.PageListService;
import com.github.mdeluise.pinboard.page.PageDTOConverter;
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

@WebMvcTest(PageListController.class)
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
public class PageListControllerTest {
    @MockBean
    PageListService pageListService;
    @MockBean
    PageDTOConverter pageDTOConverter;
    @MockBean
    PageListDTOConverter pageListDTOConverter;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void whenGetPageLists_ShouldReturnPageLists() throws Exception {
        PageList pageList1 = new PageList();
        pageList1.setId(1L);
        pageList1.setName("pageList1");
        PageListDTO pageListDTO1 = new PageListDTO();
        pageListDTO1.setId(1L);
        pageListDTO1.setName("pageList1");
        PageList pageList2 = new PageList();
        pageList2.setId(2L);
        pageList2.setName("pageList2");
        PageListDTO pageListDTO2 = new PageListDTO();
        pageListDTO2.setId(2L);
        pageListDTO2.setName("pageList2");
        Mockito.when(pageListService.getAll()).thenReturn(List.of(pageList1, pageList2));
        Mockito.when(pageListDTOConverter.convertToDTO(pageList1)).thenReturn(pageListDTO1);
        Mockito.when(pageListDTOConverter.convertToDTO(pageList2)).thenReturn(pageListDTO2);

        mockMvc.perform(MockMvcRequestBuilders.get("/list")).andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }


    @Test
    void whenGetPageList_ShouldReturnPageList() throws Exception {
        PageList pageList = new PageList();
        pageList.setId(0L);
        pageList.setName("pageList0");
        PageListDTO pageListDTO = new PageListDTO();
        pageListDTO.setId(0L);
        pageListDTO.setName("pageList0");
        Mockito.when(pageListService.get(0L)).thenReturn(pageList);
        Mockito.when(pageListDTOConverter.convertToDTO(pageList)).thenReturn(pageListDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/list/0")).andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pageList0"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }


    @Test
    void whenGetNotExistingPageList_shouldError() throws Exception {
        Mockito.when(pageListService.get(0L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/list/0"))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenDeletePageList_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(pageListService).remove(0L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/list/0")).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void whenDeleteNonExistingPageList_shouldError() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(pageListService).remove(0L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/list/0"))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenUpdatePageList_shouldReturnUpdated() throws Exception {
        PageList updated = new PageList();
        updated.setId(0L);
        updated.setName("pageList1");
        PageListDTO updatedDTO = new PageListDTO();
        updatedDTO.setId(0L);
        updatedDTO.setName("pageList1");
        Mockito.when(pageListService.update(0L, updated)).thenReturn(updated);
        Mockito.when(pageListDTOConverter.convertToDTO(updated)).thenReturn(updatedDTO);
        Mockito.when(pageListDTOConverter.convertFromDTO(updatedDTO)).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put("/list/0").content(
                                                  objectMapper.writeValueAsString(pageListDTOConverter.convertToDTO(updated)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pageList1"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }


    @Test
    void whenUpdateNonExistingPageList_shouldError() throws Exception {
        PageList updated = new PageList();
        updated.setId(1L);
        updated.setName("pageList1");
        PageListDTO updatedDTO = new PageListDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("pageList1");
        Mockito.doThrow(EntityNotFoundException.class).when(pageListService).update(0L, updated);
        Mockito.when(pageListDTOConverter.convertToDTO(updated)).thenReturn(updatedDTO);
        Mockito.when(pageListDTOConverter.convertFromDTO(updatedDTO)).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put("/list/0").content(
                                                  objectMapper.writeValueAsString(pageListDTOConverter.convertToDTO(updated)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void whenCreatePageList_shouldReturnPageList() throws Exception {
        PageList created = new PageList();
        created.setId(0L);
        created.setName("pageList1");
        PageListDTO createdDTO = new PageListDTO();
        createdDTO.setId(0L);
        createdDTO.setName("pageList1");
        Mockito.when(pageListService.save(created)).thenReturn(created);
        Mockito.when(pageListDTOConverter.convertToDTO(created)).thenReturn(createdDTO);
        Mockito.when(pageListDTOConverter.convertFromDTO(createdDTO)).thenReturn(created);

        mockMvc.perform(MockMvcRequestBuilders.post("/list").content(
                                                  objectMapper.writeValueAsString(pageListDTOConverter.convertToDTO(created)))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pageList1"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }

}
