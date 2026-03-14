package tests;

import base.BaseTest;
import data.LoginData;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;

public class LogoutTest extends BaseTest {
    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @BeforeMethod
    public void setUpPage() {
        loginPage = new LoginPage(driver, wait);
        inventoryPage = new InventoryPage(driver);
    }
    @Test
    public void TC_LOGOUT_01_logoutSuccessfully() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);

        wait.until(ExpectedConditions.urlContains("inventory"));
        inventoryPage.logout();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                org.openqa.selenium.By.id("login-button")));
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"),
                "Logout failed");
    }
}