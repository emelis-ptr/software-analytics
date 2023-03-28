package util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Utils {

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

}
