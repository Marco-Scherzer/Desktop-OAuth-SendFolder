package com.marcoscherzer.msimplegooglemailer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleKeyStore {

    private final String keystorePath;
    private final char[] keystorePassword;
    private static final String KEYSTORE_TYPE = "JCEKS";
    private KeyStore keyStore;

    public MSimpleKeyStore(String keystorePath, char[] keystorePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;

        keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        File ksFile = new File(keystorePath);

        if (ksFile.exists()) {
            try (FileInputStream fis = new FileInputStream(ksFile)) {
                keyStore.load(fis, keystorePassword);
            }
        } else {
            keyStore.load(null, keystorePassword);
        }
    }

    public MSimpleKeyStore addToken(String alias, String token) throws Exception {
        SecretKey secretKey = new SecretKeySpec(token.getBytes(), "AES");
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword);

        keyStore.setEntry(alias, entry, protParam);

        File ksFile = new File(keystorePath);
        ksFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(ksFile)) {
            keyStore.store(fos, keystorePassword);
        }

        ksFile.setReadable(true, true);
        ksFile.setWritable(true, true);

        return this;
    }

    public String getToken(String alias) throws Exception {
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword);
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, protParam);
        if (entry == null) return null;

        SecretKey secretKey = entry.getSecretKey();
        return new String(secretKey.getEncoded());
    }

}