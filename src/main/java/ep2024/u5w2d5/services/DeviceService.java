package ep2024.u5w2d5.services;

import ep2024.u5w2d5.entities.Device;
import ep2024.u5w2d5.entities.Employee;
import ep2024.u5w2d5.enums.DeviceAvailability;
import ep2024.u5w2d5.enums.DeviceType;
import ep2024.u5w2d5.exceptions.BadRequestException;
import ep2024.u5w2d5.exceptions.DeviceAlreadyAssignedException;
import ep2024.u5w2d5.exceptions.NoAssignedDevicesException;
import ep2024.u5w2d5.exceptions.NotFoundException;
import ep2024.u5w2d5.payloads.DeviceDTO;
import ep2024.u5w2d5.repositories.DevicesDAO;
import ep2024.u5w2d5.repositories.EmployeesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private DevicesDAO devicesDAO;

    @Autowired
    private EmployeesDAO employeesDAO;

    public Device save(DeviceDTO body) {
        DeviceType type;
        try {
            type = DeviceType.valueOf(body.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device type: " + body.type());
        }

        DeviceAvailability availability;
        try {
            availability = DeviceAvailability.valueOf(body.availability().toUpperCase());
            if (availability == DeviceAvailability.ASSIGNED) {
                throw new BadRequestException("Devices cannot be created with ASSIGNED status");
            }
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device type: " + body.availability());
        }

        Device newDevice = new Device(type, availability);
        return devicesDAO.save(newDevice);
    }

    public Device findById(UUID deviceId) {
        return devicesDAO.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
    }

    public void findByIdAndDelete(UUID deviceId) {
        Device found = this.findById(deviceId);
        devicesDAO.delete(found);
    }

    public Page<Device> getDevices(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return devicesDAO.findAll(pageable);
    }

    public Device findByIdAndUpdate(UUID deviceID, DeviceDTO updatedDevice) {
        Device found = this.findById(deviceID);

        DeviceType type;
        try {
            type = DeviceType.valueOf(updatedDevice.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device type: " + updatedDevice.type());
        }

        DeviceAvailability newAvailability;
        try {
            newAvailability = DeviceAvailability.valueOf(updatedDevice.availability().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device availability: " + updatedDevice.availability());
        }

        if (newAvailability == DeviceAvailability.AVAILABLE && found.getAvailability() == DeviceAvailability.ASSIGNED) {
            if (found.getEmployee() != null) {
                found.setEmployee(null);
            }
        }

        found.setType(type);
        found.setAvailability(newAvailability);
        return devicesDAO.save(found);
    }

    public Device assignDevice(UUID deviceId, UUID employeeId) {
        Device device = this.findById(deviceId);
        if (device.getAvailability() == DeviceAvailability.ASSIGNED) {
            throw new DeviceAlreadyAssignedException(device.getEmployee().getId());
        }
        if (device.getAvailability() == DeviceAvailability.DECOMMISSIONED || device.getAvailability() == DeviceAvailability.UNDER_MAINTENANCE) {
            throw new BadRequestException("This device cannot be assigned at the moment! We are sorry for the inconvenience.");
        }

        Employee employee = employeesDAO.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
        device.setEmployee(employee);
        device.setAvailability(DeviceAvailability.ASSIGNED);
        return devicesDAO.save(device);
    }

    public List<Device> getAllAssignedDevices() {
        List<Device> assignedDevices = devicesDAO.findAllByAvailability(DeviceAvailability.ASSIGNED);
        if (assignedDevices.isEmpty()) {
            throw new NoAssignedDevicesException();
        }
        return assignedDevices;
    }
}
