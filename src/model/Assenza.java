package model;

import java.time.LocalDate;


public class Assenza {
    private Persona persona;
    private LocalDate data;

    public Assenza(Persona persona, LocalDate data) {
        this.persona = persona;
        this.data = data;
    }

    public Persona getPersona() {
        return persona;
    }

    public LocalDate getData() {
        return data;
    }
}
