package clinical.domain.service;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.response.CancelledHistoryResponse;
import clinical.controller.response.CompletedHistoryResponse;
import clinical.resource.repositories.AppointmentHistoryRepository;
import clinical.resource.repositories.HistoryCancelledRepository;
import clinical.resource.repositories.model.CancelledHistoryEntity;
import clinical.resource.repositories.model.CompletedHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    AppointmentHistoryRepository appointmentHistoryRepository;

    @Autowired
    HistoryCancelledRepository historyCancelledRepository;

    public List<CompletedHistoryResponse> reportOfAllConsultations(LocalDateTime start, LocalDateTime end) {
        List<CompletedHistoryEntity> schedules = appointmentHistoryRepository.findByDateRange(start, end);

        //Conversão de uma lista de objetos CompletedHistoryEntity para uma lista
        // de objetos CompletedHistoryResponse.
        return schedules.stream().map(this::convertToCompletedResponse).toList();
    }

    private CompletedHistoryResponse convertToCompletedResponse(CompletedHistoryEntity entity) {
        CompletedHistoryResponse response = new CompletedHistoryResponse();

        if (entity.getDoctor() != null) {
            response.setDoctorName(entity.getDoctor().getFullName());
        } else {
            response.setDoctorName(null);
        }

        if (entity.getPatientId() != null) {
            response.setPatientName(entity.getPatientId().getFullName());
        } else {
            response.setPatientName(null);
        }

        response.setDateTime(entity.getDateTime());
        response.setReason(entity.getReason());
        response.setCommentFinal(entity.getCommentFinal());
        return response;
    }

    public List<CancelledHistoryResponse> listAllCancelledConsultations() {
        List<CancelledHistoryEntity> schedules = historyCancelledRepository.findAll();
        return schedules.stream().map(this::convertToCompletedResponse).toList();
    }

    private CancelledHistoryResponse convertToCompletedResponse(CancelledHistoryEntity entity) {
        CancelledHistoryResponse response = new CancelledHistoryResponse();

        if (entity.getDoctor() != null) {
            response.setDoctor(entity.getDoctor().getFullName());
        } else {
            response.setDoctor(null);
        }

        if (entity.getPatientId() != null) {
            response.setPatientName(entity.getPatientId().getFullName());
        } else {
            response.setPatientName(null);
        }

        response.setDateTime(entity.getDateTime());
        response.setCommentFinal(entity.getCommentFinal());
        return response;
    }
}
