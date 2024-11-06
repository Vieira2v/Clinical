package clinical.domain.service;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.response.CompletedHistoryResponse;
import clinical.controller.response.SchedulesResponse;
import clinical.resource.repositories.AppointmentHistoryRepository;
import clinical.resource.repositories.model.CompletedHistoryEntity;
import clinical.resource.repositories.model.ScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    AppointmentHistoryRepository appointmentHistoryRepository;

    public List<CompletedHistoryResponse> reportOfAllConsultations(LocalDateTime start, LocalDateTime end) {
        List<CompletedHistoryEntity> schedules = appointmentHistoryRepository.findByDateRange(start, end);

        return DozerMapper.parseListObjects(schedules, CompletedHistoryResponse.class);
    }
}
