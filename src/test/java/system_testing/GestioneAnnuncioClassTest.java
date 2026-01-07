package system_testing;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneAnnuncioClassTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final String HOME_URL = "http://localhost/";
    private static final String BASE_URL = HOME_URL + "myNexware";
    private static final String TEST_USER = "test";
    private static final String TEST_PWD = "test____";

    @BeforeAll
    static void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Login Pre-condizione
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username_login"))).sendKeys(TEST_USER);
        driver.findElement(By.id("password_login")).sendKeys(TEST_PWD);
        driver.findElement(By.cssSelector("#login-form input[type='submit']")).click();
        wait.until(ExpectedConditions.urlToBe(HOME_URL));

        driver.get(BASE_URL + "/products/");
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/products/"));
    }

    @Test
    @Order(1)
    @DisplayName("TC_3.1 - Titolo VUOTO")
    void testTC_3_1() {

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.className("add-product")));
        addBtn.click();


        WebElement nomeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nomeField.clear();
        nomeField.sendKeys("");


        driver.findElement(By.id("description")).sendKeys("Descrizione valida per superare i controlli.");
        driver.findElement(By.id("price")).sendKeys("49.99");
        driver.findElement(By.id("stock")).sendKeys("10");


        WebElement form = driver.findElement(By.id("product-form"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);

        // 5. Oracolo: La Servlet deve impostare err = "INV_PARAM"
        WebElement errorDiv = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error")));

        String text = errorDiv.getText();
        assertTrue(text.contains("Dati non validi"), "Il sistema deve impedire la pubblicazione senza titolo.");
    }

    @Test
    @Order(2)
    @DisplayName("TC_3.2 - Descrizione VUOTA")
    void testTC_3_2() {
        driver.get(BASE_URL + "/products/");
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.className("add-product")));
        addBtn.click();

        WebElement nomeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nomeField.clear();
        nomeField.sendKeys("Software Protezione Pro"); // Titolo valido

        WebElement descField = driver.findElement(By.id("description"));
        descField.clear();
        descField.sendKeys(""); // Valore: VUOTO (Caso di test 3.2)

        driver.findElement(By.id("price")).sendKeys("29.99");
        driver.findElement(By.id("stock")).sendKeys("50");

        WebElement form = driver.findElement(By.id("product-form"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);

        // 5. Oracolo: Il sistema deve rilevare la descrizione non valida (INV_PARAM)
        WebElement errorDiv = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error")));

        String text = errorDiv.getText();
        assertTrue(text.contains("Dati non validi"), "Il sistema deve impedire la pubblicazione con descrizione vuota.");
    }

    @Test
    @Order(3)
    @DisplayName("TC_3.3 - Prezzo non VALIDO")
    void testTC_3_3() {
        driver.get(BASE_URL + "/products/");
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.className("add-product")));
        addBtn.click();

        WebElement nomeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nomeField.sendKeys("Antivirus Premium");

        driver.findElement(By.id("description")).sendKeys("Licenza annuale per protezione completa.");

        WebElement priceField = driver.findElement(By.id("price"));
        priceField.clear();
        priceField.sendKeys("-10.00"); // Valore non permesso

        driver.findElement(By.id("stock")).sendKeys("100");

        WebElement form = driver.findElement(By.id("product-form"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);

        // 5. Oracolo: Il sistema deve mostrare 'Dati non validi'
        WebElement errorDiv = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error")));

        assertTrue(errorDiv.getText().contains("Dati non validi"),
                "Il sistema deve bloccare l'inserimento di un prezzo negativo.");
    }

    @Test
    @Order(4)
    @DisplayName("TC_3.4 - Stock non VALIDO")
    void testTC_3_4() {
        driver.get(BASE_URL + "/products/");
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.className("add-product")));
        addBtn.click();

        WebElement nomeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nomeField.sendKeys("Firewall Business Edition");

        driver.findElement(By.id("description")).sendKeys("Soluzione firewall avanzata per piccole imprese.");
        driver.findElement(By.id("price")).sendKeys("150.00");

        WebElement stockField = driver.findElement(By.id("stock"));
        stockField.clear();
        stockField.sendKeys("-5"); // Valore non permesso per le licenze

        WebElement form = driver.findElement(By.id("product-form"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);

        // 5. Oracolo: Il sistema deve mostrare 'Dati non validi'
        WebElement errorDiv = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error")));

        assertTrue(errorDiv.getText().contains("Dati non validi"),
                "Il sistema deve impedire l'inserimento di uno stock negativo.");
    }

    @Test
    @Order(5)
    @DisplayName("TC_3.5 - Nome già ESISTENTE")
    void testTC_3_5() {
        driver.get(BASE_URL + "/products/");
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.className("add-product")));
        addBtn.click();

        WebElement nomeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nomeField.sendKeys("Antivirus Premium"); // Nome duplicato

        driver.findElement(By.id("description")).sendKeys("Descrizione valida per il test.");
        driver.findElement(By.id("price")).sendKeys("19.99");
        driver.findElement(By.id("stock")).sendKeys("10");

        WebElement form = driver.findElement(By.id("product-form"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);

        // 5. Oracolo: Il sistema deve mostrare 'Dati non validi' (nome duplicato)
        WebElement errorDiv = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error")));

        String text = errorDiv.getText();
        assertTrue(text.contains("Dati non validi"),
                "Il sistema deve impedire la creazione di prodotti con nomi duplicati.");
    }

    @Test
    @Order(6)
    @DisplayName("TC_3.6 - Quantità non valida")
    void testTC_3_6() {
        driver.get(BASE_URL + "/products/");
        wait.until(ExpectedConditions.elementToBeClickable(By.className("add-product"))).click();

        // Dati da immagine TC_3.6
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Antivirus Pro 2025");
        driver.findElement(By.id("description")).sendKeys("Software antivirus professionale con protezione in tempo reale, firewall integrato e aggiornamenti automatici.");
        driver.findElement(By.id("category")).sendKeys("Sicurezza Informatica");
        driver.findElement(By.id("price")).sendKeys("49");

        WebElement stockField = driver.findElement(By.id("stock"));
        stockField.clear();
        stockField.sendKeys("-1");

        WebElement form = driver.findElement(By.id("product-form"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);

        // Oracolo: Errore INV_PARAM
        WebElement errorDiv = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error")));
        assertTrue(errorDiv.getText().contains("Dati non validi"), "Il sistema deve bloccare quantità negativa.");
    }

    @Test
    @Order(7)
    @DisplayName("TC_3.7 - Pubblicazione corretta")
    void testTC_3_7() {
        driver.get(BASE_URL + "/products/");
        wait.until(ExpectedConditions.elementToBeClickable(By.className("add-product"))).click();

        String nome = "Antivirus Pro 2025 " + System.currentTimeMillis();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(nome);
        driver.findElement(By.id("description")).sendKeys("Software antivirus professionale con protezione in tempo reale, firewall integrato e aggiornamenti automatici.");
        driver.findElement(By.id("category")).sendKeys("Sicurezza Informatica");
        driver.findElement(By.id("price")).sendKeys("49");

        WebElement stockField = driver.findElement(By.id("stock"));
        stockField.clear();
        stockField.sendKeys("10");

        WebElement form = driver.findElement(By.id("product-form"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);

        // Oracolo: Successo
        wait.until(ExpectedConditions.urlContains("/products/"));
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-message")));

        wait.until(ExpectedConditions.textToBePresentInElement(successMsg, "inserito correttamente"));
        assertTrue(successMsg.getText().contains("inserito correttamente"));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) driver.quit();
    }
}
