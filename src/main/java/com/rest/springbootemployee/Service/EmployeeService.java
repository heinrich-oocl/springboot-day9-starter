package com.rest.springbootemployee.Service;

import com.rest.springbootemployee.Models.Employee;
import com.rest.springbootemployee.Repository.EmployeeMongoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {// SUT

    private EmployeeMongoRepository employeeMongoRepository;

    public EmployeeService(EmployeeMongoRepository employeeMongoRepository) {
        this.employeeMongoRepository = employeeMongoRepository;
    }

    // 1. verify interaction
        // when EmployeeService.findAll is called, it will call employeeRepository.findAll()
    // 2. verify data
        // return the data get from employeeRepository.findAll() without any change.
    public List<Employee> findAll() {
        return employeeMongoRepository.findAll();
    }

    // 1. verify interaction
        // when EmployeeService.update is called, it will call employeeRepository.findById(id)
    // 2. verify data
        // when input an employee, only the age and salary will be changed, name and gender will not change.
    public Employee update(String id, Employee employee) {
        Employee existingEmployee = this.findById(id);
        if (employee.getAge() != null) {
            existingEmployee.setAge(employee.getAge());
        }
        if (employee.getSalary() != null) {
            existingEmployee.setSalary(employee.getSalary());
        }
        employeeMongoRepository.save(existingEmployee);
        return existingEmployee;
    }


    public Employee findById(String id) {
        return employeeMongoRepository.findById(id).get();
    }

    public List<Employee> findByGender(String gender) {
        return employeeMongoRepository.findByGender(gender);
    }

    public List<Employee> findByPage(int page, int pageSize) {
        PageRequest pageable = PageRequest.of(page-1, pageSize);
        return employeeMongoRepository.findAll(pageable).toList();
    }

    public void delete(String id) {
        employeeMongoRepository.deleteById(id);
    }

    public Employee create(Employee employee) {
        return employeeMongoRepository.save(employee);
    }
}
