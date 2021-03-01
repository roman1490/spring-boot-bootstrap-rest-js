package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class UserRestController {
    private UserService userService;
    private RoleService roleService;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("/api/admin")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/user")
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return  userService.getUserByEmail(auth.getName());
    }

    @GetMapping("/api/user/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUserById(id);
    }


    @PostMapping("/api/admin")
    public void saveUser(User user, @RequestParam(name = "rolesNewUser", required = false) List<Integer> roles) {
        if (roles.size() > 0) {
            roles.forEach(roleIndex -> user.getRoles().add(roleService.getRoleById(roleIndex)));
        }
        userService.addUser(user);
    }


    @PatchMapping("/api/admin/updateUser/{id}")
    public ResponseEntity<?> updateUser(User user,
                                        @PathVariable int id,
                                        @RequestParam(name = "rolesEditUser", required = false) List<Integer> roles) {
        try {
            if (roles.size() > 0) {
                roles.forEach(roleIndex -> user.getRoles().add(roleService.getRoleById(roleIndex)));
            }
            userService.updateUser(id, user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/api/admin/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

}
