package bg.hollyweed.greatkitsupdated.utils;

import java.util.concurrent.TimeUnit;

public class TimeFormat
{
    public static String getFormattedCooldown(final long millis) {
        if (millis <= 60000L) {
            return getFormattedCooldownSeconds(millis);
        }
        if (millis <= 3600000L) {
            return getFormattedCooldownMinutes(millis);
        }
        if (millis <= 86400000L) {
            return getFormattedCooldownHours(millis);
        }
        return getFormattedCooldownDays(millis);
    }
    
    private static String getFormattedCooldownDays(final long millis) {
        final String hms = String.format("%02dd%02dh%02dm%02ds", TimeUnit.MILLISECONDS.toDays(millis), TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }
    
    private static String getFormattedCooldownHours(final long millis) {
        final String hms = String.format("%02dh%02dm%02ds", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }
    
    private static String getFormattedCooldownMinutes(final long millis) {
        final String hms = String.format("%02dm%02ds", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }
    
    private static String getFormattedCooldownSeconds(final long millis) {
        return String.valueOf(millis / 1000L + "s");
    }
}
