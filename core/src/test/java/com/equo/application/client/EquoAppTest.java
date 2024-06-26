package com.equo.application.client;

import com.equo.application.client.exceptions.AppNameNotSpecifiedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EquoAppTest {

    private void testToSetAppName(String appName, String expected) {
        EquoApp.setAppName(appName);
        assertThat(EquoApp.getAppName()).isNotBlank().isEqualTo(expected);
    }

    @Test
    public void setNameAppTest() {
        testToSetAppName("FIRST-TEST", "first-test");
    }

    @Test
    public void setAppNameWithSpacesTest() {
        testToSetAppName("simple-url-app test with spaces", "simple-url-app-test-with-spaces");
    }

    @Test
    public void setAppNameWithMoreThanOneSpaceTest() {
        testToSetAppName("test with          more than one space",
                "test-with-more-than-one-space");
    }

    @Test
    public void setAppNameCombiningUpperCaseAndLowerCaseTest() {
        testToSetAppName("ComBIninG MayUS ANd MinUS", "combining-mayus-and-minus");
    }

    @Test
    public void setAppNameEndWithDashTest() {
        testToSetAppName("simple-url-app test with spaces and end with scor-e in the last position",
                "simple-url-app-test-with-spaces-and-end-with-scor");
        assertThat(EquoApp.getAppName().length()).isEqualTo(49);
    }

    @Test
    public void setAppNameMoreThan50CharactersTest() {
        testToSetAppName("simple-url-app test with spaces and more than 50 characteres",
                "simple-url-app-test-with-spaces-and-more-than-50-c");
        assertThat(EquoApp.getAppName().length()).isEqualTo(50);
    }

    @Test
    public void setAppNameWithWrongEntryTest() {
        testToSetAppName("-test-", "test");
    }

    @Test
    public void setAppNameStartingWith2DashesTest() {
        testToSetAppName("--MiApp", "miapp");
    }

    @Test
    public void setAppNameStartsWithDashTest() {
        testToSetAppName("-test dash", "test-dash");
    }

    @Test
    public void setAppNameShouldParseCorrectlyEndingWithDashesTest() {
        testToSetAppName("end with ---", "end-with");
    }

    @Test
    public void setAppNameShouldParseCorrectlyStartingWithWhitespacesTest() {
        testToSetAppName("           miapp", "miapp");
    }

    @Test
    public void setAppNameShouldParseCorrectlyTest() {
        testToSetAppName("-#!$$!#¡test-#!$$!#¡dash-#!$$!#¡", "test-dash");
    }

    @Test
    public void createWithEmptyAppNameTest() {
        assertThatThrownBy(() -> EquoApp.create("")).isInstanceOf(AppNameNotSpecifiedException.class);
    }

    @Test
    public void createWithWrongAppNameTest() {
        assertThatThrownBy(() -> EquoApp.create("--")).isInstanceOf(AppNameNotSpecifiedException.class);
    }

    @Test
    public void createWithADashAsAnAppNameTest() {
        assertThatThrownBy(() -> EquoApp.create("-")).isInstanceOf(AppNameNotSpecifiedException.class);
    }

    @Test
    public void createWithATotallyWrongNameTest() {
        assertThatThrownBy(() -> EquoApp.create("-#!$$!#¡?=)(/&%$!")).isInstanceOf(AppNameNotSpecifiedException.class);
    }
}
