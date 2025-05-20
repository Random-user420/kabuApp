package org.kabuapp.kabuapp.api;

import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kabuapp.kabuapp.api.exceptions.BadRequestException;
import org.kabuapp.kabuapp.api.exceptions.UnauthorisedException;
import org.kabuapp.kabuapp.api.models.AuthRequest;
import org.kabuapp.kabuapp.api.models.LessonResponse;

public class DigikabuApiService extends ApiService
{
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public DigikabuApiService()
    {
        super();
        baseUrl = "https://digikabu.de/api/";
    }

    public String auth(String username, String password) throws BadRequestException
    {
        try
        {
            String response = executeRequest(
                    "authenticate",
                    "POST",
                    String.class,
                    null,
                    null,
                    new AuthRequest(username, password));
            return response.substring(1, response.length() - 1);
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

    public List<LessonResponse> getSchedule(String token, LocalDate date, int days) throws UnauthorisedException
    {
        try
        {
            return executeRequest (
                    "stundenplan",
                    "GET",
                    new TypeToken<List<LessonResponse>>()
                    { }
                            .getType(),
                    Map.of("datum",
                            date.format(formatter) + "T00:00:01.123Z",
                            "anzahl", days),
                    Map.of("Authorization", "Bearer " + token),
                    null
            );
        }
        catch (Exception e)
        {
            if (e instanceof UnauthorisedException)
            {
                throw new UnauthorisedException();
            }
            Logger.getLogger("API").log(Level.WARNING, e.toString());
            return null;
        }
    }
}
