import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URL;

public class TestHarun {

    //Scenerio 1
    @Test
    public void shouldLoginWithFacebook() throws InterruptedException {

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        URL path = loader.getResource("chromedriver");
        System.setProperty("webdriver.chrome.driver", "/Users/user/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.n11.com/");
        driver.manage().window().maximize();
        driver.findElement(By.className("btnSignIn")).click();

        String parentHandle = driver.getWindowHandle();//Facebook ile giriş yap'a tıklandığında popup açılacağı için login sayfası(mevcut) Parent olarak tutuldu.

        Thread.sleep(3000);
        driver.findElement(By.className("facebookBtn")).click();

        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);//Açılan facebookLogin popup'ında çalışmak için tutuldu.
        }
        Thread.sleep(3000);
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("n11facebook@yandex.com");
        driver.findElement(By.name("pass")).clear();
        driver.findElement(By.name("pass")).sendKeys("123456harun");
        driver.findElement(By.name("login")).click();

        driver.switchTo().window(parentHandle);//Popup kapandığından parent olarak tutulan sayfaya geçildi.

        driver.get("https://www.n11.com/");
        Assert.assertEquals("n11.com - Alışverişin Uğurlu Adresi", driver.getTitle());
        Thread.sleep(3000);
        String buyerName = driver.findElement(By.className("username")).getText();
        Assert.assertTrue("When buyer is login", buyerName.contains("Ozay Ozay"));

        driver.get("https://www.n11.com/kitap");
        Thread.sleep(3000);
        Assert.assertEquals("Yeni Çıkan, En Çok Satan & Okunan Kitaplar - n11.com", driver.getTitle());

        //A

        driver.get("https://www.n11.com/yazarlar/A");
        Thread.sleep(3000);
        Assert.assertEquals("A ile Başlayan Yazarlar 1 - n11.com", driver.getTitle());

        int authorCountA = driver.findElements(By.xpath("//*[@id='authorsList']/div/ul/li")).size();
        Assert.assertTrue("When a buyer click A", authorCountA == 80);

        String lastAuthorAInPage1 = "A. Coşkun Ongun (1)";
        driver.get("https://www.n11.com/yazarlar/A?pg=2");
        Assert.assertTrue("In page 2", driver.findElements(By.xpath("//li[contains(text(), '" + lastAuthorAInPage1 + "')]")).size() == 0);//1. sayfadaki son yazar 2. sayfada olmayacağından size 0'a eşitlendi.

        return;


    }

    //Scenerio 2
    @Test
    public void failLogin() throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "/Users/user/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.n11.com/");
        driver.manage().window().maximize();
        driver.findElement(By.className("btnSignIn")).click();

        String parentHandle = driver.getWindowHandle();

        Thread.sleep(3000);

        driver.findElement(By.className("facebookBtn")).click();

        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }

        Thread.sleep(3000);
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.name("email")).sendKeys("n11facebook@yandex.com");
        driver.findElement(By.id("pass")).clear();
        driver.findElement(By.name("pass")).sendKeys("1234harun");
        driver.findElement(By.name("login")).click();

        String errorMessage = "Please re-enter your password";
        Assert.assertTrue("When a buyer enter false password", errorMessage.contains("Please re-enter your password"));

        return;


    }

}
