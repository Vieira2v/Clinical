package clinical.resource.repositories;

import clinical.resource.repositories.model.CompletedHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentHistoryRepository extends JpaRepository<CompletedHistoryEntity, Long> {

    @Query("SELECT cs FROM CompletedHistoryEntity cs WHERE cs.dateTime >= :startDate AND cs.dateTime <= :endDate")
    List<CompletedHistoryEntity> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
