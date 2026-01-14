-- phpMyAdmin SQL Dump
-- version 5.2.1deb1+deb12u1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Creato il: Gen 05, 2026 alle 15:33
-- Versione del server: 10.11.14-MariaDB-0+deb12u2
-- Versione PHP: 8.2.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nexware`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password_hash` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `admin`
--

INSERT INTO `admin` (`id`, `username`, `password_hash`) VALUES
(1, 'admin', '$2a$10$VxF1ZQI.fAvsQ26f.6nX3u5xtvh9E5aNFwabshaXtL8ix5LwmEbjC');

-- --------------------------------------------------------

--
-- Struttura della tabella `carted_product`
--

CREATE TABLE `carted_product` (
  `id` int(11) NOT NULL,
  `id_company` int(11) NOT NULL,
  `id_product` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `category`
--

INSERT INTO `category` (`id`, `name`) VALUES
(8, 'Analisi Dati e BI'),
(9, 'Architettura e BIM'),
(10, 'Automazione Industriale'),
(11, 'Backup e Disaster Recovery'),
(12, 'Blockchain e Crypto'),
(13, 'Cloud Computing e IaaS'),
(14, 'Comunicazione e Messaggistica'),
(15, 'CRM e Sales Force'),
(16, 'Cybersecurity Avanzata'),
(17, 'Database Management'),
(4, 'Design e Multimedia'),
(18, 'E-commerce e Retail'),
(19, 'Editing Audio e Podcast'),
(20, 'ERP e Supply Chain'),
(21, 'Finanza e Fintech'),
(7, 'Formazione e Istruzione'),
(22, 'Fotografia Digitale'),
(23, 'Gaming e VR/AR'),
(24, 'Geospatiale e GIS'),
(6, 'Gestionali e Aziendali'),
(25, 'HR e Risorse Umane'),
(26, 'Intelligenza Artificiale'),
(27, 'Internet of Things (IoT)'),
(28, 'Legal e Compliance'),
(29, 'Logistica e Trasporti'),
(30, 'Marketing e Social Media'),
(31, 'Network Administration'),
(1, 'Produttività e Ufficio'),
(32, 'Project Management'),
(33, 'Salute e Telemedicina'),
(34, 'Scientifico e Simulazione'),
(3, 'Sicurezza e Antivirus'),
(2, 'Sistema Operativo e Utility'),
(35, 'Smart Home e Domotica'),
(36, 'Streaming e Broadcasting'),
(5, 'Sviluppo e Programmazione'),
(37, 'Virtualizzazione e Container');

-- --------------------------------------------------------

--
-- Struttura della tabella `company`
--

CREATE TABLE `company` (
  `id` int(11) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `email` varchar(254) NOT NULL,
  `telephone` varchar(15) NOT NULL,
  `vat` varchar(11) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `registered_office` varchar(255) NOT NULL,
  `singup_time` datetime NOT NULL DEFAULT current_timestamp(),
  `status` enum('NORMAL','LIMITED_INFO','LIMITED','BANNED') NOT NULL DEFAULT 'NORMAL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `company`
--

INSERT INTO `company` (`id`, `username`, `password_hash`, `email`, `telephone`, `vat`, `company_name`, `registered_office`, `singup_time`, `status`) VALUES
(1, 'amazon', '$2a$10$6Cdd0Hy.VfjBoFouEDgmZueUJ8TPFdkSvqkw53nDjMtt2w48mP0Wu', 'amazon@amazon.com', '3558697485', '08973230967', 'AMAZON EU SARL', 'VIALE MONTE GRAPPA 3/5 ,20124 MILANO MI,', '2025-12-05 15:45:55', 'NORMAL'),
(2, 'test', '$2a$10$Q/vvyyB1m7SIdcKkN6lzyuT6xQcTg8OXF6i5PfyXfpNNlAkS8laAu', 'test@test.it', '026988564', '02188520544', 'TEST S.R.L.', 'STRADA BATTIFOGLIA 14/N ,06132 PERUGIA PG,', '2025-12-10 13:40:58', 'NORMAL'),
(3, 'test2', '$2a$10$s1JmBnRbim8T.pRfC4aqIuHNwmiM0W0szSAvD5IOaldOCOdqo7pR6', 'test@test.com', '3568894452', '71398240037', '', '', '2025-12-22 11:19:19', 'NORMAL');

-- --------------------------------------------------------

--
-- Struttura della tabella `newsletter_email`
--

CREATE TABLE `newsletter_email` (
  `id` int(11) NOT NULL,
  `email` varchar(254) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `newsletter_email`
--

INSERT INTO `newsletter_email` (`id`, `email`) VALUES
(1, 'amazon@amazon.com'),
(2, 'example@example.com');

-- --------------------------------------------------------

--
-- Struttura della tabella `ordered_product`
--

CREATE TABLE `ordered_product` (
  `id` int(11) NOT NULL,
  `id_order` int(11) NOT NULL,
  `id_product` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `ordered_product`
--

INSERT INTO `ordered_product` (`id`, `id_order`, `id_product`, `price`) VALUES
(7, 4, 2, 549.99),
(8, 4, 13, 19.90),
(9, 4, 12, 29.95),
(10, 4, 15, 29.95),
(11, 4, 14, 89.99),
(12, 4, 11, 149.00),
(13, 4, 10, 69.50),
(14, 4, 9, 34.99),
(15, 5, 15, 29.95),
(16, 5, 14, 89.99),
(17, 5, 13, 19.90),
(18, 5, 11, 149.00),
(19, 5, 12, 29.95),
(20, 6, 14, 89.99),
(21, 6, 15, 29.95),
(22, 7, 14, 89.99),
(23, 7, 15, 29.95),
(24, 8, 12, 29.95),
(25, 9, 10, 69.50),
(26, 9, 3, 899.99),
(27, 9, 5, 131.99),
(28, 9, 6, 299.00),
(30, 10, 15, 29.95),
(31, 2, 15, 30.00),
(32, 2, 11, 34.00);

-- --------------------------------------------------------

--
-- Struttura della tabella `order_table`
--

CREATE TABLE `order_table` (
  `id` int(11) NOT NULL,
  `id_company` int(11) NOT NULL,
  `order_nr` varchar(19) NOT NULL,
  `state` enum('WAITING','DELIVERED') NOT NULL DEFAULT 'WAITING',
  `date` date NOT NULL DEFAULT current_timestamp(),
  `half_card_number` varchar(4) NOT NULL,
  `total_price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `order_table`
--

INSERT INTO `order_table` (`id`, `id_company`, `order_nr`, `state`, `date`, `half_card_number`, `total_price`) VALUES
(2, 1, '3278378212', 'DELIVERED', '2026-01-04', '5343', 24.00),
(4, 1, '108-0000001-9121830', 'DELIVERED', '2026-01-01', '6891', 977.26),
(5, 1, '945-0000001-9645424', 'DELIVERED', '2026-01-01', '4242', 322.78),
(6, 1, '576-0000001-9588918', 'DELIVERED', '2026-01-01', '4242', 123.93),
(7, 1, '546-0000001-9480094', 'DELIVERED', '2026-01-01', '4242', 123.93),
(8, 1, '003-0000001-8590756', 'DELIVERED', '2026-01-02', '5556', 33.94),
(9, 1, '658-0000001-5679478', 'DELIVERED', '2026-01-02', '0004', 1404.47),
(10, 1, '629-0000001-7930922', 'DELIVERED', '2026-01-04', '4242', 33.94);

-- --------------------------------------------------------

--
-- Struttura della tabella `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `description` text NOT NULL,
  `id_category` int(11) NOT NULL,
  `id_company` int(11) NOT NULL,
  `creation_date` datetime NOT NULL DEFAULT current_timestamp(),
  `modifying_date` datetime NOT NULL DEFAULT current_timestamp(),
  `state` enum('ACTIVE','HIDDEN','CANCELED') NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `product`
--

INSERT INTO `product` (`id`, `name`, `description`, `id_category`, `id_company`, `creation_date`, `modifying_date`, `state`, `price`, `stock`) VALUES
(1, 'DevCore Pro', 'DevCore Pro è l\'ambiente di sviluppo integrato (IDE) definitivo, progettato per massimizzare l\'efficienza e ridurre i tempi di debug. Offre un editor di codice intelligente con autocompletamento predittivo avanzato, integrazione nativa con i sistemi di controllo versione (Git/SVN) e un motore di analisi statica che individua i bug prima della compilazione. Include un Debugger Hot-Swap che consente di modificare il codice in tempo reale durante l\'esecuzione dell\'applicazione, rendendolo indispensabile per team professionali che lavorano su architetture complesse e microservizi. Compatibile con i principali linguaggi di back-end e front-end.', 5, 1, '2025-12-09 19:47:15', '2026-01-03 14:36:26', 'ACTIVE', 799.99, 0),
(2, 'FlowDocs Ultimate', 'FlowDocs Ultimate è la suite essenziale per la gestione, la modifica e la collaborazione sui documenti. Va oltre il semplice editing PDF permettendo la creazione di flussi di lavoro automatizzati (workflow) per l\'approvazione, la firma digitale e l\'archiviazione conforme alle normative. Include strumenti avanzati per la conversione massiva di file, la redazione sicura dei dati sensibili (GDPR-compliant) e l\'unione/divisione di documenti complessi. FlowDocs si integra nativamente con i servizi cloud più diffusi (Google Drive, Dropbox) e offre una licenza perpetua, rendendolo lo strumento definitivo per uffici e professionisti che vogliono digitalizzare completamente il ciclo di vita dei documenti.', 1, 1, '2025-12-09 21:05:54', '2025-12-22 22:26:48', 'ACTIVE', 549.99, 896),
(3, 'AuraEdit Studio', 'AuraEdit Studio è la piattaforma di editing video non lineare che combina potenza di elaborazione e facilità d\'uso. Ideale per content creator, videomaker e professionisti del marketing, offre un rendering accelerato tramite GPU e strumenti avanzati come il chroma key (green screen), la stabilizzazione AI e un pannello colore professionale per un color grading cinematografico. Il software include una vasta libreria di effetti drag-and-drop, transizioni personalizzabili e supporto nativo per tutti i formati video fino a 8K. AuraEdit Studio è la soluzione perfetta per produrre contenuti di alta qualità in tempi brevi, senza sacrificare la profondità delle funzionalità.', 4, 1, '2025-12-09 21:07:29', '2025-12-30 17:58:00', 'ACTIVE', 899.99, 658),
(4, 'Vortex Creative Suite', 'Vortex Creative Suite è l\'hub definitivo per professionisti del design e team multimediali che cercano di mettere ordine nel caos creativo. Non è un semplice gestore di risorse digitali (DAM), ma un ecosistema intelligente potenziato dall\'IA che centralizza, organizza e accelera il flusso di lavoro. L\'intelligenza artificiale integrata analizza e tagga automaticamente immagini, video footage e file vettoriali, rendendo la ricerca istantanea. Vortex si integra nativamente con i principali software di settore (Adobe Creative Cloud, Figma, Sketch) tramite plugin dedicati, permettendo il drag-and-drop diretto delle risorse nei progetti attivi. Include inoltre strumenti di revisione collaborativa in tempo reale e viene offerto con una licenza perpetua, eliminando i costi ricorrenti degli abbonamenti.', 4, 1, '2025-12-22 22:39:42', '2025-12-22 22:43:24', 'ACTIVE', 570.65, 167),
(5, 'EduFlow Academy Builder', 'EduFlow Academy Builder è la suite completa per educatori, formatori e creatori di contenuti che vogliono lanciare la propria accademia online senza complessità tecniche. Dimentica le piattaforme costose e gli abbonamenti mensili: EduFlow ti offre un potente strumento desktop con licenza perpetua per creare corsi interattivi, quiz coinvolgenti e percorsi di apprendimento personalizzati. Grazie a un\'interfaccia drag-and-drop intuitiva, puoi combinare video, PDF, slide e test in pochi minuti. Il software include un sistema di tracciamento automatico dei progressi degli studenti e un generatore di certificati personalizzabili. I corsi creati possono essere esportati in formato SCORM per qualsiasi LMS o ospitati direttamente sul tuo sito web, offrendo una flessibilità totale.', 7, 1, '2025-12-22 22:49:01', '2025-12-22 22:49:01', 'ACTIVE', 131.99, 512),
(6, 'BizCore Enterprise Suite', 'BizCore Enterprise Suite è la soluzione gestionale all-in-one progettata per semplificare e potenziare le operazioni delle piccole e medie imprese. Questa suite unifica ERP (Enterprise Resource Planning) e CRM (Customer Relationship Management) in un\'unica piattaforma intuitiva, eliminando la necessità di software multipli e costosi abbonamenti ricorrenti. BizCore offre strumenti avanzati per l\'automazione della fatturazione, la gestione dell\'inventario in tempo reale, la contabilità integrata e reportistica finanziaria dettagliata potenziata dall\'intelligenza artificiale. Il modulo CRM centralizza i dati dei clienti, migliorando le relazioni e le vendite. Con una licenza perpetua, BizCore è un investimento una tantum che garantisce il controllo totale sui tuoi dati aziendali per sempre.', 6, 1, '2025-12-22 22:58:52', '2025-12-22 22:58:52', 'ACTIVE', 299.00, 147),
(7, 'ShieldGuard Total Security', 'ShieldGuard Total Security è la fortezza digitale definitiva per la tua vita online. Utilizzando un motore di rilevamento minacce basato sull\'intelligenza artificiale, blocca proattivamente malware, ransomware e phishing in tempo reale, ancor prima che possano colpire. Oltre alla protezione antivirus di livello militare, ShieldGuard include una VPN sicura e illimitata per navigare in anonimato, un firewall bidirezionale avanzato e strumenti per la protezione della privacy della webcam e del microfono. Con una licenza perpetua, ottieni una protezione completa e aggiornamenti delle definizioni a vita con un unico acquisto, senza abbonamenti annuali nascosti.', 3, 1, '2025-12-22 23:04:50', '2025-12-22 23:04:50', 'ACTIVE', 785.99, 562),
(8, 'NexusOS 12 Professional', 'NexusOS 12 Professional è il sistema operativo di nuova generazione progettato per chi rifiuta i compromessi tra velocità e compatibilità. Costruito su un kernel ibrido ottimizzato, NexusOS elimina il bloatware tradizionale garantendo tempi di avvio istantanei e una gestione della RAM ultra-efficiente, ideale per workstation e gaming ad alte prestazioni. La versione Professional include \"Omni-Layer\", un motore di compatibilità che permette di eseguire nativamente applicazioni Windows e Linux senza cali di performance. Integra inoltre una suite di utility per la pulizia del registro, l\'aggiornamento automatico dei driver e una sandbox isolata per testare file sospetti in totale sicurezza.', 2, 1, '2025-12-22 23:13:15', '2025-12-22 23:13:15', 'ACTIVE', 49.90, 87),
(9, 'ChromaStudio AI', 'ChromaStudio AI è l\'editor fotografico di nuova generazione che automatizza i compiti più noiosi della post-produzione. Grazie a reti neurali avanzate, permette la rimozione dello sfondo con un click, il ritocco della pelle automatizzato e l\'upscaling delle immagini fino all\'800% senza perdita di qualità. Include una vasta libreria di filtri LUT cinematografici e supporta l\'editing non distruttivo dei file RAW. Perfetto per fotografi e graphic designer che devono consegnare progetti ad alta velocità.', 4, 1, '2025-12-22 23:38:23', '2025-12-22 23:41:05', 'ACTIVE', 34.99, 45),
(10, 'PolyMinds Language Suite', 'PolyMinds è il software definitivo per l\'apprendimento accelerato delle lingue straniere. Utilizzando un metodo di immersione virtuale, il programma offre conversazioni simulate con avatar IA madrelingua che correggono la pronuncia in tempo reale. Include corsi completi dal livello A1 al C2 per 12 lingue globali, esercizi di grammatica gamificati e un dizionario contestuale integrato. La licenza perpetua garantisce l\'accesso a vita a tutti gli aggiornamenti dei corsi futuri senza costi mensili.', 7, 1, '2025-12-22 23:42:12', '2025-12-22 23:42:12', 'ACTIVE', 69.50, 42),
(11, 'OptiSales CRM Pro', 'OptiSales CRM Pro trasforma il modo in cui gestisci i clienti e le pipeline di vendita. Dimentica i fogli di calcolo disordinati: questo software centralizza contatti, lead e opportunità in una dashboard visiva intuitiva. Offre automazione dell\'email marketing, tracciamento delle chiamate VoIP integrato e previsioni di vendita basate sullo storico dati. Ideale per team commerciali che vogliono chiudere più contratti in meno tempo. Nessun canone mensile, i tuoi dati rimangono sul tuo server locale o cloud privato.', 6, 1, '2025-12-22 23:43:47', '2025-12-22 23:43:47', 'ACTIVE', 149.00, 88),
(12, 'DeepShield VPN & Privacy', 'DeepShield non è solo una VPN, è un mantello di invisibilità per la tua presenza digitale. Offre larghezza di banda illimitata attraverso oltre 5000 server crittografati in tutto il mondo, permettendoti di aggirare le restrizioni geografiche e navigare in totale anonimato. Include funzionalità avanzate come Kill Switch automatico, blocco dei tracker pubblicitari e protezione contro i leak DNS. La politica \"No-Logs\" rigorosa garantisce che nessuna tua attività venga mai registrata.', 3, 1, '2025-12-22 23:46:31', '2025-12-22 23:46:31', 'ACTIVE', 29.95, 367),
(13, 'DriverGenius Ultimate', 'DriverGenius Ultimate è lo strumento essenziale per mantenere il tuo PC al massimo delle prestazioni. Scansiona automaticamente l\'intero sistema per individuare driver obsoleti, mancanti o corrotti e li aggiorna con un solo click attingendo a un database di oltre 3 milioni di periferiche verificate. Risolve problemi audio, video e di connettività, e include una funzione di backup e ripristino dei driver per la massima sicurezza prima di ogni aggiornamento di sistema.', 2, 1, '2025-12-22 23:47:31', '2025-12-22 23:47:31', 'ACTIVE', 19.90, 156),
(14, 'SprintMaster Pro', 'SprintMaster Pro è la soluzione definitiva per la gestione agile dei progetti e la collaborazione di team. Progettato per team che utilizzano metodologie Scrum e Kanban, offre una dashboard visiva intuitiva per gestire task, sprint e backlog con facilità. Include strumenti integrati per il tracciamento del tempo, la generazione automatica di report avanzati (come i grafici burndown e velocity) e la gestione delle risorse. Con SprintMaster Pro, puoi allineare il tuo team, migliorare la trasparenza e accelerare la consegna dei progetti senza i costi ricorrenti degli abbonamenti cloud.', 6, 1, '2025-12-23 11:16:12', '2025-12-23 11:16:12', 'ACTIVE', 89.99, 212),
(15, 'PixelCraft Pro', 'PixelCraft Pro è la suite definitiva per la creazione di pixel art e l\'animazione di sprite, pensata per sviluppatori di giochi indie e artisti digitali. Offre un\'interfaccia a griglia intuitiva con strumenti di disegno precisi, una gestione avanzata delle palette di colori personalizzabili e una timeline di animazione con funzionalità di \"onion skinning\" per creare movimenti fluidi. Esporta facilmente i tuoi lavori in formati ottimizzati per i principali motori di gioco come Unity e Godot. Con una licenza perpetua, paghi una volta e possiedi il software per sempre, inclusi tutti gli aggiornamenti futuri.', 4, 1, '2025-12-23 11:39:37', '2025-12-23 11:39:37', 'ACTIVE', 29.95, 350);

-- --------------------------------------------------------

--
-- Struttura della tabella `report`
--

CREATE TABLE `report` (
  `id` int(11) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `email` varchar(254) NOT NULL,
  `object` varchar(100) NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `report`
--

INSERT INTO `report` (`id`, `company_name`, `email`, `object`, `description`) VALUES
(1, 'TEST S.R.L.', 'test@test.it', 'Richiesta di sblocco account', 'Richiesta di sblocco account per permettere l\'aggiunta di prodotti al catalogo');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `carted_product`
--
ALTER TABLE `carted_product`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_cart_item` (`id_company`,`id_product`),
  ADD KEY `fk_id_company_cp_idx` (`id_company`),
  ADD KEY `fk_id_product_cp_idx` (`id_product`);

--
-- Indici per le tabelle `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name_UNIQUE` (`name`);

--
-- Indici per le tabelle `company`
--
ALTER TABLE `company`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`),
  ADD UNIQUE KEY `telephone_UNIQUE` (`telephone`);

--
-- Indici per le tabelle `newsletter_email`
--
ALTER TABLE `newsletter_email`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indici per le tabelle `ordered_product`
--
ALTER TABLE `ordered_product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_id_product_op_idx` (`id_product`),
  ADD KEY `fk_id_order_op_idx` (`id_order`);

--
-- Indici per le tabelle `order_table`
--
ALTER TABLE `order_table`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero_ordine` (`order_nr`),
  ADD KEY `fk_id_company_orders` (`id_company`);

--
-- Indici per le tabelle `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_id_category_idx` (`id_category`),
  ADD KEY `fk_id_company_product_idx` (`id_company`);

--
-- Indici per le tabelle `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT per la tabella `carted_product`
--
ALTER TABLE `carted_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=213;

--
-- AUTO_INCREMENT per la tabella `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT per la tabella `company`
--
ALTER TABLE `company`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT per la tabella `newsletter_email`
--
ALTER TABLE `newsletter_email`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT per la tabella `ordered_product`
--
ALTER TABLE `ordered_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT per la tabella `order_table`
--
ALTER TABLE `order_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT per la tabella `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT per la tabella `report`
--
ALTER TABLE `report`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `carted_product`
--
ALTER TABLE `carted_product`
  ADD CONSTRAINT `fk_id_company_cp` FOREIGN KEY (`id_company`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_id_product_cp` FOREIGN KEY (`id_product`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `ordered_product`
--
ALTER TABLE `ordered_product`
  ADD CONSTRAINT `fk_id_order_op` FOREIGN KEY (`id_order`) REFERENCES `order_table` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_id_product_op` FOREIGN KEY (`id_product`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `order_table`
--
ALTER TABLE `order_table`
  ADD CONSTRAINT `fk_id_company_orders` FOREIGN KEY (`id_company`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `fk_id_category_product` FOREIGN KEY (`id_category`) REFERENCES `category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_id_company_product` FOREIGN KEY (`id_company`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
