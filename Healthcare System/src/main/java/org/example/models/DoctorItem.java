package org.example.models;

public class DoctorItem {
    private Integer id;
    private String name;

    public DoctorItem(Integer id, String firstName, String lastName) {
        this.id = id;
        this.name = "Dr. " + firstName + " " + lastName;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
