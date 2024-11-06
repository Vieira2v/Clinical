package clinical.domain.service;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.SchedulesRequest;
import clinical.controller.response.SchedulesResponse;
import clinical.domain.CompletedHistoryDomain;
import clinical.domain.CancelledHistoryDomain;
import clinical.resource.repositories.*;
import clinical.resource.repositories.model.*;
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

    @Autowired
    HistoryCancelledRepository historyCancelledRepository;

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

    public ResponseEntity<SchedulesResponse> cancelSchedule(Long scheduleId, CancelledHistoryDomain request) {
        Optional<ScheduleEntity> consultationSchedule = consultationScheduleRepository.findById(scheduleId);
        SchedulesResponse response = new SchedulesResponse();

        if (consultationSchedule.isPresent()) {
            ScheduleEntity consultationScheduleEntity = consultationSchedule.get();

            if (!consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setAvailable(true);
                consultationScheduleEntity.setSituation("Cancelada");

                UserEntity patient = userEntityRepository.findById(request.getPatientId().getId())
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

                var transfer = DozerMapper.parseObject(consultationScheduleEntity, CancelledHistoryEntity.class);
                transfer.setPatientId(patient);

                transfer.setCommentFinal(request.getCommentFinal());
                transfer.setDateTime(LocalDateTime.now());

                historyCancelledRepository.save(transfer);
                consultationScheduleRepository.delete(consultationScheduleEntity);

                response.setDateTime(transfer.getDateTime());
                response.setSituation(transfer.getCommentFinal());
                return ResponseEntity.ok(response);
            }
        }
        response.setSituation("Não foi possível cancelar a consulta: agendamento inválido ou já concluído");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    public ResponseEntity<SchedulesResponse> completedStatusConsultation(Long scheduleId, CompletedHistoryDomain request) {
        Optional<ScheduleEntity> consultationScheduleOpt = consultationScheduleRepository.findById(scheduleId);
        SchedulesResponse response = new SchedulesResponse();

        if (consultationScheduleOpt.isPresent()) {
            ScheduleEntity consultationScheduleEntity = consultationScheduleOpt.get();

            if (!consultationScheduleEntity.isAvailable()) {
                consultationScheduleEntity.setSituation("Concluída");

                UserEntity patient = userEntityRepository.findById(request.getPatientId().getId())
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

                var transfer = DozerMapper.parseObject(consultationScheduleEntity, CompletedHistoryEntity.class);
                transfer.setPatientId(patient);

                transfer.setCommentFinal(request.getCommentFinal());
                transfer.setDateTime(LocalDateTime.now());

                appointmentHistoryRepository.save(transfer);
                consultationScheduleRepository.delete(consultationScheduleEntity);

                response.setDateTime(transfer.getDateTime());
                response.setSituation(transfer.getSituation());

                return ResponseEntity.ok(response);
            }
        }

        response.setSituation("Não foi possível concluir a consulta: agendamento inválido ou já concluído");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
