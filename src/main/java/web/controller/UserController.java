package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {
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

    @GetMapping("user")
    public String getUserPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByEmail(auth.getName());
        if (user.hasRole("ROLE_ADMIN")) {
            return "redirect:/admin";
        }

        model.addAttribute("user", user);
        return "userPage";
    }

    @GetMapping("admin")
    public String getAdminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        User newUser = new User();
        model.addAttribute("newUser", newUser);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", roleService.getRoleList());
        return "adminPage";
    }
}
