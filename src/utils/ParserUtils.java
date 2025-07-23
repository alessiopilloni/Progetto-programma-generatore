package utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParserUtils {
    public static Set<DayOfWeek> parseGiorniSettimana(String input) {
        Set<DayOfWeek> giorniSet = new HashSet<>();
        if (!input.trim().isEmpty()) {
            for (String token : input.split(",")) {
                token = token.trim();
                try {
                    int num = Integer.parseInt(token);
                    if (num >= 1 && num <= 7) {
                        giorniSet.add(DayOfWeek.of(num));
                    }
                } catch (NumberFormatException ignored) {
                    // Ignoriamo i token non validi
                }
            }
        }
        return giorniSet;
    }

    public static List<LocalDate> parseDateDaNonPianificare(String input) {
        List<LocalDate> date = new ArrayList<>();
        if (!input.trim().isEmpty()) {
            for (String token : input.split(",")) {
                token = token.trim();
                try {
                    date.add(LocalDate.parse(token));
                } catch (DateTimeParseException ignored) {
                    // Qui potremmo lanciare un evento o utilizzare un callback per notificare l'errore
                }
            }
        }
        return date;
    }
}