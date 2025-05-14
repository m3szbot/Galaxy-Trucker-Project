package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum that represents the side of a component
 *
 * @author carlo
 */


public enum SideType {
    SMOOTH("SMOOTH"),
    SINGLE("SINGLE"),
    DOUBLE("DOUBLE"),
    UNIVERSAL("UNIVERSAL"),
    SPECIAL("SPECIAL");

    //String linked to the "name" of the enum
    private final String jsonValue;

    SideType(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonCreator
    public static SideType fromString(String value) {
        for (SideType type : SideType.values()) {
            if (type.jsonValue.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown SideType: " + value);
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }
}


