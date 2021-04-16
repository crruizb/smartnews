package com.github.cristianrb.smartnews.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

@Component
public class GoogleTokenVerifier {

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();
    @Value("${security.oauth2.client.client-id}")
    private static String CLIENT_ID;
    private static final String CLIENT_SECRET = "9JEXXegig0Y4QHwtB_LxHYEQ";


    public Payload verify(String idTokenString)
            throws GeneralSecurityException, IOException {
        return GoogleTokenVerifier.verifyToken(idTokenString);
    }

    private static Payload verifyToken(String idTokenString)
            throws GeneralSecurityException, IOException {
        final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.
                Builder(transport, jsonFactory)
                .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();


        System.out.println("validating:" + idTokenString);

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
            }
        } catch (IllegalArgumentException e){
            // means token was not valid and idToken
            // will be null
            e.printStackTrace();
        }

        if (idToken == null) {
            // Not valid

        }

        return idToken.getPayload();
    }
}
