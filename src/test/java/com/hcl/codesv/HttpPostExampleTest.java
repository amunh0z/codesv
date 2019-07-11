package com.hcl.codesv;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.*;
import com.ca.codesv.engine.junit4.VirtualServerRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

public class HttpPostExampleTest {
    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    private static final String URL = "http://www.ca.com/portfolio";
    private static final String RESPONSE_BODY_GET = "Response body from virtualized service.";
    private static final int CUSTOM_STATUS_CODE = 258;

    @Test
    public void testHttpPostCallService() throws IOException {
        HttpPost httpPost = new HttpPost(URL);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(httpPost);

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        Assert.assertEquals(CUSTOM_STATUS_CODE, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testHttpPostCallWithVirtualizedService() throws IOException {
        forPost(URL).doReturn(aMessage(CUSTOM_STATUS_CODE).withStringBody(RESPONSE_BODY_GET));

        HttpPost httpPost = new HttpPost(URL);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(httpPost);

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        System.out.println("Response code: " + httpResponse.getStatusLine().getStatusCode());
        System.out.println("Response body: " + response.toString());

        Assert.assertEquals(CUSTOM_STATUS_CODE, httpResponse.getStatusLine().getStatusCode());

    }

}
