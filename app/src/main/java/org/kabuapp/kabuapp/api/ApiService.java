package org.kabuapp.kabuapp.api;

import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.kabuapp.kabuapp.api.exceptions.BadRequestException;
import org.kabuapp.kabuapp.api.exceptions.UnauthorisedException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class ApiService
{
    protected String baseUrl;
    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    private final Gson gson = new Gson();

    protected <T> T executeRequest(
            String url,
            String httpMethod,
            Type responseType,
            Map<String, Object> params,
            Map<String, String> headers,
            Object body) throws Exception
    {

        URI uri;
        try
        {
            URIBuilder uriBuilder = new URIBuilder(baseUrl + url);
            if (params != null && !params.isEmpty())
            {
                params.forEach((key, value) -> uriBuilder.addParameter(key, String.valueOf(value)));
            }
            uri = uriBuilder.build();
        }
        catch (Exception e)
        {
            throw new IOException("Failed to build URI", e);
        }


        HttpUriRequestBase request;

        switch (httpMethod.toUpperCase())
        {
            case "GET":
                request = new HttpGet(uri);
                break;
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                if (body != null)
                {
                    String jsonBody = gson.toJson(body);
                    httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
                }
                request = httpPost;
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                if (body != null)
                {
                    String jsonBody = gson.toJson(body);
                    httpPut.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
                }
                request = httpPut;
                break;
            default:
                throw new UnsupportedOperationException("HTTP method " + httpMethod + " is not supported.");
        }

        if (headers != null && !headers.isEmpty())
        {
            headers.forEach(request::setHeader);
        }
        request.setHeader("Host", getDefaultHostHeader(baseUrl + url));

        try (CloseableHttpResponse response = httpClient.execute(request))
        {
            int statusCode = response.getCode();
            HttpEntity responseEntity = response.getEntity();
            String responseString = responseEntity != null ? EntityUtils.toString(responseEntity, StandardCharsets.UTF_8) : null;

            if (statusCode < 200 || statusCode >= 300)
            {
                if (statusCode == 400)
                {
                    throw new BadRequestException();
                }
                else if (statusCode == 401)
                {
                    throw new UnauthorisedException();
                }
                throw new Exception("API-Error: " + statusCode + " - " + response.getReasonPhrase() + "\nResponse Body: " + responseString);
            }

            if (responseType instanceof Class && responseType.equals(String.class))
            {
                return (T) responseString;
            }
            else if (responseString != null)
            {
                return gson.fromJson(responseString, responseType);
            }
            else
            {
                return null;
            }
        }
        finally
        {
            request.abort();
        }
    }

    public void closeHttpClient() throws IOException
    {
        if (httpClient != null)
        {
            httpClient.close();
        }
    }

    private static String getDefaultHostHeader(String urlString) throws MalformedURLException
    {
        URL url = new URL(urlString);
        String host = url.getHost();
        int port = url.getPort();
        if (port != -1)
        {
            return host + ":" + port;
        }
        else
        {
            return host;
        }
    }
}