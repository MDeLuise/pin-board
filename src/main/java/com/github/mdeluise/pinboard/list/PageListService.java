package com.github.mdeluise.pinboard.list;

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
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageListService extends AbstractCrudService<PageList, Long> {
    private final PermissionService permissionService;
    private final UserService userService;
    private final PageService pageService;


    @Autowired
    public PageListService(PageListRepository repository, UserService userService, PermissionService permissionService,
                           PageService pageService) {
        super(repository);
        this.userService = userService;
        this.permissionService = permissionService;
        this.pageService = pageService;
    }


    @Override
    @PostFilter("hasRole('ADMIN') or hasAuthority('read:list:' + filterObject.id)")
    public org.springframework.data.domain.Page<PageList> getAll(int pageNo, int pageSize, String sortBy,
                                                                 Sort.Direction sortDir) {
        return super.getAll(pageNo, pageSize, sortBy, sortDir);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('read:list:' + #id)")
    public PageList get(Long id) {
        return super.get(id);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:list:' + #id)")
    public void remove(Long id) {
        super.remove(id);
    }


    @Override
    public PageList save(PageList entityToSave) {
        PageList saved = super.save(entityToSave);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User owner = userService.get(username);

        Permission readPageListPermission = permissionService.getOrCreate(
            new Permission(
                PType.READ, PageList.class.getSimpleName().toLowerCase(), saved.getId().toString())
        );
        Permission writePageListPermission = permissionService.getOrCreate(
            new Permission(
                PType.WRITE, PageList.class.getSimpleName().toLowerCase(), saved.getId().toString())
        );

        owner.getPermissions().add(readPageListPermission);
        owner.getPermissions().add(writePageListPermission);
        userService.update(owner.getId(), owner);
        return saved;
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:list:' + #id)")
    public PageList update(Long id, PageList updatedEntity) {
        // FIXME if change name then also permission (even for other entities)
        return super.update(id, updatedEntity);
    }


    @Override
    protected void updateFields(PageList toUpdate, PageList updatedEntity) {
        toUpdate.setPages(updatedEntity.getPages());
        toUpdate.setName(updatedEntity.getName());
    }


    @Transactional
    public void addPagesToLists(List<Long> pageListIds, List<Long> pageIds) {
        for (Long pageListId : pageListIds) {
            for (Long pageId : pageIds) {
                addPageToList(pageListId, pageId);
            }
        }
    }


    @PreAuthorize("hasRole('ADMIN') or" +
                      "(hasAuthority('write:list:' + #pageListId) and hasAuthority('read:page:' + #pageId))")
    private void addPageToList(Long pageListId, Long pageId) {
        PageList pageList = get(pageListId);
        Page page = pageService.get(pageId);
        page.addList(pageList);
        pageService.update(page.getId(), page);
    }


    @Transactional
    public void removePagesFromLists(List<Long> pageListIds, List<Long> pageIds) {
        for (Long pageListId : pageListIds) {
            for (Long pageId : pageIds) {
                removePageFromList(pageListId, pageId);
            }
        }
    }


    @PreAuthorize("hasRole('ADMIN') or" +
                      "(hasAuthority('write:list:' + #pageListId) and hasAuthority('read:page:' + #pageId))")
    private void removePageFromList(Long pageListId, Long pageId) {
        PageList pageList = get(pageListId);
        Page page = pageService.get(pageId);
        page.removeList(pageList);
        pageService.update(page.getId(), page);
    }


    @PostAuthorize("hasRole('ADMIN') or hasAuthority('read:list:' + #returnObject.id)")
    public PageList getByName(String name) {
        return ((PageListRepository) repository).getByName(name)
                                                .orElseThrow(() -> new EntityNotFoundException("name", name));
    }
}
