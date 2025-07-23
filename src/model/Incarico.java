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

}
 