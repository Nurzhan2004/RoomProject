package nurzhan.Project.room.Services;

import nurzhan.Project.room.entities.Countries;
import nurzhan.Project.room.entities.Rooms;
import nurzhan.Project.room.entities.Users;

import java.util.List;

public interface RoomService {
    List<Rooms> getAllRooms();
    Rooms getRoomById(Long id);
    List<Rooms> getAllByRoomUser(Users user);
    List<Rooms> getAllByRoomCountry(Countries countries);
    Rooms addRoom(Rooms room);
    void deleteRoom(Long id);
    List<Rooms> getAllByAuthorRoom(Users user);
    Rooms saveRoom(Rooms room);
    Rooms getRoomByUrl(String url);

}
