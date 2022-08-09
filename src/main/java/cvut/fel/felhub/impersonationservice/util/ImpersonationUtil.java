package cvut.fel.felhub.impersonationservice.util;

import cvut.fel.felhub.impersonationservice.exception.InvalidImpersonationException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ImpersonationUtil {

    private ImpersonationUtil() {}

    public static final long DEFAULT_ALLOWED_NUM_OF_DAYS_BEFORE_EXPIRATION = 7;

    private static final long MAX_ALLOWED_NUM_OF_DAYS_BEFORE_EXPIRATION = 30;

    public static LocalDateTime getDefaultExpirationDate() {
        return LocalDateTime.now().plusDays(DEFAULT_ALLOWED_NUM_OF_DAYS_BEFORE_EXPIRATION);
    }

    public static void validateRequestedImpersonationExpirationDate(LocalDateTime requestedDate) {

        if (requestedDate.isBefore(LocalDateTime.now()))
            throw new InvalidImpersonationException("Expiration date should be bigger than current date");

        long daysDifference = ChronoUnit.DAYS.between(LocalDateTime.now(), requestedDate);

        if (daysDifference > MAX_ALLOWED_NUM_OF_DAYS_BEFORE_EXPIRATION)
            throw new InvalidImpersonationException("Maximum allowed number of days before expiration is equal to: " + MAX_ALLOWED_NUM_OF_DAYS_BEFORE_EXPIRATION);
    }
}
