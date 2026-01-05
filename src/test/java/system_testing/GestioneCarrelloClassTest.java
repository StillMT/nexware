package system_testing;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Case: Gestione Carrello - Eliminazione prodotto
 * Riferimento PDF: Sezione 1.2
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneCarrelloClassTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    // CONFIGURAZIONE: Adatta questi parametri al tuo ambiente
    private static final String HOME_URL = "http://localhost/";
    private static final String BASE_URL = HOME_URL + "myNexware";
    private static final String TEST_USER = "test";
    private static final String TEST_PWD = "test____";

    // ID del prodotto da usare per il test
    private static final int ID_PRODOTTO = 1;

    @BeforeAll
    static void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Login (Pre-condizione Globale)
        driver.get(BASE_URL + "/login");

        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username_login")));
        WebElement passField = driver.findElement(By.id("password_login"));
        WebElement submitBtn = driver.findElement(By.cssSelector("#login-form input[type='submit']"));

        userField.sendKeys(TEST_USER);
        passField.sendKeys(TEST_PWD);
        submitBtn.click();

        // Attesa login completato
        wait.until(ExpectedConditions.urlToBe(HOME_URL));
    }

    @BeforeEach
    void ensureProductInCart() {
        // PRE-CONDITION: Il prodotto DEVE essere nel carrello prima di ogni test.
        driver.get(BASE_URL + "/cart/addProduct?p=" + ID_PRODOTTO);
        wait.until(ExpectedConditions.urlContains("/cart/"));
    }

    @Test
    @Order(1)
    @DisplayName("TC_2.2 - Eliminazione corretta di un prodotto")
    void testEliminazioneProdottoSuccesso() {
        // Identifica il pulsante "Rimuovi" specifico per il prodotto ID_PRODOTTO
        By removeBtnLocator = By.cssSelector(".item-remove[data-id='" + ID_PRODOTTO + "']");
        WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(removeBtnLocator));

        // Memorizza l'elemento riga (.item) per verificare dopo che sia sparito
        WebElement itemRow = removeBtn.findElement(By.xpath("./ancestor::div[contains(@class, 'item')][1]"));

        // Click su Rimuovi
        removeBtn.click();

        // Oracolo: Il prodotto viene eliminato
        wait.until(ExpectedConditions.stalenessOf(itemRow));

        // Verifica ulteriore: il bottone non deve più esistere nella pagina
        assertTrue(driver.findElements(removeBtnLocator).isEmpty(), "Il prodotto è stato rimosso dalla visualizzazione.");
    }

    @Test
    @Order(2)
    @DisplayName("TC_2.1 - Fallimento eliminazione (Errore di Sincronizzazione)")
    void testEliminazioneProdottoFallimento() {
        // Salva l'handle della scheda corrente (Scheda 1)
        String originalTab = driver.getWindowHandle();

        // Apri una nuova scheda (Scheda 2)
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(BASE_URL + "/cart/");

        // Nella Scheda 2: Rimuovi il prodotto realmente
        By removeBtnLocator = By.cssSelector(".item-remove[data-id='" + ID_PRODOTTO + "']");
        WebElement removeBtnTab2 = wait.until(ExpectedConditions.elementToBeClickable(removeBtnLocator));
        removeBtnTab2.click();

        // Attendi che la rimozione sia completata nella scheda 2
        WebElement itemRowTab2 = removeBtnTab2.findElement(By.xpath("./ancestor::div[contains(@class, 'item')][1]"));
        wait.until(ExpectedConditions.stalenessOf(itemRowTab2));

        // Chiudi la Scheda 2 e torna alla Scheda 1
        driver.close();
        driver.switchTo().window(originalTab);

        // Nella Scheda 1: Il prodotto è ANCORA visibile.
        // Prova a cliccare "Rimuovi" sullo stesso prodotto.
        WebElement removeBtnTab1 = wait.until(ExpectedConditions.elementToBeClickable(removeBtnLocator));
        removeBtnTab1.click();

        // Oracolo: Apparizione del Popup di errore specifico
        // Cerchiamo il testo specifico del popup definito nel JS
        By popupMessageLocator = By.xpath("//*[contains(text(), 'Errore di sincronizzazione con il database')]");
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(popupMessageLocator));

        assertTrue(popup.isDisplayed(), "Dovrebbe apparire il popup di errore sincronizzazione.");
    }

    @AfterAll
    static void tearDown() {
        if (driver != null)
            try {
                // Logout pulito
                driver.get(BASE_URL + "/logout");
            } catch (Exception e) {
                System.err.println("Logout non riuscito: " + e.getMessage());
            } finally {
                driver.quit();
            }
    }
}