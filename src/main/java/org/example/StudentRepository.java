package org.example;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StudentRepository {
    private static final Logger log = Logger.getLogger(StudentRepository.class.getName());
    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) throws ValidationException{
        Student validStudent = Optional.ofNullable(student)
                .orElseThrow(() -> {
                    log.log(Level.SEVERE, "Validation failed! Student is null");
                    return new ValidationException("Student is null");
                });

    validateStudent(validStudent);
    Optional<Student> duplicate = students.stream()
            .filter(s-> s.getId().trim().equals(validStudent.getId().trim()))
            .findFirst();
    Optional.of(duplicate)
            .filter(Optional::isEmpty)
            .orElseThrow(() ->{
                log.log(Level.WARNING, "Student with id " + validStudent.getId() + " already exists");
                return new ValidationException("Student with id " + validStudent.getId() + " already exists");
            });

    students.add(validStudent);
    log.log(Level.INFO, "Student with id " + validStudent.getId() + " has been added");
    }


    private void validateStudent (Student student) throws ValidationException{
        int currentYear = LocalDate.now().getYear();
        record ValidationRule (Predicate<Student> isInvalid, String errorMessage){}

        Optional<ValidationRule> error = Stream.of(

                new ValidationRule(s-> s.getName()== null || s.getName().trim().isEmpty(),
                        "Student name can't be empty!"),
                new ValidationRule(s-> s.getLastName()== null || s.getLastName().trim().isEmpty(),
                        "Student name can't be empty!"),
                new ValidationRule(s-> s.getId()== null || s.getId().trim().isEmpty(),
                        "Student Id(CNP) can't be empty!"),
                new ValidationRule(s-> s.getDateOfBirth()== null || s.getDateOfBirth().getYear()< 1900|| s.getDateOfBirth().getYear()>(currentYear-18),
                        "Student dat of birth must be between 1900 ant "+(currentYear-18)),
                new ValidationRule( s-> s.getGender()== null || Stream.of("M","Male","F","FEMALE")
                        .noneMatch(valid -> valid.equalsIgnoreCase(s.getGender().trim())),
                "Student gender must be male (M) or female(F)")
        )
                .filter(rule -> rule.isInvalid.test(student))
                .findFirst();
        Optional.of(error)
                .filter(Optional::isEmpty)
                .orElseThrow(()-> {
                    String msg = error.get().errorMessage();
                    log.log(Level.SEVERE, "Validation failed! "+msg);
                    return new ValidationException(msg);
                });

    }

    public void  deleteStudent(String id) throws ValidationException, StudentNotFoundException {
        if(id == null || id.trim().isEmpty()){
            log.log(Level.SEVERE,"Validation failed! ID is empty");
            throw new ValidationException("Student id cannot be empty!");
        }

        Student studentToRemove = students.stream()
                .filter(s -> s.getId().trim().equals(id.trim()))
                .findFirst()
                .orElseThrow(()->{
                            log.log(Level.WARNING,"Student with id " + id + " not found");
                            return new StudentNotFoundException("Student with ID " + id + "does not exist");
                            });
                students.remove(studentToRemove);
                log.log(Level.INFO, "Student with id " + id + " has been deleted");
    }


    public  List<Student> getStudentsByAge(String age) throws ValidationException{
        if(age == null || age.trim().isEmpty()){
            log.log(Level.SEVERE,"Validation failed! Age is empty");
            throw new ValidationException("Student age cannot be empty!");
        }
        int targetAge;
        try{
            targetAge = Integer.parseInt(age.trim());
            } catch (NumberFormatException e){
            throw  new ValidationException("Age must be a valid number");
                              }
        if(targetAge < 0){
            log.log(Level.SEVERE,"Validation failed! Age must be positive");
            throw new ValidationException("Age must be a positive number");
        }

        log.log(Level.INFO, "Student with age " + age );
        return students.stream()
                .filter(s -> s.getAge()==targetAge)
                .collect(Collectors.toList());

        }

        public List<Student> getStudentsOrdered() throws ValidationException{
        if (students.isEmpty()){
            log.log(Level.WARNING,"Student list is empty");
            throw new ValidationException("Student list is empty");
        }

        log.log(Level.INFO, "Students ordered by name");
        return students.stream()
                .sorted(Comparator.comparing(Student::getLastName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Student::getDateOfBirth))
            .collect(Collectors.toList());
    }
    




}


