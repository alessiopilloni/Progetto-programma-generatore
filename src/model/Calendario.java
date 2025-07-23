package model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta il calendario di output comprensivo di assenze e assegnazioni.
 * Per ora contiene solo le date pianificate.
 */
public class Calendario {
    private final List<LocalDate> datePianificate;

    public Calendario(List<LocalDate> datePianificate) {
        // Manteniamo la lista non modificabile per sicurezza
        this.datePianificate = datePianificate == null ? Collections.emptyList() : Collections.unmodifiableList(datePianificate);
    }

    public List<LocalDate> getDate() {
        return datePianificate;
    }

    @Override
    public String toString() {
        return datePianificate.toString();
    }
}