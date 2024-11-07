package clinical.controller;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.CreateScheduleRequest;
import clinical.controller.request.DoctorAppointments;
import clinical.domain.CancelledHistoryDomain;
import clinical.domain.CompletedHistoryDomain;
import clinical.domain.ScheduleDomain;
import clinical.domain.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/clinical/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/create/appointment")
    public ResponseEntity createAppointment(@RequestBody CreateScheduleRequest request) {
        var domain = DozerMapper.parseObject(request, ScheduleDomain.class);
        var create = doctorService.createSchedule(domain);
        return ResponseEntity.ok(create);
    }


    @SuppressWarnings("rawtypes")
    @PutMapping("/check/schedules/{scheduleId}")
    public ResponseEntity checkSchedules(@PathVariable("scheduleId") Long scheduleId, @RequestBody DoctorAppointments comentRequest) {
        var request = DozerMapper.parseObject(comentRequest, CompletedHistoryDomain.class);
        var edit = doctorService.completedStatusConsultation(scheduleId, request);
        return ResponseEntity.ok(edit);
    }

    @SuppressWarnings("rawtypes")
    @PutMapping("/cancel/{scheduleId}")
    public ResponseEntity cancelSchedule(@PathVariable("scheduleId") Long scheduleId, @RequestBody DoctorAppointments request) {
        var reason = DozerMapper.parseObject(request, CancelledHistoryDomain.class);
        var cancelled = doctorService.cancelSchedule(scheduleId, reason);
        return ResponseEntity.ok(cancelled);
    }

}
