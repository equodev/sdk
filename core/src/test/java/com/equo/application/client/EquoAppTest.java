package com.equo.application.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquoAppTest {

    @Test
    public void setNameAppTest() {
        EquoApp.setAppName("FIRST-TEST");
        assertEquals("first-test", EquoApp.getAppName());

        EquoApp.setAppName("simple-url-app test with spaces");
        assertEquals("simple-url-app-test-with-spaces", EquoApp.getAppName());

        EquoApp.setAppName("test with          more than one space");
        assertEquals("test-with-more-than-one-space", EquoApp.getAppName());

        EquoApp.setAppName("ComBIninG MayUS ANd MinUS");
        assertEquals("combining-mayus-and-minus", EquoApp.getAppName());

        EquoApp.setAppName("simple-url-app test with spaces and end with scor-e in the last position");
        assertEquals("simple-url-app-test-with-spaces-and-end-with-scor", EquoApp.getAppName());
        assertEquals(49, EquoApp.getAppName().length());

        EquoApp.setAppName("simple-url-app test with spaces and more than 50 characteres");
        assertEquals("simple-url-app-test-with-spaces-and-more-than-50-c", EquoApp.getAppName());
        assertEquals(50, EquoApp.getAppName().length());
    }
}
