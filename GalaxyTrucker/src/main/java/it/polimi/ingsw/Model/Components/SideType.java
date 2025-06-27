package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * Enum that represents the side of a component
 *
 * @author carlo
 * @author Ludo
 */


public enum SideType implements Serializable {
    // no connectors
    Smooth("Smooth"),
    // 1 connector
    Single("Single"),
    // 2 connectors
    Double("Double"),
    // 1 + 2 connectors
    Universal("Universal"),
    // cannon, engine, shield etc
    Special("Special");

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


