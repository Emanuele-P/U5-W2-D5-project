package ep2024.u5w2d5.controllers;

import ep2024.u5w2d5.entities.Device;
import ep2024.u5w2d5.exceptions.BadRequestException;
import ep2024.u5w2d5.payloads.DeviceDTO;
import ep2024.u5w2d5.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    //1. GET all http://localhost:3001/devices
    @GetMapping
    public Page<Device> getAllDevices(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        return deviceService.getDevices(page, size, sortBy);
    }

    //2. POST http://localhost:3001/devices(+body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Device save(@RequestBody @Validated DeviceDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return deviceService.save(body);
    }

    //3. GET one http://localhost:3001/devices/{id}
    @GetMapping("/{id}")
    public Device findDeviceById(@PathVariable UUID id) {
        return deviceService.findById(id);
    }

    //4. PUT http://localhost:3001/employees/{id} (+body)
    @PutMapping("/{id}")
    public Device findDeviceByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated DeviceDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return deviceService.findByIdAndUpdate(id, body);
    }

    //5. DELETE http://localhost:3001/devices/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findDevicesByIdAndDelete(@PathVariable UUID id) {
        deviceService.findByIdAndDelete(id);
    }

    //6. ASSIGN DEVICE http://localhost:3001/devices/{deviceId}/assign/{employeeId}}
    @PutMapping("/{deviceId}/assign/{employeeId}")
    public Device assignDeviceToEmployee(@PathVariable UUID deviceId, @PathVariable UUID employeeId) {
        return deviceService.assignDevice(deviceId, employeeId);
    }

    // 7. GET all assigned devices http://localhost:3001/devices/assigned
    @GetMapping("/assigned")
    public List<Device> getAllAssignedDevices() {
        return deviceService.getAllAssignedDevices();
    }
}
