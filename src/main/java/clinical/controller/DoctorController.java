package clinical.controller;

import clinical.controller.request.DoctorAppointments;
import clinical.resource.repositories.model.Schedule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/clinical/doctor")
public class DoctorController {

//    @PostMapping(value = "/check/schedules")
//    public ResponseEntity checkSchedules(@RequestBody DoctorAppointments request) {
//
//    }
}
