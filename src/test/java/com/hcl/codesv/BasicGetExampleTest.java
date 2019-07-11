package com.hcl.codesv;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.*;
import com.ca.codesv.engine.junit4.VirtualServerRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class BasicGetExampleTest {

    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    private static final String URL_EXAMPLE = "http://customUrl:8877/test/url";

    @Before
    public void setupVirtualService() {
        forGet(URL_EXAMPLE).doReturn(okMessage().withStringBody("Sucess"));
    }

    @Test
    public void testBasicGetExample() throws IOException {
        URL obj = new URL(URL_EXAMPLE);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL: " + URL_EXAMPLE);
        System.out.println("Response Code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        System.out.println(response.toString());
    }
}
