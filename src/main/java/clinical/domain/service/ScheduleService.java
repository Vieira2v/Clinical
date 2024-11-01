package clinical.domain.service;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.DoctorAppointments;
import clinical.controller.request.SchedulesRequest;
import clinical.controller.response.SchedulesResponse;
import clinical.resource.repositories.ConsultationScheduleRepository;
import clinical.resource.repositories.PermissionEntityRepository;
import clinical.resource.repositories.model.Schedule;
import clinical.resource.repositories.model.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    PermissionEntityRepository permissionEntityRepository;

    @Autowired
    ConsultationScheduleRepository consultationScheduleRepository;

    public List<String> listOfDoctors() {
        Permissions permissions = new Permissions();
        permissions.setId(3L);
        return permissionEntityRepository.findUserFullnamesByPermissionId(permissions.getId());
    }

    public List<SchedulesResponse> availableSchedulesForDoctor(Long doctorId) {
        List<Schedule> schedules = consultationScheduleRepository.findByDoctorIdAndIsAvailableTrue(doctorId);

        return DozerMapper.parseListObjects(schedules, SchedulesResponse.class);
    }

    public boolean reserveSchedule(Long scheduleId, SchedulesRequest request) {
        Optional<Schedule> consultationSchedule = consultationScheduleRepository.findById(scheduleId);
        if (consultationSchedule.isPresent()) {
            Schedule consultationScheduleEntity = consultationSchedule.get();
            if (consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setAvailable(false);
                consultationScheduleEntity.setReason(request.getReason());
                consultationScheduleRepository.save(consultationScheduleEntity);
                return true;
            }
        }
        return false;
    }

    public boolean cancelSchedule(Long scheduleId) {
        Optional<Schedule> consultationSchedule = consultationScheduleRepository.findById(scheduleId);
        if (consultationSchedule.isPresent()) {
            Schedule consultationScheduleEntity = consultationSchedule.get();
            if (!consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setAvailable(true);
                consultationScheduleRepository.save(consultationScheduleEntity);
                return true;
            }
        }
        return false;
    }

//    public String currentStatusConsultation(DoctorAppointments request, Long doctorId) {
//
//    }
}
