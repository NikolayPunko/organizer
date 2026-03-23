package com.organizer.controller;

import com.organizer.dto.RegisterRequest;
import com.organizer.dto.UpdateUserRolesRequest;
import com.organizer.dto.UserListItemResponse;
import com.organizer.entity.User;
import com.organizer.entity.UserCreateDTO;
import com.organizer.service.AuthService;
import com.organizer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/email")
    public User getByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public User create(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}/roles")
    public User updateRoles(@PathVariable Long id, @RequestBody UpdateUserRolesRequest request) {
        return userService.updateRoles(id, request.getRoleIds());
    }

    @GetMapping("/list")
    public List<UserListItemResponse> getList() {
        return userService.findAll().stream()
                .map(user -> new UserListItemResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getIsActive(),
                        user.getRoles().stream().map(r -> r.getRoleName()).toList()
                ))
                .toList();
    }
}