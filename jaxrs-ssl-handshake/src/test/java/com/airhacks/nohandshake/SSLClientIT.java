/*
 */
package com.airhacks.nohandshake;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class SSLClientIT {

    private Client client;

    private String host = "https://localhost:4848";
    private WebTarget tut;

    @Before
    public void init() throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] noopTrustManager = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext sc = SSLContext.getInstance("ssl");
        sc.init(null, noopTrustManager, null);
        this.client = ClientBuilder.newBuilder().
                sslContext(sc).
                build();

        this.tut = this.client.target(this.host);
    }

    @Test
    public void get() {
        String result = this.tut.request().get(String.class);
        System.out.println("result = " + result);
    }

}
