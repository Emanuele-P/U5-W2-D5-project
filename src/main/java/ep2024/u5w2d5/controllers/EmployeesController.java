package ep2024.u5w2d5.controllers;

import ep2024.u5w2d5.entities.Employee;
import ep2024.u5w2d5.exceptions.BadRequestException;
import ep2024.u5w2d5.payloads.NewEmployeeDTO;
import ep2024.u5w2d5.services.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    private EmployeesService employeesService;

    //1. GET all http://localhost:3001/employees
    @GetMapping
    public Page<Employee> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        return employeesService.getEmployees(page, size, sortBy);
    }

    //2. POST http://localhost:3001/employees (+body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee save(@RequestBody @Validated NewEmployeeDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return employeesService.save(body);
    }

    //3. GET one http://localhost:3001/employees/{id}
    @GetMapping("/{id}")
    public Employee findEmployeeById(@PathVariable UUID id) {
        return employeesService.findById(id);
    }

    //4. PUT http://localhost:3001/employees/{id} (+body)
    @PutMapping("/{id}")
    public Employee findEmployeeByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated NewEmployeeDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return employeesService.findByIdAndUpdate(id, body);
    }

    //5. DELETE http://localhost:3001/employees/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findEmployeeByIdAndDelete(@PathVariable UUID id) {
        employeesService.findByIdAndDelete(id);
    }


}
