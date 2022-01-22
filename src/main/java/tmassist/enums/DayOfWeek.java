package tmassist.enums;

/**
 * Enum representing the days of the week.
 */
public enum DayOfWeek {
    /** enum value representing Sunday. */
    SUNDAY ("Sunday"),
    /** enum value representing Monday. */
    MONDAY ("Monday"),
    /** enum value representing Tuesday. */
    TUESDAY ("Tuesday"),
    /** enum value representing Wednesday. */
    WEDNESDAY ("Wednesday"),
    /** enum value representing Thursday. */
    THURSDAY ("Thursday"),
    /** enum value representing Friday. */
    FRIDAY ("Friday"),
    /** enum value representing Saturday. */
    SATURDAY ("Saturday");

    DayOfWeek(String dayName) {
        setDayName(dayName);
    }

    private String dayName;

    private void setDayName(String dayName) {
        this.dayName = dayName;
    }

    /**
     * @return The name of this day.
     */
    public String getDayName() {
        return dayName;
    }
}