package com.marcoscherzer.msimplegooglemailer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;

/**
     * Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final class MServerSocketConfig {

                 /*
    Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
    */
    final SSLServerSocket createSocket(int port,int backlog) throws SocketException, UnknownHostException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
            // Initialize the SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // Set up key manager factory
            char[] password = "mypassword".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(Files.newInputStream(Paths.get(System.getProperty("user.dir")+"mystore.jks")), password);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);
            // Set up trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);
            // Initialize SSL context
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLServerSocket serverSocket=((SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket(port,backlog,InetAddress.getLoopbackAddress()));
            System.out.println("SECURE ServerSocket started @ "+serverSocket.getInetAddress()+" @ port " + serverSocket.getLocalPort());
            serverSocket.setReceiveBufferSize(100);
            serverSocket.setReuseAddress(true);
            serverSocket.setPerformancePreferences(20,5,10);
            serverSocket.setSoTimeout(10);
            return serverSocket;
       }

       
    }