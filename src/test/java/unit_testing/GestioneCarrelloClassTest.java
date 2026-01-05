package unit_testing;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.storage.dao.CartDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Case: Gestione Carrello - Eliminazione prodotto
 * Riferimento PDF: Sezione 1.2
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneCarrelloClassTest {
    private CompanyBean company;
    private final int ID_PRODOTTO = 1;
    private final int ID_AZIENDA_UTENTE = 2;

    @BeforeEach
    void setUp() {
        company = new CompanyBean();
        company.setId(ID_AZIENDA_UTENTE);
    }

    @Test
    @Order(1)
    @DisplayName("TC_2.2 - Eliminazione corretta di un prodotto")
    void testEliminazioneProdottoSuccesso() {
        // Pre-condizione: Il prodotto deve essere nel carrello
        CartDAO.doAddProduct(ID_PRODOTTO, company);

        // Esecuzione dell'azione: l'utente clicca su elimina
        boolean result = CartDAO.doRemoveProduct(ID_PRODOTTO, company);

        // Oracolo: Il prodotto viene eliminato correttamente
        assertTrue(result, "L'oracolo prevede che il prodotto venga eliminato con successo dal DB.");
    }

    @Test
    @Order(2)
    @DisplayName("TC_2.1 - Fallimento eliminazione (Prodotto non presente)")
    void testEliminazioneProdottoFallimento() {
        // Per simulare un "impossibile eliminare" a livello di logica DAO
        // proviamo a eliminare un prodotto che non esiste nel carrello.
        int idInesistente = 9999;

        boolean result = CartDAO.doRemoveProduct(idInesistente, company);

        // Oracolo: Il sistema non elimina nulla e restituisce false
        assertFalse(result, "L'oracolo prevede che il sistema restituisca false se l'eliminazione fallisce.");
    }
}