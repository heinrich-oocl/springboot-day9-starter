package com.rest.springbootemployee;

import com.rest.springbootemployee.Models.Employee;
import com.rest.springbootemployee.Repository.EmployeeMongoRepository;
import com.rest.springbootemployee.Service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeMongoRepository employeeMongoRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employees_when_find_all_given_employees() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee("Susan", 22, "Female", 10000);
        employees.add(employee);

        when(employeeMongoRepository.findAll()).thenReturn(employees);

        //when
        List<Employee> result = employeeService.findAll();

        //then
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(employee));
        verify(employeeMongoRepository).findAll();

    }

    @Test
    void should_update_only_age_and_salary_when_update_all_given_employees() {
        //given
        String employeeId = "1";
        Employee employee = new Employee("Susan", 22, "Female", 10000);
        Employee toUpdateEmployee = new Employee("Tom", 23, "Male", 12000);

        when(employeeMongoRepository.findById(employeeId)).thenReturn(java.util.Optional.of(employee));

        //when
        Employee updatedEmployee = employeeService.update(employeeId, toUpdateEmployee);

        //then
        verify(employeeMongoRepository).findById(employeeId);
        assertThat(updatedEmployee.getAge(), equalTo(23));
        assertThat(updatedEmployee.getSalary(), equalTo(12000));
        assertThat(updatedEmployee.getName(), equalTo("Susan"));
        assertThat(updatedEmployee.getGender(), equalTo("Female"));

    }

    @Test
    void should_return_employee_when_find_by_id_given_employee() {
        // given
        String employeeId = "1";
        Employee employee = new Employee("Susan", 22, "Female", 7000);
        given(employeeMongoRepository.findById(employeeId)).willReturn(java.util.Optional.of(employee));

        // when
        Employee result = employeeService.findById(employeeId);

        // should
        verify(employeeMongoRepository).findById(employeeId);
        assertThat(result, equalTo(employee));
    }

    @Test
    void should_return_employees_when_find_by_gender_given_employees() {
        // given
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee("Susan", 22, "Female", 7000);
        Employee employee2 = new Employee("Lisa", 20, "Female", 7000);
        Employee employee3 = new Employee("Jim", 21, "Male", 7000);

        String gender = "Female";
        given(employeeMongoRepository.findAll()).willReturn(employees);

        // when
        List<Employee> result = employeeService.findByGender(gender);

        // should
        verify(employeeMongoRepository).findByGender(gender);
        assertThat(result, equalTo(employees));
    }

    @Test
    void should_return_employees_when_find_by_page_given_employees() {
        // given
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee("Susan", 22, "Female", 7000);
        Employee employee2 = new Employee("Lisa", 20, "Female", 7000);

        employees.add(employee1);
        employees.add(employee2);

        int page = 1;
        int pageSize = 2;
        PageRequest pageable = PageRequest.of(page-1, pageSize);
        given(employeeMongoRepository.findAll(pageable)).willReturn(new PageImpl(employees));

        // when
        List<Employee> result = employeeService.findByPage(page, pageSize);

        // should
        verify(employeeMongoRepository).findAll(pageable);
        assertThat(result, equalTo(employees));
    }

    @Test
    void should_call_delete_with_specific_id_when_delete_given_an_id() {
        // given
        String employeeId = "1";

        // when
        employeeService.delete(employeeId);

        // should
        verify(employeeMongoRepository).deleteById(employeeId);
    }

    @Test
    void should_call_create_with_specific_employee_when_create_given_an_employee() {
        // given
        Employee employee = new Employee("Susan", 22, "Female", 7000);
        Employee createdEmployee = new Employee("Susan", 22, "Female", 7000);

        given(employeeMongoRepository.save(employee)).willReturn(createdEmployee);

        // when
        Employee result = employeeService.create(employee);

        // should
        verify(employeeMongoRepository).save(employee);
        assertThat(result, equalTo(createdEmployee));
    }
}
