package clinical.domain.service;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.response.SchedulesResponse;
import clinical.resource.repositories.ConsultationScheduleRepository;
import clinical.resource.repositories.PermissionEntityRepository;
import clinical.resource.repositories.UserEntityRepository;
import clinical.resource.repositories.model.ConsultationSchedule;
import clinical.resource.repositories.model.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationsService {

    @Autowired
    PermissionEntityRepository permissionEntityRepository;

    @Autowired
    ConsultationScheduleRepository consultationScheduleRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;

    public List<String> listOfDoctors() {
        Permissions permissions = new Permissions();
        permissions.setId(2L);
        return permissionEntityRepository.findUserFullnamesByPermissionId(permissions.getId());
    }

    public List<SchedulesResponse> availableSchedulesForDoctor(Long doctorId) {
        List<ConsultationSchedule> schedules = consultationScheduleRepository.findByDoctorIdAndIsAvailableTrue(doctorId);

        return DozerMapper.parseListObjects(schedules, SchedulesResponse.class);
    }

    public boolean reserveSchedule(Long scheduleId) {
        Optional<ConsultationSchedule> consultationSchedule = consultationScheduleRepository.findById(scheduleId);
        if (consultationSchedule.isPresent()) {
            ConsultationSchedule consultationScheduleEntity = consultationSchedule.get();
            if (consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setAvailable(false);
                consultationScheduleRepository.save(consultationScheduleEntity);
                return true;
            }
        }
        return false;
    }

    public boolean cancelSchedule(Long scheduleId) {
        Optional<ConsultationSchedule> consultationSchedule = consultationScheduleRepository.findById(scheduleId);
        if (consultationSchedule.isPresent()) {
            ConsultationSchedule consultationScheduleEntity = consultationSchedule.get();
            if (!consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setAvailable(true);
                consultationScheduleRepository.save(consultationScheduleEntity);
                return true;
            }
        }
        return false;
    }
}
