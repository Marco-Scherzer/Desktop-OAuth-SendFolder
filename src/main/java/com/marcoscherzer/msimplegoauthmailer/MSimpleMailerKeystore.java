package com.marcoscherzer.msimplegoauthmailer;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.File;
import java.io.FileReader;
import java.util.UUID;

import static com.marcoscherzer.msimplegoauthmailer.MSimpleMailerUtil.checkPasswordComplexity;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MSimpleMailerKeystore {

    private MSimpleKeystore keystore;
    private static String clientSecretDir = System.getProperty("user.dir");
    private File keystoreFile = new File(clientSecretDir, "mystore.p12");
    private File jsonFile = new File(clientSecretDir, "client_secret.json");

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleMailerKeystore(String keystorePassword,String clientSecretFileDir){
        initializeKeyStore(keystorePassword, clientSecretFileDir);
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
    private final boolean initializeKeyStore(String keystorePassword, String clientSecretFileDir) {
        try {
            checkPasswordComplexity(keystorePassword, 15, true, true, true);
            keystore = new MSimpleKeystore(keystoreFile, keystorePassword);
            keystore.loadKeyStoreOrCreateKeyStoreIfNotExists();
            if (!keystore.newCreated()) onPasswordIntegritySuccess();
            //setup mode, setzt "clientId", "google-client-id", "google-client-secret"
            checkAndSetupKeystoreIfNeeded(jsonFile);
        } catch (MPasswordIntegrityException exc) {
            onPasswordIntegrityFailure(exc);
            return false;
        } catch (MClientSecretException exc) {
            //try { clearKeystore(); } catch (Exception exc2) { exc.addSuppressed(exc2);};
            onClientSecretInitalizationFailure((MClientSecretException) exc);
            return false;
        } catch (Exception exc) {
            onCommonInitializationFailure(exc);
            return false;
        }
        return true;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void checkAndSetupKeystoreIfNeeded(File jsonFile) throws Exception {
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        String clientId;
        String clientSecret;
        if (keystore.newCreated() || !keystore.containsAllNonNullKeys("clientId", "google-client-id", "google-client-secret")) {
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
                throw new MClientSecretException("client_secret.json must be placed in the directory \"" + clientSecretDir + "\" before first launch.");
            }

            UUID uuid = UUID.randomUUID();
            System.out.println("Client security UUID generated. " + uuid + ". Saving UUID in encrypted keystore. ");
            keystore.put("clientId", uuid.toString());
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onCommonInitializationFailure(Throwable exc);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onClientSecretInitalizationFailure(MClientSecretException exc);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onPasswordIntegrityFailure(MPasswordIntegrityException exc);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onPasswordComplexityFailure(MPasswordComplexityException exc);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onPasswordIntegritySuccess();
}
