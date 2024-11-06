package clinical.controller;

import clinical.controller.request.SchedulesRequest;
import clinical.controller.response.SchedulesResponse;
import clinical.domain.service.ScheduleService;
import clinical.resource.repositories.ConsultationScheduleRepository;
import clinical.resource.repositories.model.ScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/clinical/schedules")
public class SchedulesController {

    @Autowired
    private ScheduleService consultationsService;

    @Autowired
    private ConsultationScheduleRepository consultationScheduleRepository;

    @SuppressWarnings("rawtypes")
    @GetMapping("/doctors")
    public ResponseEntity getAllDoctors() {
        return ResponseEntity.ok(consultationsService.listOfDoctors());
    }

    @GetMapping("/available/{doctorId}")
    public ResponseEntity<Object> getAvailableSchedules(@PathVariable("doctorId") Long doctorId) {
        List<SchedulesResponse> schedules = consultationsService.availableSchedulesForDoctor(doctorId);
        if (schedules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum horário disponível!");
        } else {
            return ResponseEntity.ok(schedules);
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/reserve/{scheduleId}")
    public ResponseEntity reserveSchedule(@PathVariable("scheduleId") Long scheduleId, @RequestBody SchedulesRequest request) {
        boolean success = consultationsService.reserveSchedule(scheduleId, request);
        if (success && request != null) {
            return ResponseEntity.ok("Horário reservado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Horário não disponível!");
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ScheduleEntity>> getConsultationsForDoctor(@PathVariable Long doctorId) {
        List<ScheduleEntity> consultations = consultationScheduleRepository.findByDoctorIdAndIsAvailableFalse(doctorId);

        if (consultations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(consultations);
    }
}
