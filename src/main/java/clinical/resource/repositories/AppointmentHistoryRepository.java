package clinical.resource.repositories;

import clinical.resource.repositories.model.CompletedHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentHistoryRepository extends JpaRepository<CompletedHistoryEntity, Long> {

}
