package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SeleniumTest {
	
	WebDriver driver;
	@BeforeMethod
	public void setup() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
		driver.get("https://finance.yahoo.com/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	}
	
	@Test
    public void testToVerifyTeslaStockPrice() {
      

        try {
           
           

            // Locate the search box and search for "TSLA"
            WebElement searchBox = driver.findElement(By.id("ybar-sbq"));
            searchBox.sendKeys("TSLA");

            // Wait for autosuggest to load and verify the first suggestion
            TimeUnit.SECONDS.sleep(2); // Adding a small delay for autosuggest to load

            WebElement firstSuggestion = driver.findElement(By.cssSelector("[data-id='result-quotes-0']"));

            String firstSuggestionText = firstSuggestion.getText();

            if (!firstSuggestionText.contains("Tesla, Inc.")) {
                System.out.println("Autosuggest verification failed. First suggestion: " + firstSuggestionText);
                return;
            }

            System.out.println("Autosuggest verification passed.");

            // Click on the first suggestion
            firstSuggestion.click();

            // Wait for the Tesla stock page to load
            WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[text()='Tesla, Inc.']")));
            System.out.println("Successfully Navigated to Tesla Stock Page");

            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            javascriptExecutor.executeScript("window.scrollBy(0,250)","");
            // Verify stock price is greater than $200
            WebElement stockPriceElement = driver.findElement(By.xpath("//*[@data-testid='quote-hdr']//following::fin-streamer[@data-field='regularMarketPrice' and @data-testid='qsp-price']/span"));
            webDriverWait.until(ExpectedConditions.visibilityOf(stockPriceElement));
            javascriptExecutor.executeScript("arguments[0].scrollIntoView();",stockPriceElement);
            String stockPriceText = stockPriceElement.getText();
            double stockPrice = Double.parseDouble(stockPriceText);

            if (stockPrice > 200) {
                System.out.println("Stock price verification passed. Current price: $" + stockPrice);
            } else {
                System.out.println("Stock price verification failed. Current price: $" + stockPrice);
            }

            // Capture additional details: Previous Close and Volume
            WebElement previousCloseElement = driver.findElement(By.xpath("//fin-streamer[@data-field='regularMarketPreviousClose']"));
            WebElement volumeElement = driver.findElement(By.xpath("//fin-streamer[@data-field='regularMarketVolume']"));

            String previousClose = previousCloseElement.getText();
            String volume = volumeElement.getText();

            // Log the details
            System.out.println("Previous Close: " + previousClose);
            System.out.println("Volume: " + volume);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }

    }
}
