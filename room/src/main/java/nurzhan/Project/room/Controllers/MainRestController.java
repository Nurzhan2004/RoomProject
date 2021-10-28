package nurzhan.Project.room.Controllers;

import nurzhan.Project.room.Services.RoomService;
import nurzhan.Project.room.Services.UserService;
import nurzhan.Project.room.entities.Items;
import nurzhan.Project.room.entities.Rooms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value ="/api")
public class MainRestController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/getAllItems")
    public ResponseEntity<List<Items>> getAllRooms(){
        List<Items> items = userService.getAllItem();
        return new ResponseEntity<List<Items>>(items, HttpStatus.OK);
    }

    @PostMapping(value = "/addItems")
    public ResponseEntity<Items> addItem(@RequestBody Items item){
        return new ResponseEntity<Items>(userService.addItem(item),HttpStatus.OK);
    }
    @GetMapping(value = "/getItem/{id}")
    public ResponseEntity<Items> getItem(@PathVariable(name = "id")Long id){
        return new ResponseEntity<Items>(userService.getItemById(id),HttpStatus.OK);
    }

}
