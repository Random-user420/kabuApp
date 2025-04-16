package org.lilith.kabuapp.api.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthRequest implements Serializable {
    private String userName;
    private String password;
}
