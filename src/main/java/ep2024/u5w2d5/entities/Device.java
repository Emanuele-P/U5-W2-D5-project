package ep2024.u5w2d5.entities;

import ep2024.u5w2d5.enums.DeviceAvailability;
import ep2024.u5w2d5.enums.DeviceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Device {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceAvailability availability;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public Device(DeviceType type, DeviceAvailability availability, Employee employee) {
        this.type = type;
        this.availability = availability;
        this.employee = employee;
    }
}
