package tests;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginTest extends BaseTest {

    private void clearAndType(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    private void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button"))).click();
    }

    private String getErrorText() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']"))
        ).getText();
    }

    @Test
    void TC_LOGIN_01_loginWithValidAccount() {
        clearAndType(By.id("user-name"), "standard_user");
        clearAndType(By.id("password"), "secret_sauce");
        clickLogin();

        wait.until(ExpectedConditions.urlContains("inventory.html"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Login fail");
    }

    @Test
    void TC_LOGIN_02_loginWithInvalidPassword() {
        clearAndType(By.id("user-name"), "standard_user");
        clearAndType(By.id("password"), "secret_sauce1");
        clickLogin();

        String errorText = getErrorText();
        Assertions.assertTrue(errorText.contains("Username and password do not match"),
                "Expected error message was not displayed. Actual: " + errorText);

        // đảm bảo vẫn ở login (không vào inventory)
        Assertions.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should stay on login page");
    }

    @Test
    void TC_LOGIN_03_loginWithInvalidUsername() {
        clearAndType(By.id("user-name"), "standard_user1");
        clearAndType(By.id("password"), "secret_sauce");
        clickLogin();

        String errorText = getErrorText();
        Assertions.assertTrue(errorText.contains("Username and password do not match"),
                "Expected error message was not displayed. Actual: " + errorText);

        Assertions.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should stay on login page");
    }

    @Test
    void TC_LOGIN_04_loginWithEmptyUsername() {

        clearAndType(By.id("password"), "secret_sauce");
        clickLogin();
        String errorText = getErrorText();
        Assertions.assertTrue(errorText.contains("Username is required"),
                "Expected 'Username is required'. Actual: " + errorText);

        Assertions.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should stay on login page");
    }

    @Test
    void TC_LOGIN_05_loginWithEmptyPassword() {
        clearAndType(By.id("user-name"), "standard_user");
        clickLogin();
        String errorText = getErrorText();
        Assertions.assertTrue(errorText.contains("Password is required"),
                "Expected 'Password is required'. Actual: " + errorText);

        Assertions.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should stay on login page");
    }

    @Test
    void TC_LOGIN_06_loginWithEmptyUsernamePassword() {
        clickLogin();

        String errorText = getErrorText();
        Assertions.assertTrue(errorText.contains("Username is required"),
                "Expected 'Username is required'. Actual: " + errorText);

        Assertions.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should stay on login page");
    }
    @Test
    void TC_LOGIN_07_loginWithInvalidUsernamePassword() {
        clearAndType(By.id("user-name"), "standard_user1");
        clearAndType(By.id("password"), "secret_sauce1");
        clickLogin();

        String errorText = getErrorText();
        Assertions.assertTrue(errorText.contains("Username and password do not match"),
                "Expected error message was not displayed. Actual: " + errorText);
        
        Assertions.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should stay on login page");
    }
    @Test
    void TC_LOGIN_08_lockedOutUser() {
        clearAndType(By.id("user-name"), "locked_out_user");
        clearAndType(By.id("password"), "secret_sauce");
        clickLogin();

        String errorText = getErrorText();
        Assertions.assertTrue(errorText.contains("locked out"),
                "Expected locked out message. Actual: " + errorText);

        Assertions.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should stay on login page");
    }

    @Test
    void TC_LOGIN_09_performanceGlitchUser() {
        clearAndType(By.id("user-name"), "performance_glitch_user");
        clearAndType(By.id("password"), "secret_sauce");
        clickLogin();

        // user này có thể load chậm, wait trong BaseTest là 10s ok, nếu vẫn fail tăng lên 15s
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                "Login FAILED not redirected to Inventory page");
    }
}