package tmassist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import tmassist.enums.DayOfWeek;
import tmassist.enums.ScheduledEventFilter;

public class ScheduledEventUtils {
	public static String toTable(final ScheduledEvent[] events, final TimeZone timeZone, final ScheduledEventFilter scheduledEventFilter, final String newline) {
		final long currentTimeMillis = System.currentTimeMillis();
		
		final SimpleDateFormat dayOnly = new SimpleDateFormat("EEEE");
		DayOfWeek searchBy = null;
		switch(scheduledEventFilter) {
			case TODAY: {
				searchBy = DayOfWeek.valueOf(dayOnly.format(currentTimeMillis).toUpperCase());
				break;
			}
			case TOMORROW: {
				searchBy = DayOfWeek.valueOf(dayOnly.format(currentTimeMillis+oneDayMs).toUpperCase());
				break;
			}
			case ALL:
			default: {
				break;
			}
		}
		
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("%-16s %-6s %s%s%s","Type","Event","When",newline,newline));
		for (final ScheduledEvent event : events) {
			if (searchBy != null && event.getDayOfWeek() != searchBy) {
				continue;
			}

			final Calendar c = event.getStartTime();
			c.setTimeZone(timeZone);
			final long diff = c.getTimeInMillis() - currentTimeMillis;
			if (scheduledEventFilter == ScheduledEventFilter.TODAY) {
				if (diff > oneDayMs) {
					continue;
				}
			}
			stringBuilder.append(String.format("%-16s %-6s %s%s", event.toString().trim(), "#"+event.getEventNumber(), formatTimeDifference((int) diff, true).trim(), newline));
		}
		return stringBuilder.toString();
	}
	
	public static final int oneHourMs = 60 * 60 * 1000;
	public static final int oneDayMs = 24 * oneHourMs;
	
	public static String formatTimeDifference(final long difference, final boolean includeDays) {
		final long days = difference / (1000 * 60 * 60 * 24) % 24;
		final long hours = difference / (1000 * 60 * 60) % 24;
		final long minutes = (difference) / (1000 * 60) % 60;
		final long seconds = (difference / 1000) % 60;
		if (includeDays) { 
			return String.format("%dd %dh %dm %ds",days,hours,minutes,seconds);
		} else {
			return String.format("%dh %dm %ds",hours,minutes,seconds);
		}
	}
}
