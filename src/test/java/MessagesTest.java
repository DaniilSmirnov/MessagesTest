import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
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
    String toolbar = "com.vkontakte.android:id/toolbar";
    String subtitle_text = "com.vkontakte.android:id/subtitle_text";
    String about = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.view.ViewPager/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]";
    String count = "com.vkontakte.android:id/count";
    String kick = "com.vkontakte.android:id/kick";
    String invite = "com.vkontakte.android:id/invite";

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
    public void CreateConverstionTest() throws InterruptedException {
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
        driver.findElementById(vk_im_title).sendKeys("Appium");

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


        //Создаем беседу
        driver.findElementById(create_new_chat).click();
        //driver.getScreenshotAs("png")
    }

    @Test
    public void ConversationTest(){
        //Переходим в раздел Сообщения
        driver.findElementById(tabMessages).click();

        //Ищем диалог и заходим в него
        List<MobileElement> elements = (List<MobileElement>) driver.findElementsByClassName("android.widget.TextView");
        for (MobileElement element : elements) {
            if (element.getText().contains("Appium")) {
                element.click();
                break;
            }
        }

        //Проверяем беседу созданную до этого
        //Assert.assertEquals(driver.findElementById(dialogs_refresh_status).getText(), "Appium");
        Assert.assertEquals(driver.findElementById(subtitle_text).getText(),"2 участника");

        //Раскрываем меню
        driver.findElementById(toolbar).click();

        //Переходим в информацию о беседе
        driver.findElementByXPath(about).click();

        //Проверяем название беседы в поле
        Assert.assertEquals(driver.findElementByClassName("android.widget.EditText").getText(), "Appium");

        //Проверяем количество участников
        Assert.assertEquals(driver.findElementById(count).getText(), "2 участника");

        //Исключаем участника
        int x = 10;
        int y = 0;
        while (true) {
            new TouchAction(driver).press(PointOption.point(x, y)).moveTo(PointOption.point(x, y+=10)).release().perform();
            if (driver.findElementById(kick).isDisplayed()){
                driver.findElementById(kick).click();
                break;
            }
        }

        //Проверяем количество участников
        x = 10;
        y = 0;
        while (true) {
            new TouchAction(driver).press(PointOption.point(x, y)).moveTo(PointOption.point(x, y+=10)).release().perform();
            if (driver.findElementById(count).isDisplayed()){
                Assert.assertEquals(driver.findElementById(count).getText(), "1 участник");
                break;
            }
        }

        //Добавляем участника в беседу
        driver.findElementById(invite).click();
        driver.findElementById(search).click();
        driver.findElementById(search).sendKeys("Влад Камызякин");
        elements.clear();
        elements = (List<MobileElement>) driver.findElementsByClassName("android.widget.TextView");
        for (MobileElement element : elements) {
            if (element.getText().contains("Камызякин")) {
                element.click();
                break;
            }
        }
        driver.findElementById(search_clear).click();
        driver.findElementById(search_done).click();

        //Проверяем количество
        x = 10;
        y = 0;
        while (true) {
            new TouchAction(driver).press(PointOption.point(x, y)).moveTo(PointOption.point(x, y+=10)).release().perform();
            if (driver.findElementById(count).isDisplayed()){
                Assert.assertEquals(driver.findElementById(count).getText(), "1 участник");
                break;
            }
        }



    }

    @AfterMethod
    public void teardown() {
        //driver.resetApp();
        driver.quit();
    }
}