package com.github.mdeluise.pinboard.tag.service;

import com.github.mdeluise.pinboard.authentication.User;
import com.github.mdeluise.pinboard.authentication.UserService;
import com.github.mdeluise.pinboard.authorization.permission.PermissionService;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import com.github.mdeluise.pinboard.tag.Tag;
import com.github.mdeluise.pinboard.tag.TagRepository;
import com.github.mdeluise.pinboard.tag.TagService;
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
public class TagServiceTest {
    @Mock
    TagRepository tagRepository;
    @Mock
    UserService userService;
    @Mock
    PermissionService permissionService;
    @InjectMocks
    TagService tagService;


    @Test
    void whenSaveTag_thenReturnTag() {
        Tag toSave = new Tag();
        toSave.setId(0L);
        toSave.setName("tag0");
        Mockito.when(tagRepository.save(toSave)).thenReturn(toSave);
        Mockito.when(userService.get("admin")).thenReturn(new User());

        Assertions.assertThat(tagService.save(toSave)).isSameAs(toSave);
    }


    @Test
    void whenGetTag_thenReturnTag() {
        Tag toGet = new Tag();
        toGet.setId(0L);
        toGet.setName("tag0");
        Mockito.when(tagRepository.findById(0L)).thenReturn(Optional.of(toGet));

        Assertions.assertThat(tagService.get(0L)).isSameAs(toGet);
    }


    @Test
    void whenGetNonExistingTag_thenError() {
        Mockito.when(tagRepository.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> tagService.get(0L)).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void whenGetAllTags_thenReturnAllTags() {
        Tag toGet1 = new Tag();
        toGet1.setId(0L);
        toGet1.setName("tag0");
        Tag toGet2 = new Tag();
        toGet2.setId(1L);
        toGet2.setName("tag1");

        List<Tag> allTags = List.of(toGet1, toGet2);
        Mockito.when(tagRepository.findAll()).thenReturn(allTags);

        Assertions.assertThat(tagService.getAll()).isSameAs(allTags);
    }


    @Test
    void givenTag_whenDeleteTag_thenDeleteTag() {
        Tag tag = new Tag();
        Mockito.when(tagRepository.findById(0L)).thenReturn(Optional.of(tag));
        tagService.remove(0L);
        Mockito.verify(tagRepository, Mockito.times(1)).delete(tag);
    }


    @Test
    void whenDeleteNonExistingTag_thenError() {
        Mockito.when(tagRepository.existsById(0L)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> tagService.remove(0L)).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void givenTag_whenUpdateTag_thenUpdateTag() {
        Tag updated = new Tag();
        updated.setName("updated");
        Mockito.when(tagRepository.existsById(0L)).thenReturn(true);
        Mockito.when(tagRepository.findById(0L)).thenReturn(Optional.of(new Tag()));
        Mockito.when(tagRepository.save(Mockito.any())).thenReturn(updated);

        Assertions.assertThat(tagService.update(0L, updated)).isSameAs(updated);
    }


    @Test
    void whenUpdateNonExistingTag_thenError() {
        Mockito.when(tagRepository.existsById(0L)).thenReturn(false);
        Tag updated = new Tag();
        updated.setName("updated");

        Assertions.assertThatThrownBy(() -> tagService.update(0L, updated))
                  .isInstanceOf(EntityNotFoundException.class);
    }
}
