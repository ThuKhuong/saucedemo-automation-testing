package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InventoryPage {

    private WebDriver driver;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    private By backpackAddBtn = By.id("add-to-cart-sauce-labs-backpack");
    private By bikeLightAddBtn = By.id("add-to-cart-sauce-labs-bike-light");

    private By backpackRemoveBtn = By.id("remove-sauce-labs-backpack");
    private By bikeLightRemoveBtn = By.id("remove-sauce-labs-bike-light");

    private By cartLink = By.className("shopping_cart_link");
    private By cartBadge = By.className("shopping_cart_badge");

    private By sortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private By productNames = By.className("inventory_item_name");
    private By productPrices = By.className("inventory_item_price");

    private By menuBtn = By.id("react-burger-menu-btn");
    private By logoutLink = By.id("logout_sidebar_link");

    public void addBackpackToCart() {
        driver.findElement(backpackAddBtn).click();
    }

    public void addBikeLightToCart() {
        driver.findElement(bikeLightAddBtn).click();
    }
    public void removeBackpack() {
        driver.findElement(backpackRemoveBtn).click();
    }
    public void removeBikeLight() {
        driver.findElement(bikeLightRemoveBtn).click();
    }
    public void openCart() {
        driver.findElement(cartLink).click();
    }

    public String getCartBadgeText() {
        return driver.findElement(cartBadge).getText();
    }

    public int getCartBadgeCount() {
        return driver.findElements(cartBadge).size();
    }
    public void sortByNameAToZ() {
        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByVisibleText("Name (A to Z)");
    }

    public void sortByNameZToA() {
        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByVisibleText("Name (Z to A)");
    }

    public void sortByPriceLowToHigh() {
        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByVisibleText("Price (low to high)");
    }

    public void sortByPriceHighToLow() {
        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByVisibleText("Price (high to low)");
    }

    public String getFirstProductName() {
        return driver.findElements(productNames).get(0).getText();
    }
    public List<Double> getAllProductPrices() {
        return driver.findElements(productPrices)
                .stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }
    public void openMenu() {
        driver.findElement(menuBtn).click();
    }
    public void clickLogout() {
        driver.findElement(logoutLink).click();
    }
    public void logout() {
        openMenu();
        clickLogout();
    }

    public boolean isOnInventoryPage() {
        return driver.getCurrentUrl().contains("inventory");
    }
}