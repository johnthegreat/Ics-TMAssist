package tmassist.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RatedMode {
    RATED, UNRATED;

    @JsonCreator
    public static RatedMode forValue(final String value) {
        return RatedMode.valueOf(value.toUpperCase());
    }
}