package system_testing;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Case: Gestione Catalogo - Aggiunta prodotto al carrello
 * Riferimento PDF: Sezione 1.1
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneCatalogoClassTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final String HOME_URL = "http://localhost/";
    private static final String BASE_URL = HOME_URL + "myNexware";
    private static final String RESERVED_AREA_URL = "http://localhost/myNexware";
    private static final String TEST_USER = "test";
    private static final String TEST_PWD = "test____";

    // ID Prodotti per il test
    private static final int AVAILABLE_PRODUCT = 2;
    private static final int NOT_AVAILABLE_PRODUCT = 1000; // Prodotto inesistente oppure 'HIDDEN' o 'CANCELED'

    @BeforeAll
    static void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // PRE-CONDITION GLOBALE: Login
        driver.get(RESERVED_AREA_URL + "/login");

        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username_login")));
        WebElement passField = driver.findElement(By.id("password_login"));
        WebElement submitBtn = driver.findElement(By.cssSelector("#login-form input[type='submit']"));

        userField.sendKeys(TEST_USER);
        passField.sendKeys(TEST_PWD);
        submitBtn.click();

        // Verifica redirect alla home (Login avvenuto)
        wait.until(ExpectedConditions.urlToBe(HOME_URL));
    }

    @Test
    @Order(1)
    @DisplayName("TC_1.2 - Aggiunta prodotto valido e disponibile")
    void testAggiuntaProdottoValido() {
        // Elimino preventivamente il prodotto che andremo a inserire
        driver.get(BASE_URL + "/cart/removeProduct?p=" + AVAILABLE_PRODUCT);
        wait.until(d -> {
            String bodyText = d.findElement(By.tagName("body")).getText();
            return bodyText != null && !bodyText.trim().isEmpty();
        });

        // Naviga alla pagina del prodotto
        driver.get(HOME_URL + "/catalogue/view/?p=" + AVAILABLE_PRODUCT);

        // Clicca su "Aggiungi al carrello"
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".add-to-cart")));
        addToCartBtn.click();

        // Verifica redirect al carrello
        wait.until(ExpectedConditions.urlContains("/cart/"));

        // Oracolo: Il prodotto viene aggiunto correttamente al carrello
        WebElement messageBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("adding-message")));

        String messageText = messageBox.getText().trim();

        // Controllo che non contenga la classe "error"
        assertFalse(messageBox.getAttribute("class").contains("error"), "Il messaggio non dovrebbe indicare errore");

        // Controllo del testo
        assertTrue(messageText.contains("Prodotto aggiunto correttamente"), "Messaggio atteso: 'Prodotto aggiunto correttamente', Trovato: " + messageText);
    }

    @Test
    @Order(2)
    @DisplayName("TC_1.1: Prodotto Già Nel Carrello")
    void testAggiuntaProdottoGiaNelCarrello() {
        // Naviga alla pagina del prodotto
        driver.get(HOME_URL + "/catalogue/view/?p=" + AVAILABLE_PRODUCT);

        // Clicca su "Aggiungi al carrello"
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".add-to-cart")));
        addToCartBtn.click();

        // Verifica redirect al carrello
        wait.until(ExpectedConditions.urlContains("/cart/"));


        // Navighiamo alla pagina dello stesso prodotto
        driver.get(HOME_URL + "/catalogue/view/?p=" + AVAILABLE_PRODUCT);

        // Clicca su "Aggiungi al carrello" e verifichiamo il redirect
        WebElement addToCartBtnAgain = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".add-to-cart")));
        addToCartBtnAgain.click();

        wait.until(ExpectedConditions.urlContains("/cart/"));

        // Oracolo: Il prodotto non viene aggiunto al carrello
        WebElement messageBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("adding-message")));
        String messageText = messageBox.getText().trim();

        // Controllo che non contenga la classe "error"
        assertTrue(messageBox.getAttribute("class").contains("error"),
                "Il messaggio dovrebbe indicare errore ");

        // Verifica del testo specifico
        assertTrue(messageText.contains("Errore durante l'aggiunta del prodotto."),
                "Messaggio atteso: 'Errore durante l'aggiunta del prodotto.', Trovato: " + messageText);
    }


    @AfterAll
    static void tearDown() {
        if (driver != null)
            try {
                // Effettua il logout
                driver.get(RESERVED_AREA_URL + "/logout");

                // Verifica che il logout sia avvenuto
                wait.until(ExpectedConditions.urlToBe(HOME_URL));
            } catch (Exception e) {
                System.out.println("Errore durante il logout: " + e.getMessage());
            } finally {
                // Chiude la finestra del browser e termina il processo del driver
                driver.quit();
            }
    }
}