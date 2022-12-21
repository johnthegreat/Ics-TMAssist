package tmassist;

import java.util.Arrays;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tmassist.enums.ScheduledEventFilter;

public class ServerInputHandler {
	private final String personalTellPatternStr = "^(\\w{3,17}(?:\\(.*\\))?) tells you: (.*)$";
	private final Pattern personalTellPattern = Pattern.compile(personalTellPatternStr);
	private final Pattern eventsPattern = Pattern.compile("^events(?: (today|tomorrow|all))?$");
	
	private final TMAssist tmAssistBot;
	
	public ServerInputHandler(TMAssist tmAssistBot) {
		this.tmAssistBot = tmAssistBot;
	}
	
	public ParsedCommand parseAndHandleInput(final String input) {
		System.out.println("IN: " + input);
		
		ParsedCommand parsedCommand = ParsedCommand.COMMAND_UNKNOWN;
		boolean matched;
		
		matched = this.tryParsePersonalTell(input);
		if (matched) {
			parsedCommand = ParsedCommand.COMMAND_TELL;
			return parsedCommand;
		}
		
		this.tryParseQtell(input);

		return parsedCommand;
	}
	
	private boolean tryParseQtell(String input) {
		if (!input.startsWith(":")) {
			return false;
		}
		
		Pattern p = Pattern.compile(":Tourney #(\\d+) created with your default settings.");
		Matcher m = p.matcher(input);
		if (m.matches()) {
			int tournamentId = Integer.parseInt(m.group(1));
			this.tmAssistBot.getServerConnection().write(String.format("xtell 48 Tournament #%s has been created. \"xtell mamer managetourney %s\" to takeover.",tournamentId,tournamentId));
			return true;
		}
		
		return false;
	}
	
	private boolean tryParsePersonalTell(String input) {
		Matcher m = personalTellPattern.matcher(input);
		if (!m.matches()) {
			return false;
		}

		boolean isTm = m.group(1).contains("(TM)");

		final String username = ServerConnection.stripUserTags(m.group(1));
		final String message = m.group(2).trim();

		// parse message

		if (this.tmAssistBot.isAdmin(username)) {
			if (message.equals("stats")) {
				this.tmAssistBot.getServerConnection().printNetworkStats();
				return true;
			} else if (message.startsWith("exec ")) {
				this.tmAssistBot.getServerConnection().write(message.substring(5).trim());
				return true;
			}
		}

		if (message.equals("help") && (this.tmAssistBot.isAdmin(username) || isTm)) {
			final String url = this.tmAssistBot.getProperties().getProperty("webInterfaceUrl");
			if (url != null && !url.isEmpty()) {
				this.tmAssistBot.getServerConnection().write(String.format("tell %s Click here to create a tournament: %s - Once submitted, go to 'mam lt -n' and take over the tourney.", username, url));
			}
			return true;
		}

		final Matcher matcher = eventsPattern.matcher(message);
		if (matcher.matches()) {
			ScheduledEventFilter scheduledEventFilter = ScheduledEventFilter.TODAY;
			if (matcher.groupCount() >= 1) {
				String args = matcher.group(1);
				if (args != null) {
					args = args.trim();

					if (args.equals("all")) {
						scheduledEventFilter = ScheduledEventFilter.ALL;
					} else if (args.equals("tomorrow")) {
						scheduledEventFilter = ScheduledEventFilter.TOMORROW;
					}
				}
			}

			final ScheduledEvent[] scheduledEvents = Arrays.copyOf(this.tmAssistBot.getScheduledEvents(), this.tmAssistBot.getScheduledEvents().length);
			Arrays.sort(scheduledEvents);
			final String eventsTable = ScheduledEventUtils.toTable(scheduledEvents, TimeZone.getDefault(), scheduledEventFilter, "\n").trim();
			final String[] eventsTableLines = Utils.splitToLengthIfPossible(eventsTable,350,"\\n");
			for(final String eventTableLine : eventsTableLines) {
				int lastNewlineIndex = eventTableLine.lastIndexOf("\\n");
				final String commandToWrite = String.format("qtell %s %s", username, (lastNewlineIndex > 0 ? eventTableLine.substring(0, lastNewlineIndex) : eventTableLine).trim());
				this.tmAssistBot.getServerConnection().write(commandToWrite);
			}
			return true;
		}

		if (message.equals("version")) {
			this.tmAssistBot.getServerConnection().write(String.format("qtell %s TMAssist version: %s", username, TMAssist.compileDate));
			return true;
		}

		return true;
	}
}
