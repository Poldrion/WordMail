package model;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.Objects;

public class Department implements Serializable {
    private int id;
    private SimpleStringProperty nameProperty;
    private SimpleStringProperty shortNameProperty;
    private Director director;

    public Department(int id, String name, String shortName, Director director) {
        this.id = id;
        this.nameProperty = new SimpleStringProperty(name);
        this.shortNameProperty = new SimpleStringProperty(shortName);
        this.director = director;
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public SimpleStringProperty namePropertyProperty() {
        return nameProperty;
    }

    public void setNameProperty(String name) {
        this.nameProperty.set(name);
    }

    public String getShortName() {
        return shortNameProperty.get();
    }

    public SimpleStringProperty shortNameProperty() {
        return shortNameProperty;
    }

    public void setShortName(String shortName) {
        this.shortNameProperty.set(shortName);
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getShortName();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(nameProperty, that.nameProperty) && Objects.equals(shortNameProperty, that.shortNameProperty) && Objects.equals(director, that.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameProperty, shortNameProperty, director);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(getId());
        out.writeObject(getNameProperty());
        out.writeObject(getShortName());
        out.writeObject(getDirector());

    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        id = (int) in.readObject();
        nameProperty = new SimpleStringProperty((String) in.readObject());
        shortNameProperty = new SimpleStringProperty((String) in.readObject());
        director = (Director) in.readObject();

    }
}


