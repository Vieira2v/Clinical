package clinical.resource.repositories;

import clinical.resource.repositories.model.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsultationScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    @Query("SELECT cs FROM ScheduleEntity cs WHERE cs.doctor.id = :doctorId AND cs.isAvailable = true")
    List<ScheduleEntity> findByDoctorIdAndIsAvailableTrue(@Param("doctorId") Long doctorId);

    @Query("SELECT cs FROM ScheduleEntity cs WHERE cs.doctor.id = :doctorId AND cs.isAvailable = false")
    List<ScheduleEntity> findByDoctorIdAndIsAvailableFalse(@Param("doctorId") Long doctorId);

}
