package tests;

import base.BaseTest;
import data.CheckoutData;
import data.LoginData;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;

public class CheckoutTest extends BaseTest {
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver, wait);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    private void openCheckoutWithBackpack() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();
        cartPage.clickCheckout();
    }

    @Test
    public void TC_CHECKOUT_01_checkoutSuccessfully() {
        openCheckoutWithBackpack();

        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                CheckoutData.LAST_NAME,
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();
        Assert.assertEquals(
                checkoutPage.getCompleteHeader(),
                "Thank you for your order!",
                "Checkout complete header is incorrect"
        );

        Assert.assertTrue(
                driver.getCurrentUrl().contains("checkout-complete"),
                "Should navigate to checkout complete page"
        );
    }

    @Test
    public void TC_CHECKOUT_02_missingFirstName() {
        openCheckoutWithBackpack();
        checkoutPage.fillCheckoutInfo(
                "",
                CheckoutData.LAST_NAME,
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();
        String error = checkoutPage.getErrorText();

        Assert.assertTrue(
                error.contains("First Name is required"),
                "Expected 'First Name is required'. Actual: " + error
        );
    }

    @Test
    public void TC_CHECKOUT_03_missingLastName() {
        openCheckoutWithBackpack();
        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                "",
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();

        String error = checkoutPage.getErrorText();

        Assert.assertTrue(
                error.contains("Last Name is required"),
                "Expected 'Last Name is required'. Actual: " + error
        );
    }

    @Test
    public void TC_CHECKOUT_04_missingPostalCode() {
        openCheckoutWithBackpack();
        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                CheckoutData.LAST_NAME,
                ""
        );
        checkoutPage.clickContinue();
        String error = checkoutPage.getErrorText();
        Assert.assertTrue(
                error.contains("Postal Code is required"),
                "Expected 'Postal Code is required'. Actual: " + error
        );
    }

    @Test
    public void TC_CHECKOUT_05_cancelCheckoutStep1() {
        openCheckoutWithBackpack();
        checkoutPage.clickCancel();
        Assert.assertTrue(
                driver.getCurrentUrl().contains("cart"),
                "Should navigate back to cart page"
        );
    }

    @Test
    public void TC_CHECKOUT_06_cancelCheckoutStep2() {
        openCheckoutWithBackpack();

        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                CheckoutData.LAST_NAME,
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();
        checkoutPage.clickCancel();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("inventory"),
                "Should navigate back to inventory page"
        );
    }

    @Test
    public void TC_CHECKOUT_07_emptyCartStillCheckout() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        openCart();
        Assert.assertEquals(
                driver.findElements(org.openqa.selenium.By.className("cart_item")).size(),
                0,
                "Precondition failed: cart is not empty"
        );
        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                CheckoutData.LAST_NAME,
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();
        Assert.assertTrue(
                driver.getCurrentUrl().contains("checkout-step-two"),
                "Should be on overview page"
        );
        Assert.fail("BUG: Checkout succeeds with empty cart");
    }

    @Test
    public void TC_CHECKOUT_08_backHomeAfterFinish() {
        openCheckoutWithBackpack();

        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                CheckoutData.LAST_NAME,
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();
        checkoutPage.clickBackHome();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("inventory"),
                "Should navigate back to inventory page"
        );
        Assert.assertEquals(
                getCartBadgeCount(),
                0,
                "Cart badge should be reset after finishing checkout"
        );
    }

    @Test
    public void TC_CHECKOUT_09_verifyItemTotalTaxAndTotal() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.addBackpackToCart();
        inventoryPage.addBikeLightToCart();
        inventoryPage.openCart();
        cartPage.clickCheckout();

        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                CheckoutData.LAST_NAME,
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();

        double expectedItemTotal = 29.99 + 9.99;
        double actualItemTotal = checkoutPage.getItemTotal();
        double actualTax = checkoutPage.getTax();
        double actualTotal = checkoutPage.getTotal();
        Assert.assertEquals(
                actualItemTotal,
                expectedItemTotal,
                0.01,
                "Item total is incorrect"
        );
        Assert.assertEquals(
                actualTax,
                actualItemTotal * 0.08,
                0.01,
                "Tax is incorrect"
        );
        Assert.assertEquals(
                actualTotal,
                actualItemTotal + actualTax,
                0.01,
                "Total is incorrect"
        );
    }
    @Test
    public void TC_CHECKOUT_10_firstNameOnlySpaces() {
        openCheckoutWithBackpack();

        checkoutPage.fillCheckoutInfo(
                "   ",
                CheckoutData.LAST_NAME,
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();
        Assert.fail("BUG: System allows checkout when First Name contains only spaces");
    }
}