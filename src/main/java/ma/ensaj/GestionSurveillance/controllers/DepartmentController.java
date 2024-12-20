package ma.ensaj.GestionSurveillance.controllers;

import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.repositories.DepartmentRepository;
import ma.ensaj.GestionSurveillance.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        return department.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value="/")
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Optional<Department> existingDepartment = departmentService.getDepartmentById(id);
        if (existingDepartment.isPresent()) {
            department.setId(id);
            return ResponseEntity.ok(departmentService.saveDepartment(department));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        Optional<Department> existingDepartment = departmentService.getDepartmentById(id);
        if (existingDepartment.isPresent()) {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/count", produces = { "application/json", "application/xml" })
    public Long countDepartments() {
        return departmentService.countDepartments();
    }
}