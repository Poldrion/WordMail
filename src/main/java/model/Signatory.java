package model;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;

public class Signatory implements Serializable {
    private SimpleStringProperty nameSignatoryProperty;
    private SimpleStringProperty postProperty;
    private SimpleStringProperty shortPostProperty;

    public Signatory(String name, String post, String shortPost) {
        this.nameSignatoryProperty = new SimpleStringProperty(name);
        this.postProperty = new SimpleStringProperty(post);
        this.shortPostProperty = new SimpleStringProperty(shortPost);
    }

    public String getNameSignatory() {
        return nameSignatoryProperty.get();
    }

    public SimpleStringProperty nameProperty() {
        return nameSignatoryProperty;
    }

    public void setNameSignatoryProperty(String nameSignatory) {
        this.nameSignatoryProperty.set(nameSignatory);
    }

    public String getPost() {
        return postProperty.get();
    }

    public SimpleStringProperty postProperty() {
        return postProperty;
    }

    public void setPostProperty(String post) {
        this.postProperty.set(post);
    }

    public String getShortPost() {
        return shortPostProperty.get();
    }

    public SimpleStringProperty shortPostProperty() {
        return shortPostProperty;
    }

    public void setShortPostProperty(String shortPostProperty) {
        this.shortPostProperty.set(shortPostProperty);
    }

    @Override
    public String toString() {
        return getShortPost() + " - " + getNameSignatory();
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(getNameSignatory());
        out.writeObject(getPost());
        out.writeObject(getShortPost());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        nameSignatoryProperty = new SimpleStringProperty((String) in.readObject());
        postProperty = new SimpleStringProperty((String) in.readObject());
        shortPostProperty = new SimpleStringProperty((String) in.readObject());
    }

}
