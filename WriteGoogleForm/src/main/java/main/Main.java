package main;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(final String[] args) throws Exception {
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "https://docs.google.com/forms/d/e/1FAIpQLSeASyrsu98TJ6tMQ-7vayUJJ1aqL1PhbIlXrG7dqCDAJjZOJg/formResponse";
        String entry = "entry.2087753367";
        System.out.print("Input login: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String login = reader.readLine();

        final HttpPost httpPost = new HttpPost(url);
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(entry, login));
        params.add(new BasicNameValuePair("fvv", "1"));
        params.add(new BasicNameValuePair("draftResponse", "[null,null,\\\"-7478797354586800581\\\"]\\r\\n"));
        params.add(new BasicNameValuePair("pageHistory", "0"));
        params.add(new BasicNameValuePair("fbzx", "-7478797354586800581"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        try (
                CloseableHttpResponse response2 = httpclient.execute(httpPost)
        ) {
            final HttpEntity entity2 = response2.getEntity();
            //System.out.println(EntityUtils.toString(entity2));
        }
        httpclient.close();
    }
}