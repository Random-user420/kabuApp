package org.lilith.kabuapp.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Map;

public abstract class ApiService
{
    protected String baseUrl;
    private final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    private final GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    protected  <T> T executeRequest(
            String url,
            String httpMethod,
            Class<T> responseModel,
            Map<String, Object> params,
            Map<String, String> headers,
            Object body) throws Exception {

        GenericUrl genericUrl = new GenericUrl(baseUrl + url);

        if (params != null && !params.isEmpty())
        {
            genericUrl.putAll(params);
        }

        HttpRequest request = requestFactory.buildRequest(httpMethod, genericUrl, null);

        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null && !headers.isEmpty())
        {
            httpHeaders.putAll(headers);
        }
        request.setHeaders(httpHeaders);

        if (body != null) {
            request.setContent(new JsonHttpContent(jsonFactory, body));
        }

        HttpResponse response = request.execute();

        if (!response.isSuccessStatusCode())
        {
            if (response.getStatusCode() == 400)
            {
                throw new BadRequestException();
            }
            throw new Exception("API-Fehler: " + response.getStatusCode() + " - " + response.getStatusMessage());
        }

        if (responseModel.equals(String.class))
        {
            return (T) response.parseAsString();
        }
        else 
        {
            return response.parseAs(responseModel);
        }
    }
}
