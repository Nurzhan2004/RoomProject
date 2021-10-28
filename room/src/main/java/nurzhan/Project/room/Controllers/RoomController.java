package nurzhan.Project.room.Controllers;

import nurzhan.Project.room.Services.RoomService;
import nurzhan.Project.room.Services.UserService;
import nurzhan.Project.room.entities.Countries;
import nurzhan.Project.room.entities.Rooms;
import nurzhan.Project.room.entities.Users;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
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
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@Controller
@PreAuthorize("isAuthenticated()")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${loadImageRoom}")
    private String loadImageRoom;

    @Value("${targetImageRoom}")
    private String targetImageRoom;


    @GetMapping(value = "/image/{Url}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] userviewimage(@PathVariable(name = "Url") String url) throws IOException {

        String picURL = loadImageRoom + "noimage.jpg";
        if (url != null) {
            picURL = loadImageRoom + url;
        }
        InputStream in;
        try {
            ClassPathResource classPathResource = new ClassPathResource(picURL);
            in = classPathResource.getInputStream();
        } catch (Exception e) {
            picURL = loadImageRoom + "noimage.jpg";
            ClassPathResource classPathResource = new ClassPathResource(picURL);
            in = classPathResource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/image")
    public String postimage(@RequestParam(name = "image") MultipartFile file,
                            @RequestParam(name = "room_id") Long id) {
        Rooms checkRoom = roomService.getRoomById(id);
        try {
            if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
                String fileName = "avatar_" + checkRoom.getUrl() + ".jpg";

                byte bytes[] = file.getBytes();

                Path path = Paths.get(targetImageRoom + fileName);
                Files.write(path, bytes);
                checkRoom.setImage(fileName);
                roomService.addRoom(checkRoom);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/rooms/update/" + checkRoom.getUrl();
    }

    @GetMapping(value = "/update/{Url}")
    public String roomupdate(Model model,
                             @PathVariable(name = "Url") String url) {
        Rooms room = roomService.getRoomByUrl(url);
        if (room.getAuthor().getId() == getUser().getId()) {
            model.addAttribute("room", room);
            return "roomupdate";
        } else {
            return "redirect:/rooms/room/" + room.getId();
        }

    }

    @PostMapping(value = "/updateroom")
    public String roomupdate(@RequestParam(name = "room_id") Long id,
                             @RequestParam(name = "room_name") String name,
                             @RequestParam(name = "room_url") String url) {
        Rooms room = roomService.getRoomById(id);
        Rooms roomurl = roomService.getRoomByUrl(url);
        if (roomurl == null) {
            return "redirect:/rooms/update/" + room.getUrl() + "?urlerror#u";
        }
        room.setName(name);
        room.setUrl(url);
        roomService.addRoom(room);
        return "redirect:/rooms/update/" + room.getUrl() + "?success#s";
    }


    @GetMapping(value = "/")
    public String home(Model model) {
        Users checkUser = userService.getUserByEmail(getUser().getEmail());
        List<Rooms> rooms = roomService.getAllByRoomCountry(checkUser.getCountry());
        List<Rooms> userRoom = roomService.getAllByRoomUser(checkUser);
        rooms.removeAll(userRoom);
        model.addAttribute("userRoom", userRoom);
        model.addAttribute("rooms", rooms);
        model.addAttribute("users", checkUser);

        return "rooms";
    }

    @GetMapping(value = "/my")
    public String room_home(Model model) {
        Users checkUser = userService.getUserByEmail(getUser().getEmail());
        model.addAttribute("users", checkUser);
        List<Rooms> rooms = roomService.getAllByAuthorRoom(checkUser);
        model.addAttribute("rooms", rooms);
        return "roomsmy";
    }


    @PostMapping(value = "/updatecolor")
    public String updatecolor(@RequestParam(name = "room_id") Long id,
                              @RequestParam(name = "room_color") String color) {
        if (id != null && color != null) {
            Rooms room = roomService.getRoomById(id);
            if (room != null) {
                room.setColor(color);
                roomService.saveRoom(room);
                return "redirect:/rooms/room/" + id;
            }
        }
        return "redirect:/rooms/room/" + id;
    }


    @GetMapping(value = "/add")
    public String addroom(Model model){
        Users checkUser = userService.getUserById(getUser().getId());
        model.addAttribute("users",checkUser);
        return "addroom";
    }

    @PostMapping(value="/add")
    public String add(Model model,
                      @RequestParam(name = "room_name")String name,
                      @RequestParam(name = "country_id")Long country_id,
                      @RequestParam(name = "room_url")String url,
                      @RequestParam(name = "room_color")String color){
        Rooms room_url = roomService.getRoomByUrl(url);
        if(room_url!=null){
            return "redirect:/rooms/add?url";
        }
       if(name!=null&&country_id!=null){
           Rooms room = new Rooms();
           room.setName(name);
           Countries country = userService.getCountryByid(country_id);
           room.setCountry(country);
           List<Users> users = new ArrayList<>();
           Users user = userService.getUserById(getUser().getId());
           users.add(user);
           room.setUsers(users);
           room.setAuthor(user);
           room.setImage("noimage.jpg");
           room.setUrl(url);
           room.setColor(color);
           roomService.addRoom(room);
           return "redirect:/rooms/update/"+room.getUrl()+"?image";
       }else{
           return "redirect:/rooms/add?error";
       }
    }
    @PostMapping(value = "/removeuser")
    public String removeuser(@RequestParam(name = "room_id")Long id){
        Rooms room = roomService.getRoomById(id);
        Users checkUser = userService.getUserById(getUser().getId());
        if(room.getUsers().contains(checkUser)){
            room.getUsers().remove(checkUser);
            roomService.saveRoom(room);
            return "redirect:/rooms/";
        }else{
            return "redirect:/rooms/?error";
        }
    }

    @PostMapping(value = "/adduser")
    public String adduser(@RequestParam(name = "room_id")Long id){
        Rooms room = roomService.getRoomById(id);
        Users checkuser = userService.getUserById(getUser().getId());
        if(!room.getUsers().contains(checkuser)){
            room.getUsers().add(checkuser);
            roomService.saveRoom(room);
            return "redirect:/rooms/";
        }else{
            return "redirect:/rooms/?error";
        }
    }

    @GetMapping(value = "/room/{room_id}")
    public String roomroom(Model model,@PathVariable(name = "room_id")Long id){
        Rooms room = roomService.getRoomById(id);
        if(room!=null) {
            Users checkUser = userService.getUserById(getUser().getId());
            model.addAttribute("room", room);
            model.addAttribute("users", checkUser);
            return "room_chat";
        }else{
            return "redirect:/rooms/?error";
        }
    }


    @PostMapping(value = "/delete")
    public String deletroom(@RequestParam(name = "room_id")Long id){
        Rooms room = roomService.getRoomById(id);
        if(room!=null){
            roomService.deleteRoom(id);
            return "redirect:/rooms/my";
        }else{
            return "redirect:/rooms/my?error";
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
