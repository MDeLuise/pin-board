package com.github.mdeluise.pinboard.authentication;

import com.github.mdeluise.pinboard.authentication.payload.request.LoginRequest;
import com.github.mdeluise.pinboard.authentication.payload.request.SignupRequest;
import com.github.mdeluise.pinboard.authentication.payload.response.MessageResponse;
import com.github.mdeluise.pinboard.authentication.payload.response.UserInfoResponse;
import com.github.mdeluise.pinboard.security.apikey.ApiKeyDTO;
import com.github.mdeluise.pinboard.security.apikey.ApiKeyDTOConverter;
import com.github.mdeluise.pinboard.security.apikey.ApiKeyService;
import com.github.mdeluise.pinboard.security.jwt.JwtWebUtil;
import com.github.mdeluise.pinboard.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authentication")
@Tag(name = "Authentication", description = "Endpoints for authentication")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtWebUtil jwtWebUtil;
    private final UserService userService;
    private final ApiKeyService apiKeyService;
    private final ApiKeyDTOConverter apiKeyDTOConverter;


    @Autowired
    public AuthController(AuthenticationManager authManager, JwtWebUtil jwtWebUtil,
                          UserService userService, ApiKeyService apiKeyService, ApiKeyDTOConverter apiKeyDTOConverter) {
        this.authManager = authManager;
        this.jwtWebUtil = jwtWebUtil;
        this.userService = userService;
        this.apiKeyService = apiKeyService;
        this.apiKeyDTOConverter = apiKeyDTOConverter;
    }


    @PostMapping("/login")
    @Operation(
        summary = "Username and password login",
        description = "Login using username and password."
    )
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtWebUtil.generateJwtCookie(userDetails);
        List<String> authorities = userDetails.getAuthorities().stream()
                                              .map(GrantedAuthority::getAuthority)
                                              .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                             .body(new UserInfoResponse(
                                 userDetails.getId(),
                                 userDetails.getUsername(),
                                 authorities
                             ));
    }


    @PostMapping("/logout")
    @Operation(
        summary = "Logout",
        description = "Logout from the system."
    )
    public ResponseEntity<MessageResponse> logoutUser() {
        ResponseCookie cookie = jwtWebUtil.generateCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                             .body(new MessageResponse("You've been signed out"));
    }


    @PostMapping("/signup")
    @Operation(
        summary = "Signup",
        description = "Create a new account."
    )
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.username())) {
            return ResponseEntity.badRequest()
                                 .body(new MessageResponse("Error: Username is already taken!"));
        }

        userService.save(signUpRequest.username(), signUpRequest.password());
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }


    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh the authentication token",
        description = "Refresh the given authentication token."
    )
    public ResponseEntity<String> refreshAuthToken(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtWebUtil.refreshToken(
            jwtWebUtil.getJwtTokenFromCookies(httpServletRequest)).toString()
        ).body("Token refreshed successfully");
    }


    @PostMapping("/api-key")
    @Operation(
        summary = "Create a new API Key",
        description = "Create a new API Key."
    )
    public ResponseEntity<String> createNewApiKey(@RequestParam(required = false) String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.get(username);
        String result = apiKeyService.createNew(user, name);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @DeleteMapping("/api-key/{id}")
    @Operation(
        summary = "Delete an API Key",
        description = "Delete an API Key."
    )
    public ResponseEntity<String> removeApiKey(@PathVariable("id") Long apiKeyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.get(username);
        apiKeyService.remove(user, apiKeyId);
        return new ResponseEntity<>("Api Key correctly removed.", HttpStatus.OK);
    }


    @GetMapping("/api-key")
    @Operation(
        summary = "Get all the API Key", description = "Get all the API Key."
    )
    public ResponseEntity<Collection<ApiKeyDTO>> getAllApiKey() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.get(username);
        List<ApiKeyDTO> result = apiKeyService.getAll(user).stream().map(apiKeyDTOConverter::convertToDTO).toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
