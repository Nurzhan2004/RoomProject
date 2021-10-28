package nurzhan.Project.room.Repositories;

import nurzhan.Project.room.entities.Countries;
import nurzhan.Project.room.entities.Rooms;
import nurzhan.Project.room.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RoomRepository extends JpaRepository<Rooms,Long> {
    List<Rooms> findAllByUsers(Users user);
    List<Rooms> findAllByCountry(Countries country);
    List<Rooms> findAllByAuthor(Users user);
    Rooms findByUrl(String url);
}
