import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessagesTest {

    public AndroidDriver<MobileElement> driver;
    public WebDriverWait wait;

    //Elements
    String tabMessages = "com.vkontakte.android:id/tab_messages";
    String dialogs_add = "com.vkontakte.android:id/dialogs_add";
    String new_conversation_button = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[6]/android.widget.TextView";
    String search = "com.vkontakte.android:id/search";
    String search_done = "com.vkontakte.android:id/search_done";
    String search_clear = "com.vkontakte.android:id/search_clear";
    String search_input = "com.vkontakte.android:id/search_input";
    String vk_im_title = "com.vkontakte.android:id/vkim_title";
    String create_new_chat = "com.vkontakte.android:id/create_new_chat";
    String dialogs_refresh_status = "com.vkontakte.android:id/vkim_dialogs_refresh_status";
    String title_dropdown = "com.vkontakte.android:id/title_dropdown";
    String subtitle_text = "com.vkontakte.android:id/subtitle_text";


    @BeforeMethod
    public void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", "Sony XZ");
        caps.setCapability("udid", "CB512DPQK5"); //DeviceId from "adb devices" command
        caps.setCapability("skipUnlock", "true");
        caps.setCapability("appPackage", "com.vkontakte.android");
        caps.setCapability("appActivity", ".MainActivity");
        caps.setCapability("unicodeKeyboard", "true");
        caps.setCapability("noReset", "true");
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        wait = new WebDriverWait(driver, 10);
    }


    @Test
    public void basicTest() throws InterruptedException {
        //Переходим в раздел Сообщения
        driver.findElementById(tabMessages).click();

        //Открываем меню создания новой беседы
        driver.findElementById(dialogs_add).click();
        driver.findElementByXPath(new_conversation_button).click();

        //Поиск и добавление пользователя по имени
        driver.findElementById(search).click();
        driver.findElementById(search).sendKeys("Елисей");

        //Анализ результатов поиска
        boolean flag = false;
        List<MobileElement> elements = (List<MobileElement>) driver.findElementsByClassName("android.widget.TextView");
        for (MobileElement element : elements) {
            if (element.getText().contains("Елисей")) {
                element.click();
                flag = true;
                break;
            }
        }
        Assert.assertEquals(flag, true);

        //Поиск и добавление пользователя по имени и фамилии
        driver.findElementById(search_clear).click();
        driver.findElementById(search).sendKeys("Влад Камызякин");

        //Анализ результатов поиска
        flag = false;
        elements.clear();
        elements = (List<MobileElement>) driver.findElementsByClassName("android.widget.TextView");
        for (MobileElement element : elements) {
            if (element.getText().contains("Камызякин")) {
                element.click();
                flag = true;
                break;
            }
        }
        Assert.assertEquals(flag, true);


        //Завершаем поиск
        driver.findElementById(search_clear).click();
        driver.findElementById(search_done).click();

        //Вводим название беседы
        driver.findElementById(vk_im_title).click();
        driver.findElementById(vk_im_title).sendKeys("Test");

        //Проверяем список пользователей в беседе
        int counter = 0;
        elements.clear();
        elements = (List<MobileElement>) driver.findElementsByClassName("android.widget.TextView");
        for (MobileElement element : elements) {
            if (element.getText().contains("Камызякин") || element.getText().contains("Елисей")) {
                counter++;
            }
        }
        Assert.assertEquals(counter, 2);


        //Создаем беседу и проверяем название, количество участников
        driver.findElementById(create_new_chat).click();
        //driver.getScreenshotAs("png");
        driver.findElementById(title_dropdown).click();
        Assert.assertEquals(driver.findElementById(title_dropdown).getText(), "Test");
        Assert.assertEquals(driver.findElementById(subtitle_text).getText(),"2");




    }

    @AfterMethod
    public void teardown() {
        //driver.resetApp();
        driver.quit();
    }
}