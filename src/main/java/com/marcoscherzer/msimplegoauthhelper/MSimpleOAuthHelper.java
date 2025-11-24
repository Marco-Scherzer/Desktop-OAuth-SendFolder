package com.marcoscherzer.msimplegoauthhelper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.marcoscherzer.msimplegoauthhelper.swinggui.MMutableBoolean;
import com.marcoscherzer.msimplekeystore.MKeystoreException;
import com.marcoscherzer.msimplekeystore.MSimpleKeystore;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MSimpleOAuthHelper {
    private final List<String> scopes;
    private final String appName;
    private Thread initThread;
    private Credential credential;
    private boolean doNotPersistOAuthToken;
    //private Gmail service;
    private MSimpleKeystore keystore;
    private static String clientSecretDir = System.getProperty("user.dir");
    private File jsonFile = new File(clientSecretDir, "client_secret.json");

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * scopes z.B Collections.singletonList(GmailScopes.GMAIL_SEND)
         */
        public MSimpleOAuthHelper(MSimpleOAuthKeystore mailerKeystore, String applicationName, List<String> scopes, boolean doNotPersistOAuthToken){

            this.keystore = mailerKeystore.getKeyStore();
            this.appName = applicationName;
            this.scopes = scopes;
            this.doNotPersistOAuthToken = doNotPersistOAuthToken;

            initThread = new Thread(() -> {
                try{
                    if (appName == null || appName.equals("")) throw new IllegalArgumentException("Application name must not be empty.");
                    credential = doBrowserOAuthFlow(keystore, scopes, doNotPersistOAuthToken);
                    //token funktioniert, file löschen
                    if (jsonFile.exists()) {
                        boolean jsonFileDeleted = jsonFile.delete();
                        if (!jsonFileDeleted) {
                            System.out.println("File \"client_secret.json\" could not be deleted. Please delete it manually.");
                        } else {
                            System.out.println("client_secret.json successfully imported and deleted.");
                        }
                    }
                } catch (Exception exc) {
                    //System.err.println("Error in initialization."+ exc.getMessage());
                    onOAuthFailure(exc);
                }
                onOAuthSucceeded(credential,applicationName);
            }, "MSimpleMailer-Init");
        }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void startOAuth() {
        initThread.start();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onOAuthFailure(Exception exc);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Credential doBrowserOAuthFlow(MSimpleKeystore keystore, List<String> scopes, boolean doNotPersistOAuthToken) throws Exception, MKeystoreException {
        String clientId = keystore.get("google-client-id");
        String clientSecret = keystore.get("google-client-secret");

        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setAuthUri("https://accounts.google.com/o/oauth2/auth")
                .setTokenUri("https://oauth2.googleapis.com/token");

        GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setInstalled(details);
        GoogleAuthorizationCodeFlow flow;
        int gglsLocalJettyPort = 8888;

        if (doNotPersistOAuthToken) {
            if (keystore.contains("OAuth")) {
                System.out.println("Securer OAuth Mode was chosen. Not keeping old tokens. Removing persistent OAuth token.");
                keystore.remove("OAuth");
            }
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
                    .setAccessType("online")
                    .setCredentialDataStore(new MemoryDataStoreFactory().getDataStore("tempsession"))
                    .build();
        } else {
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
                    .setAccessType("offline")
                    .setCredentialDataStore(new MSimpleKeystoreDataStoreFactory(keystore).getDataStore("OAuth"))
                    .build();
        }

        MMutableBoolean continueOAuth = new MMutableBoolean(true);

        // URL mit prompt=login
        GoogleAuthorizationCodeRequestUrl authUrl = flow.newAuthorizationUrl()
                .setRedirectUri("http://localhost:" + gglsLocalJettyPort + "/Callback")
                .set("response_type", "code")
                .set("scope", String.join(" ", scopes))
                .set("prompt", doNotPersistOAuthToken ? "login" : "consent");

        String url = authUrl.build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(gglsLocalJettyPort).build();
        String redirectUri = receiver.getRedirectUri();
        onStartOAuth(url, continueOAuth);
        System.out.println("continue oauth "+continueOAuth.get());
        if (continueOAuth.get()) {

            System.out.println("continue oauth "+continueOAuth.get());

            // Code abwarten und Tokens tauschen
            String code = receiver.waitForCode();

            TokenResponse tokenResponse = flow.newTokenRequest(code)
                    .setRedirectUri(redirectUri)
                    .execute();

            Credential credential = flow.createAndStoreCredential(tokenResponse, "OAuth");
            if (credential == null) {
                throw new IllegalStateException("No OAuth credential obtained.");
            }

        }
        receiver.stop();
        httpTransport.shutdown();
        return credential;
    }

/**
 *    * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 *    z.B. this.service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
 *                     .setApplicationName(applicationName + " [" + clientId + "]")
 *                     .build();
 */
    protected abstract void onOAuthSucceeded(Credential credential, String applicationName);


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onStartOAuth(String oAuthLink, MMutableBoolean continueOAuthOrNot);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleKeystore getKeystore() {
        return this.keystore;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * analog des löschens von OAuth cookies im browser so dass diese nicht verbleiben
     */
    public final void removePersistetOAuthToken() throws Exception {
        try {
            System.out.println("Removing persistet OAuth Token from KeyStore.");
           if(keystore.contains("OAuth")) this.keystore.remove("OAuth"); else System.out.println("OAuth Token not contained. Started in doNotPersistOAuthToken Mode ?)");
        } catch (Exception exc) {
            System.err.println("Error removing OAuth token from Keystore. " +
                    "\nPlease delete the keystore file manually and restart the program (you have to setup pw and mail adresses new)." +
                    "\nConsider to use secure OAuthMode Parameter next time."+ exc.getMessage());
            throw new Exception(exc);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final boolean isInDoNotPersistOAuthTokenMode() throws Exception {
               return this.doNotPersistOAuthToken;
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void revokeOAuthTokenFromServer() throws GeneralSecurityException, IOException {
        try {
            if (credential == null) {
                System.out.println("No credential available to revoke.");
                return;
            }

            String token = credential.getAccessToken();
            if (token == null && credential.getRefreshToken() != null) {
                token = credential.getRefreshToken();
            }

            if (token == null) {
                System.out.println("No access or refresh token found to revoke.");
                return;
            }

            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

            GenericUrl url = new GenericUrl("https://oauth2.googleapis.com/revoke");

            // Body als x-www-form-urlencoded
            String body = "token=" + token;
            ByteArrayContent content = new ByteArrayContent("application/x-www-form-urlencoded", body.getBytes(StandardCharsets.UTF_8));

            HttpRequest request = requestFactory.buildPostRequest(url, content);
            HttpResponse response = request.execute();

            if (response.getStatusCode() == 200) {
                System.out.println("OAuth token successfully revoked at server.");
            } else {
                System.err.println("Failed to revoke token. HTTP status: " + response.getStatusCode());
            }
        } catch (Exception exc) {
            System.err.println("Error while revoking OAuth token: " + exc.getMessage());
            throw exc;
        }
    }


}



