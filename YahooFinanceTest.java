import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class YahooFinanceTest {
    public static void main(String[] args) {
        // Set the path for the WebDriver executable
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); // Update the path to your chromedriver

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to Yahoo Finance
            driver.get("https://finance.yahoo.com/");

            // Maximize browser window
            driver.manage().window().maximize();

            // Implicit wait
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

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
            TimeUnit.SECONDS.sleep(3);

            // Verify stock price is greater than $200
            WebElement stockPriceElement = driver.findElement(By.xpath("//fin-streamer[@data-field='regularMarketPrice']"));
            String stockPriceText = stockPriceElement.getText();
            double stockPrice = Double.parseDouble(stockPriceText.replace(",", ""));

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