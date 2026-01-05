package unit_testing;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.storage.dao.CartDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Case: Gestione Catalogo - Aggiunta prodotto al carrello
 * Riferimento PDF: Sezione 1.1
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneCatalogoClassTest {
    private CompanyBean company;
    private final int ID_PRODOTTO_ESISTENTE = 1;
    private final int ID_AZIENDA_UTENTE = 2;

    @BeforeEach
    void setUp() {
        company = new CompanyBean();
        company.setId(ID_AZIENDA_UTENTE);
    }

    @Test
    @Order(1)
    @DisplayName("TC_1.2 - Aggiunta prodotto valido e disponibile")
    void testAggiuntaProdottoValido() {
        // Pulizia preventiva per garantire il successo dell'aggiunta (TC_1.2)
        CartDAO.doRemoveProduct(ID_PRODOTTO_ESISTENTE, company);

        // Oracolo: Il prodotto viene aggiunto correttamente al carrello
        boolean result = CartDAO.doAddProduct(ID_PRODOTTO_ESISTENTE, company);
        assertTrue(result, "L'oracolo prevede che il prodotto venga aggiunto correttamente");
    }

    @Test
    @Order(2)
    @DisplayName("TC_1.1 - Prodotto già presente nel carrello")
    void testAggiuntaProdottoDuplicato() {
        // Assicuriamoci che il prodotto sia già presente (già aggiunto nel test precedente)
        CartDAO.doAddProduct(ID_PRODOTTO_ESISTENTE, company);

        // Oracolo: Il prodotto non viene duplicato nel carrello
        // Il DAO restituisce false perché executeUpdate() su INSERT IGNORE darà 0.
        boolean result = CartDAO.doAddProduct(ID_PRODOTTO_ESISTENTE, company);
        assertFalse(result, "L'oracolo prevede che il prodotto non venga duplicato");
    }

}