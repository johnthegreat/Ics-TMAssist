package tmassist;

import tmassist.enums.DayOfWeek;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduledEventParser {
	private final Pattern linePattern;
	
	public ScheduledEventParser() {
		this.linePattern = Pattern.compile("^:\\|\\s(\\w{3})\\s\\|\\s(\\d{4})\\s\\|\\s*(\\d{1,2})\\s\\|\\s*(.*)\\s*\\|\\s+(.*?)\\s*\\|$");
	}

	public ScheduledEvent[] parseFromFile(final File file, final TimeZone timeZone) throws IOException {
		final List<ScheduledEvent> scheduledEventList = new ArrayList<>();
		final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		while (bufferedReader.ready()) {
			final ScheduledEvent scheduledEvent = parseSingle(bufferedReader.readLine(), timeZone);
			if (scheduledEvent != null) {
				scheduledEventList.add(scheduledEvent);
			}
		}
		Collections.sort(scheduledEventList);

		return scheduledEventList.toArray(new ScheduledEvent[0]);
	}

	public ScheduledEvent parseSingle(final String source, final TimeZone timeZone) {
		final Matcher matcher = this.linePattern.matcher(source);
		if (!matcher.matches()) {
			return null;
		}

		final ScheduledEvent scheduledEvent = new ScheduledEvent();

		final String dOfW = matcher.group(1);
		switch (dOfW) {
			case "Mon":
				scheduledEvent.setDayOfWeek(DayOfWeek.MONDAY);
				break;
			case "Tue":
				scheduledEvent.setDayOfWeek(DayOfWeek.TUESDAY);
				break;
			case "Wed":
				scheduledEvent.setDayOfWeek(DayOfWeek.WEDNESDAY);
				break;
			case "Thu":
				scheduledEvent.setDayOfWeek(DayOfWeek.THURSDAY);
				break;
			case "Fri":
				scheduledEvent.setDayOfWeek(DayOfWeek.FRIDAY);
				break;
			case "Sat":
				scheduledEvent.setDayOfWeek(DayOfWeek.SATURDAY);
				break;
			case "Sun":
				scheduledEvent.setDayOfWeek(DayOfWeek.SUNDAY);
				break;
		}

		final String dateTime = matcher.group(2);
		final int hour = Integer.parseInt(dateTime.substring(0,2));
		final int minute = Integer.parseInt(dateTime.substring(2,4));
		scheduledEvent.setHour(hour);
		scheduledEvent.setMinute(minute);

		int eventNumber = Integer.parseInt(matcher.group(3));
		scheduledEvent.setEventNumber(eventNumber);

		scheduledEvent.setEventInfo(matcher.group(4));
		scheduledEvent.setManager(matcher.group(5));

		scheduledEvent.setTimeZone(timeZone);

		return scheduledEvent;
	}
}
