package ep2024.u5w2d5.services;

import ep2024.u5w2d5.entities.Employee;
import ep2024.u5w2d5.exceptions.BadRequestException;
import ep2024.u5w2d5.exceptions.NotFoundException;
import ep2024.u5w2d5.repositories.EmployeesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeesService {
    @Autowired
    private EmployeesDAO ed;

    public Employee save(Employee body) {
        this.ed.findByEmail(body.getEmail()).ifPresent(
                employee -> {
                    throw new BadRequestException("The email: " + body.getEmail() + " is already in use!");
                }
        );
        this.ed.findByUsername(body.getUsername()).ifPresent(
                employee -> {
                    throw new BadRequestException("The username: " + body.getUsername() + " is already in use!");
                }
        );

        Employee newEmployee = new Employee(body.getUsername(), body.getFirstName(), body.getLastName(), body.getEmail(), body.getAvatarURL());
        newEmployee.setAvatarURL("https://ui-avatars.com/api/?name=" + newEmployee.getFirstName() + '+' + newEmployee.getLastName());
        return ed.save(newEmployee);
    }

    public Employee findById(UUID employeeId) {
        return this.ed.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
    }

    public void findByIdAndDelete(UUID employeeId) {
        Employee found = this.findById(employeeId);
        this.ed.delete(found);
    }


}
