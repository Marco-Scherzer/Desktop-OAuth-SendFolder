package com.marcoscherzer.msimplegoauthhelper;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.marcoscherzer.msimplekeystore.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleOAuthKeystore {

    private MSimpleKeystore keystore;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleOAuthKeystore(String keystorePassword, String clientSecretJsonFile, String keystoreFilePath) throws MKeystoreException, MClientSecretException, MPasswordComplexityException, MPasswordIntegrityException, IOException {
        File jsonFile = new File(clientSecretJsonFile);
        File keyStoreFile = new File(keystoreFilePath);
        keystore = new MSimpleKeystore(keyStoreFile, keystorePassword);
        if (!keyStoreFile.exists() && !jsonFile.exists()) throw new MClientSecretException("client_secret.json must be placed in the directory \"" + jsonFile.getPath() + "\" before first launch.");
        if(keyStoreFile.exists()) keystore.loadKeystore(); else{ keystore.createKeystore();}
        //check if incomplete for existing or new created keystore , sets "clientId", "google-client-id", "google-client-secret" if not existing
        checkAndSetupKeystoreIfNeeded(jsonFile);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleKeystore getKeystore(){
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
            if (incomplete) {  //check if incomplete for existing or new created keystore
                    System.out.println("Checking if file \"client_secret.json\" exists");
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
