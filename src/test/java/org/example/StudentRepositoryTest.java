package org.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {
    private StudentRepository studentRepository;

@BeforeEach
        void setUp() {
    studentRepository = new StudentRepository();
}

    @Test
    void addValidStudentTest() throws ValidationException {

    //given
        Student student = new Student("Maria", "Popescu", LocalDate.of(2001,5,20),"F","2001520193284");

        //when
        studentRepository.addStudent(student);
        List<Student> students = studentRepository.getStudentsOrdered();

        //then
        assertEquals(1,students.size());
        assertEquals("Maria",students.get(0).getName());
    }
@Test
    void addNullStudentTest() {
    //given
    Student nullStudent = null;

    //then
    assertThrows(ValidationException.class,()-> studentRepository.addStudent(null));
}

@Test
    void addEmptyIdStudentTest (){
    //given
    Student student = new Student("Elena", "Marin", LocalDate.of(2000,7,5),"F","");

    //then
    assertThrows(ValidationException.class,()-> studentRepository.addStudent(student));

}
    @Test
    void addEmptyNameStudentTest (){
        //given
        Student student = new Student("  ", "Marin", LocalDate.of(1990,7,5)," M","1990705319221");
        Student student1 = new Student("Dan", "  ", LocalDate.of(1990,7,5)," M","1990705319221");
        //then
        assertThrows(ValidationException.class,()-> studentRepository.addStudent(student));
        assertThrows(ValidationException.class,()-> studentRepository.addStudent(student1));

    }

    @Test
    void addDuplicateStudentsTest () throws ValidationException {
    //given
        Student student = new Student("Ana", "Marian", LocalDate.of(2000,7,5),"F","2001520193284");
        Student student1 = new Student("Julia", "Marc", LocalDate.of(2000,6,3),"F","2001520193284");

        studentRepository.addStudent(student);

        //then
        assertThrows(ValidationException.class,()-> studentRepository.addStudent(student1));
}

@Test
    void addStudentsInvalidGenderTest (){
    //given
    Student student = new Student("Ion","Marian", LocalDate.of(2000,7,5)," ","1001520193284");

    //then
    assertThrows(ValidationException.class,()-> studentRepository.addStudent(student));
}


@Test
     void deleteStudentsTest() throws ValidationException, StudentNotFoundException {
    //given
    Student student = new Student("Marius", "Pop", LocalDate.of(2000,7,5),"F","1001520193284");
    studentRepository.addStudent(student);

    //when
    studentRepository.deleteStudent("1001520193284");

    //then
    assertThrows(ValidationException.class,()-> studentRepository.getStudentsOrdered());
}

@Test
    void deleteemptyStudentsTest() {
    //given
    String emptyId = "";
    String nullId = null;

    //then
    assertThrows(ValidationException.class,()-> studentRepository.deleteStudent(emptyId));
    assertThrows(ValidationException.class,()-> studentRepository.deleteStudent(nullId));
}

@Test
    void deleteInexistentStudentsTest(){
    //given
    String inexistentId = "123456692782303";

    //then
    assertThrows(StudentNotFoundException.class,()-> studentRepository.deleteStudent(inexistentId));
}

@Test
    void getStudentsOrderedTest() throws ValidationException {
    //given
    Student student = new Student("Ana","Marian", LocalDate.of(2000,7,5),"F","2000520193284");
    Student student1 = new Student("Manuela","Ghemis", LocalDate.of(1999,7,4),"F","1999520193287");
    Student student2 = new Student("Danut","Pop", LocalDate.of(1994,3,8),"M","1994520193284");
    Student student3 = new Student("Maria","Mihut", LocalDate.of(1999,7,4),"F","1999520193284");

    studentRepository.addStudent(student);
    studentRepository.addStudent(student1);
    studentRepository.addStudent(student2);
    studentRepository.addStudent(student3);

    //when
    List<Student> students = studentRepository.getStudentsOrdered();

    //then
    assertEquals(4,students.size());
    assertEquals("Manuela",students.get(0).getName());
    assertEquals("Ana",students.get(1).getName());
    assertEquals("Maria",students.get(2).getName());
    assertEquals("Danut",students.get(3).getName());
}


@Test
    void getStudentsByAgeTest() throws ValidationException {
    //given
    Student student = new Student("Ana","Marian", LocalDate.of(2000,7,5),"F","2000520193284");
    Student student1 = new Student("Manuela","Ghemis", LocalDate.of(1999,7,4),"F","1999520193287");
    studentRepository.addStudent(student);
    studentRepository.addStudent(student1);

    String expectedAge = String.valueOf(student.getAge());

    //when
    List<Student> students = studentRepository.getStudentsByAge(expectedAge);

    //then

    assertEquals("Ana",students.get(0).getName());

}

}
