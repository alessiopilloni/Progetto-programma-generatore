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
}
