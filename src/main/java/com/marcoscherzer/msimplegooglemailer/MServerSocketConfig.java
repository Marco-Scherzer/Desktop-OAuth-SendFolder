/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
package com.marcoscherzer.msimplegooglemailer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MServerSocketConfig {

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final SSLServerSocket createSocket(MSimpleKeyStore store, int port, int backlog)
            throws Exception {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(
                store.createKeyManagerFactory("SunX509").getKeyManagers(),
                store.createTrustManagerFactory("SunX509").getTrustManagers(),
                null
        );

        SSLServerSocket serverSocket = (SSLServerSocket) sslContext
                .getServerSocketFactory()
                .createServerSocket(port, backlog, InetAddress.getLoopbackAddress());

        System.out.println("SECURE ServerSocket started @ " + serverSocket.getInetAddress() + " @ port " + serverSocket.getLocalPort());
        serverSocket.setReuseAddress(true);

        return serverSocket;
    }
}
