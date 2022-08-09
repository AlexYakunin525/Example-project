package cvut.fel.felhub.impersonationservice.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CredentialsParserUtil {

    private CredentialsParserUtil() {}

    public static Map<String, String> parseCredentials(String credentials) {
        boolean validFormat = Pattern.matches("^(\\w+:\\w+)(,\\w+:\\w+)*$", credentials);
        if (!validFormat) {
            throw new IllegalArgumentException("Invalid format of management credentials");
        }

        Map<String, String> adminsCredentials = new HashMap<>();
        var parsedCredentials = credentials.split(",");

        for (String parsedCredential : parsedCredentials) {
            var userCredential = parsedCredential.split(":");

            adminsCredentials.put(userCredential[0], userCredential[1]);
        }

        return adminsCredentials;
    }
}
