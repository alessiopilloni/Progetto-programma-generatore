package model;

import java.util.List;

public class Incarico {

    private List<Persona> lista;
    private String incarico;

    public Incarico(List<Persona> lista, String incarico) {
        this.lista = lista;
        this.incarico = incarico;
    }

    public String getIncarico() {
        return incarico;
    }

    public List<Persona> getLista() {
        return lista;
    }
    public Persona getPersona(int index) {
        return lista.get(index);
    }

    public void addPersona(Persona persona) {
        lista.add(persona);
    }

    public void removePersona(Persona persona) {
        lista.remove(persona);
    }

    public int getNumeroPersone() {
        return lista.size();
    }
}
 