package it.polimi.ingsw.Model.Components;

import static it.polimi.ingsw.View.TUI.TUIView.*;

public class ComponentStringGetterVisitor implements ComponentVisitor<String, RuntimeException> {
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
                componentSideTranslator(alienSupport.getLeft()), alienSupport.isPurple() ? PURPLE + "Pur" + RESET : YELLOW + "Bro" + RESET, componentSideTranslator(alienSupport.getRight()),
                componentSideTranslator(alienSupport.getBack()));
    }

    @Override
    public String visitBattery(Battery battery) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d   %s%d%s   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(battery.getFront()),
                battery.getComponentName().substring(0, 5),
                componentSideTranslator(battery.getLeft()), GREEN, battery.getBatteryPower(), RESET, componentSideTranslator(battery.getRight()),
                componentSideTranslator(battery.getBack()));
    }

    @Override
    public String visitCabin(Cabin cabin) {
        String crew = "   ";
        if (cabin.getCrewType() != null) {
            crew = switch (cabin.getCrewType()) {
                case CrewType.Purple -> PURPLE + "Pur" + RESET;
                case CrewType.Brown -> YELLOW + "Bro" + RESET;
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
                        %d   %s%d%s   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(cannon.getFront()),
                cannon.getComponentName().substring(0, 5),
                componentSideTranslator(cannon.getLeft()), PURPLE, cannon.isSingle() ? 1 : 2, RESET, componentSideTranslator(cannon.getRight()),
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
                        %d   %s%d%s   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(engine.getFront()),
                engine.getComponentName().substring(0, 5),
                componentSideTranslator(engine.getLeft()), YELLOW, engine.isSingle() ? 1 : 2, RESET, componentSideTranslator(engine.getRight()),
                componentSideTranslator(engine.getBack()));
    }

    @Override
    public String visitShield(Shield shield) {
        StringBuilder builder = new StringBuilder();

        // row 1
        // front covered
        if (shield.getCoveredSides()[0])
            builder.append(String.format("+%s###%s%d%s###%s+\n", GREEN, RESET, componentSideTranslator(shield.getFront()), GREEN, RESET));
        else
            builder.append(String.format("+---%d---+\n", componentSideTranslator(shield.getFront())));

        // row 2-4
        // left and right uncovered
        if (!shield.getCoveredSides()[1] && !shield.getCoveredSides()[3]) {
            builder.append(String.format("""
                            | %s |
                            %d       %d
                            |       |
                            """,
                    shield.getComponentName().substring(0, 5),
                    componentSideTranslator(shield.getLeft()), componentSideTranslator(shield.getRight())));
        }

        // only left covered
        else if (!shield.getCoveredSides()[1] && shield.getCoveredSides()[3]) {
            builder.append(String.format("""
                            %s#%s %s |
                            %d       %d
                            %s#%s       |
                            """,
                    GREEN, RESET, shield.getComponentName().substring(0, 5),
                    componentSideTranslator(shield.getLeft()), componentSideTranslator(shield.getRight()),
                    GREEN, RESET));
        }
        // only right covered
        else if (shield.getCoveredSides()[1] && !shield.getCoveredSides()[3]) {
            builder.append(String.format("""
                            | %s %s#%s
                            %d       %d
                            |       %s#%s
                            """,
                    shield.getComponentName().substring(0, 5), GREEN, RESET,
                    componentSideTranslator(shield.getLeft()), componentSideTranslator(shield.getRight()),
                    GREEN, RESET));
        }
        // right and left covered
        else if (shield.getCoveredSides()[1] && shield.getCoveredSides()[3]) {
            builder.append(String.format("""
                            %s#%s %s %s#%s
                            %d       %d
                            %s#%s       %s#%s
                            """,
                    GREEN, RESET, shield.getComponentName().substring(0, 5), GREEN, RESET,
                    componentSideTranslator(shield.getLeft()), componentSideTranslator(shield.getRight()),
                    GREEN, RESET, GREEN, RESET));
        }

        // row 5
        // back covered
        if (shield.getCoveredSides()[2])
            builder.append(String.format("+%s###%s%d%s###%s+\n", GREEN, RESET, componentSideTranslator(shield.getFront()), GREEN, RESET));
        else
            builder.append(String.format("+---%d---+\n", componentSideTranslator(shield.getFront())));

        // component string constructed
        return builder.toString();
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
                componentSideTranslator(storage.getLeft()), storage.isRed() ? RED + "Red" + RESET : BLUE + "Blu" + RESET, slots, componentSideTranslator(storage.getRight()),
                RED, storage.getGoods()[0], RESET, YELLOW, storage.getGoods()[1], RESET, GREEN, storage.getGoods()[2], RESET, BLUE, storage.getGoods()[3], RESET,
                componentSideTranslator(storage.getBack()));
    }
}
