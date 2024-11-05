package clinical.resource.repositories.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cancelled_appointments")
@Entity
public class HistoryCancelledAppointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnore
    private UserEntity doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private UserEntity patientId;

    @Column(nullable = false, name = "date_time")
    private LocalDateTime dateTime;

    @Column(nullable = false, name = "reason_for_cancellation")
    private String reasonForCancellation;
}
