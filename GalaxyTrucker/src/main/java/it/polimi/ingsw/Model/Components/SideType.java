package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * Enum that represents the side of a component
 *
 * @author carlo
 */


public enum SideType implements Serializable {
    Smooth("Smooth"),
    Single("Single"),
    Double("Double"),
    Universal("Universal"),
    Special("Special");

    //String linked to the "name" of the enum
    private final String jsonValue;

    SideType(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
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
}


