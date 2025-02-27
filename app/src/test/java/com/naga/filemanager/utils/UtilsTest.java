package com.naga.filemanager.utils;

import org.junit.Test;

import static com.naga.filemanager.utils.Utils.sanitizeInput;
import static com.naga.filemanager.utils.Utils.formatTimer;
import static org.junit.Assert.*;

public class UtilsTest {
    @Test
    public void testSanitizeInput() {  //This function is sanitize the string. It removes ";","|","&&","..." from string.
        assertEquals("a",sanitizeInput("|a|"));  //test the removing of pipe sign from string.
        assertEquals("a",sanitizeInput("...a..."));  //test the removing of dots from string.
        assertEquals("a",sanitizeInput(";a;"));  //test the removing of semicolon sign from string.
        assertEquals("a",sanitizeInput("&&a&&"));  //test the removing of AMP sign from string.
        assertEquals("a",sanitizeInput("|a..."));   //test the removing of pipe sign and semicolon sign from string.
        assertEquals("an apple",sanitizeInput("an &&apple"));  //test the removing of AMP sign which are between two words.
        assertEquals("an apple",sanitizeInput("an ...apple"));  //test the removing of dots which are between two words.
        assertEquals("an apple.",sanitizeInput(";an |apple...."));  //test the removing of pipe sign and dots which are between two words. And test the fourth dot is not removed.
    }

    @Test
    public void testFormatTimer() {
        assertEquals("10:00", formatTimer(600));
        assertEquals("00:00", formatTimer(0));
        assertEquals("00:45", formatTimer(45));
        assertEquals("02:45", formatTimer(165));
        assertEquals("30:33", formatTimer(1833));
    }
}