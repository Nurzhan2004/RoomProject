package nurzhan.Project.room.Services;

import nurzhan.Project.room.entities.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    //countries
    List<Countries> getAllCountries();
    Countries getCountryByid(Long id);
    //roles
    List<Roles> getAllRoles();
    Roles getRoleById(Long id);
    //rooms

    //users
    List<Users> getAllUsers();
    Users getUserById(Long id);
    Users addUser(Users user);
    Users getUserByEmail(String email);
    Users getUserByUrl(String url);
    //items
    List<Items> getAllItem();
    Items getItemById(Long id);
    Items addItem(Items item);
}
