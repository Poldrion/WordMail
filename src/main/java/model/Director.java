package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;

public class Director implements Serializable {
    private SimpleStringProperty firstNameProperty;
    private SimpleStringProperty patronymicProperty;
    private SimpleStringProperty lastNameProperty;
    private SimpleBooleanProperty isMaleProperty;
    private SimpleStringProperty appealProperty;

    public Director(String firstName, String patronymic, String lastName, boolean isMale, String appeal) {
        this.firstNameProperty = new SimpleStringProperty(firstName);
        this.patronymicProperty = new SimpleStringProperty(patronymic);
        this.lastNameProperty = new SimpleStringProperty(lastName);
        this.isMaleProperty = new SimpleBooleanProperty(isMale);
        this.appealProperty = new SimpleStringProperty(appeal);
    }

    public String getFirstName() {
        return firstNameProperty.get();
    }

    public SimpleStringProperty getFirstNameProperty() {
        return firstNameProperty;
    }

    public void setFirstNameProperty(String firstName) {
        this.firstNameProperty.set(firstName);
    }

    public String getPatronymic() {
        return patronymicProperty.get();
    }

    public SimpleStringProperty getPatronymicProperty() {
        return patronymicProperty;
    }

    public void setPatronymicProperty(String patronymic) {
        this.patronymicProperty.set(patronymic);
    }

    public String getLastName() {
        return lastNameProperty.get();
    }

    public SimpleStringProperty getLastNameProperty() {
        return lastNameProperty;
    }

    public void setLastNameProperty(String lastNameProperty) {
        this.lastNameProperty.set(lastNameProperty);
    }

    public boolean isMale() {
        return isMaleProperty.get();
    }

    public SimpleBooleanProperty isMaleProperty() {
        return isMaleProperty;
    }

    public void setIsMaleProperty(boolean isMale) {
        this.isMaleProperty.set(isMale);
    }

    public String getAppeal() {
        return appealProperty.get();
    }

    public SimpleStringProperty getAppealProperty() {
        return appealProperty;
    }

    public void setAppealProperty(String appeal) {
        this.appealProperty.set(appeal);
    }

    public SimpleStringProperty fioDirectorDepartment() {
        return new SimpleStringProperty(getLastName() + " " + getFirstName() + " " + getPatronymic());
    }

    @Override
    public String toString() {
        return getLastName() + " " + getFirstName() + " " + getPatronymic();
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(getFirstName());
        out.writeObject(getPatronymic());
        out.writeObject(getLastName());
        out.writeObject(isMale());
        out.writeObject(getAppeal());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        firstNameProperty = new SimpleStringProperty((String) in.readObject());
        patronymicProperty = new SimpleStringProperty((String) in.readObject());
        lastNameProperty = new SimpleStringProperty((String) in.readObject());
        isMaleProperty = new SimpleBooleanProperty((boolean) in.readObject());
        appealProperty = new SimpleStringProperty((String) in.readObject());
    }
}

