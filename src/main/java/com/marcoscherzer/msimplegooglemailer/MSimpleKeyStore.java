package com.marcoscherzer.msimplegooglemailer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleKeystore {

    private final File ksFile;
    private final String keystorePassword;
    private static final String KEYSTORE_TYPE = "PKCS12";
    private KeyStore keyStore;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleKeystore(File ksFile, String keystorePassword) {
        this.ksFile = ksFile;
        this.keystorePassword = keystorePassword;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final boolean loadKeyStoreOrCreateKeyStoreIfNotExists() throws MPasswordIncorrectException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            if (ksFile.exists()) {
                try (FileInputStream fis = new FileInputStream(ksFile)) {
                    keyStore.load(fis, keystorePassword.toCharArray());
                }
                return false;
            } else {
                System.out.println("Keystore is being created: " + ksFile.getAbsolutePath());
                keyStore.load(null, keystorePassword.toCharArray());
                ksFile.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(ksFile)) {
                    keyStore.store(fos, keystorePassword.toCharArray());
                }
                return true;
            }
        } catch (IOException exc) {
            throw new MPasswordIncorrectException("Incorrect password", exc);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleKeystore add(String alias, String token) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        SecretKey secretKey = new SecretKeySpec(token.getBytes(), "AES");
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword.toCharArray());

        keyStore.setEntry(alias, entry, protParam);

        try (FileOutputStream fos = new FileOutputStream(ksFile)) {
            keyStore.store(fos, keystorePassword.toCharArray());
        }

        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final boolean isCompletelyInitialized(String... keys) {
        try {
            for (String key : keys) {
                String value = get(key);
                if (value == null || value.isBlank()) {
                    return false;
                }
            }
            return true;
        } catch (Exception exc) {
            return false;
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String get(String alias) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword.toCharArray());
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, protParam);
        if (entry == null) return null;

        SecretKey secretKey = entry.getSecretKey();
        return new String(secretKey.getEncoded());
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final boolean contains(String alias) {
        try {
            return keyStore.containsAlias(alias);
        } catch (KeyStoreException exc) {
            return false;
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final boolean remove(String alias) throws RuntimeException {
        try {
            if (keyStore.containsAlias(alias)) {
                keyStore.deleteEntry(alias);
                try (FileOutputStream fos = new FileOutputStream(ksFile)) {
                    keyStore.store(fos, keystorePassword.toCharArray());
                }
                return true;
            }
        } catch (Exception exc) {
            throw new RuntimeException("Error while removing \"" + alias + "\"", exc);
        }
        return false;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void clear() throws RuntimeException {
        try {
            java.util.Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                keyStore.deleteEntry(alias);
            }

            try (FileOutputStream fos = new FileOutputStream(ksFile)) {
                keyStore.store(fos, keystorePassword.toCharArray());
            }
        } catch (Exception exc) {
            throw new RuntimeException("Error while clearing the keystore", exc);
        }
    }
}

