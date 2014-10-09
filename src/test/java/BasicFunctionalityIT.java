/**
 * Created by Markus Kängsepp on 29.09.2014.
 */


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class BasicFunctionalityIT {

    private static final String pattyURL = "http://localhost:9090";
    private static final String jiraURL = "http://localhost:8080";
    private static final String pattyFormUrl = "http://localhost:9090/#/form";
    private static final String pattyLoginUrl = "http://localhost:9090/#/login";
    private static final String pattyLoginSubmittedUrl = "http://localhost:9090/#/submitted";
    private static final String pattyUser = "markus93";
    private static final String pattyPass = "pattyparool";
    private static final String jiraUser = "markus93";
    private static final String jiraPass = "pattyparool";
    private static final String assingee = "Markus Kängsepp";

    private WebDriver driver;
    private WebElement element;
    private String errorMsg;

    @Before
    public void openBrowser() {

        //booting Firefox - later should use non-browser option.
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //WebDriver looks for element up to 10 seconds
        driver.get(pattyURL);
    }

    @After
    public void closeBrowser() {
        driver.quit();
    }

    @Test
    public void login() throws Exception {

        //login part
        driver.findElement(By.name("username")).sendKeys(pattyUser);
        element = driver.findElement(By.name("password"));
        element.sendKeys(pattyPass);
        element.submit();

        //checking if user is directed to main view (currently pat form view)
        driver.findElement(By.name("to"));
        String message = "Log in was NOT successful (or user was not directed to form page.)";
        Assert.assertEquals(message, driver.getCurrentUrl(), pattyFormUrl);

    }

    @Test
    public void sendingPat() throws Exception {

        login();

        //for description and checking whether issue was sent to JIRA
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);

        //filling and sending form
        driver.findElement(By.name("to")).sendKeys(assingee);
        element = driver.findElement(By.id("description"));
        element.sendKeys("Test pat, sent at: " + time);
        element.submit();

        //check for alerts
        assertAlertPresent();

        //TODO check if user directed to submitted page, atm user isn't.
//        driver.findElement(By.name("username"));
//        errorMsg = "Pat sending was NOT successful (or user was not directed to submitted page)";
//        Assert.assertEquals(errorMsg, driver.getCurrentUrl(), pattySubmittedUrl);

        //checking if JIRA has received issue.
        driver.get(jiraURL);

        //login to JIRA
        driver.switchTo().frame("gadget-0");
        driver.findElement(By.name("os_username")).sendKeys(jiraUser);
        driver.findElement(By.name("os_password")).sendKeys(jiraPass);
        driver.findElement(By.id("login")).click();

        //look for issue
        driver.findElement(By.id("find_link")).click();
        driver.findElement(By.id("issues_new_search_link_lnk")).click();
        element = driver.findElement(By.id("searcher-query"));
        element.sendKeys(time, Keys.ENTER);

        errorMsg = "For some reason, issue was NOT sent to JIRA (when issue was in fact sent to JIRA, contact with test team.";
        Assert.assertEquals(errorMsg, driver.getPageSource().contains("No issues were found to match your search"), false);



    }

    @Test
    public void logout() throws Exception{

        login();

        //logging out
        driver.findElement(By.linkText("Log out")).click();
        driver.findElement(By.name("username"));
        errorMsg = "Log out was NOT successful (or user was not directed to login page.)";
        Assert.assertEquals(errorMsg, driver.getCurrentUrl(), pattyLoginUrl);

    }

    public void assertAlertPresent()
    {
        try{
            ExpectedConditions.alertIsPresent();
            String alertMsg = "Alert popped up with following message: " + driver.switchTo().alert().getText();
            Assert.fail(alertMsg);
        }catch (NoAlertPresentException Ex){}
    }

}