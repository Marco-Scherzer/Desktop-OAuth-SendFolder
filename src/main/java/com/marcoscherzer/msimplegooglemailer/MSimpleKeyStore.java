package com.marcoscherzer.msimplegooglemailer; /**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
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
    private static final String KEYSTORE_TYPE = "JKS";
    private KeyStore keyStore;

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
            keyStore.load(null, keystorePassword.toCharArray());
        }
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final KeyManagerFactory createKeyManagerFactory(String algorithm) throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        kmf.init(keyStore, keystorePassword.toCharArray());
        return kmf;
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final TrustManagerFactory createTrustManagerFactory(String algorithm) throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(keyStore);
        return tmf;
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleKeyStore addToken(String alias, String token) throws Exception {
        SecretKey secretKey = new SecretKeySpec(token.getBytes(), "AES");
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword.toCharArray());

        keyStore.setEntry(alias, entry, protParam);

        File ksFile = new File(keystorePath);
        ksFile.getParentFile().mkdirs();
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
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword.toCharArray());
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, protParam);
        if (entry == null) return null;

        SecretKey secretKey = entry.getSecretKey();
        return new String(secretKey.getEncoded());
    }
}