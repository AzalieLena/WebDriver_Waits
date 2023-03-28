package com.softserve.edu;

import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WebDriverTest {
	//private final String BASE_URL = "https://devexpress.github.io/devextreme-reactive/react/grid/docs/guides/paging/";
	private final String BASE_URL ="https://devexpress.github.io/devextreme-reactive/react/grid/docs/guides/filtering/";

	private final Long IMPLICITLY_WAIT_SECONDS = 10L;
	private WebDriver driver;

	@BeforeClass
	public void beforeClass() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver(options);
		//driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
		driver.manage().window().maximize();
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		driver.get(BASE_URL);
		//
		closePopup();
	}

	private void closePopup() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        Duration fiveSeconds = Duration.ofSeconds(5);
        Duration oneSeconds = Duration.ofSeconds(1);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(fiveSeconds)
                .pollingEvery(oneSeconds)
                .ignoring(NoSuchElementException.class)
                .ignoring(NullPointerException.class)
                .ignoring(TimeoutException.class);
        WebElement footerButton = wait.until(new Function<WebDriver, WebElement>() {
          public WebElement apply(WebDriver driver) {
              System.out.println("\t***apply running ...");
            return driver.findElement(By.xpath("//footer[contains(@class,'cookie')]//button"));
          }
        });
        System.out.println("footerButton = " + footerButton);
        footerButton.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
    }
	
	@Test
	public void AjaxIframePage() {
		// Switch To IFrame
		driver.switchTo().frame(driver.findElement(By.xpath("//div[contains(@data-options,'filter-row')]//iframe")));
		// Search City Filter
		WebElement tdCityFilter = driver.findElement(By.xpath("//th[3]//input[@placeholder='Filter...']"));
		System.out.println("tdCityFilter.placeholder = " + tdCityFilter.getAttribute("placeholder"));
		tdCityFilter.clear();
		System.out.println("Print L");
		tdCityFilter.sendKeys("L");
		System.out.println("Refresh page... ");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
		(new WebDriverWait(driver, Duration.ofSeconds(10)))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[text()='Paris']")));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
		WebElement Las_Vegas2 = driver.findElement(By.xpath("//td[text()='Las Vegas']"));
		WebElement London2 = driver.findElement(By.xpath("//td[text()='London']"));
		System.out.println("Las_Vegas is displayed = " + Las_Vegas2.isDisplayed());
		System.out.println("Las_Vegas.getText() = " + Las_Vegas2.getText());
		System.out.println("London is displayed = " + London2.isDisplayed());
		System.out.println("London.getText() = " + London2.getText());
	}
}