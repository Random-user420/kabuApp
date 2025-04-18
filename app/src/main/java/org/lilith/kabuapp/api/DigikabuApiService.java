package org.lilith.kabuapp.api;


import com.google.api.client.http.HttpResponseException;
import com.google.gson.reflect.TypeToken;

import org.lilith.kabuapp.api.models.AuthRequest;
import org.lilith.kabuapp.api.models.LessonResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DigikabuApiService extends ApiService{
    public DigikabuApiService() {
        super();
        baseUrl = "https://digikabu.de/api/";
    }

    public String auth(String username, String password) throws BadRequestException
    {
        try
        {
            return executeRequest(
                    "authenticate",
                    "POST",
                    String.class,
                    null,
                    null,
                    new AuthRequest(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadRequestException)
            {
                throw (BadRequestException) e;
            }
            Logger.getLogger("API").log(Level.WARNING, e.toString());
            return null;
        }
    }

    public List<LessonResponse> getSchedule(String token, LocalDate date, int days) throws UnauthorisedException {
        try
        {
            return executeRequest(
                    "stundenplan",
                    "GET",
                    new TypeToken<List<LessonResponse>>(){}.getType(),
                    Map.of("datum", date, "anzahl", days),
                    Map.of("Authorization", token),
                    null
            );
        }
        catch (Exception e)
        {
            if (e instanceof HttpResponseException)
            {
                if (((HttpResponseException) e).getStatusCode() == 401)
                {
                    throw new UnauthorisedException();
                }
            }
            Logger.getLogger("API").log(Level.WARNING, e.toString());
            return null;
        }
    }
}
