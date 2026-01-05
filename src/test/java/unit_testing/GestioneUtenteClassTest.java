package unit_testing;

import it.unisa.nexware.application.utils.FieldValidator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Case: Gestione Utente - Registrazione al sistema
 * Riferimento PDF: Sezione 1.4 (TC_4.1 a TC_4.15)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneUtenteClassTest {

    @Test
    @Order(1)
    @DisplayName("TC_4.1 - Username VUOTO")
    void testUsernameVuoto() {
        String username = "";

        // Oracolo: La registrazione non viene effettuata perché il campo "Username" non è compilato
        assertFalse(FieldValidator.usernameValidate(username), "L'oracolo prevede fallimento per username non inserito.");
    }

    @Test
    @Order(2)
    @DisplayName("TC_4.2 - Username formato errato")
    void testUsernameFormatoErrato() {
        String username = "ab";

        // Oracolo: La registrazione non viene effettuata perché l'username non segue il formato richiesto
        assertFalse(FieldValidator.usernameValidate(username), "L'oracolo prevede fallimento per username con formato non valido.");
    }

    @Test
    @Order(3)
    @DisplayName("TC_4.3 - Password VUOTA")
    void testPasswordVuota() {
        String password = "";

        // Oracolo: La registrazione non viene effettuata perché il campo "Password" non è compilato
        assertFalse(FieldValidator.passwordValidate(password), "L'oracolo prevede fallimento per password non inserita.");
    }

    @Test
    @Order(4)
    @DisplayName("TC_4.4 - Password formato errato")
    void testPasswordFormatoErrato() {
        String password = "Password1";

        // Oracolo: La registrazione non viene effettuata perché la password non segue il formato richiesto (manca il carattere speciale)
        assertFalse(FieldValidator.passwordValidate(password), "L'oracolo prevede fallimento per password con formato errato.");
    }

    @Test
    @Order(5)
    @DisplayName("TC_4.5 - Email VUOTA")
    void testEmailVuota() {
        String email = "";

        // Oracolo: La registrazione non viene effettuata perché il campo "E-mail" non è compilato
        assertFalse(FieldValidator.emailValidate(email), "L'oracolo prevede fallimento per email vuota.");
    }

    @Test
    @Order(6)
    @DisplayName("TC_4.6 - Email formato errato")
    void testEmailFormatoErrato() {
        String email = "Giovannipalatucci.ciao";

        // Oracolo: La registrazione non viene effettuata perché l'e-mail non segue il formato richiesto
        assertFalse(FieldValidator.emailValidate(email), "L'oracolo prevede fallimento per email con formato non valido.");
    }

    @Test
    @Order(7)
    @DisplayName("TC_4.7 - Telefono VUOTO")
    void testTelefonoVuoto() {
        String telefono = "";

        // Oracolo: La registrazione non viene effettuata perché il campo "Telefono" non è compilato
        assertFalse(FieldValidator.phoneValidate(telefono), "L'oracolo prevede fallimento per telefono non inserito.");
    }

    @Test
    @Order(8)
    @DisplayName("TC_4.8 - Telefono formato errato")
    void testTelefonoFormatoErrato() {
        String telefono = "3520156";

        // Oracolo: La registrazione non viene effettuata perché il numero di telefono non segue il formato richiesto
        assertFalse(FieldValidator.phoneValidate(telefono), "L'oracolo prevede fallimento per numero di telefono con formato errato.");
    }

    @Test
    @Order(9)
    @DisplayName("TC_4.9 - Ripeti Password VUOTO")
    void testRipetiPasswordVuoto() {
        String password = "Secure!123";
        String ripetiPassword = "";

        // Oracolo: La registrazione non viene effettuata perché il campo "Ripeti Password" non è compilato
        assertFalse(FieldValidator.repPswValidate(password, ripetiPassword), "L'oracolo prevede fallimento per il campo ripeti password vuoto.");
    }

    @Test
    @Order(10)
    @DisplayName("TC_4.10 - Ripeti Password diversa")
    void testRipetiPasswordDiversa() {
        String password = "Secure!123";
        String ripetiPassword = "Secure!122";

        // Oracolo: La registrazione non viene effettuata perché le password non coincidono
        assertFalse(FieldValidator.repPswValidate(password, ripetiPassword), "L'oracolo prevede fallimento se le password inserite non sono uguali.");
    }

    @Test
    @Order(11)
    @DisplayName("TC_4.11 - Registrazione corretta per Azienda base")
    void testRegistrazioneCorretta() {
        String username = "Giovanni";
        String password = "Secure!123";
        String email = "info@aziendatech.it";
        String telefono = "3524521560";
        String ripetiPassword = "Secure!123";

        boolean isValid = FieldValidator.usernameValidate(username) && FieldValidator.passwordValidate(password) &&
                FieldValidator.emailValidate(email) && FieldValidator.phoneValidate(telefono) && FieldValidator.repPswValidate(password, ripetiPassword);

        // Oracolo: La registrazione viene effettuata correttamente
        assertTrue(isValid);
    }

    @Test
    @Order(12)
    @DisplayName("TC_4.12 - Partita IVA formato errato")
    void testPartitaIvaErrata() {
        String pIva = "12345";

        // Oracolo: La registrazione non viene effettuata perché la partita IVA non segue il formato richiesto
        assertFalse(FieldValidator.vatValidate(pIva), "L'oracolo prevede fallimento per partita IVA non valida.");
    }

    @Test
    @Order(13)
    @DisplayName("TC_4.13 - Nome aziendale VUOTO")
    void testNomeAziendaleVuoto() {
        String username = "Giovanni";
        String password = "Secure!123";
        String email = "info@aziendatech.it";
        String telefono = "3524521560";
        String ripetiPassword = "Secure!123";
        String pIva = "08374620951";
        String nomeAzienda = ""; // Campo oggetto del test

        boolean isValid = FieldValidator.usernameValidate(username) && FieldValidator.passwordValidate(password) &&
                FieldValidator.emailValidate(email) && FieldValidator.phoneValidate(telefono) &&
                FieldValidator.repPswValidate(password, ripetiPassword) && FieldValidator.vatValidate(pIva) && FieldValidator.companyNameValidate(nomeAzienda);

        // Oracolo: La registrazione non viene effettuata perché il campo "Nome aziendale" non è compilato
        assertFalse(isValid, "L'oracolo prevede fallimento per nome aziendale vuoto.");
    }

    @Test
    @Order(14)
    @DisplayName("TC_4.14 - Registrazione Azienda Completa")
    void testRegistrazioneAziendaSuccesso() {
        String username = "Giovanni";
        String password = "Secure!123";
        String email = "info@aziendatech.it";
        String telefono = "3524521560";
        String ripetiPassword = "Secure!123";
        String pIva = "08973230967";
        String nomeAzienda = "Azienda Tech S.r.l.";

        boolean isValid = FieldValidator.usernameValidate(username) && FieldValidator.passwordValidate(password) &&
                FieldValidator.emailValidate(email) && FieldValidator.phoneValidate(telefono) && FieldValidator.repPswValidate(password, ripetiPassword) &&
                FieldValidator.vatValidate(pIva) && FieldValidator.companyNameValidate(nomeAzienda);

        // Oracolo: La registrazione viene effettuata correttamente
        assertTrue(isValid, "L'oracolo prevede successo con tutti i campi validi per utente business.");
    }

    @Test
    @Order(15)
    @DisplayName("TC_4.15 - Partita IVA VUOTA")
    void testPartitaIvaVuota() {
        String username = "Giovanni";
        String password = "Secure!123";
        String email = "giovanni@email.it";
        String telefono = "3524521560";
        String ripetiPassword = "Secure!123";
        String pIva = ""; // Campo oggetto del test
        String nomeAzienda = "Azienda Tech S.r.l.";

        boolean isValid = FieldValidator.usernameValidate(username) && FieldValidator.passwordValidate(password) &&
                FieldValidator.emailValidate(email) && FieldValidator.phoneValidate(telefono) &&
                FieldValidator.repPswValidate(password, ripetiPassword) && FieldValidator.companyNameValidate(nomeAzienda);

        // Oracolo: La registrazione viene effettuata correttamente inserendo tutti i campi tranne la P.IVA
        assertTrue(isValid);
        assertFalse(FieldValidator.vatValidate(pIva), "L'oracolo prevede successo per tutti i campi validi e la partita IVA vuota.");
    }
}