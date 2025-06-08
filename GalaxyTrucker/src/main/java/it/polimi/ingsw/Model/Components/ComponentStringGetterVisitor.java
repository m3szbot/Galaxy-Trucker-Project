package it.polimi.ingsw.Model.Components;

import static it.polimi.ingsw.View.TUI.TUIView.*;

public class ComponentStringGetterVisitor implements ComponentVisitor {
    @Override
    public String visitAlienSupport(AlienSupport alienSupport) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d  %s  %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(alienSupport.getFront()),
                alienSupport.getComponentName().substring(0, 5),
                componentSideTranslator(alienSupport.getLeft()), alienSupport.isPurple() ? "Pur" : "Bro", componentSideTranslator(alienSupport.getRight()),
                componentSideTranslator(alienSupport.getBack()));
    }

    @Override
    public String visitBattery(Battery battery) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d   %d   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(battery.getFront()),
                battery.getComponentName().substring(0, 5),
                componentSideTranslator(battery.getLeft()), battery.getBatteryPower(), componentSideTranslator(battery.getRight()),
                componentSideTranslator(battery.getBack()));
    }

    @Override
    public String visitCabin(Cabin cabin) {
        String crew = "   ";
        if (cabin.getCrewType() != null) {
            crew = switch (cabin.getCrewType()) {
                case CrewType.Purple -> "Pur";
                case CrewType.Brown -> "Bro";
                case CrewType.Human -> "Hum";
            };
        }
        return String.format("""
                        +---%d---+
                        | %s |
                        %d  %s  %d
                        |   %d   |
                        +---%d---+
                        """,
                componentSideTranslator(cabin.getFront()),
                cabin.getComponentName().substring(0, 5),
                componentSideTranslator(cabin.getLeft()), crew, componentSideTranslator(cabin.getRight()),
                cabin.getCrewMembers(),
                componentSideTranslator(cabin.getBack()));
    }

    @Override
    public String visitCannon(Cannon cannon) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d   %d   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(cannon.getFront()),
                cannon.getComponentName().substring(0, 5),
                componentSideTranslator(cannon.getLeft()), cannon.isSingle() ? 1 : 2, componentSideTranslator(cannon.getRight()),
                componentSideTranslator(cannon.getBack()));
    }

    @Override
    public String visitComponent(Component component) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d       %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), componentSideTranslator(component.getRight()),
                componentSideTranslator(component.getBack()));
    }

    @Override
    public String visitEngine(Engine engine) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d   %d   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(engine.getFront()),
                engine.getComponentName().substring(0, 5),
                componentSideTranslator(engine.getLeft()), engine.isSingle() ? 1 : 2, componentSideTranslator(engine.getRight()),
                componentSideTranslator(engine.getBack()));
    }

    @Override
    public String visitShield(Shield shield) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d       %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(shield.getFront()),
                shield.getComponentName().substring(0, 5),
                componentSideTranslator(shield.getLeft()), componentSideTranslator(shield.getRight()),
                componentSideTranslator(shield.getBack()));
    }

    @Override
    public String visitStorage(Storage storage) {
        // RED YELLOW GREEN BLUE
        int slots = storage.getAvailableRedSlots() + storage.getAvailableBlueSlots();
        return String.format("""
                        +---%d---+
                        | %s |
                        %d %s %d %d
                        |%s%d%s %s%d%s %s%d%s %s%d%s|
                        +---%d---+
                        """,
                componentSideTranslator(storage.getFront()),
                storage.getComponentName().substring(0, 5),
                componentSideTranslator(storage.getLeft()), storage.isRed() ? "Red" : "Blu", slots, componentSideTranslator(storage.getRight()),
                RED, storage.getGoods()[0], RESET, YELLOW, storage.getGoods()[1], RESET, GREEN, storage.getGoods()[2], RESET, BLUE, storage.getGoods()[3], RESET,
                componentSideTranslator(storage.getBack()));
    }
}
