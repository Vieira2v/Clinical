package clinical.controller;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.DoctorAppointments;
import clinical.domain.CompletedConsultationsDomain;
import clinical.domain.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/clinical/doctor")
public class DoctorController {

    @Autowired
    private ScheduleService scheduleService;


    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/check/schedules/{scheduleId}")
    public ResponseEntity checkSchedules(@PathVariable("scheduleId") Long scheduleId, @RequestBody DoctorAppointments comentRequest) {
        var request = DozerMapper.parseObject(comentRequest, CompletedConsultationsDomain.class);
        var edit = scheduleService.completedStatusConsultation(scheduleId, request);
        return ResponseEntity.ok(edit);
    }

}
