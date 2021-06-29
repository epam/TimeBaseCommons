/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.net;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;

/**
 * SSLContext provider creates and inits SSLContext object for SSL support
 */
public class SSLContextProvider {

    public static final String KEYSTORE_FORMAT                            = "JKS";
    public static final String SAFE_TRANSPORT_PROTOCOL                    = "TLS";

    public static SSLContext                        createSSLContext(String keystoreFile, String keystorePass, boolean trustALL)
            throws GeneralSecurityException, IOException {

        return createSSLContext(SAFE_TRANSPORT_PROTOCOL, keystoreFile, keystorePass, trustALL);
    }

    public static SSLContext                        createSSLContext(String protocol, String keystoreFile, String keystorePass, boolean trustALL)
            throws GeneralSecurityException, IOException
    {
        SSLContext context = SSLContext.getInstance(protocol);
        context.init(getKeyManagers(keystoreFile, keystorePass), getTrustManagers(trustALL, keystoreFile, keystorePass), null);
        //Util.LOGGER.info("SSLContext based on " + keystoreFile + " loaded successfully.");
        return context;
    }

//    public static SSLContext                        getDefaultContext() {
//        SSLContext context;
//        try {
//            context = SSLContext.getDefault();
//            Util.LOGGER.info("Default SSLContext loaded successfully.");
//        } catch (Exception e) {
//            Util.LOGGER.warning(e.getMessage());
//            context = null;
//        }
//
//        return context;
//    }

    private static SSLContext                       createSSLContext(KeyStore keyStore) throws GeneralSecurityException {

        TrustManagerFactory tmf =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance(SAFE_TRANSPORT_PROTOCOL);
        context.init(null, tmf.getTrustManagers(), null);

        return context;
    }

    private static KeyManager[]                     getKeyManagers(String keystoreFile, String keystorePass)
            throws GeneralSecurityException, IOException
    {
        KeyManager[] keyManagers = null;

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_FORMAT);
        keyStore.load(new FileInputStream(keystoreFile), keystorePass != null ? keystorePass.toCharArray() : null);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePass != null ? keystorePass.toCharArray() : null);

        keyManagers = keyManagerFactory.getKeyManagers();


        return keyManagers;
    }

    private static TrustManager[]                   getTrustManagers(boolean trustAll, String keystoreFile, String keystorePass)
            throws GeneralSecurityException, IOException
    {
        TrustManager[] trustManagers = null;

        if (!trustAll) {
            KeyStore keystore = KeyStore.getInstance(KEYSTORE_FORMAT);
            try (FileInputStream stream = new FileInputStream(keystoreFile)) {
                keystore.load(stream, keystorePass != null ? keystorePass.toCharArray() : null);
            }

            //create and init trust manager factory
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);

            //create trust manager
            trustManagers = trustManagerFactory.getTrustManagers();
        } else {
            //trust all connections
            trustManagers = new TrustManager[] {
                    new TrustAllX509TrustManager()
            };
        }


        return trustManagers;
    }

    /**
     * This trust manager accepts all certificates as valid.
     */
    @SuppressFBWarnings(value = "WEAK_TRUST_MANAGER", justification = "Intended behavior")
    private static class TrustAllX509TrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
    }
}