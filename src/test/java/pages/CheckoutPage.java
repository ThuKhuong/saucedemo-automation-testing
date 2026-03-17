package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {

    private WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    private By firstName = By.id("first-name");
    private By lastName = By.id("last-name");
    private By postalCode = By.id("postal-code");

    private By continueBtn = By.id("continue");
    private By finishBtn = By.id("finish");
    private By cancelBtn = By.id("cancel");
    private By backHomeBtn = By.id("back-to-products");

    private By errorMsg = By.cssSelector("[data-test='error']");
    private By completeHeader = By.className("complete-header");

    private By itemTotalLabel = By.className("summary_subtotal_label");
    private By taxLabel = By.className("summary_tax_label");
    private By totalLabel = By.className("summary_total_label");

    public void enterFirstName(String first) {
        driver.findElement(firstName).clear();
        driver.findElement(firstName).sendKeys(first);
    }
    public void enterLastName(String last) {
        driver.findElement(lastName).clear();
        driver.findElement(lastName).sendKeys(last);
    }
    public void enterPostalCode(String zip) {
        driver.findElement(postalCode).clear();
        driver.findElement(postalCode).sendKeys(zip);
    }

    public void fillCheckoutInfo(String first, String last, String zip) {
        enterFirstName(first);
        enterLastName(last);
        enterPostalCode(zip);
    }
    public void clickContinue() {

        driver.findElement(continueBtn).click();
    }
    public void clickFinish() {

        driver.findElement(finishBtn).click();
    }
    public void clickCancel() {

        driver.findElement(cancelBtn).click();
    }

    public void clickBackHome() {

        driver.findElement(backHomeBtn).click();
    }

    public String getErrorText() {

        return driver.findElement(errorMsg).getText();
    }

    public String getCompleteHeader() {

        return driver.findElement(completeHeader).getText();
    }
    public double getItemTotal() {
        String text = driver.findElement(itemTotalLabel).getText();
        return Double.parseDouble(text.replace("Item total: $", "").trim());
    }
    public double getTax() {
        String text = driver.findElement(taxLabel).getText(); // Tax: $2.40
        return Double.parseDouble(text.replace("Tax: $", "").trim());
    }
    public double getTotal() {
        String text = driver.findElement(totalLabel).getText(); // Total: $32.39
        return Double.parseDouble(text.replace("Total: $", "").trim());
    }
    public boolean isOnCart() {
        return driver.getCurrentUrl().contains("cart");
    }
    public boolean isOnInventory() {
        return driver.getCurrentUrl().contains("inventory");
    }
    public boolean isOnCheckoutStepTwo() {

        return driver.getCurrentUrl().contains("checkout-step-two");
    }
    public boolean isOnCheckoutCompletePage() {

        return driver.getCurrentUrl().contains("checkout-complete");
    }
}