package clinical.resource.repositories;

import clinical.resource.repositories.model.CompletedConsultationsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentHistoryRepository extends JpaRepository<CompletedConsultationsHistory, Long> {

}
