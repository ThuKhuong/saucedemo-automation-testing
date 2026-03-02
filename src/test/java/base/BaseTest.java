package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // tăng lên 10s cho ổn định, nhất là performance_glitch_user
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));
    }

    protected void login(String username, String password) {
        // đảm bảo đang ở đúng trang login (nếu test trước có navigate chỗ khác)
        if (!driver.getCurrentUrl().contains("saucedemo.com")) {
            driver.get("https://www.saucedemo.com/");
        }

        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

        user.clear();
        user.sendKeys(username);

        pass.clear();
        pass.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button"))).click();
    }

    protected void loginExpectSuccess(String username, String password) {
        login(username, password);
        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }

    protected String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test='error']")
        )).getText();
    }

    protected void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link"))).click();
    }

    protected int getCartBadgeCount() {
        return driver.findElements(By.className("shopping_cart_badge")).size();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}