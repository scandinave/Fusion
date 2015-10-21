package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Enumération permettant de gérer les dates dans les fichiers .xml
 * @author Scandinave
 *
 */
public enum ReplacementDate {
    NOW,
    YESTERDAY(-1, 0, 0),
    TOMORROW(1, 0, 0),
    LASTWEEK(-7, 0, 0),
    NEXTWEEK(7, 0, 0),
    LASTMONTH(0, -1, 0),
    LASTMONTH2(0, -2, 0),
    LASTMONTH3(0, -3, 0),
    LASTMONTH4(0, -4, 0),
    LASTMONTH7(0, -7, 0),
    NEXTMONTH(0, 1, 0),
    NEXTMONTH2(0, 2, 0),
    NEXTMONTH3(0, 3, 0),
    NEXTMONTH4(0, 4, 0),
    LASTYEAR(0, 0, -1),
    NEXTYEAR(0, 0, 1),
    THIRTYYEAROLD(0, 0, -30),
    TENYEAROLD(0, 0, -10);

    /**
     * Jours à incrémenter.
     */
    private int days;

    /**
     * Mois à incrémenter.
     */
    private int months;

    /**
     * Années à incrémenter.
     */
    private int years;

    /**
     * Constructeur vide.
     */
    private ReplacementDate() {}

    /**
     * Constructeur.
     * @param pDays {@link Integer}
     * @param pMonths {@link Integer}
     * @param pYears {@link Integer}
     */
    private ReplacementDate(int pDays, int pMonths, int pYears) {
        this.days = pDays;
        this.months = pMonths;
        this.years = pYears;
    }

    /**
     * Permet de connaitre la date à générer
     * @return {@link Date}
     */
    public Date date() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, this.days);
        calendar.add(Calendar.MONTH, this.months);
        calendar.add(Calendar.YEAR, this.years);
        return calendar.getTime();
    }

    /**
     * Permet de connaitre la date à générer avec un pattern.
     * @return {@link String}
     */
    public String date(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(this.date());
    }

    /**
     * Permet de connaitre un élément de la date.
     * @return {@link Integer}
     */
    public int get(int arg0) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, this.days);
        calendar.add(Calendar.MONTH, this.months);
        calendar.add(Calendar.YEAR, this.years);
        return calendar.get(arg0);
    }

    /**
     * Permet de déterminer le tag à placer dans le fichier .xml.
     * @return {@link String}
     */
    public String tagName() {
        return String.format("[%s]", this.name());
    }

    @Override
    public String toString() {
        return "ReplacementDate [" + "name=" + this.name() + ", "
            + "tagName =" + this.tagName() + ", "
            + "date=" + this.date() + "]";
    }
}
