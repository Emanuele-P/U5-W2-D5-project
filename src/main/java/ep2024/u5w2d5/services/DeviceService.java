package ep2024.u5w2d5.services;

import ep2024.u5w2d5.entities.Device;
import ep2024.u5w2d5.entities.Employee;
import ep2024.u5w2d5.enums.DeviceAvailability;
import ep2024.u5w2d5.enums.DeviceType;
import ep2024.u5w2d5.exceptions.BadRequestException;
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

import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private DevicesDAO devicesDAO;

    @Autowired
    private EmployeesDAO employeesDAO;

    public Device save(DeviceDTO body) {
        Employee employee = employeesDAO.findById(body.employeeId()).orElseThrow(() -> new NotFoundException(body.employeeId()));

        DeviceType type;
        try {
            type = DeviceType.valueOf(body.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device type: " + body.type());
        }

        DeviceAvailability availability;
        try {
            availability = DeviceAvailability.valueOf(body.availability().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device type: " + body.availability());
        }

        Device newDevice = new Device(type, availability, employee);
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
        Employee employee = employeesDAO.findById(updatedDevice.employeeId()).orElseThrow(() -> new NotFoundException(updatedDevice.employeeId()));

        DeviceType type;
        try {
            type = DeviceType.valueOf(updatedDevice.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device type: " + updatedDevice.type());
        }

        DeviceAvailability availability;
        try {
            availability = DeviceAvailability.valueOf(updatedDevice.availability().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid device type: " + updatedDevice.availability());
        }

        found.setType(type);
        found.setAvailability(availability);
        found.setEmployee(employee);
        return devicesDAO.save(found);
    }
}
