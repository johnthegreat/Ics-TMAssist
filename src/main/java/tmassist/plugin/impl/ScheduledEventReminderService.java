package tmassist.plugin.impl;

import tmassist.ScheduledEvent;
import tmassist.ScheduledEventUtils;
import tmassist.ServerConnection;
import tmassist.plugin.AbstractPlugin;

import java.util.*;

public class ScheduledEventReminderService extends AbstractPlugin {
	protected final static long INTERVAL_ONE_WEEK_MS = 1000 * 60 * 60 * 24 * 7;

	protected final ServerConnection serverConnection;
	protected final Timer timer;

	public ScheduledEventReminderService(final ScheduledEvent[] scheduledEvents, final ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
		this.timer = new Timer();

		for (final ScheduledEvent scheduledEvent : scheduledEvents) {
			scheduleEvent(scheduledEvent);
		}

		System.out.println("Initialized " + getName());
	}

	@Override
	public void configure(Properties properties) { }

	public String getName() {
		return "ScheduledEventReminderService";
	}

	private void scheduleEvent(final ScheduledEvent scheduledEvent) {
		final long diff = scheduledEvent.getStartTime().getTimeInMillis() - System.currentTimeMillis();
		System.out.println("An event is scheduled to open in " + ScheduledEventUtils.formatTimeDifference(diff,true).trim());
		
		final Calendar openTimeCalendar = scheduledEvent.getOpenTime(-20);
		openTimeCalendar.set(Calendar.SECOND, 0);
		
		final Date openTime = openTimeCalendar.getTime();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (isStopped()) {
					return;
				}
				
				final long diff = scheduledEvent.getStartTime().getTimeInMillis() - System.currentTimeMillis();
				final int minutes = (int)(diff / (60*1000));
				
				final String eventInfo = scheduledEvent.getEventInfo().replaceAll(" {2,}"," ").trim();
				final String message = String.format("tell 48 Reminder: Event #%s, a %s tourney, is set to start in %s minute%s. To create it, use \"td cr -e %s\"",
						scheduledEvent.getEventNumber(), eventInfo, minutes, minutes>1?"s":"", scheduledEvent.getEventNumber());
				serverConnection.write(message);
			}
		}, openTime, INTERVAL_ONE_WEEK_MS);
	}
}
