package clinical.resource.repositories;

import clinical.resource.repositories.model.ConsultationSchedule;
import clinical.resource.repositories.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsultationScheduleRepository extends JpaRepository<ConsultationSchedule, Long> {
    @Query("SELECT cs FROM ConsultationSchedule cs WHERE cs.doctor.id = :doctorId AND cs.isAvailable = true")
    List<ConsultationSchedule> findByDoctorIdAndIsAvailableTrue(@Param("doctorId") Long doctorId);

    @Query("SELECT cs FROM ConsultationSchedule cs WHERE cs.doctor.id = :doctorId AND cs.isAvailable = false")
    List<ConsultationSchedule> findByDoctorIdAndIsAvailableFalse(@Param("doctorId") Long doctorId);

}
