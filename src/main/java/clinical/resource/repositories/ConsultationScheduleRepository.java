package clinical.resource.repositories;

import clinical.resource.repositories.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsultationScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT cs FROM Schedule cs WHERE cs.doctor.id = :doctorId AND cs.isAvailable = true")
    List<Schedule> findByDoctorIdAndIsAvailableTrue(@Param("doctorId") Long doctorId);

    @Query("SELECT cs FROM Schedule cs WHERE cs.doctor.id = :doctorId AND cs.isAvailable = false")
    List<Schedule> findByDoctorIdAndIsAvailableFalse(@Param("doctorId") Long doctorId);

}
