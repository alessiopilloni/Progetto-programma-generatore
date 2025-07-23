package model;

import java.util.ArrayList;
import java.util.List;


public class Assenze {
    private List<Assenza> assenze;

    public Assenze() {
        this.assenze = new ArrayList<>();
    }

    public void addAssenza(Assenza assenza) {
        this.assenze.add(assenza);
    }

    public List<Assenza> getAssenze() {
        return assenze;
    }

    public boolean isAssente(Persona persona) {
        for (Assenza assenza : assenze) {
            if (assenza.getPersona().equals(persona)) {
                return true;
            }
        }
        return false;
    }
}
