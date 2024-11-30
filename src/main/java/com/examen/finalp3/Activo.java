package com.examen.finalp3;

import javafx.beans.property.*;

public class Activo {
    private final IntegerProperty id;
    private final StringProperty tipo;
    private final StringProperty marca;
    private final StringProperty modelo;
    private final StringProperty serial;
    private final StringProperty responsable;

    public Activo(int id, String tipo, String marca, String modelo, String serial, String responsable) {
        this.id = new SimpleIntegerProperty(id);
        this.tipo = new SimpleStringProperty(tipo);
        this.marca = new SimpleStringProperty(marca);
        this.modelo = new SimpleStringProperty(modelo);
        this.serial = new SimpleStringProperty(serial);
        this.responsable = new SimpleStringProperty(responsable);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public StringProperty marcaProperty() {
        return marca;
    }

    public StringProperty modeloProperty() {
        return modelo;
    }

    public StringProperty serialProperty() {
        return serial;
    }

    public StringProperty responsableProperty() {
        return responsable;
    }

    public String toCSV() {
        return id.get() + ";" + tipo.get() + ";" + marca.get() + ";" + modelo.get() + ";" + serial.get() + ";" + responsable.get();
    }
}
