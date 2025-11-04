package com.marcoscherzer.msimplegooglemailer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleKeyStore {

    private final String keystorePath;
    private final String keystorePassword;
    private static final String KEYSTORE_TYPE = "PKCS12"; // moderner Keystore-Typ
    private KeyStore keyStore;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleKeyStore(String keystorePath, String keystorePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;

        keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        File ksFile = new File(keystorePath);

        if (ksFile.exists()) {
            try (FileInputStream fis = new FileInputStream(ksFile)) {
                keyStore.load(fis, keystorePassword.toCharArray());
            }
        } else {
            System.out.println("Keystore wird neu erstellt: " + ksFile.getAbsolutePath());
            keyStore.load(null, keystorePassword.toCharArray());

            // Leerer Keystore wird sofort gespeichert
            ksFile.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(ksFile)) {
                keyStore.store(fos, keystorePassword.toCharArray());
            }
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleKeyStore addToken(String alias, String token) throws Exception {
        SecretKey secretKey = new SecretKeySpec(token.getBytes(), "AES");
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter protParam =
                new KeyStore.PasswordProtection(keystorePassword.toCharArray());

        keyStore.setEntry(alias, entry, protParam);

        File ksFile = new File(keystorePath);
        try (FileOutputStream fos = new FileOutputStream(ksFile)) {
            keyStore.store(fos, keystorePassword.toCharArray());
        }

        ksFile.setReadable(true, true);
        ksFile.setWritable(true, true);

        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String getToken(String alias) throws Exception {
        KeyStore.ProtectionParameter protParam =
                new KeyStore.PasswordProtection(keystorePassword.toCharArray());
        KeyStore.SecretKeyEntry entry =
                (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, protParam);
        if (entry == null) return null;

        SecretKey secretKey = entry.getSecretKey();
        return new String(secretKey.getEncoded());
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public boolean contains(String alias) {
        try {
            return keyStore.containsAlias(alias);
        } catch (KeyStoreException e) {
            return false;
        }
    }
}
