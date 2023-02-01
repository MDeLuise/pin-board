package com.github.mdeluise.pinboard.tag;

import com.github.mdeluise.pinboard.authentication.User;
import com.github.mdeluise.pinboard.authentication.UserService;
import com.github.mdeluise.pinboard.authorization.permission.PType;
import com.github.mdeluise.pinboard.authorization.permission.Permission;
import com.github.mdeluise.pinboard.authorization.permission.PermissionService;
import com.github.mdeluise.pinboard.common.AbstractCrudService;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import com.github.mdeluise.pinboard.page.Page;
import com.github.mdeluise.pinboard.page.PageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TagService extends AbstractCrudService<Tag, Long> {
    private final UserService userService;
    private final PermissionService permissionService;
    private final PageService pageService;


    @Autowired
    public TagService(TagRepository repository, UserService userService, PermissionService permissionService,
                      PageService pageService) {
        super(repository);
        this.userService = userService;
        this.permissionService = permissionService;
        this.pageService = pageService;
    }


    @Override
    @PostFilter("hasRole('ADMIN') or hasAuthority('read:tag:' + filterObject.id)")
    public Collection<Tag> getAll() {
        return ((TagRepository) repository).findAll();
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('read:tag:' + #id)")
    public Tag get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:tag:' + #id)")
    public void remove(Long id) {
        Tag toRemove = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        repository.delete(toRemove);
    }


    @Override
    public Tag save(Tag entityToSave) {
        Tag saved = repository.save(entityToSave);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User owner = userService.get(username);

        Permission readTagPermission = permissionService.getOrCreate(
            new Permission(
                PType.READ, Tag.class.getSimpleName().toLowerCase(), saved.getId().toString())
        );
        Permission writeTagPermission = permissionService.getOrCreate(
            new Permission(
                PType.WRITE, Tag.class.getSimpleName().toLowerCase(), saved.getId().toString())
        );

        owner.getPermissions().add(readTagPermission);
        owner.getPermissions().add(writeTagPermission);
        userService.update(owner.getId(), owner);
        return saved;
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:tag:' + #id)")
    public Tag update(Long id, Tag updatedEntity) {
        Tag toUpdate = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        toUpdate.setName(updatedEntity.getName());
        toUpdate.setPages(updatedEntity.getPages());
        return repository.save(toUpdate);
    }


    @Transactional
    public void addTagsToPage(List<Long> tagIds, Long pageId) {
        Page page = pageService.get(pageId);
        for (Long tagId: tagIds) {
            addTagToPage(page, tagId);
        }
    }


    @PreAuthorize("hasRole('ADMIN') or (hasAuthority('write:tag:' + #tagId) and hasAuthority('write:page:' + #page.id))")
    private void addTagToPage(Page page, Long tagId) {
        Tag tag = get(tagId);
        tag.addPage(page);
        repository.save(tag);
    }


    @Transactional
    public void removeTagsToPage(List<Long> tagIds, Long pageId) {
        Page page = pageService.get(pageId);
        for (Long tagId: tagIds) {
            removeTagFromPage(page, tagId);
        }
    }


    @PreAuthorize("hasRole('ADMIN') or (hasAuthority('write:tag:' + #tagId) and hasAuthority('write:page:' + #page.id))")
    private void removeTagFromPage(Page page, Long tagId) {
        Tag tag = get(tagId);
        tag.removePage(page);
        repository.save(tag);
    }
}
