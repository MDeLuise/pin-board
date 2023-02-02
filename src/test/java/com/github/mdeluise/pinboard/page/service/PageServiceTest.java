package com.github.mdeluise.pinboard.page.service;

import com.github.mdeluise.pinboard.authentication.User;
import com.github.mdeluise.pinboard.authentication.UserService;
import com.github.mdeluise.pinboard.authorization.permission.PermissionService;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import com.github.mdeluise.pinboard.page.Page;
import com.github.mdeluise.pinboard.page.PageRepository;
import com.github.mdeluise.pinboard.page.PageService;
import com.github.mdeluise.pinboard.scraper.PageScraper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WithMockUser(username = "admin", roles = "ADMIN")
public class PageServiceTest {
    @Mock
    PageRepository pageRepository;
    @Mock
    UserService userService;
    @Mock
    PermissionService permissionService;
    @Mock
    PageScraper pageScraper;
    @InjectMocks
    PageService pageService;


    @Test
    void whenSavePage_thenReturnPage() throws IOException {
        Page toSave = new Page();
        toSave.setId(0L);
        toSave.setUrl("page0");
        Mockito.when(pageRepository.save(toSave)).thenReturn(toSave);
        Mockito.when(userService.get("admin")).thenReturn(new User());
        Mockito.when(pageScraper.fillMissingFields(toSave)).thenReturn(toSave);

        Assertions.assertThat(pageService.save(toSave)).isSameAs(toSave);
    }


    @Test
    void whenGetPage_thenReturnPage() {
        Page toGet = new Page();
        toGet.setId(0L);
        toGet.setUrl("page0");
        Mockito.when(pageRepository.findById(0L)).thenReturn(Optional.of(toGet));

        Assertions.assertThat(pageService.get(0L)).isSameAs(toGet);
    }


    @Test
    void whenGetNonExistingPage_thenError() {
        Mockito.when(pageRepository.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> pageService.get(0L)).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void whenGetAllPages_thenReturnAllPages() {
        Page toGet1 = new Page();
        toGet1.setId(0L);
        toGet1.setUrl("page0");
        Page toGet2 = new Page();
        toGet2.setId(1L);
        toGet2.setUrl("page1");

        List<Page> allPages = List.of(toGet1, toGet2);
        Mockito.when(pageRepository.findAll()).thenReturn(allPages);

        Assertions.assertThat(pageService.getAll()).isSameAs(allPages);
    }


    @Test
    void givenPage_whenDeletePage_thenDeletePage() {
        Page page = new Page();
        Mockito.when(pageRepository.findById(0L)).thenReturn(Optional.of(page));
        pageService.remove(0L);
        Mockito.verify(pageRepository, Mockito.times(1)).delete(page);
    }


    @Test
    void whenDeleteNonExistingPage_thenError() {
        Mockito.when(pageRepository.existsById(0L)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> pageService.remove(0L)).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void givenPage_whenUpdatePage_thenUpdatePage() {
        Page updated = new Page();
        updated.setUrl("updated");
        Mockito.when(pageRepository.existsById(0L)).thenReturn(true);
        Mockito.when(pageRepository.findById(0L)).thenReturn(Optional.of(updated));
        Mockito.when(pageRepository.save(updated)).thenReturn(updated);

        Assertions.assertThat(pageService.update(0L, updated)).isSameAs(updated);
    }


    @Test
    void whenUpdateNonExistingPage_thenError() {
        Mockito.when(pageRepository.existsById(0L)).thenReturn(false);
        Page updated = new Page();
        updated.setUrl("updated");

        Assertions.assertThatThrownBy(() -> pageService.update(0L, updated))
                  .isInstanceOf(EntityNotFoundException.class);
    }
}
