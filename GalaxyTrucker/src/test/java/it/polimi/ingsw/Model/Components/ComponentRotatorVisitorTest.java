package it.polimi.ingsw.Model.Components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ComponentRotatorVisitorTest {

    SideType[] differentSides1 = new SideType[]{SideType.Smooth, SideType.Single, SideType.Double, SideType.Universal};
    SideType[] differentSides2 = new SideType[]{SideType.Universal, SideType.Smooth, SideType.Single, SideType.Double};
    SideType[] differentSides3 = new SideType[]{SideType.Double, SideType.Universal, SideType.Smooth, SideType.Single};
    SideType[] differentSides4 = new SideType[]{SideType.Single, SideType.Double, SideType.Universal, SideType.Smooth};


    @Test
    void visitComponent() {
        Component component = new Component(differentSides1);
        assertArrayEquals(differentSides1, component.getAllSides());
        component.accept(new ComponentRotatorVisitor());
        assertArrayEquals(differentSides2, component.getAllSides());
        component.accept(new ComponentRotatorVisitor());
        assertArrayEquals(differentSides3, component.getAllSides());
        component.accept(new ComponentRotatorVisitor());
        assertArrayEquals(differentSides4, component.getAllSides());
        component.accept(new ComponentRotatorVisitor());
        assertArrayEquals(differentSides1, component.getAllSides());
    }

    @Test
    void visitShield() {
        Shield shield = new Shield(differentSides1, 0, 0);
        assertArrayEquals(differentSides1, shield.getAllSides());
        assertArrayEquals(new boolean[]{true, false, false, false}, shield.getCoveredSides());

        shield.accept(new ComponentRotatorVisitor());
        assertArrayEquals(differentSides2, shield.getAllSides());
        assertArrayEquals(new boolean[]{false, true, false, false}, shield.getCoveredSides());

        shield.accept(new ComponentRotatorVisitor());
        assertArrayEquals(differentSides3, shield.getAllSides());
        assertArrayEquals(new boolean[]{false, false, true, false}, shield.getCoveredSides());

        shield.accept(new ComponentRotatorVisitor());
        assertArrayEquals(differentSides4, shield.getAllSides());
        assertArrayEquals(new boolean[]{false, false, false, true}, shield.getCoveredSides());

        shield.accept(new ComponentRotatorVisitor());
        assertArrayEquals(differentSides1, shield.getAllSides());
        assertArrayEquals(new boolean[]{true, false, false, false}, shield.getCoveredSides());
    }
}