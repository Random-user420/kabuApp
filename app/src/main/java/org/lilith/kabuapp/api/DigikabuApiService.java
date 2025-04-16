package org.lilith.kabuapp.api;


import org.lilith.kabuapp.api.models.AuthRequest;

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
                    Map.of("Content-Type", "application/json"),
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

}
