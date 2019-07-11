package com.hcl.codesv;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.*;
import com.ca.codesv.engine.junit4.VirtualServerRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

public class HttpsExampleTest {
    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    private static final String KEYSTORE_PATH = HttpsExampleTest.class.getClassLoader()
            .getResource("ssl/keystore.jks").getPath();

    private TrustManager[] trustAllCerts;
    private SSLContext sslContext;

    @Before
    public void setUp() throws Exception {
        trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        if (x509Certificates.length != 1 || !x509Certificates[0].getIssuerX500Principal().getName().contains("CN=localhost")) {
                            throw new SecurityException("Invalid certificate");
                        }
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }

        };

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null,trustAllCerts,new SecureRandom());
    }

    @Test
    public void testHttpsExample() throws Exception {
        forGet("https://localhost:8090/")
                .usingHttps(withSecureProtocol("TLS")
                        .keystorePath(KEYSTORE_PATH)
                        .keystorePassword("password")
                        .keyPassword("password")
                ).doReturn(okMessage().withStringBody("Success"));

        URL url = new URL("https://localhost:8090/");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setSSLSocketFactory(sslContext.getSocketFactory());

        String responseBody = IOUtils.toString((InputStream) connection.getContent());

        System.out.println("Response status code: " + connection.getResponseCode());
        System.out.println("Response body: " + responseBody);

        Assert.assertEquals(200, connection.getResponseCode());
        Assert.assertEquals("Success", responseBody);
    }

}

