package org.example;

import java.time.LocalDate;
import java.util.Locale;

public class Student {
    private String name;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String id;

    public Student(String name, String lastName, LocalDate dateOfBirth, String gender, String id) {
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

  public String getId() {
    return id;
    }

    public int getAge() {
      if (this.dateOfBirth==null) {
        return 0;
      }
      return java.time.Period.between(this.dateOfBirth, java.time.LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
