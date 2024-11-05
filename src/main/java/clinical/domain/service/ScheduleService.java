package clinical.domain.service;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.SchedulesRequest;
import clinical.controller.response.SchedulesResponse;
import clinical.domain.CompletedConsultationsDomain;
import clinical.resource.repositories.AppointmentHistoryRepository;
import clinical.resource.repositories.ConsultationScheduleRepository;
import clinical.resource.repositories.PermissionEntityRepository;
import clinical.resource.repositories.UserEntityRepository;
import clinical.resource.repositories.model.CompletedConsultationsHistory;
import clinical.resource.repositories.model.ScheduleEntity;
import clinical.resource.repositories.model.Permissions;
import clinical.resource.repositories.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    UserEntityRepository userEntityRepository;

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

    public ResponseEntity<SchedulesResponse> completedStatusConsultation(Long scheduleId, CompletedConsultationsDomain request) {
        Optional<ScheduleEntity> consultationScheduleOpt = consultationScheduleRepository.findById(scheduleId);
        SchedulesResponse response = new SchedulesResponse();

        if (consultationScheduleOpt.isPresent()) {
            ScheduleEntity consultationScheduleEntity = consultationScheduleOpt.get();

            if (!consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setSituation("Concluída");

                UserEntity patient = userEntityRepository.findById(request.getPatientId().getId())
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

                var transfer = DozerMapper.parseObject(consultationScheduleEntity, CompletedConsultationsHistory.class);
                transfer.setPatientId(patient);

                transfer.setCommentFinal(request.getCommentFinal());
                transfer.setDateTime(LocalDateTime.now());

                appointmentHistoryRepository.save(transfer);
                consultationScheduleRepository.delete(consultationScheduleEntity);

                response.setDateTime(transfer.getDateTime());
                response.setAvailable(true);
                response.setSituation(transfer.getSituation());

                return ResponseEntity.ok(response);
            }
        }

        response.setAvailable(false);
        response.setSituation("Horário não disponível!");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
