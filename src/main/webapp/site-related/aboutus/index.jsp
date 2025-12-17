<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <% final String pageTitle = "Chi siamo"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <div class="about-container">

                <div class="about-row">
                    <div class="about-text">
                        <span class="about-title">Chi siamo e la nostra missione</span>
                        <p>
                            Nexware è una piattaforma B2B specializzata nel facilitare la connessione tra aziende
                            sviluppatrici di software e imprese che ricercano soluzioni digitali professionali.
                            Mettiamo a disposizione uno spazio moderno, affidabile e progettato per valorizzare
                            l’innovazione tecnologica.
                        </p>
                        <p>
                            La nostra missione è semplificare il processo di vendita e acquisto di software tra aziende,
                            garantendo un ambiente sicuro, trasparente e orientato ai risultati. Vogliamo supportare
                            gli sviluppatori nel raggiungere nuovi clienti e aiutare le imprese a trovare rapidamente
                            le soluzioni digitali di cui hanno bisogno.
                        </p>
                    </div>
                    <div class="about-image">
                        <img src="imgs/ambiente_di_sviluppo.png" alt="Ambiente di lavoro moderno">
                    </div>
                </div>

                <div class="about-row">
                    <div class="about-image">
                        <img src="imgs/sviluppo_software.png" alt="Sviluppo software e codice">
                    </div>
                    <div class="about-text">
                        <span class="about-title">Cosa facciamo</span>
                        <p>
                            Su Nexware le aziende possono pubblicare, presentare e vendere i propri software a un
                            pubblico professionale di altre imprese.Offriamo strumenti intuitivi per la gestione dei prodotti,
                            una vetrina digitale per dare massima visibilità alle offerte e un ambiente
                            che favorisce scelte consapevoli e investimenti mirati.
                        </p>
                    </div>
                </div>

                <div class="about-row">
                    <div class="about-text">
                        <span class="about-title">Il team</span>
                        <p>
                            Il team Nexware è composto da sviluppatori, designer e professionisti del settore digitale,
                            uniti dalla volontà di creare un marketplace B2B efficiente e all’avanguardia. Collaboriamo
                            per offrire un’esperienza fluida, professionale e allineata alle esigenze reali delle aziende.
                        </p>
                    </div>
                    <div class="about-image">
                        <img src="imgs/team.png" alt="Il team Nexware">
                    </div>
                </div>

            </div>
        </main>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>

    </body>
</html>