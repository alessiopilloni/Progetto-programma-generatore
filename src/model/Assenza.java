package model;

import java.time.LocalDate;
import java.util.Objects;


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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assenza assenza = (Assenza) o;
        return Objects.equals(persona, assenza.persona) && 
               Objects.equals(data, assenza.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persona, data);
    }
}
