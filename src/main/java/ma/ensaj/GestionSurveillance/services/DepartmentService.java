package ma.ensaj.GestionSurveillance.services;

import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;



    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public Long countDepartments() {
        return departmentRepository.count();
    }
    public List<Department> importDepartmentsFromCsv(MultipartFile file) {
        List<Department> departments = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            reader.readNext(); // Skip header line
            while ((nextLine = reader.readNext()) != null) {
                Department department = new Department();
                department.setNom(nextLine[0]); // Assuming the CSV only contains the department name
                departments.add(department);
            }
            departmentRepository.saveAll(departments);
        } catch (Exception e) {
            throw new RuntimeException("Error importing departments from CSV: " + e.getMessage());
        }
        return departments;
    }
}