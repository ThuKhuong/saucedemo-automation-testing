package tests;

import base.BaseTest;
import data.LoginData;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;

public class CartTest extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver, wait);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
    }

    @Test
    public void TC_CART_01_addOneProductToCart() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.addBackpackToCart();
        Assert.assertEquals(
            inventoryPage.getCartBadgeText(),
            "1",
                "Cart badge should be 1"
        );
    }

    @Test
    public void TC_CART_02_addTwoProductToCart() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.addBackpackToCart();
        inventoryPage.addBikeLightToCart();

        Assert.assertEquals(
            inventoryPage.getCartBadgeText(),
            "2",
                "Cart badge should be 2"
        );
    }
    @Test
    public void TC_CART_03_removeOneProductFromCart() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.addBackpackToCart();
        inventoryPage.addBikeLightToCart();
        inventoryPage.removeBikeLight();

        Assert.assertEquals(
            inventoryPage.getCartBadgeText(),
            "1",
                "Cart badge should display 1"
        );
    }

    @Test
    public void TC_CART_04_verifyProductNameInCart() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        String productName = cartPage.getFirstProductName();

        Assert.assertEquals(
            productName,
            "Sauce Labs Backpack",
                "Product name in cart should be correct"
        );
    }

    @Test
    public void TC_CART_05_continueShopping() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);

        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();
        cartPage.clickContinueShopping();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("inventory"),
                "Should navigate back to inventory page"
        );
    }

    @Test
    public void TC_CART_06_removeItemInCart() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();
        cartPage.removeBackpack();
        Assert.assertEquals(
            getCartBadgeCount(),
            0,
                "Cart badge should disappear after removing item"
        );
    }

    @Test
    public void TC_CART_07_removeItemInInventory() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);

        inventoryPage.addBackpackToCart();
        inventoryPage.removeBackpack();

        Assert.assertEquals(
            getCartBadgeCount(),
            0,
                "Cart badge should disappear after removing item"
        );
    }
}