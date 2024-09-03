package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */

    public void topicSearch(String topic, WebDriver driver) {
        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'" + topic + "')]"));
        element.click();
    }

    public Float webElementToNumber(WebElement element, WebDriver driver) {
        String s = element.getText();
        float number = Float.parseFloat(s);
        return number;
    }

}
