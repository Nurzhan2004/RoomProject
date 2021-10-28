package nurzhan.Project.room.Controllers;

import nurzhan.Project.room.Services.UserService;
import nurzhan.Project.room.entities.Users;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping(value = "/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${loadUrl}")
    private String loadURL;

    @Value("${targetUrl}")
    private String targetUrl;

    @GetMapping(value = "/image/{Url}",produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] userviewimage(@PathVariable(name = "Url")String url) throws IOException {

        String picURL = loadURL + "default.png";
        if(url!=null){
            picURL = loadURL + url;
        }
        InputStream in;
        try{
            ClassPathResource classPathResource = new ClassPathResource(picURL);
            in = classPathResource.getInputStream();
        }catch(Exception e){
            picURL = loadURL + "default.png";
            ClassPathResource classPathResource = new ClassPathResource(picURL);
            in = classPathResource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/profile/image")
    public String postimage(@RequestParam(name = "image") MultipartFile file) {
        try {
            if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
                String fileName = "avatar_" + getUser().getUrl() + ".jpg";

                byte bytes[] = file.getBytes();

                Path path = Paths.get(targetUrl+fileName);
                Files.write(path,bytes);
                Users checkUser = userService.getUserById(getUser().getId());
                checkUser.setImage(fileName);
                userService.addUser(checkUser);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/user/profile/"+getUser().getUrl();
    }


    @GetMapping(value = "/profile/{Url}")
    public String userprofile(Model model, @PathVariable(name = "Url")String url) {

        Users checkUser = userService.getUserByUrl(url);
        Users getuser = userService.getUserById(getUser().getId());
        if(checkUser==null){
            return "redirect:/users/profile/"+getuser.getUrl();
        }
        Users user = userService.getUserById(getUser().getId());
        if (checkUser.getCountry().getId() == user.getCountry().getId() &&  checkUser.getId()==user.getId()) {
            model.addAttribute("user", user);
            return "getuserprofile";
        } else if (checkUser.getCountry().getId() == checkUser.getCountry().getId() && !checkUser.getUrl().equals(user.getUrl())) {
            model.addAttribute("user", checkUser);
            return "userprofile";
        }else{
            return "redirect:/rooms/my?error";
        }
    }

    @PostMapping(value = "/updatepassword")
    public String updatepassword(@RequestParam(name = "old_password")String old_password,
                                 @RequestParam(name = "new_password")String new_password,
                                 @RequestParam(name = "re_new_password")String re_new_password){
        Users checkUser = userService.getUserById(getUser().getId());
        if (passwordEncoder.matches(old_password.trim(),checkUser.getPassword())) {
            if(new_password.trim().equals(re_new_password.trim())) {
                checkUser.setPassword(passwordEncoder.encode(re_new_password));
                userService.addUser(checkUser);
                return "redirect:/user/profile/" + checkUser.getUrl()+"?success#success";
            }else{
                return "redirect:/user/profile/"+checkUser.getUrl()+"?retypepasswordError#retypepasswordError";
            }
        }else{
            return "redirect:/user/profile/"+checkUser.getUrl()+"?oldpasswordError#oldpasswordError";
        }

    }

    @PostMapping(value = "/updateprofile")
    public String updateprofile(@RequestParam(name = "user_name")String name,
                                @RequestParam(name = "user_surname")String surname,
                                @RequestParam(name = "user_url")String url){
        Users checkUser = userService.getUserById(getUser().getId());
        Users urlUser = userService.getUserByUrl(url);

        if(url.contains("%")||url.contains("!")||url.contains("~")||url.contains(")")||url.contains("(")||url.contains("#")
                ||url.contains("@")||url.contains("^")||url.contains("*")||url.contains("/")||url.contains(">")||url.contains("<")||url.contains("|")
                ||url.contains("{")||url.contains("}")||url.contains("\"")||url.contains("$")
        ){
            return "redirect:/user/profile/"+getUser().getUrl()+"?s#s";
        }

        if(urlUser==null){
            checkUser.setUrl(url);
            checkUser.setName(name);
            checkUser.setSurname(surname);
            userService.addUser(checkUser);
            return "redirect:/user/profile/"+url;
        }else if(checkUser.getUrl().equals(url)){
            checkUser.setName(name);
            checkUser.setSurname(surname);
            userService.addUser(checkUser);
            return "redirect:/user/profile/"+getUser().getUrl();
        }else{
            return "redirect:/user/profile/"+getUser().getUrl()+"?error";
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
