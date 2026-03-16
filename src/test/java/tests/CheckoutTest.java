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
                checkoutPage.isOnCheckoutCompletePage(),
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
                checkoutPage.isOnCart(),
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
                checkoutPage.isOnInventory(),
                "Should navigate back to inventory page"
        );
    }
    @Test
    public void TC_CHECKOUT_07_emptyCartStillCheckout() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
                inventoryPage.openCart();

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

        Assert.assertFalse(
                checkoutPage.isOnCheckoutStepTwo(),
                "BUG: System allows checkout with empty cart"
        );
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
                checkoutPage.isOnInventory(),
                "Should navigate back to inventory page"
        );
        Assert.assertEquals(
                inventoryPage.getCartBadgeCount(),
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

        double expectedTax = Math.round(expectedItemTotal * 0.08 * 100.0) / 100.0;
        double expectedTotal = Math.round((expectedItemTotal + expectedTax) * 100.0) / 100.0;

        Assert.assertEquals(actualItemTotal, expectedItemTotal, 0.01, "Item total is incorrect");
        Assert.assertEquals(actualTax, expectedTax, 0.01, "Tax is incorrect");
        Assert.assertEquals(actualTotal, expectedTotal, 0.01, "Total is incorrect");
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
        Assert.assertFalse(
                checkoutPage.isOnCheckoutStepTwo(),
                "BUG: System allows checkout when First Name contains only spaces"
        );
    }
    @Test
    public void TC_CHECKOUT_11_lastNameOnlySpaces() {
        openCheckoutWithBackpack();

        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                "   ",
                CheckoutData.ZIP_CODE
        );
        checkoutPage.clickContinue();

        Assert.assertFalse(
                checkoutPage.isOnCheckoutStepTwo(),
                "BUG: System allows checkout when Last Name contains only spaces"
        );
    }
    @Test
    public void TC_CHECKOUT_12_postalCodeOnlySpaces() {
        openCheckoutWithBackpack();

        checkoutPage.fillCheckoutInfo(
                CheckoutData.FIRST_NAME,
                CheckoutData.LAST_NAME,
                "  "
        );
        checkoutPage.clickContinue();

        Assert.assertFalse(
                checkoutPage.isOnCheckoutStepTwo(),
                "BUG: System allows checkout when Postal Code contains only spaces"
        );
    }
    @Test
    public void TC_CHECKOUT_13_allFieldsOnlySpaces() {
        openCheckoutWithBackpack();

        checkoutPage.fillCheckoutInfo("  ", "  ", "  ");
        checkoutPage.clickContinue();

        Assert.assertFalse(
                checkoutPage.isOnCheckoutStepTwo(),
                "BUG: System allows checkout when all fields contain only spaces"
        );
    }
}