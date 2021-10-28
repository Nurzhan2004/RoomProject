package nurzhan.Project.room.Controllers;

import nurzhan.Project.room.Services.UserService;
import nurzhan.Project.room.entities.Countries;
import nurzhan.Project.room.entities.Roles;
import nurzhan.Project.room.entities.Users;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/")
    public String home(){
        return "index";
    }
    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/loginpage")
    public String login(){
        return "login";
    }
    @GetMapping(value = "error")
    public String errorpage(){
        return "error";
    }
    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/register")
    public String register(Model model){
        List<Countries> countries = userService.getAllCountries();
        model.addAttribute("countries",countries);
        return "register";
    }
    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register")
    public String registerpost(@RequestParam(name="user_name")String user_name,
                               @RequestParam(name="user_surname")String user_surname,
                               @RequestParam(name = "user_email")String user_email,
                               @RequestParam(name = "user_password")String user_password,
                               @RequestParam(name = "user_url")String user_url,
                               @RequestParam(name = "country_id")Long country_id){

        Users checkUser = userService.getUserByEmail(user_email);

        if(user_url.contains("%")||user_url.contains("!")||user_url.contains("~")||user_url.contains(")")||user_url.contains("(")||user_url.contains("#")
                ||user_url.contains("@")||user_url.contains("^")||user_url.contains("*")||user_url.contains("/")||user_url.contains(">")||user_url.contains("<")||user_url.contains("|")
        ||user_url.contains("{")||user_url.contains("}")||user_url.contains("\"")
        ){
            return "redirect:/register?urlerror";
        }


        if(checkUser==null){
            Users urlUser = userService.getUserByUrl(user_url);
            if(urlUser==null){
                Countries country = userService.getCountryByid(country_id);
                List<Roles> role_users = new ArrayList<>();
                Roles role_user = userService.getRoleById((long)3);
                role_users.add(role_user);
                Users new_user = new Users();
                new_user.setImage("/avatars/default.png");
                new_user.setRoles(role_users);
                new_user.setEmail(user_email);
                new_user.setCountry(country);
                new_user.setName(user_name);
                new_user.setSurname(user_surname);
                new_user.setPassword(passwordEncoder.encode(user_password));
                new_user.setUrl(user_url);
                userService.addUser(new_user);
                return "redirect:/loginpage";
            }else{
                return "redirect:/register?urlzaregistirovan";
            }
        }else{
            return "redirect:/register?emailzaregistirovan";
        }
    }



    public Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user = (Users)authentication.getPrincipal();
            return user;
        }
        return null;
    }

}
