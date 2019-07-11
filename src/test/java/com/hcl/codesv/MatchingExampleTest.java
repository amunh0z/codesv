package com.hcl.codesv;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.*;
import com.ca.codesv.engine.junit4.VirtualServerRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.hamcrest.CoreMatchers;

public class MatchingExampleTest {
    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    private static final String URL = "http://wwww.ca.com/portfolio?year=2019&tokenQuery=X4sPhj15WQE";
    private static final String JSON_EXAMPLES_PORTFOLIO = "{"
            + "\"portfolio\": {\n"
            + " \"id\": \"1\", \n"
            + " \"year\": \"2019\", \n"
            + " \"productNamesList\": [\n"
            + " \"CA Agile Requirements Designer\", \n"
            + " \"CA Test Data Manager\", \n"
            + " \"CA Service Virtualization\", \n"
            + " \"CA Continuous Delivery Director\", \n"
            + " \"CA Continuous Delivery Automation\", \n"
            + " \"CA Blazemeter\"\n"
            + " ]\n"
            + "}}";

    @Test
    public void testMatching() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);
        request.addHeader("Accept-Language", "en_us");
        request.addHeader("Custom-Header", "CustomValue");
        HttpResponse response = client.execute(request);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line;

        while((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testVirtualizedMatching() throws Exception {
        forGet(URL)
                .matchesHeader("Custom-Header", "CustomValue")
                .matchesHeader("Accept-Language", contains("us"))
                .matchesQuery("tokenQuery", isEqualIgnoringCaseTo("x4sphj15wqe"))
                .matchesQuery("year", "2019")
                .doReturn(okMessage().withJsonBody(JSON_EXAMPLES_PORTFOLIO));

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);
        request.addHeader("Accept-Language", "en_us");
        request.addHeader("Custom-Header", "CustomValue");
        HttpResponse response = client.execute(request);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line;

        while((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        System.out.println("Response code: " + response.getStatusLine().getStatusCode());
        System.out.println("Response body: " + result.toString());


        Assert.assertEquals(200, response.getStatusLine().getStatusCode());

    }

    @Test
    public void testVirtualizedMatchingUsingLambda() throws IOException {
        forGet(URL)
                .matchesHeader("Custom-Header", s -> s.equals("CustomValue"))
                .doReturn(okMessage().withStringBody("Success"));

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);
        request.addHeader("Accept-Language", "en_us");
        request.addHeader("Custom-Header", "CustomValue");
        HttpResponse response = client.execute(request);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line;

        while((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        System.out.println("Response code: " + response.getStatusLine().getStatusCode());
        System.out.println("Response body: " + result.toString());


        Assert.assertEquals(200, response.getStatusLine().getStatusCode());

    }

    @Test
    public void testJsonPath() throws Exception {
        forPost(URL)
                .matchesBodyPayload(matchesJsonPath("$.portfolio.id","1"))
                .matchesBodyPayload(matchesJsonPath("$.portfolio.year","2019"))
                .matchesBodyPayload(matchesJsonPath("$.portfolio.year",contains("19")))
                .matchesBodyPayload(matchesJsonPath("$.portfolio.productNamesList",
                        CoreMatchers.hasItem("CA Service Virtualization")))
                .doReturn(okMessage().withStringBody("Success"));

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(URL);
        request.addHeader("Content-Type", "application/json");
        StringEntity params = new StringEntity(JSON_EXAMPLES_PORTFOLIO);
        request.setEntity(params);

        HttpResponse response = client.execute(request);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line;

        while((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        System.out.println("Response code: " + response.getStatusLine().getStatusCode());
        System.out.println("Response body: " + result.toString());


        Assert.assertEquals(200, response.getStatusLine().getStatusCode());

    }
}


