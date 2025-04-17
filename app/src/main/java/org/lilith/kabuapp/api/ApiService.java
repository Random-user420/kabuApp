package org.lilith.kabuapp.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ApiService
{
    protected String baseUrl;
    private final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    private final Gson gson = new Gson();
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
            final String jsonBody = gson.toJson(body);

            Logger.getLogger("API").log(Level.INFO,
                    "AuthRequest Body: " + jsonBody);

            request.setContent(new HttpContent() {
                @Override
                public long getLength() {
                    return jsonBody.getBytes(StandardCharsets.UTF_8).length;
                }

                @Override
                public String getType() {
                    return "application/json";
                }

                @Override
                public boolean retrySupported() {
                    return false;
                }

                @Override
                public void writeTo(OutputStream out) throws IOException {
                    out.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                }
            });
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
