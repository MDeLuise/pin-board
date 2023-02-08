package com.github.mdeluise.pinboard.authentication;

import com.github.mdeluise.pinboard.common.CrudController;
import com.github.mdeluise.pinboard.common.EntityBucket;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints for CRUD operations on users")
@Hidden
public class UserController implements CrudController<User, Long> {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Override
    @Operation(
        summary = "Get all Users",
        description = "Get all the User."
    )
    public ResponseEntity<EntityBucket<User>> findAll(Integer pageNo, Integer pageSize, String sortBy,
                                                      Sort.Direction sortDir) {
        EntityBucket<User> result = new EntityBucket<>(userService.getAll(pageNo, pageSize, sortBy, sortDir));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Override
    @Operation(
        summary = "Get a single User",
        description = "Get the details of a given User, according to the `id` parameter."
    )
    public ResponseEntity<User> find(
        @Parameter(description = "The ID of the User on which to perform the operation") Long id) {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }


    @Override
    @Operation(
        summary = "Update a single User",
        description = "Update the details of a given User, according to the `id` parameter." +
                          "Please note that some fields may be readonly for integrity purposes."
    )
    public ResponseEntity<User> update(User updatedEntity, @Parameter(
        description = "The ID of the User on which to perform the operation") Long id) {
        return new ResponseEntity<>(userService.update(id, updatedEntity), HttpStatus.OK);
    }


    @Override
    @Operation(
        summary = "Delete a single User",
        description = "Delete the given User, according to the `id` parameter."
    )
    public void remove(@Parameter(description = "The ID of the User on which to perform the operation") Long id) {
        userService.remove(id);
    }


    @Override
    @Operation(
        summary = "Create a new User",
        description = "Create a new User."
    )
    public ResponseEntity<User> save(User entityToSave) {
        return new ResponseEntity<>(userService.save(entityToSave), HttpStatus.OK);
    }
}
