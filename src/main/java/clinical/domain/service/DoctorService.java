package clinical.domain.service;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.response.SchedulesResponse;
import clinical.domain.CancelledHistoryDomain;
import clinical.domain.CompletedHistoryDomain;
import clinical.domain.ScheduleDomain;
import clinical.resource.repositories.AppointmentHistoryRepository;
import clinical.resource.repositories.ConsultationScheduleRepository;
import clinical.resource.repositories.HistoryCancelledRepository;
import clinical.resource.repositories.UserEntityRepository;
import clinical.resource.repositories.model.CancelledHistoryEntity;
import clinical.resource.repositories.model.CompletedHistoryEntity;
import clinical.resource.repositories.model.ScheduleEntity;
import clinical.resource.repositories.model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    AppointmentHistoryRepository appointmentHistoryRepository;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    HistoryCancelledRepository historyCancelledRepository;

    @Autowired
    ConsultationScheduleRepository consultationScheduleRepository;

    @SuppressWarnings("rawtypes")
    public ResponseEntity createSchedule(ScheduleDomain schedule) {
        if (schedule == null) throw  new IllegalArgumentException();

        UserEntity doctor = userEntityRepository.findByUsername(schedule.getDoctor().getUsername());

        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Médico não encontrado");
        }

        var entity = DozerMapper.parseObject(schedule, ScheduleEntity.class);
        entity.setSituation("Disponível");
        entity.setReason("Nenhuma");
        entity.setAvailable(true);
        entity.setDoctor(doctor);
        entity.setDateTime(schedule.getDateTime());
        consultationScheduleRepository.save(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
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
