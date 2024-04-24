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
    WebElement priceHighToLowOption = driver.findElement(By.xpath("//option[text()='Price (high to low)']"));
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
}
