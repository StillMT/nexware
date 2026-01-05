package unit_testing;

import it.unisa.nexware.application.utils.FieldValidator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Case: Gestione Annuncio - Creazione annuncio
 * Riferimento PDF: Sezione 1.3 (TC_3.1 a TC_3.9)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneAnnuncioClassTest {

    @Test
    @Order(1)
    @DisplayName("TC_3.1 - Titolo VUOTO")
    void testTitoloVuoto() {
        String titolo = "";

        // Oracolo: La pubblicazione non viene effettuata perché il campo "Titolo" non è compilato
        assertFalse(FieldValidator.productNameValidate(titolo), "L'oracolo prevede fallimento per titolo vuoto.");
    }

    @Test
    @Order(2)
    @DisplayName("TC_3.2 - Descrizione VUOTA")
    void testDescrizioneVuota() {
        String descrizione = "";

        // Oracolo: La pubblicazione non è stata effettuata perché il campo "Descrizione" non è compilato
        assertFalse(FieldValidator.productDescValidate(descrizione), "L'oracolo prevede fallimento per descrizione vuota.");
    }

    @Test
    @Order(3)
    @DisplayName("TC_3.3 - Categoria VUOTA")
    void testCategoriaVuota() {
        String categoria = "";

        // Oracolo: La pubblicazione non è stata effettuata perché il campo "Categoria" non è compilato
        assertFalse(FieldValidator.productCategoryValidate(categoria), "L'oracolo prevede fallimento per categoria vuota.");
    }

    @Test
    @Order(4)
    @DisplayName("TC_3.4 - Prezzo NON VALIDO")
    void testPrezzoNonValido() {
        String prezzo = "non_valido";

        // Oracolo: La pubblicazione non è stata effettuata perché il campo "Prezzo" non è valido
        assertFalse(FieldValidator.productPriceValidate(prezzo), "L'oracolo prevede fallimento per prezzo in formato non valido.");
    }

    @Test
    @Order(5)
    @DisplayName("TC_3.5 - Quantità VUOTA")
    void testQuantitaVuota() {
        String quantita = "";

        // Oracolo: La pubblicazione non è stata effettuata perché il campo "Quantità" è vuoto.
        assertFalse(FieldValidator.productStockValidate(quantita), "L'oracolo prevede fallimento per quantità vuota.");
    }

    @Test
    @Order(6)
    @DisplayName("TC_3.6 - Quantità NON VALIDA")
    void testQuantitaZero() {
        String quantita = "-1";

        // Oracolo: La pubblicazione non è stata effettuata perché il campo "Quantità" non è valido
        assertFalse(FieldValidator.productStockValidate(quantita), "L'oracolo prevede fallimento per quantità minore di 0.");
    }

    @Test
    @Order(7)
    @DisplayName("TC_3.7 - Annuncio Corretto")
    void testAnnuncioCorretto() {
        String titolo = "Antivirus Pro 2025";
        String descrizione = "Software antivirus professionale con protezione in tempo reale, firewall integrato e aggiornamenti automatici.";
        String categoria = "1";
        String prezzo = "49.00";
        String quantita = "10";

        boolean isValid = FieldValidator.productNameValidate(titolo) && FieldValidator.productDescValidate(descrizione) &&
                FieldValidator.productCategoryValidate(categoria) && FieldValidator.productPriceValidate(prezzo) &&
                FieldValidator.productStockValidate(quantita);

        // Oracolo: La pubblicazione è stata effettuata correttamente
        assertTrue(isValid);
    }
}