package tmassist.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Variant {
    ATOMIC		("Atomic","atom"),
    BLITZ		("Blitz","blitz"),
    BUGHOUSE	("Bughouse","bug"),
    CRAZYHOUSE	("Crazyhouse","zh"),
    LIGHTNING	("Lightning","lightning"),
    LOSERS		("Losers","los"),
    STANDARD	("Standard","standard"),
    SUICIDE		("Suicide","sui"),
    WILD		("Wild","wild")
    ;

    private String name;
    private String abbreviation;
    Variant(final String name, final String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }
    public String getAbbreviation() {
        return abbreviation;
    }

    @JsonCreator
    public static Variant forValue(String value) {
        return Variant.valueOf(value.toUpperCase());
    }

    public static boolean isChess(Variant variant) {
        return variant == LIGHTNING || variant == BLITZ || variant == STANDARD;
    }
}
