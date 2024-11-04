package clinical.domain.service;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.SchedulesRequest;
import clinical.controller.response.SchedulesResponse;
import clinical.domain.Schedule;
import clinical.resource.repositories.AppointmentHistoryRepository;
import clinical.resource.repositories.ConsultationScheduleRepository;
import clinical.resource.repositories.PermissionEntityRepository;
import clinical.resource.repositories.model.AppointmentHistory;
import clinical.resource.repositories.model.ScheduleEntity;
import clinical.resource.repositories.model.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    PermissionEntityRepository permissionEntityRepository;

    @Autowired
    ConsultationScheduleRepository consultationScheduleRepository;

    @Autowired
    AppointmentHistoryRepository appointmentHistoryRepository;

    public List<String> listOfDoctors() {
        Permissions permissions = new Permissions();
        permissions.setId(3L);
        return permissionEntityRepository.findUserFullnamesByPermissionId(permissions.getId());
    }

    public List<SchedulesResponse> availableSchedulesForDoctor(Long doctorId) {
        List<ScheduleEntity> schedules = consultationScheduleRepository.findByDoctorIdAndIsAvailableTrue(doctorId);

        return DozerMapper.parseListObjects(schedules, SchedulesResponse.class);
    }

    public boolean reserveSchedule(Long scheduleId, SchedulesRequest request) {
        Optional<ScheduleEntity> consultationSchedule = consultationScheduleRepository.findById(scheduleId);
        if (consultationSchedule.isPresent()) {
            ScheduleEntity consultationScheduleEntity = consultationSchedule.get();
            if (consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setAvailable(false);
                consultationScheduleEntity.setReason(request.getReason());
                consultationScheduleEntity.setSituation("Aguardando");
                consultationScheduleRepository.save(consultationScheduleEntity);
                return true;
            }
        }
        return false;
    }

    public boolean cancelSchedule(Long scheduleId) {
        Optional<ScheduleEntity> consultationSchedule = consultationScheduleRepository.findById(scheduleId);
        if (consultationSchedule.isPresent()) {
            ScheduleEntity consultationScheduleEntity = consultationSchedule.get();
            if (!consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setAvailable(true);
                consultationScheduleEntity.setSituation("Cancelada");
                consultationScheduleRepository.save(consultationScheduleEntity);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity completedStatusConsultation(Long scheduleId, AppointmentHistory request) {
        var entity = DozerMapper.parseObject(request, ScheduleEntity.class);
        Optional<ScheduleEntity> consultationSchedule = consultationScheduleRepository.findById(scheduleId);
        if (consultationSchedule.isPresent()) {
            ScheduleEntity consultationScheduleEntity = consultationSchedule.get();
            if (!consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setSituation(entity.getSituation());
                consultationScheduleRepository.save(consultationScheduleEntity);
                var response = DozerMapper.parseObject(consultationScheduleEntity, SchedulesResponse.class);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Horário não disponível!");
    }
}
