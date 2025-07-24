package model;

import java.util.Objects;

public class Persona{
    private String nomeECognome;

    public Persona(String nomeECognome) {
        this.nomeECognome = nomeECognome;
    }

    public String getNomeECognome() {
        return nomeECognome;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                    // Se è lo stesso oggetto in memoria
        if (o == null || getClass() != o.getClass()) return false;  // Se è null o di tipo diverso
        Persona persona = (Persona) o;                 // Cast sicuro dopo il controllo del tipo
        return Objects.equals(nomeECognome, persona.nomeECognome);  // Confronto dei nomi
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeECognome);  // Usa il metodo utility di Objects per generare l'hash
    }
}