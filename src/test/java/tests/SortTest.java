package tests;

import base.BaseTest;
import data.LoginData;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;

import java.util.List;

public class SortTest extends BaseTest {
    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @BeforeMethod
    public void setUpPage() {
        loginPage = new LoginPage(driver, wait);
        inventoryPage = new InventoryPage(driver);
    }

    @Test
    public void TC_SORT_01_sortProductsByNameAToZ() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.sortByNameAToZ();

        String firstItem = inventoryPage.getFirstProductName();

        Assert.assertEquals(
            firstItem,
            "Sauce Labs Backpack",
                "First product should be Backpack when sorted A to Z"
        );
    }

    @Test
    public void TC_SORT_02_sortProductsByNameZToA() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);

        inventoryPage.sortByNameZToA();

        String firstItem = inventoryPage.getFirstProductName();

        Assert.assertEquals(
            firstItem,
            "Test.allTheThings() T-Shirt (Red)",
                "First product should be Test.allTheThings() when sorted Z to A"
        );
    }

    @Test
    public void TC_SORT_03_sortProductsByPriceLowToHigh() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.sortByPriceLowToHigh();

        List<Double> prices = inventoryPage.getAllProductPrices();
        Assert.assertTrue(
                prices.get(0) <= prices.get(prices.size() - 1),
                "Products should be sorted low → high"
        );
    }

    @Test
    public void TC_SORT_04_sortProductsByPriceHighToLow() {
        loginPage.login(LoginData.VALID_USER, LoginData.VALID_PASS);
        inventoryPage.sortByPriceHighToLow();

        List<Double> prices = inventoryPage.getAllProductPrices();
        Assert.assertTrue(
                prices.get(0) >= prices.get(prices.size() - 1),
                "Products should be sorted high → low"
        );
    }
}