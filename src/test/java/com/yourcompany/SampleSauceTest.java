package com.yourcompany;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testobject.appium.TestObjectListenerProvider;
import org.testobject.appium.testng.TestObjectTestNGTestResultWatcher;
import org.testobject.appium.testng.TestObjectWatcherProvider;
import io.appium.java_client.android.AndroidDriver;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Simple TestNG test which demonstrates being instantiated via a DataProvider in order to supply multiple browser combinations.
 */

//@Listeners({ TestObjectTestNGTestResultWatcher.class })
public class SampleSauceTest /*implements TestObjectWatcherProvider*/ {

    private ThreadLocal<AndroidDriver> driver = new ThreadLocal<AndroidDriver>();
//    private TestObjectListenerProvider provider = TestObjectListenerProvider.newInstance();
    
    /**
     * DataProvider that explicitly sets the browser combinations to be used.
     *
     * @param testMethod
     * @return
     */
    @DataProvider(name = "hardCodedBrowsers", parallel = true)
    public static Object[][] sauceBrowserDataProvider(Method testMethod) {
        return new Object[][]{
                new Object[]{"ADD_DEVICE_ID_HERE"},
                new Object[]{"ADD_DEVICE_ID_HERE"},
                new Object[]{"ADD_DEVICE_ID_HERE"}
        };
    }

    /**
     * /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the platformName,
     * deviceName, platformVersion, and app and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @return
     * @throws MalformedURLException if an error occurs parsing the url
     */
    private WebDriver createDriver(String deviceId, String methodName) throws MalformedURLException {
    	
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("testobject_api_key", "ADD_API_KEY_HERE");
        capabilities.setCapability("testobject_device", deviceId);
        capabilities.setCapability("testobject_test_name",  methodName);

        driver.set(new AndroidDriver<WebElement>(
                new URL("http://appium.testobject.com/wd/hub"),
                capabilities));
//        provider.setDriver(driver.get());
//        provider.setLocalTest(false);
        return driver.get();
    }

    /**
     * Runs a simple test that clicks the add contact button.
     *
     * @param platformName Represents the platform to be run.
     * @param deviceName Represents the device to be tested on
     * @param platform Version Represents version of the platform.
     * @param app Represents the location of the app under test.
     * @throws Exception if an error occurs during the running of the test
     */
    @Test(dataProvider = "hardCodedBrowsers")
    public void addContactTest(String deviceId, Method method) throws Exception {
        WebDriver driver = createDriver(deviceId, method.getName());
        
        WebElement button = driver.findElement(By.className("android.widget.Button"));
        button.click();
        List<WebElement> textFieldsList = driver.findElements(By.className("android.widget.EditText"));
        textFieldsList.get(0).sendKeys("Some Name");
        textFieldsList.get(2).sendKeys("Some@example.com");
        button.click();
        
        driver.quit();
    }
    
    /**
     * @return the {@link WebDriver} for the current thread
     */
    public WebDriver getWebDriver() {
        return driver.get();
    }

//    @Override
//    public TestObjectListenerProvider getProvider() {
//        return provider.get();
//    }
}
