package edu.fcu.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexityBoosterTest {
    @Test
    void testBranchIfElse() {
        ComplexityBooster booster = new ComplexityBooster();
        // a > 0, b > 10
        assertEquals(1 + 4, booster.branchIfElse(1, 11));
        // a > 0, b < -10
        assertEquals(1 + 5, booster.branchIfElse(2, -11));
        // a > 0, -10 <= b <= 10
        assertEquals(1 + 6, booster.branchIfElse(3, 0));
        // a < 0, b > 10
        assertEquals(2 + 4, booster.branchIfElse(-1, 20));
        // a < 0, b < -10
        assertEquals(2 + 5, booster.branchIfElse(-2, -20));
        // a < 0, -10 <= b <= 10
        assertEquals(2 + 6, booster.branchIfElse(-3, 5));
        // a == 0, b > 10
        assertEquals(3 + 4, booster.branchIfElse(0, 15));
        // a == 0, b < -10
        assertEquals(3 + 5, booster.branchIfElse(0, -15));
        // a == 0, -10 <= b <= 10
        assertEquals(3 + 6, booster.branchIfElse(0, 0));
    }

    @Test
    void testBranchSwitch() {
        ComplexityBooster booster = new ComplexityBooster();
        assertEquals(1, booster.branchSwitch(0));
        assertEquals(2, booster.branchSwitch(1));
        assertEquals(3, booster.branchSwitch(2));
        assertEquals(4, booster.branchSwitch(3));
        assertEquals(5, booster.branchSwitch(99)); // default
    }

    @Test
    void testBranchLoop() {
        ComplexityBooster booster = new ComplexityBooster();
        // n = 0
        assertEquals(0, booster.branchLoop(0));
        // n = 1 (i=0, j=0)
        assertEquals(1 + 4, booster.branchLoop(1));
        // n = 2 (i=0,1; j=0,1)
        // i=0: +1, i=1: +2; j=0: +4, j=1: +5
        assertEquals(1 + 2 + 4 + 5, booster.branchLoop(2));
        // n = 3 (i=0:1, i=1:2, i=2:3; j=0:4, j=1:5, j=2:4)
        assertEquals(1 + 2 + 3 + 4 + 5 + 4, booster.branchLoop(3));
    }

    @Test
    void testComplexNest() {
        ComplexityBooster booster = new ComplexityBooster();
        // a > 0, b = 2 (偶數)
        assertFalse(booster.complexNest(1, 2, 0));
        // a > 0, b = 3 (奇數)
        assertTrue(booster.complexNest(1, 3, 0));
        // a < 0, c = 1
        assertTrue(booster.complexNest(-1, 0, 1));
        // a < 0, c = 2
        assertFalse(booster.complexNest(-1, 0, 2));
        // a < 0, c = 3
        assertTrue(booster.complexNest(-1, 0, 3));
        // a < 0, c = 4 (其他)
        assertFalse(booster.complexNest(-1, 0, 4));
        // a == 0, b==c
        assertTrue(booster.complexNest(0, 5, 5));
        // a == 0, b!=c
        assertFalse(booster.complexNest(0, 1, 2));
    }
}
