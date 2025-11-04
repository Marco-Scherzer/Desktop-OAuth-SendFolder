# MSimpleGoogleMailer

MSimpleGoogleMailer
- Versendet E-Mails über die Gmail API, inklusive Text und beliebiger Dateianhänge (MIME-kompatibel)
- Nutzt OAuth2 zur sicheren Authentifizierung mit Google – entweder bei jedem Start oder nur bei Bedarf
- Der Authentifizierungsmodus wird über den Parameter forceOAuth gesteuert:
  → true: OAuth2-Flow wird bei jeder Instanzierung ausgeführt (z. B. für sicherheitskritische Anwendungen)
  → false: Tokens werden aus dem Keystore geladen und wiederverwendet (z. B. für automatisierte Dienste)
- Beim ersten Start muss eine gültige client_secret.json im konfigurierten Verzeichnis liegen
  → Diese wird eingelesen, die enthaltenen Zugangsdaten werden im Keystore gespeichert und die Datei wird gelöscht
- Der Keystore wird automatisch im gesetzten Verzeichnis erzeugt, falls er nicht existiert
  → Kein manuelles Setup nötig – ideal für portable oder einmalige Tools
- Der Pfad zur client_secret.json kann über setInitialClientSecretFileReadDir(...) gesetzt werden
  → Ermöglicht flexible Konfiguration für verschiedene Umgebungen oder Container
- Die Klasse erzeugt und speichert Access- und Refresh-Tokens im Keystore
  → Diese können bei Bedarf neu generiert oder wiederverwendet werden
- Eine UUID wird beim ersten Start erzeugt und im Keystore unter clientId gespeichert
  → Diese dient zur eindeutigen Identifikation des Clients
  → Wird automatisch in den applicationName eingebunden und erscheint im Betreff jeder versendeten Mail
- Die Methode getKeystore() erlaubt Zugriff auf gespeicherte Tokens und UUID
  → Ermöglicht eigene Logik zur Anzeige, Prüfung oder Weiterverarbeitung
- Die Methode send(MOutgoingMail mail) übernimmt den Versandprozess:
  → Baut eine MIME-Mail mit Text und Anhängen
  → Kodiert sie Base64-url-safe
  → Sendet sie über die Gmail API und gibt die Message-ID aus
  
- MSimpleGoogleMailer
- Sends emails via the Gmail API, including text and any file attachments (MIME-compatible)
- Uses OAuth2 for secure authentication with Google, either on every startup or only when needed
- Authentication mode is controlled via the forceOAuth parameter:
  → true: OAuth2 flow is triggered every time the class is instantiated (ideal for security-critical apps)
  → false: Tokens are loaded from the keystore and reused (ideal for automated services)
- On first startup, a valid client_secret.json must be present in the configured directory
  → It is read, the credentials are stored in the keystore, and the file is deleted for security
- The keystore is automatically created in the configured directory if it doesn’t exist
  → No manual setup required – perfect for portable or one-off tools
- The path to client_secret.json can be configured via setInitialClientSecretFileReadDir(...)
  → Enables flexible deployment across environments or containers
- Access and refresh tokens are stored in the keystore
  → They can be regenerated or reused depending on the authentication mode
- A UUID is generated on first startup and stored in the keystore under clientId
  → This UUID uniquely identifies the client
  → It is automatically embedded in the applicationName and appears in the subject of every sent email
- The getKeystore() method provides access to stored tokens and UUID
  → Useful for custom logic, inspection, or token management
- The send(MOutgoingMail mail) method handles the email delivery:
  → Builds a MIME message with text and attachments
  → Encodes it in Base64-url-safe format
  → Sends it via the Gmail API and returns the message ID

