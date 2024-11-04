package clinical.controller;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.DoctorAppointments;
import clinical.domain.Schedule;
import clinical.domain.service.ScheduleService;
import clinical.resource.repositories.ConsultationScheduleRepository;
import clinical.resource.repositories.model.ScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/clinical/doctor")
public class DoctorController {

    @Autowired
    private ScheduleService scheduleService;


/*
    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/check/schedules/{scheduleId}")
    public ResponseEntity checkSchedules(@PathVariable("scheduleId") Long scheduleId, @RequestBody DoctorAppointments situationRequest) {
        var request = DozerMapper.parseObject(situationRequest, Schedule.class);
        var edit = scheduleService.completedStatusConsultation(scheduleId, request);
        return ResponseEntity.ok(edit);
    }
 */

}
