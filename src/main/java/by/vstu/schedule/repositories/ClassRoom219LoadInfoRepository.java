package by.vstu.schedule.repositories;

import by.vstu.schedule.models.entities.ClassRoom219LoadInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ClassRoom219LoadInfoRepository extends JpaRepository<ClassRoom219LoadInfo, Integer> {

    @Query("select t from ClassRoom219LoadInfo t where t.localDate >= :startDate and t.localDate <= :endDate")
    List<ClassRoom219LoadInfo> findClassRoom219LoadsInfoByLocalDateAfterAndLocalDateBefore(@Param("startDate") LocalDate startDate,
                                                                                                  @Param("endDate") LocalDate endDate);

    List<ClassRoom219LoadInfo> findClassRoom219LoadsInfoByLocalDate(LocalDate localDate);
}
