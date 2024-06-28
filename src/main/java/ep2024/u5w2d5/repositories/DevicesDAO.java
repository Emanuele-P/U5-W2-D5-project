package ep2024.u5w2d5.repositories;

import ep2024.u5w2d5.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DevicesDAO extends JpaRepository<Device, UUID> {
}
