package system_testing;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Case: Gestione Utente - Registrazione al sistema
 * Riferimento PDF: Sezione 1.4 (TC_4.1 a TC_4.15)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneUtenteClassTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final String HOME_URL = "http://localhost/";
    private static final String BASE_URL = HOME_URL + "myNexware";

    @BeforeAll
    static void setUpClass() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @BeforeEach
    void goToRegisterPage() {
        // Logout preventivo
        driver.get(BASE_URL + "/logout");

        // Vai alla pagina di login
        driver.get(BASE_URL + "/login");

        // Clicca sul pulsante per mostrare il form di registrazione
        WebElement toggler = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-toggler")));
        toggler.click();

        // Attendi che il form di registrazione sia visibile
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("register-form")));
    }

    @Test
    @Order(1)
    @DisplayName("TC_4.1 - Username vuoto")
    void testUsernameVuoto() {
        final String expectedFinalMessage = "Alcuni campi sono invalidi o risultano già registrati. Controlla gli errori in rosso.";

        // Lasciamo username vuoto e proviamo a inviare
        WebElement submitBtn = driver.findElement(By.cssSelector("#register-form input[type='submit']"));
        submitBtn.click();

        // Oracolo: Appare il popup "Alcuni campi sono invalidi..."
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main-popup")));
        String actualPopupText = popup.getText().trim();

        assertTrue(popup.isDisplayed(), "Il popup dovrebbe essere visualizzabile dall'utente");
        assertTrue(actualPopupText.contains(expectedFinalMessage), "Il popup dovrebbe contenere: '" + expectedFinalMessage + "'" +
                " ma contiene '" + actualPopupText + "'");
    }

    @Test
    @Order(2)
    @DisplayName("TC_4.2 - Username formato errato")
    void testUsernameFormatoErrato() {
        WebElement userField = driver.findElement(By.id("username_register"));

        // Input invalido (troppo corto, < 3 caratteri)
        userField.sendKeys("ab");

        // Trigger evento 'blur' cliccando altrove
        driver.findElement(By.id("password_register")).click();

        // Oracolo: Appare il messaggio di errore specifico sotto il campo
        WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameError")));
        assertTrue(errorDiv.isDisplayed(), "Dovrebbe apparire l'errore inline per username formato errato.");
    }

    @Test
    @Order(3)
    @DisplayName("TC_4.3 - Password vuota")
    void testPasswordVuota() {
        final String expectedFinalMessage = "Alcuni campi sono invalidi o risultano già registrati. Controlla gli errori in rosso.";

        // Inseriamo la stringa 'Giovanni' nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys("Giovanni");

        // Lasciamo la password vuota e proviamo a inviare
        WebElement submitBtn = driver.findElement(By.cssSelector("#register-form input[type='submit']"));
        submitBtn.click();

        // Oracolo: Appare il popup "Alcuni campi sono invalidi..."
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main-popup")));
        String actualPopupText = popup.getText().trim();

        assertTrue(popup.isDisplayed(), "Il popup dovrebbe essere visualizzabile dall'utente");
        assertTrue(actualPopupText.contains(expectedFinalMessage), "Il popup dovrebbe contenere: '" + expectedFinalMessage + "'" +
                " ma contiene '" + actualPopupText + "'");
    }

    @Test
    @Order(4)
    @DisplayName("TC_4.4 - Password formato errato")
    void testPasswordFormatoErrato() {
        // Inseriamo la stringa 'Giovanni' nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys("Giovanni");

        // Input invalido (manca carattere speciale)
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Password123");

        // Oracolo: Appare il messaggio di errore password
        WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("passwordError")));
        assertTrue(errorDiv.isDisplayed());
    }

    @Test
    @Order(5)
    @DisplayName("TC_4.5 - Email vuota")
    void testEmailVuota() {
        final String expectedFinalMessage = "Alcuni campi sono invalidi o risultano già registrati. Controlla gli errori in rosso.";

        // Inseriamo la stringa 'Giovanni' nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys("Giovanni");

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Lasciamo la email vuota e proviamo a inviare
        WebElement submitBtn = driver.findElement(By.cssSelector("#register-form input[type='submit']"));
        submitBtn.click();

        // Oracolo: Appare il popup "Alcuni campi sono invalidi..."
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main-popup")));
        String actualPopupText = popup.getText().trim();

        assertTrue(popup.isDisplayed(), "Il popup dovrebbe essere visualizzabile dall'utente");
        assertTrue(actualPopupText.contains(expectedFinalMessage), "Il popup dovrebbe contenere: '" + expectedFinalMessage + "'" +
                " ma contiene '" + actualPopupText + "'");
    }

    @Test
    @Order(6)
    @DisplayName("TC_4.6 - Email formato errato")
    void testEmailFormatoErrato() {
        // Inseriamo la stringa 'Giovanni' nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys("Giovanni");

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Input invalido
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("Giovannipalatucci.ciao");

        // Trigger evento 'blur' cliccando altrove
        driver.findElement(By.id("password_register")).click();

        // Oracolo: Appare il messaggio di errore email
        WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("emailError")));
        assertTrue(errorDiv.isDisplayed());
    }

    @Test
    @Order(7)
    @DisplayName("TC_4.7 - Telefono vuoto")
    void testTelefonoVuoto() {
        final String expectedFinalMessage = "Alcuni campi sono invalidi o risultano già registrati. Controlla gli errori in rosso.";

        // Inseriamo la stringa 'Giovanni' nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys("Giovanni");

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa 'info@aziendatech.it' nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("info@aziendatech.it");

        // Lasciamo il telefono vuoto e proviamo a inviare
        WebElement submitBtn = driver.findElement(By.cssSelector("#register-form input[type='submit']"));
        submitBtn.click();

        // Oracolo: Appare il popup "Alcuni campi sono invalidi..."
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main-popup")));
        String actualPopupText = popup.getText().trim();

        assertTrue(popup.isDisplayed(), "Il popup dovrebbe essere visualizzabile dall'utente");
        assertTrue(actualPopupText.contains(expectedFinalMessage), "Il popup dovrebbe contenere: '" + expectedFinalMessage + "'" +
                " ma contiene '" + actualPopupText + "'");
    }

    @Test
    @Order(8)
    @DisplayName("TC_4.8 - Telefono formato errato")
    void testTelefonoFormatoErrato() {
        // Inseriamo la stringa 'Giovanni' nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys("Giovanni");

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa 'info@aziendatech.it' nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("info@aziendatech.it");

        // Input invalido
        WebElement telephoneField = driver.findElement(By.id("telephone"));
        telephoneField.sendKeys("352452ò156");

        // Trigger evento 'blur' cliccando altrove
        driver.findElement(By.id("password_register")).click();

        // Oracolo: Appare il messaggio di errore telefono
        WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("telephoneError")));
        assertTrue(errorDiv.isDisplayed());
    }

    @Test
    @Order(9)
    @DisplayName("TC_4.9 - Ripeti password vuota")
    void testRipetiPasswordVuota() {
        final String expectedFinalMessage = "Alcuni campi sono invalidi o risultano già registrati. Controlla gli errori in rosso.";

        // Inseriamo la stringa 'Giovanni' nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys("Giovanni");

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa 'info@aziendatech.it' nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("info@aziendatech.it");

        // Input invalido
        WebElement telephoneField = driver.findElement(By.id("telephone"));
        telephoneField.sendKeys("3524521560");

        // Lasciamo il campo ripeti password vuoto e proviamo a inviare
        WebElement submitBtn = driver.findElement(By.cssSelector("#register-form input[type='submit']"));
        submitBtn.click();

        // Oracolo: Appare il popup "Alcuni campi sono invalidi..."
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main-popup")));
        String actualPopupText = popup.getText().trim();

        assertTrue(popup.isDisplayed(), "Il popup dovrebbe essere visualizzabile dall'utente");
        assertTrue(actualPopupText.contains(expectedFinalMessage), "Il popup dovrebbe contenere: '" + expectedFinalMessage + "'" +
                " ma contiene '" + actualPopupText + "'");
    }

    @Test
    @Order(10)
    @DisplayName("TC_4.10 - Ripeti password formato errato")
    void testRipetiPasswordFormatoErrato() {
        // Inseriamo la stringa 'Giovanni' nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys("Giovanni");

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa 'info@aziendatech.it' nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("info@aziendatech.it");

        // Inseriamo la stringa '3524521560' nell'input del telefono
        WebElement telephoneField = driver.findElement(By.id("telephone"));
        telephoneField.sendKeys("3524521560");

        // Input invalido
        WebElement repeatPasswordField = driver.findElement(By.id("rep_password_register"));
        repeatPasswordField.sendKeys("Secure!122");

        // Oracolo: Appare il messaggio di errore ripeti password
        WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("repPasswordError")));
        assertTrue(errorDiv.isDisplayed());
    }

    @Test
    @Order(11)
    @DisplayName("TC_4.11 - Registrazione corretta (Azienda con Limitazioni)")
    void testRegistrazioneCorrettaAziendaLimitata() {
        // Generiamo dati univoci per evitare errori di "Utente già registrato" nei test di successo
        final String UNIQUE_SUFFIX = String.valueOf(System.currentTimeMillis());
        final String VALID_USER = "Giovanni" + UNIQUE_SUFFIX.substring(8);
        final String VALID_EMAIL = "test" + UNIQUE_SUFFIX + "@example.it";
        final String VALID_PHONE = "3" + (100000000L + new java.util.Random().nextInt(900000000));

        // Inseriamo la stringa random nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys(VALID_USER);

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa random nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(VALID_EMAIL);

        // Inseriamo la stringa random nell'input del telefono
        WebElement telephoneField = driver.findElement(By.id("telephone"));
        telephoneField.sendKeys(VALID_PHONE);

        // Inseriamo la stringa 'Secure!123' nell'input del ripeti password
        WebElement repeatPasswordField = driver.findElement(By.id("rep_password_register"));
        repeatPasswordField.sendKeys("Secure!123");

        // Invio
        driver.findElement(By.cssSelector("#register-form input[type='submit']")).click();

        // Oracolo: Redirect alla Home Page
        wait.until(ExpectedConditions.urlToBe(HOME_URL));
        assertEquals(HOME_URL, driver.getCurrentUrl(), "Il redirect alla Home Page non è avvenuto.");
    }

    @Test
    @Order(12)
    @DisplayName("TC_4.12 - Partita IVA formato errato")
    void testPartitaIvaErrata() {
        // Generiamo dati univoci per evitare errori di "Utente già registrato" nei test di successo
        final String UNIQUE_SUFFIX = String.valueOf(System.currentTimeMillis());
        final String VALID_USER = "Giovanni" + UNIQUE_SUFFIX.substring(8);
        final String VALID_EMAIL = "test" + UNIQUE_SUFFIX + "@example.it";
        final String VALID_PHONE = "3" + (100000000L + new java.util.Random().nextInt(900000000));

        // Inseriamo la stringa random nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys(VALID_USER);

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa random nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(VALID_EMAIL);

        // Inseriamo la stringa random nell'input del telefono
        WebElement telephoneField = driver.findElement(By.id("telephone"));
        telephoneField.sendKeys(VALID_PHONE);

        // Inseriamo la stringa 'Secure!123' nell'input del ripeti password
        WebElement repeatPasswordField = driver.findElement(By.id("rep_password_register"));
        repeatPasswordField.sendKeys("Secure!123");

        // Abilita campi azienda e attendi che i campi diventino visibili
        driver.findElement(By.id("add_info")).click();

        // Input invalido
        WebElement vatField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vat")));
        vatField.sendKeys("12345");

        // Clicca sul pulsante "Valida" per validare la P.IVA
        driver.findElement(By.id("validate_vat")).click();

        // Oracolo: Appare errore Partita IVA non valida
        WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vat-not-valid")));
        assertTrue(errorDiv.isDisplayed());
    }

    @Test
    @Order(13)
    @DisplayName("TC_4.13 - Registrazione corretta (Azienda senza limitazioni, incompleta)")
    void testRegistrazioneCorrettaAziendaNonLimitataIncompleta() {
        // Generiamo dati univoci per evitare errori di "Utente già registrato" nei test di successo
        final String UNIQUE_SUFFIX = String.valueOf(System.currentTimeMillis());
        final String VALID_USER = "Giovanni" + UNIQUE_SUFFIX.substring(8);
        final String VALID_EMAIL = "test" + UNIQUE_SUFFIX + "@example.it";
        final String VALID_PHONE = "3" + (100000000L + new Random().nextInt(900000000));
        final String VALID_VAT = generateRandomVAT();

        // Inseriamo la stringa random nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys(VALID_USER);

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa random nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(VALID_EMAIL);

        // Inseriamo la stringa random nell'input del telefono
        WebElement telephoneField = driver.findElement(By.id("telephone"));
        telephoneField.sendKeys(VALID_PHONE);

        // Inseriamo la stringa 'Secure!123' nell'input del ripeti password
        WebElement repeatPasswordField = driver.findElement(By.id("rep_password_register"));
        repeatPasswordField.sendKeys("Secure!123");

        // Abilita campi azienda e attendi che i campi diventino visibili
        driver.findElement(By.id("add_info")).click();

        // Inseriamo la stringa random nell'input della P.IVA
        WebElement vatField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vat")));
        vatField.sendKeys(VALID_VAT);

        // Invio
        driver.findElement(By.cssSelector("#register-form input[type='submit']")).click();

        // Oracolo: Redirect alla Home Page
        wait.until(ExpectedConditions.urlToBe(HOME_URL));
        assertEquals(HOME_URL, driver.getCurrentUrl(), "Il redirect alla Home Page non è avvenuto.");
    }

    @Test
    @Order(14)
    @DisplayName("TC_4.14 - Registrazione corretta (Azienda senza limitazioni, completa)")
    void testRegistrazioneCorrettaAziendaNonLimitataCompleta() {
        // Generiamo dati univoci per evitare errori di "Utente già registrato" nei test di successo
        final String UNIQUE_SUFFIX = String.valueOf(System.currentTimeMillis());
        final String VALID_USER = "Giovanni" + UNIQUE_SUFFIX.substring(8);
        final String VALID_EMAIL = "test" + UNIQUE_SUFFIX + "@example.it";
        final String VALID_PHONE = "3" + (100000000L + new Random().nextInt(900000000));
        final String VALID_VAT = generateRandomVAT();
        final String VALID_COMPANY_NAME = "Compagnia" + UNIQUE_SUFFIX;

        // Inseriamo la stringa random nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys(VALID_USER);

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa random nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(VALID_EMAIL);

        // Inseriamo la stringa random nell'input del telefono
        WebElement telephoneField = driver.findElement(By.id("telephone"));
        telephoneField.sendKeys(VALID_PHONE);

        // Inseriamo la stringa 'Secure!123' nell'input del ripeti password
        WebElement repeatPasswordField = driver.findElement(By.id("rep_password_register"));
        repeatPasswordField.sendKeys("Secure!123");

        // Abilita campi azienda e attendi che i campi diventino visibili
        driver.findElement(By.id("add_info")).click();

        // Inseriamo la stringa random nell'input della P.IVA
        WebElement vatField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vat")));
        vatField.sendKeys(VALID_VAT);

        // Inseriamo la stringa random nell'input del nome azienda
        WebElement companyNameField = driver.findElement(By.id("company_name"));
        companyNameField.sendKeys(VALID_COMPANY_NAME);

        // Inseriamo la stringa 'Azienda Tech S.r.l.' nell'input della sede legale
        WebElement registeredOfficeField = driver.findElement(By.id("registered_office"));
        registeredOfficeField.sendKeys("Azienda Tech S.r.l.");

        // Invio
        driver.findElement(By.cssSelector("#register-form input[type='submit']")).click();

        // Oracolo: Redirect alla Home Page
        wait.until(ExpectedConditions.urlToBe(HOME_URL));
        assertEquals(HOME_URL, driver.getCurrentUrl(), "Il redirect alla Home Page non è avvenuto.");
    }

    @Test
    @Order(15)
    @DisplayName("TC_4.15 - Registrazione fallita, partita IVA vuota")
    void testRegistrazioneAziendaNonLimitataCompleta() {
        // Generiamo dati univoci per evitare errori di "Utente già registrato" nei test di successo
        final String UNIQUE_SUFFIX = String.valueOf(System.currentTimeMillis());
        final String VALID_USER = "Giovanni" + UNIQUE_SUFFIX.substring(8);
        final String VALID_EMAIL = "test" + UNIQUE_SUFFIX + "@example.it";
        final String VALID_PHONE = "3" + (100000000L + new Random().nextInt(900000000));
        final String VALID_COMPANY_NAME = "Compagnia" + UNIQUE_SUFFIX;

        // Inseriamo la stringa random nell'input dell'username
        WebElement userField = driver.findElement(By.id("username_register"));
        userField.sendKeys(VALID_USER);

        // Inseriamo la stringa 'Secure!123' nell'input della password
        WebElement passwordField = driver.findElement(By.id("password_register"));
        passwordField.sendKeys("Secure!123");

        // Inseriamo la stringa random nell'input della email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(VALID_EMAIL);

        // Inseriamo la stringa random nell'input del telefono
        WebElement telephoneField = driver.findElement(By.id("telephone"));
        telephoneField.sendKeys(VALID_PHONE);

        // Inseriamo la stringa 'Secure!123' nell'input del ripeti password
        WebElement repeatPasswordField = driver.findElement(By.id("rep_password_register"));
        repeatPasswordField.sendKeys("Secure!123");

        // Abilita campi azienda e attendi che i campi diventino visibili
        driver.findElement(By.id("add_info")).click();

        // Inseriamo la stringa random nell'input del nome azienda
        WebElement companyNameField = driver.findElement(By.id("company_name"));
        companyNameField.sendKeys(VALID_COMPANY_NAME);

        // Inseriamo la stringa 'Azienda Tech S.r.l.' nell'input della sede legale
        WebElement registeredOfficeField = driver.findElement(By.id("registered_office"));
        registeredOfficeField.sendKeys("Azienda Tech S.r.l.");

        // Lasciamo il campo partita IVA vuoto e proviamo a inviare
        driver.findElement(By.cssSelector("#register-form input[type='submit']")).click();

        // Oracolo: Redirect pagina di login con errore da popup
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/login/?e=NOT_VALID_R"));
        assertEquals(BASE_URL + "/login/?e=NOT_VALID_R", driver.getCurrentUrl(), "Il redirect alla pagina di login non è avvenuto correttamente.");
    }

    @AfterAll
    static void tearDown() {
        if (driver != null)
            driver.quit();
    }

    private String generateRandomVAT() {
        Random random = new Random();
        int[] vat = new int[11];
        int sum = 0;

        for (int i = 0; i < 10; i++) {
            vat[i] = random.nextInt(10);

            if (i % 2 == 0)
                sum += vat[i];
            else {
                int temp = vat[i] * 2;
                if (temp > 9) temp -= 9;
                sum += temp;
            }
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        vat[10] = checkDigit;

        StringBuilder sb = new StringBuilder();
        for (int digit : vat) sb.append(digit);
        return sb.toString();
    }
}