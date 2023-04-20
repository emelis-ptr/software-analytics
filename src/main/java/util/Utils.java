package util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private Utils() {
    }

    /**
     * Converte date in LocalDate
     *
     * @param dateToConvert:
     * @return:
     */
    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Metodo che verifica se una stringa è contenuta in un testo
     *
     * @param comment:
     * @param find:
     * @return:
     */
    public static boolean isContained(String comment, String find) {
        String pattern = "\\b" + find + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(comment);
        return m.find();
    }

}
