package com.github.mdeluise.pinboard.list.service;

import com.github.mdeluise.pinboard.authentication.User;
import com.github.mdeluise.pinboard.authentication.UserService;
import com.github.mdeluise.pinboard.authorization.permission.PermissionService;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import com.github.mdeluise.pinboard.list.PageList;
import com.github.mdeluise.pinboard.list.PageListRepository;
import com.github.mdeluise.pinboard.list.PageListService;
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
public class PageListServiceTest {
    @Mock
    PageListRepository pageListRepository;
    @Mock
    UserService userService;
    @Mock
    PermissionService permissionService;
    @InjectMocks
    PageListService pageListService;


    @Test
    void whenSavePageList_thenReturnPageList() {
        PageList toSave = new PageList();
        toSave.setId(0L);
        toSave.setName("pageList0");
        Mockito.when(pageListRepository.save(toSave)).thenReturn(toSave);
        Mockito.when(userService.get("admin")).thenReturn(new User());

        Assertions.assertThat(pageListService.save(toSave)).isSameAs(toSave);
    }


    @Test
    void whenGetPageList_thenReturnPageList() {
        PageList toGet = new PageList();
        toGet.setId(0L);
        toGet.setName("pageList0");
        Mockito.when(pageListRepository.findById(0L)).thenReturn(Optional.of(toGet));

        Assertions.assertThat(pageListService.get(0L)).isSameAs(toGet);
    }


    @Test
    void whenGetNonExistingPageList_thenError() {
        Mockito.when(pageListRepository.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> pageListService.get(0L)).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void whenGetAllPageLists_thenReturnAllPageLists() {
        PageList toGet1 = new PageList();
        toGet1.setId(0L);
        toGet1.setName("pageList0");
        PageList toGet2 = new PageList();
        toGet2.setId(1L);
        toGet2.setName("pageList1");

        List<PageList> allPageLists = List.of(toGet1, toGet2);
        Mockito.when(pageListRepository.findAll()).thenReturn(allPageLists);

        Assertions.assertThat(pageListService.getAll()).isSameAs(allPageLists);
    }


    @Test
    void givenPageList_whenDeletePageList_thenDeletePageList() {
        PageList pageList = new PageList();
        Mockito.when(pageListRepository.findById(0L)).thenReturn(Optional.of(pageList));
        pageListService.remove(0L);
        Mockito.verify(pageListRepository, Mockito.times(1)).delete(pageList);
    }


    @Test
    void whenDeleteNonExistingPageList_thenError() {
        Mockito.when(pageListRepository.existsById(0L)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> pageListService.remove(0L)).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void givenPageList_whenUpdatePageList_thenUpdatePageList() {
        PageList updated = new PageList();
        updated.setName("updated");
        Mockito.when(pageListRepository.existsById(0L)).thenReturn(true);
        Mockito.when(pageListRepository.findById(0L)).thenReturn(Optional.of(new PageList()));
        Mockito.when(pageListRepository.save(Mockito.any())).thenReturn(updated);

        Assertions.assertThat(pageListService.update(0L, updated)).isSameAs(updated);
    }


    @Test
    void whenUpdateNonExistingPageList_thenError() {
        Mockito.when(pageListRepository.existsById(0L)).thenReturn(false);
        PageList updated = new PageList();
        updated.setName("updated");

        Assertions.assertThatThrownBy(() -> pageListService.update(0L, updated))
                  .isInstanceOf(EntityNotFoundException.class);
    }
}
