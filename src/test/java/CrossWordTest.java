import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CrossWordTest {
    WebDriver driver;
    WebDriverWait wait;
    @BeforeClass
    public void setupDriver()
    {

        WebDriverManager.chromedriver().setup();
        driver =new ChromeDriver();
        wait=new WebDriverWait(driver,Duration.ofSeconds(20));
        driver.get("https://www.crossword.in/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        System.out.println("we are in crossworld");

    }
    @Test
    public void test1() throws InterruptedException {
        System.out.println("in test 1");
        driver.getTitle();
        WebElement search_textbox=driver.findElement(By.xpath("(//input[@class='main-search__input wizzy-search-input'])[1]"));
        search_textbox.sendKeys("manifest");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class,'wizzy-autocomplete-top-products-view-more')]")));

        driver.findElement(By.xpath("//button[contains(@class,'wizzy-autocomplete-top-products-view-more')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated( By.xpath("(//a[@class='wizzy-result-product-item'])[1]")));
        driver.findElement(By.xpath("//div[@class='wizzy-common-select-selector']")).click();
        driver.findElement(By.xpath("//div[contains(@title,'Price: Low to High')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated( By.xpath("(//a[@class='wizzy-result-product-item'])[1]")));
        Thread.sleep(5000);
        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        List<WebElement> products = driver.findElements(By.xpath("//div[@class='wizzy-product-item-price ']"));
        List<WebElement> products_names = driver.findElements(By.xpath("//p[@class='product-item-title']"));
        List<Integer> discounted_price=new ArrayList<>();
        List<Integer> non_discounted_price=new ArrayList<>();
        List<String> book_names=new ArrayList<>();
        for(int i=1;i<=products.size();i++)
        {
            String template = "(//div[@class='wizzy-product-item-price '])[{i}]";
            String val=String.valueOf(i);
            String message = template.replace("{i}",val);
            //System.out.println(message);
            List<WebElement> is_discount = driver.findElements(By.xpath(message+"//span"));
            String book="(//p[@class='product-item-title'])[{i}]";
            String book1=String.valueOf(i);
            String book_name = template.replace("{i}",book1);

            if(is_discount.size()>0)
            {
                String value=is_discount.get(0).getText();
                value.trim();
                StringBuilder sb = new StringBuilder(value);
                // Remove "world" by index
                sb.delete(0,1);
                int price=Integer.parseInt(String.valueOf(sb));
                discounted_price.add(price);
                //System.out.println(price);
                //discounted price book names
                String books_name_xpath = "(//p[@class='product-item-title'])[{i}]";
                String book_xpath = template.replace("{i}",String.valueOf(i));
                String books_name=driver.findElement(By.xpath(book_xpath)).getText().trim();
                book_names.add(books_name);
                System.out.println(books_name);
            }
            else
            {
                //String s=driver.findElement(By.xpath(message)).getText().trim();
                //StringBuilder sb = new StringBuilder(s);
                // Remove "world" by index
                //sb.delete(0,1);
                //int price=Integer.parseInt(String.valueOf(sb));
                //non_discounted_price.add(price);
                //System.out.println(price);

            }

        }
        List<Integer> copied_list=new ArrayList<>(non_discounted_price);
        Collections.sort(copied_list);
        System.out.println("Original List: " + non_discounted_price);
        System.out.println("Copied and Sorted List: " + copied_list);
        Assert.assertEquals(non_discounted_price,copied_list,"The non discounted price are not in sorted order");
        List<String> copied_booklist=new ArrayList<>(book_names);
        System.out.println("Discounted book names: " + copied_booklist);

    }
    @Test
    public void applySort()
    {
        }
    @Test
    public void test3()
    {
        System.out.println("in test 3");
    }
    @AfterClass
    public void closeDriver()
    {
        driver.quit();
        System.out.println("in close driver");
        System.out.println("my name is rupam");
        System.out.println("hello");
    }

}
