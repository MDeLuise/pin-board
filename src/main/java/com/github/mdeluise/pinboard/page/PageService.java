package com.github.mdeluise.pinboard.page;

import com.github.mdeluise.pinboard.authentication.User;
import com.github.mdeluise.pinboard.authentication.UserService;
import com.github.mdeluise.pinboard.authorization.permission.PType;
import com.github.mdeluise.pinboard.authorization.permission.Permission;
import com.github.mdeluise.pinboard.authorization.permission.PermissionService;
import com.github.mdeluise.pinboard.common.AbstractCrudService;
import com.github.mdeluise.pinboard.exception.InvalidPageException;
import com.github.mdeluise.pinboard.scraper.PageScraper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PageService extends AbstractCrudService<Page, Long> {
    private final UserService userService;
    private final PermissionService permissionService;
    private final PageScraper pageScraper;


    @Autowired
    public PageService(PageRepository repository, UserService userService, PermissionService permissionService,
                       PageScraper pageScraper) {
        super(repository);
        this.userService = userService;
        this.permissionService = permissionService;
        this.pageScraper = pageScraper;
    }


    @Override
    @PostFilter("hasRole('ADMIN') or hasAuthority('read:page:' + filterObject.id)")
    public org.springframework.data.domain.Page<Page> getAll(int pageNo, int pageSize, String sortBy,
                                                             Sort.Direction sortDir) {
        return super.getAll(pageNo, pageSize, sortBy, sortDir);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('read:page:' + #id)")
    public Page get(Long id) {
        return super.get(id);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:page:' + #id)")
    public void remove(Long id) {
        super.remove(id);
    }


    @Override
    @Transactional
    public Page save(Page entityToSave) {
        Page filledPage;
        try {
            filledPage = pageScraper.fillMissingFields(entityToSave);
        } catch (IOException e) {
            throw new InvalidPageException(e.getMessage());
        }
        Page saved = super.save(filledPage);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User owner = userService.get(username);

        Permission readPagePermission = permissionService.getOrCreate(
            new Permission(
                PType.READ, Page.class.getSimpleName().toLowerCase(), saved.getId().toString())
        );
        Permission writePagePermission = permissionService.getOrCreate(
            new Permission(
                PType.WRITE, Page.class.getSimpleName().toLowerCase(), saved.getId().toString())
        );

        owner.getPermissions().add(readPagePermission);
        owner.getPermissions().add(writePagePermission);
        userService.update(owner.getId(), owner);
        return saved;
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:page:' + #id)")
    public Page update(Long id, Page updatedEntity) {
        Page toUpdate = get(id);
        updateFields(toUpdate, updatedEntity);
        if (!toUpdate.getUrl().equals(updatedEntity.getUrl())) {
            toUpdate.setUrl(updatedEntity.getUrl());
            try {
                toUpdate = pageScraper.fillMissingFields(toUpdate);
            } catch (IOException e) {
                throw new InvalidPageException(e.getMessage());
            }
        }
        return repository.save(toUpdate);
    }


    @Override
    protected void updateFields(Page toUpdate, Page updatedEntity) {
        toUpdate.setTitle(updatedEntity.getTitle());
        toUpdate.setTags(updatedEntity.getTags());
        toUpdate.setLists(updatedEntity.getLists());
    }


    public org.springframework.data.domain.Page<Page> getPagesWithTitleLike(int pageNo, int pageSize, String sortBy,
                                                                            String title) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return ((PageRepository) repository).findByTitleIgnoreCaseContaining(title, paging);
    }
}
