package com.github.mdeluise.pinboard.page.body.service;

import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import com.github.mdeluise.pinboard.page.Page;
import com.github.mdeluise.pinboard.page.body.PageBody;
import com.github.mdeluise.pinboard.page.body.PageBodyRepository;
import com.github.mdeluise.pinboard.page.body.PageBodyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WithMockUser(username = "admin", roles = "ADMIN")
public class PageBodyServiceTest {
    @Mock
    PageBodyRepository pageBodyRepository;
    @InjectMocks
    PageBodyService pageBodyService;


    @Test
    void whenSavePageBody_thenReturnPageBody() {
        PageBody toSave = new PageBody();
        toSave.setId(0L);
        toSave.setContent("pageBody0");
        toSave.setPage(new Page());
        Mockito.when(pageBodyRepository.save(toSave)).thenReturn(toSave);

        Assertions.assertThat(pageBodyService.save(toSave)).isSameAs(toSave);
    }


    @Test
    void whenGetPageBody_thenReturnPageBody() {
        PageBody toGet = new PageBody();
        toGet.setId(0L);
        toGet.setContent("pageBody0");
        Mockito.when(pageBodyRepository.findById(0L)).thenReturn(Optional.of(toGet));

        Assertions.assertThat(pageBodyService.get(0L)).isSameAs(toGet);
    }


    @Test
    void whenGetNonExistingPageBody_thenError() {
        Mockito.when(pageBodyRepository.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> pageBodyService.get(0L)).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void whenGetAllPageBodies_thenReturnAllBodies() {
        PageBody toGet1 = new PageBody();
        toGet1.setId(0L);
        toGet1.setContent("pageBody0");
        PageBody toGet2 = new PageBody();
        toGet2.setId(1L);
        toGet2.setContent("pageBody1");

        List<PageBody> allPageBodies = List.of(toGet1, toGet2);
        Mockito.when(pageBodyRepository.findAll()).thenReturn(allPageBodies);

        Assertions.assertThat(pageBodyService.getAll()).isSameAs(allPageBodies);
    }


    @Test
    void givenPageBody_whenDeletePageBody_thenDeletePageBody() {
        PageBody pageBody = new PageBody();
        Mockito.when(pageBodyRepository.findById(0L)).thenReturn(Optional.of(pageBody));
        pageBodyService.remove(0L);
        Mockito.verify(pageBodyRepository, Mockito.times(1)).delete(pageBody);
    }


    @Test
    void whenDeleteNonExistingPageBody_thenError() {
        Mockito.when(pageBodyRepository.existsById(0L)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> pageBodyService.remove(0L)).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void givenPageBody_whenUpdatePageBody_thenUpdatePageBody() {
        PageBody updated = new PageBody();
        updated.setContent("updated");
        Mockito.when(pageBodyRepository.existsById(0L)).thenReturn(true);
        Mockito.when(pageBodyRepository.findById(0L)).thenReturn(Optional.of(updated));
        Mockito.when(pageBodyRepository.save(updated)).thenReturn(updated);

        Assertions.assertThat(pageBodyService.update(0L, updated)).isSameAs(updated);
    }


    @Test
    void whenUpdateNonExistingPageBody_thenError() {
        Mockito.when(pageBodyRepository.existsById(0L)).thenReturn(false);
        PageBody updated = new PageBody();
        updated.setContent("updated");

        Assertions.assertThatThrownBy(() -> pageBodyService.update(0L, updated))
                  .isInstanceOf(EntityNotFoundException.class);
    }
}
