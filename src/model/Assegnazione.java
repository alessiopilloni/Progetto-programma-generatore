package model;

import java.time.LocalDate;

public class Assegnazione {
    private Incarico incarico;
    private Persona persona;
    private LocalDate data;

    public Assegnazione(Incarico incarico, Persona persona, LocalDate data) {
        this.incarico = incarico;
        this.persona = persona;
        this.data = data;
    }

 

    public Incarico getIncarico() {
        return incarico;
    }

    public Persona getPersona() {
        return persona;
    }

    public LocalDate getData() {
        return data;
    }
    public void assegnaIncarico(Incarico incarico, Persona persona, LocalDate data) {
        this.incarico = incarico;
        this.persona = persona;
        this.data = data;
    }
}
