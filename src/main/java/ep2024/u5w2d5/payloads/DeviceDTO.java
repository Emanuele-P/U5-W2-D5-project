package ep2024.u5w2d5.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeviceDTO(
        @NotBlank(message = "Device type must not be empty!")
        String type,
        @NotBlank(message = "Device availability must not be empty!")
        String availability,
        @NotNull
        UUID employeeId
) {
}
