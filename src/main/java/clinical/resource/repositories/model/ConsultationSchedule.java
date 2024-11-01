package clinical.resource.repositories.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "consultation_schedules")
public class ConsultationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnore
    private UserEntity doctor;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private boolean isAvailable = true;

    public ConsultationSchedule(UserEntity doctor, LocalDateTime dateTime) {
        this.doctor = doctor;
        this.dateTime = dateTime;
    }
}
