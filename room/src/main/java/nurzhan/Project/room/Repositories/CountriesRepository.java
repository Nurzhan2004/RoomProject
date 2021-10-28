package nurzhan.Project.room.Repositories;

import nurzhan.Project.room.entities.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CountriesRepository extends JpaRepository<Countries,Long> {

}
