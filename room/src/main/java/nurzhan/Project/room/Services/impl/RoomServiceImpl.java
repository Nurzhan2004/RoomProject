package nurzhan.Project.room.Services.impl;
import nurzhan.Project.room.Repositories.RoomRepository;
import nurzhan.Project.room.Services.RoomService;
import nurzhan.Project.room.entities.Countries;
import nurzhan.Project.room.entities.Rooms;
import nurzhan.Project.room.entities.Users;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Rooms> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Rooms getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public List<Rooms> getAllByRoomUser(Users user) {
        return roomRepository.findAllByUsers(user);
    }

    @Override
    public List<Rooms> getAllByRoomCountry(Countries countries) {
        return roomRepository.findAllByCountry(countries);
    }

    @Override
    public Rooms addRoom(Rooms room) {
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long id) {
        Rooms room = getRoomById(id);
        roomRepository.delete(room);
    }

    @Override
    public List<Rooms> getAllByAuthorRoom(Users user) {
        return roomRepository.findAllByAuthor(user);
    }

    @Override
    public Rooms saveRoom(Rooms room) {
        return roomRepository.save(room);
    }

    @Override
    public Rooms getRoomByUrl(String url) {
        return roomRepository.findByUrl(url);
    }
}
