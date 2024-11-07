package clinical.controller;

import clinical.controller.request.DateRequest;
import clinical.domain.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/clinical/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @SuppressWarnings("rawtypes")
    @GetMapping("/report/consultations")
    public ResponseEntity consultations(@RequestBody DateRequest request) {
        return ResponseEntity.ok(adminService.reportOfAllConsultations(request.getStart(), request.getEnd()));
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/cancelled/appointments")
    public ResponseEntity cancelledAppointments() {
        return ResponseEntity.ok(adminService.listAllCancelledConsultations());
    }
}
