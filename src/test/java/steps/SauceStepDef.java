package steps;

import static org.testng.AssertJUnit.assertTrue;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class SauceStepDef {

  private WebDriver driver;

  @Before
  public void setUp() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.manage().window().maximize();
  }

  @After
  public void closeWindow() {
    if (driver != null) {
      driver.quit();
    }
  }


  @Given("user open the saucelab login page")
  public void userOpenTheSaucelabLoginPage() {
    driver.get("https://www.saucedemo.com/");
  }

  @When("user login with valid credential")
  public void userLoginWithValidCredential() {
    driver.findElement(By.id("user-name")).sendKeys("standard_user");
    driver.findElement(By.id("password")).sendKeys("secret_sauce");
    driver.findElement(By.id("login-button")).click();
  }

  @Then("user see saucelab homepage")
  public void userSeeSaucelabHomepage() {
    assertTrue("Inventory page title should be displayed after login",
        driver.findElement(By.className("title")).getText().contains("Products"));
  }

  @And("user sorting the price high to low")
  public void userSortingThePriceHighToLow() {
    WebElement sortDropdown = driver.findElement(By.className("product_sort_container"));
    sortDropdown.click();
    WebElement priceHighToLowOption = driver.findElement(
        By.xpath("//option[text()='Price (high to low)']"));
    priceHighToLowOption.click();

  }

  @Then("user see the price sorted from high to low")
  public void userSeeThePriceSortedFromHighToLow() {
    List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
    List<Double> prices = new ArrayList<>();

    for (WebElement priceElement : priceElements) {
      String priceText = priceElement.getText().replace("$", "");
      double price = Double.parseDouble(priceText);
      prices.add(price);
    }
    for (int i = 0; i < prices.size() - 1; i++) {
      Assert.assertTrue(prices.get(i) >= prices.get(i + 1),
          "Prices are not sorted from high to low");
    }
  }

  @And("user purchase {int} item which has {string} price")
  public void userPurchaseItemWhichHasPrice(int amount, String price) {
    List<WebElement> items = driver.findElements(By.className("inventory_item_price"));
    int itemCount = 0;
    for (WebElement item : items) {
      if (item.getText().contains(price)) {
        WebElement addToCartButton = item.findElement(By.xpath("../../..//button"));
        addToCartButton.click();
        itemCount++;
        if (itemCount == amount) {
          break;
        }
      }
    }
    WebElement cartItemCount = driver.findElement(By.className("shopping_cart_badge"));
    Assert.assertEquals(cartItemCount.getText(), "2", "Items not added to cart");

    driver.findElement(By.className("shopping_cart_link")).click();
    driver.findElement(By.id("checkout")).click();
  }

  @And("user fill the buyer details")
  public void userFillTheBuyerDetails() {
    driver.findElement(By.id("first-name")).sendKeys("Raymond");
    driver.findElement(By.id("last-name")).sendKeys("Nuari");
    driver.findElement(By.id("postal-code")).sendKeys("10440");
    driver.findElement(By.id("continue")).click();
  }

  @Then("user successful complete purchase items")
  public void userSuccessfulCompletePurchaseItems() {
    WebElement checkoutTitle = driver.findElement(By.className("title"));
    Assert.assertTrue(checkoutTitle.getText().contains("Checkout: Overview"), "Checkout failed");
    driver.findElement(By.id("finish")).click();
    Assert.assertTrue(driver.findElement(By.className("complete-header")).getText().contains("Thank you for your order!"), "Checkout failed");
    driver.findElement(By.id("back-to-products")).click();
    Assert.assertTrue(driver.findElement(By.className("title")).getText().contains("Products"));
  }

  @Then("user see correct details of checkout summary")
  public void userSeeCorrectDetailsOfCheckoutSummary() {
    WebElement checkoutTitle = driver.findElement(By.className("title"));
    Assert.assertTrue(checkoutTitle.getText().contains("Checkout: Overview"), "Checkout failed");
    List<WebElement> items = driver.findElements(By.className("inventory_item_price"));
    for (WebElement item : items) {
      Assert.assertTrue(item.getText().contains("15.99"), "item price wrong");
    }
  }

  @When("user login with {string} username and {string} password")
  public void userLoginWithUsernameAndPassword(String username, String password) {
    driver.findElement(By.id("user-name")).sendKeys(username);
    driver.findElement(By.id("password")).sendKeys(password);
    driver.findElement(By.id("login-button")).click();
  }

  @Then("user should see error message indicating the user is locked out")
  public void userShouldSeeErrorMessageIndicatingTheUserIsLockedOut() {
    WebElement errorMessage = driver.findElement(By.cssSelector(".error-message-container"));
    Assert.assertTrue(errorMessage.isDisplayed(), "Error message is not displayed");
    Assert.assertTrue(errorMessage.getText().contains("Epic sadface: Sorry, this user has been locked out."), "Incorrect error message");
  }

}

