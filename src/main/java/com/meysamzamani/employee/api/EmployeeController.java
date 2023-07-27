package com.meysamzamani.employee.api;

import com.meysamzamani.employee.dto.EmployeeDTO;
import com.meysamzamani.employee.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1.0/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<EmployeeDTO> getEmployees() {
        return employeeService.getEmployees().stream()
                .map(EmployeeDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public EmployeeDTO registerNewEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return EmployeeDTO.convertToDTO(employeeService.createEmployee(EmployeeDTO.convertToEntity(employeeDTO)));
    }

    @DeleteMapping(path = "{employeeID}")
    public void deleteEmployee(@PathVariable("employeeID") UUID employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    @PutMapping(path = "{employeeID}")
    public void updateEmployee(@PathVariable("employeeID") UUID employeeId,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String birthDate) {
        employeeService.updateEmployee(employeeId, firstName, lastName, email, birthDate);
    }
}
