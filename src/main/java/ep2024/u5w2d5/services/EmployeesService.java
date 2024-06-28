package ep2024.u5w2d5.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import ep2024.u5w2d5.entities.Employee;
import ep2024.u5w2d5.exceptions.BadRequestException;
import ep2024.u5w2d5.exceptions.NotFoundException;
import ep2024.u5w2d5.payloads.NewEmployeeDTO;
import ep2024.u5w2d5.repositories.EmployeesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class EmployeesService {
    @Autowired
    private EmployeesDAO employeesDAO;

    @Autowired
    private Cloudinary cloudinaryUploader;

    public Employee save(NewEmployeeDTO body) {
        employeesDAO.findByEmail(body.email()).ifPresent(
                employee -> {
                    throw new BadRequestException("The email: " + body.email() + " is already in use!");
                }
        );
        employeesDAO.findByUsername(body.username()).ifPresent(
                employee -> {
                    throw new BadRequestException("The username: " + body.username() + " is already in use!");
                }
        );

        Employee newEmployee = new Employee(body.username(), body.firstName(), body.lastName(), body.email(), body.avatarUrl());
        newEmployee.setAvatarURL("https://ui-avatars.com/api/?name=" + newEmployee.getFirstName() + '+' + newEmployee.getLastName());
        return employeesDAO.save(newEmployee);
    }

    public Employee findById(UUID employeeId) {
        return employeesDAO.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
    }

    public void findByIdAndDelete(UUID employeeId) {
        Employee found = this.findById(employeeId);
        employeesDAO.delete(found);
    }

    public Page<Employee> getEmployees(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return employeesDAO.findAll(pageable);
    }

    public Employee findByIdAndUpdate(UUID employeeId, NewEmployeeDTO updatedEmployee) {
        Employee found = this.findById(employeeId);
        found.setUsername(updatedEmployee.username());
        found.setFirstName(updatedEmployee.firstName());
        found.setLastName(updatedEmployee.lastName());
        found.setEmail(updatedEmployee.email());
        found.setAvatarURL(found.getAvatarURL());
        return employeesDAO.save(found);
    }

    public String uploadAvatar(MultipartFile file) throws IOException {
        return (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
    }

    public Employee updateAvatar(UUID employeeId, String url) {
        Employee employee = this.findById(employeeId);
        employee.setAvatarURL(url);
        return employeesDAO.save(employee);
    }
}
