package tmassist;

import tmassist.enums.DayOfWeek;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ScheduledEvent implements Comparable<ScheduledEvent> {	
	//
	// PROPERTIES
	//
	
	private TimeZone timeZone;
	private DayOfWeek dayOfWeek;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	private int eventNumber;
	private String eventInfo;
	private String manager;
	
	//
	// CONSTRUCTORS
	//
	
	ScheduledEvent() {
		
	}
	
	/**
	 * Constructor.
	 * @param dayOfWeek Which day of the week that this scheduled event is going to be run.
	 * @param timeOfDay What time of day that this scheduled event is going to be run.
	 */
	public ScheduledEvent(final DayOfWeek dayOfWeek, final String timeOfDay) {
		setDayOfWeek(dayOfWeek);
		setTimeString(timeOfDay);
	}
	
	/**
	 * Constructor.
	 * @param dayOfWeek Which day of the week that this scheduled event is going to be run.
	 * @param hour What hour (in military time) that this scheduled event is going to be run.
	 * @param minute What minute that this scheduled event is going to be run.
	 */
	public ScheduledEvent(final DayOfWeek dayOfWeek, final int hour, final int minute) {
		this(dayOfWeek,hour,minute,0);
	}
	
	/**
	 * Constructor.
	 * @param dayOfWeek Which day of the week that this scheduled event is going to be run.
	 * @param hour What hour (in military time) that this scheduled event is going to be run.
	 * @param minute What minute that this scheduled event is going to be run.
	 * @param second What second that this scheduled event is going to be run.
	 */
	public ScheduledEvent(final DayOfWeek dayOfWeek, final int hour, final int minute, final int second) {
		setDayOfWeek(dayOfWeek);
		setHour(hour);
		setMinute(minute);
		setSecond(second);
	}
	
	//
	// METHODS
	//
	
	/**
	 * @return A string representing what hour,minute, and second this event is scheduled to start.
	 */
	public String getTimeString() {
		return String.format("%d:%d:%d",hour,minute,second);
	}
	
	/**
	 * @param str Input string to be divided and parsed to set the hour, minute, and second fields.
	 */
	public void setTimeString(String str) {
		final String[] elements = str.split(":");
		if (elements.length >= 1) {
			setHour(Integer.parseInt(elements[0]));
		}
		
		if (elements.length >= 2) {
			setMinute(Integer.parseInt(elements[1]));
		}
		
		if (elements.length == 3) {
			setSecond(Integer.parseInt(elements[2]));
		}
	}
	
	public Calendar getOpenTime(final int minutes) {
		final Calendar calendar = getStartTime();
		if (minutes > 0) {
			// open after start doesn't make sense.
			return calendar;
		}
		calendar.add(Calendar.MINUTE, minutes);
		return calendar;
	}
	
	/**
	 * Implementation Note: getOpenTime() calls this method(), so getStartTime() is in reality called twice whereas getOpenTime() is only called once.
	 */
	public final Calendar getStartTime() {
		final Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, getHour());
		calendar.set(Calendar.MINUTE, getMinute());
		calendar.set(Calendar.SECOND, getSecond());
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setTimeZone(getTimeZone());

		int calendarDayOfWeek = 0;
		switch(dayOfWeek) {
			case SUNDAY:
				calendarDayOfWeek = Calendar.SUNDAY;
				break;
			case MONDAY:
				calendarDayOfWeek = Calendar.MONDAY;
				break;
			case TUESDAY:
				calendarDayOfWeek = Calendar.TUESDAY;
				break;
			case WEDNESDAY:
				calendarDayOfWeek = Calendar.WEDNESDAY;
				break;
			case THURSDAY:
				calendarDayOfWeek = Calendar.THURSDAY;
				break;
			case FRIDAY:
				calendarDayOfWeek = Calendar.FRIDAY;
				break;
			case SATURDAY:
				calendarDayOfWeek = Calendar.SATURDAY;
				break;
		}
		if (calendarDayOfWeek > 0) {
			calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek);
		}

		if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
			calendar.add(Calendar.WEEK_OF_MONTH, 1);
		}
		
		return calendar;
	}
	
	/**
	 * @return The day of the week that this scheduled event is going to be run at.
	 */
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
	/**
	 * Sets the day of week that this scheduled event is going to be run at.
	 * @param dayOfWeek Day of Week of the ScheduledEvent.
	 */
	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public void setEventNumber(int eventNumber) {
		this.eventNumber = eventNumber;
	}

	public int getEventNumber() {
		return eventNumber;
	}

	@Override
	public int compareTo(ScheduledEvent o) {
		return getStartTime().compareTo(
			o.getStartTime()
		);
	}
	
	@Override
	public String toString() {
		return this.eventInfo;
	}
	
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public void setEventInfo(String eventInfo) {
		this.eventInfo = eventInfo;
	}

	public String getEventInfo() {
		return eventInfo;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManager() {
		return manager;
	}
}
