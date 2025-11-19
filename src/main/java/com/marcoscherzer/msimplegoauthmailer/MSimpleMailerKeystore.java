package com.marcoscherzer.msimplegoauthmailer;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.marcoscherzer.msimplekeystore.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleMailerKeystore {

    private MSimpleKeystore keystore;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleMailerKeystore(String keystorePassword, String clientSecretJsonFile, String keystoreFile) throws MKeystoreException, MClientSecretException, MPasswordComplexityException, MPasswordIntegrityException, IOException {
        File clientSecretFile = new File(clientSecretJsonFile);
        File keyStoreFile = new File(keystoreFile);
        keystore = new MSimpleKeystore(keyStoreFile, keystorePassword);
        if(keyStoreFile.exists()) keystore.loadKeystore(); else{ keystore.createKeystore();}
        //setup mode, setzt "clientId", "google-client-id", "google-client-secret"
        checkAndSetupKeystoreIfNeeded(clientSecretFile);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleKeystore getKeyStore(){
          return keystore;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void checkAndSetupKeystoreIfNeeded(File jsonFile) throws MKeystoreException, MClientSecretException, IOException {
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        String clientId;
        String clientSecret;
        boolean incomplete;
        try { incomplete= !keystore.containsAllNonNullKeys("clientId", "google-client-id", "google-client-secret");} catch(Exception exc){ throw exc;}
            if (incomplete) {
                System.out.println("Checking if file \"client_secret.json\" exists");
                if (jsonFile.exists()) {
                    System.out.println("File \"client_secret.json\" found. Reading tokens.");
                    GoogleClientSecrets secrets = GoogleClientSecrets.load(jsonFactory, new FileReader(jsonFile));
                    clientId = secrets.getDetails().getClientId();
                    clientSecret = secrets.getDetails().getClientSecret();
                    if (clientId != null && clientSecret != null) {
                        System.out.println("Tokens exist. Saving found tokens to encrypted keystore. ");
                        keystore.put("google-client-id", clientId).put("google-client-secret", clientSecret);
                        System.out.println("Tokens successfully saved");
                    } else {
                        throw new MClientSecretException("client_secret.json does not contain valid credentials.");
                    }
                } else {
                    throw new MClientSecretException("client_secret.json must be placed in the directory \"" + jsonFile.getPath() + "\" before first launch.");
                }

                UUID uuid = UUID.randomUUID();
                System.out.println("Client security UUID generated. " + uuid + ". Saving UUID in encrypted keystore. ");
                keystore.put("clientId", uuid.toString());
            }

    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void clearKeystore() throws MKeystoreException, Exception {
        if (keystore.successfullyInitialized()) {
            System.out.println("clearing KeyStore");
            try {
                keystore.clear();
            } catch (Exception exc) {
                System.err.println("Error while clearing keystore.\n" + exc.getMessage());
                System.err.println("Initialization failed and keystore could not be cleared. Please delete it manually");
                throw new Exception(exc);
            }
        }
    }

}
