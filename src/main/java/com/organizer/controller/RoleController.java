package com.organizer.controller;

import com.organizer.entity.Role;
import com.organizer.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<Role> getAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public Role getById(@PathVariable Long id) {
        return roleService.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @GetMapping("/name/{roleName}")
    public Role getByRoleName(@PathVariable String roleName) {
        return roleService.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @PostMapping
    public Role create(@RequestBody Role role) {
        return roleService.save(role);
    }

    @PutMapping("/{id}")
    public Role update(@PathVariable Long id, @RequestBody Role role) {
        return roleService.update(id, role);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }
}