package org.example.models;

public class PatientItem {
    private Integer id;
    private String name;

    public PatientItem(Integer id, String firstName, String lastName) {
        this.id = id;
        this.name = firstName + " " + lastName;
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
