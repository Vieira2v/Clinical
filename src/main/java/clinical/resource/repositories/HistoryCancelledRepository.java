package clinical.resource.repositories;

import clinical.resource.repositories.model.CancelledHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryCancelledRepository extends JpaRepository<CancelledHistoryEntity, Long> {
}
