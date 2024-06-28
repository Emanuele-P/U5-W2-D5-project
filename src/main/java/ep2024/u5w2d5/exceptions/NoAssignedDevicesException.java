package ep2024.u5w2d5.exceptions;

public class NoAssignedDevicesException extends RuntimeException {
    public NoAssignedDevicesException() {
        super("No devices are currently assigned.");
    }
}
