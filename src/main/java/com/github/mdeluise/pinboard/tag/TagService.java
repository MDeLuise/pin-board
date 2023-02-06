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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    public org.springframework.data.domain.Page<Tag> getAll(int pageNo, int pageSize, String sortBy) {
        return super.getAll(pageNo, pageSize, sortBy);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('read:tag:' + #id)")
    public Tag get(Long id) {
        return super.get(id);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:tag:' + #id)")
    public void remove(Long id) {
        super.remove(id);
    }


    @Override
    public Tag save(Tag entityToSave) {
        Tag saved = super.save(entityToSave);

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
        // FIXME change permission name if needed
        Tag toUpdate = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        updateFields(toUpdate, updatedEntity);
        return repository.save(toUpdate);
    }


    @Override
    protected void updateFields(Tag toUpdate, Tag updatedEntity) {
        toUpdate.setName(updatedEntity.getName());
        toUpdate.setPages(updatedEntity.getPages());
    }


    @Transactional
    public void addTagsToPages(List<Long> tagIds, List<Long> pageIds) {
        for (Long tagId : tagIds) {
            for (Long pageId : pageIds) {
                addTagToPage(tagId, pageId);
            }
        }
    }


    @PreAuthorize("hasRole('ADMIN') or (hasAuthority('write:tag:' + #tagId) and hasAuthority('write:page:' + #page.id))")
    private void addTagToPage(Long tagId, Long pageId) {
        Tag tag = get(tagId);
        Page page = pageService.get(pageId);
        page.addTag(tag);
        pageService.update(page.getId(), page);
    }


    @Transactional
    public void removeTagsFromPages(List<Long> tagIds, List<Long> pageIds) {
        for (Long tagId : tagIds) {
            for (Long pageId : pageIds) {
                removeTagFromPage(tagId, pageId);
            }
        }
    }


    @PreAuthorize("hasRole('ADMIN') or (hasAuthority('write:tag:' + #tagId) and hasAuthority('write:page:' + #page.id))")
    private void removeTagFromPage(Long tagId, Long pageId) {
        Tag tag = get(tagId);
        Page page = pageService.get(pageId);
        page.removeTag(tag);
        pageService.update(page.getId(), page);
    }


    @PostAuthorize("hasRole('ADMIN') or hasAuthority('read:tag:' + #returnObject.id)")
    public Tag getByName(String name) {
        return ((TagRepository) repository).findByName(name)
                                           .orElseThrow(() -> new EntityNotFoundException("name", name));
    }
}
