package model;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;

public class Executor implements Serializable {
    private SimpleStringProperty nameProperty;
    private SimpleStringProperty phoneProperty;
    private SimpleStringProperty emailProperty;

    public Executor(String name, String phone, String email) {
        this.nameProperty = new SimpleStringProperty(name);
        this.phoneProperty = new SimpleStringProperty(phone);
        this.emailProperty = new SimpleStringProperty(email);
    }

    public String getName() {
        return nameProperty.get();
    }

    public SimpleStringProperty getNameProperty() {
        return nameProperty;
    }

    public void setNameProperty(String name) {
        this.nameProperty.set(name);
    }

    public String getPhone() {
        return phoneProperty.get();
    }

    public SimpleStringProperty getPhoneProperty() {
        return phoneProperty;
    }

    public void setPhoneProperty(String phone) {
        this.phoneProperty.set(phone);
    }

    public String getEmail() {
        return emailProperty.get();
    }

    public SimpleStringProperty getEmailProperty() {
        return emailProperty;
    }

    public void setEmailProperty(String email) {
        this.emailProperty.set(email);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(getName());
        out.writeObject(getPhone());
        out.writeObject(getEmail());

    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        nameProperty = new SimpleStringProperty((String) in.readObject());
        phoneProperty = new SimpleStringProperty((String) in.readObject());
        emailProperty = new SimpleStringProperty((String) in.readObject());

    }


}
