package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

import org.testng.Assert;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest() {
        // driver.close();
        // driver.quit();

    }

     @Test
    public void testCase01() throws InterruptedException, StreamWriteException, DatabindException, IOException {

        driver.get("https://www.scrapethissite.com/pages/");
        Wrappers wrap = new Wrappers();
        wrap.topicSearch("Hockey Teams: Forms, Searching and Pagination", driver);
        List<Map<String, Object>> teamDataList = new ArrayList<>();
        // int total = 0;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@class='pagination']/li/a")));
        for (int i = 1; i < 5; i++) {
            WebElement page = driver.findElement(By.xpath("(//ul[@class='pagination']//a)[" + (i + 1) + "]"));
            page.click();
            Thread.sleep(2000);
            List<WebElement> winPercentages = driver.findElements(By.xpath("//td[contains(@class,'pct text')]"));
            for (int j = 0; j < winPercentages.size(); j++) {
                WebElement element = winPercentages.get(j);
                float wins = wrap.webElementToNumber(element, driver);
                if (wins < 0.40) {
                    WebElement teamName = element.findElement(By.xpath("./preceding-sibling::td[@class='name']"));
                    WebElement year = element.findElement(By.xpath("./preceding-sibling::td[@class='year']"));
                    long currentEpochTime = System.currentTimeMillis();
                    // System.out.println("Current Epoch Time in milliseconds: " +
                    // currentEpochTime);
                    // System.out.println("Team Name :" + teamName.getText());
                    // System.out.println("Year :" + year.getText());
                    // System.out.println("Win% :" + element.getText());
                    // total++;

                    Map<String, Object> teamData = new HashMap<>();
                    teamData.put("Epoch Time", currentEpochTime);
                    teamData.put("Team Name", teamName.getText());
                    teamData.put("Year", year.getText());
                    teamData.put("Win %", element.getText());
                    teamDataList.add(teamData);

                }
            }

        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String userDir = System.getProperty("user.dir");
            File jsonFile = new File(userDir + "\\src\\test\\resources\\hockey-team-data.json");
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(userDir + "\\src\\test\\resources\\hockey-team-data.json"), teamDataList);
            System.out.println("JSON data written to :" + jsonFile.getAbsolutePath());
            Assert.assertTrue(jsonFile.length() != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCase02() throws InterruptedException, IOException {
        driver.get("https://www.scrapethissite.com/pages/");
        Wrappers wrap = new Wrappers();
        wrap.topicSearch("Oscar Winning Films", driver);
        List<Map<String, Object>> teamDataList = new ArrayList<>();

        List<WebElement> years = driver.findElements(By.xpath("(//a[contains(@class,'year-link')])"));
        for (int i = 0; i < years.size(); i++) {
            years.get(i).click();
            Thread.sleep(3000);
            // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            // wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//a[contains(@class,'year-link')])")));

            List<WebElement> filmsList = driver.findElements(By.xpath("(//tr[contains(@class,'film')])"));
            for (int j = 0; j < 5; j++) {
                long currentEpochTime = System.currentTimeMillis();
                String year = years.get(i).getText();

                WebElement titleElement = filmsList.get(j).findElement(By.xpath("./td[@class='film-title']"));
                String title = titleElement.getText();
                WebElement nominationsElement = filmsList.get(j)
                        .findElement(By.xpath("./td[@class='film-nominations']"));
                String nominations = nominationsElement.getText();
                WebElement awardsElement = filmsList.get(j).findElement(By.xpath("./td[@class='film-awards']"));
                String awards = awardsElement.getText();

                // System.out.println("Year :" + year);
                // System.out.println("Title :" + title);
                // System.out.println("Nomination :" + nominations);
                // System.out.println("Awards :" + awards);

                boolean winner = false;
                try {
                    WebElement bestPicture = filmsList.get(j)
                            .findElement(By.xpath("./td[@class='film-best-picture']//i"));
                    // System.out.println("isWinner : True");
                    winner = true;
                } catch (Exception e) {
                    // System.out.println("isWinner : False");
                }
                Map<String, Object> teamData = new HashMap<>();
                teamData.put("Epoch Time", currentEpochTime);
                teamData.put("Year", year);
                teamData.put("Title", title);
                teamData.put("Nomination", nominations);
                teamData.put("Awards", awards);
                teamData.put("isWinner", winner);
                teamDataList.add(teamData);

            }

        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String userDir = System.getProperty("user.dir");
            File jsonFile = new File(userDir + "\\src\\test\\resources\\oscar-winner-data.json");
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(userDir + "\\src\\test\\resources\\oscar-winner-data.json"), teamDataList);
            System.out.println("JSON data written to :" + jsonFile.getAbsolutePath());
            Assert.assertTrue(jsonFile.length() != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}