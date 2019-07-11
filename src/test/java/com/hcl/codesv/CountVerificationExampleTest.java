package com.hcl.codesv;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.*;
import com.ca.codesv.engine.junit4.VirtualServerRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;

public class CountVerificationExampleTest {
    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    @Before
    public void setUp() {
        System.setProperty("codesv.export", "CodeSvExport/");
    }

    @Test
    public void testCountVerification() throws IOException {
        forGet("http://www.ca.com/portfolio")
                .doReturn(okMessage().withStringBody("Success"))
                .invoked(2);

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://www.ca.com/portfolio");

        client.execute(request);
        client.execute(request);

    }

    @Test
    public void testCountVerification2() throws IOException {
        forGet("http://www.ca.com/portfolio")
                .doReturn(okMessage().withStringBody("Success"))
                .invoked(moreThan(2));

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://www.ca.com/portfolio");

        client.execute(request);
        client.execute(request);

    }

    @Test
    public void testCountVerification3() throws IOException {
        forGet("http://www.ca.com/portfolio")
                .doReturn(okMessage().withStringBody("Success"))
                .invoked(moreThanOrEqualTo(2));

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://www.ca.com/portfolio");

        client.execute(request);
        client.execute(request);

    }

    @Test
    public void testCountVerification4() throws IOException {
        forGet("http://www.ca.com/portfolio")
                .doReturn(okMessage().withStringBody("Success"))
                .invoked(lessThan(2));

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://www.ca.com/portfolio");

        client.execute(request);
        client.execute(request);

    }

    @Test
    public void testCountVerification5() throws IOException {
        forGet("http://www.ca.com/portfolio")
                .doReturn(okMessage().withStringBody("Success"))
                .invoked(lessThanOrEqualTo(2));

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://www.ca.com/portfolio");

        client.execute(request);
        client.execute(request);

    }
}

