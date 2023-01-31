package com.github.mdeluise.pinboard.authentication;

import com.github.mdeluise.pinboard.authentication.payload.request.LoginRequest;
import com.github.mdeluise.pinboard.authentication.payload.request.SignupRequest;
import com.github.mdeluise.pinboard.authentication.payload.response.MessageResponse;
import com.github.mdeluise.pinboard.authentication.payload.response.UserInfoResponse;
import com.github.mdeluise.pinboard.security.jwt.JwtWebUtil;
import com.github.mdeluise.pinboard.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authentication")
@Tag(name = "Authentication", description = "Endpoints for authentication")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtWebUtil jwtWebUtil;
    private final UserService userService;


    @Autowired
    public AuthController(AuthenticationManager authManager, JwtWebUtil jwtWebUtil,
                          UserService userService) {
        this.authManager = authManager;
        this.jwtWebUtil = jwtWebUtil;
        this.userService = userService;
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
}
