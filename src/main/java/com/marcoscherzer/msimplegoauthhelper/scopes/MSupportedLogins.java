package com.marcoscherzer.msimplegoauthhelper.scopes;
/**
 * Login-Arten für Google Service APIs
 * @author Marco Scherzer, Copyright Marco Scherzer
 */
public enum MSupportedLogins {

    /**
     * OAuth 2.0 mit User-Consent Flow.
     * Wird für APIs genutzt, die Zugriff auf Nutzerdaten benötigen
     * (z.B. Gmail, Drive, Calendar, YouTube, People).
     */
    OAUTH_USER,

    /**
     * OAuth 2.0 mit Service Accounts (JWT-basiert).
     * Wird für serverseitige Anwendungen und Cloud-Ressourcen genutzt
     * (z.B. BigQuery, Cloud Storage, Pub/Sub).
     */
    OAUTH_SERVICE_ACCOUNT,

    /**
     * API-Key basierter Zugriff.
     * Wird für öffentliche oder Maps-ähnliche APIs genutzt,
     * die keinen User-Consent benötigen (z.B. Maps, Safe Browsing, Custom Search).
     */
    API_KEY,

    /**
     * Kombination aus API-Key und OAuth.
     * Manche APIs erlauben beide Varianten, abhängig vom Endpunkt
     * (z.B. YouTube Data API: API-Key für öffentliche Daten, OAuth für private Daten).
     */
    HYBRID
}

