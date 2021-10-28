package nurzhan.Project.room.Services.impl;

import nurzhan.Project.room.Repositories.*;
import nurzhan.Project.room.Services.UserService;
import nurzhan.Project.room.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Countries> getAllCountries() {
        return countriesRepository.findAll();
    }

    @Override
    public Countries getCountryByid(Long id) {
        return countriesRepository.findById(id).orElse(null);
    }

    @Override
    public Users addUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }



    @Override
    public Roles getRoleById(Long id) {
        return rolesRepository.findById(id).orElse(null);
    }



    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users getUserByUrl(String url) {
        return userRepository.findByUrl(url);
    }

    @Override
    public List<Items> getAllItem() {
        return itemRepository.findAll();
    }

    @Override
    public Items getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
    public Items addItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if(user!=null){
            return user;
        }else {
            throw new UsernameNotFoundException("not found exception");
        }
    }
}
