package model;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;

public class IdDepartment implements Serializable {
    private SimpleStringProperty nameProperty;
    private SimpleStringProperty idDepartmentProperty;

    public IdDepartment(String name, String idDepartment) {
        this.nameProperty = new SimpleStringProperty(name);
        this.idDepartmentProperty = new SimpleStringProperty(idDepartment);
    }

    public String getNameDepartment() {
        return nameProperty.get();
    }

    public SimpleStringProperty getNameProperty() {
        return nameProperty;
    }

    public void setNameProperty(String name) {
        this.nameProperty.set(name);
    }

    public String getIdDepartment() {
        return idDepartmentProperty.get();
    }

    public SimpleStringProperty getIdDepartmentProperty() {
        return idDepartmentProperty;
    }

    public void setIdDepartmentProperty(String idDepartment) {
        this.idDepartmentProperty.set(idDepartment);
    }

    @Override
    public String toString() {
        return getNameDepartment() + " - " + getIdDepartment();
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(getNameDepartment());
        out.writeObject(getIdDepartment());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        nameProperty = new SimpleStringProperty((String) in.readObject());
        idDepartmentProperty = new SimpleStringProperty((String) in.readObject());
    }
}
