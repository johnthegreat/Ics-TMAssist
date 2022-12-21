package tmassist;

import tmassist.plugin.Plugin;
import tmassist.plugin.impl.KeepAliveServicePlugin;
import tmassist.plugin.impl.ScheduledEventReminderService;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		System.out.println("Welcome to TMAssist!");
		System.out.printf("Compiled On: %s%n", TMAssist.compileDate);
		System.out.println();
		
		final TMAssist tmAssist = new TMAssist();
		
		//
		// Load configuration
		//
		final String propertiesFilePath = args.length >= 1 ? args[0] : null;
		if (propertiesFilePath == null) {
			// Show usage
			System.out.println("Usage: TMAssist <path to config file>");
			System.out.println();
			System.exit(1);
		}

		FileReader fileReader;
		try {
			final File file = new File(propertiesFilePath);
			if (!file.exists()) {
				System.err.println("Configuration file does not exist.");
				System.err.println();
				System.exit(1);
			}

			fileReader = new FileReader(file);

			final Properties properties = new Properties();
			properties.load(fileReader);
			tmAssist.setProperties(properties);

			fileReader.close();
		} catch (IOException e) {
			// Unable to load configuration file.
			e.printStackTrace(System.err);
			System.err.println("Unable to load configuration file.");
			System.err.println();
			System.exit(1);
		}

		// Get connection information configuration
		final String host = tmAssist.getProperties().getProperty("host");
		final String port = tmAssist.getProperties().getProperty("port");
		final String username = tmAssist.getProperties().getProperty("username");
		final String password = tmAssist.getProperties().getProperty("password");

		// Use server time zone if found in config, otherwise use America/New_York
		final String serverTimeZoneString =
				tmAssist.getProperties().containsKey("serverTimeZone")
						? tmAssist.getProperties().getProperty("serverTimeZone")
						: "America/New_York";
		final String webInterfaceUrl = tmAssist.getProperties().getProperty("webInterfaceUrl");
		
		int portNum;
		try {
			portNum = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			portNum = 5000;
		}
		
		// Add Admins
		final String adminListString = tmAssist.getProperties().getProperty("adminList");
		if (adminListString != null) {
			final String[] admins = adminListString.split(",");
			for(final String admin : admins) {
				tmAssist.addAdmin(admin.trim());
			}
		}

		// Setup server connection
		tmAssist.setServerConnection(new ServerConnection("\n\rfics%"));
		tmAssist.getServerConnection().setOutPrintStream(System.out);
		tmAssist.getServerConnection().setErrPrintStream(System.err);

		// Unable to connect to the server.
		try {
			if (!tmAssist.getServerConnection().connect(host, portNum, username, password)) {
				System.exit(1);
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		} catch (ServerConnectionClosedException e) {
			e.printStackTrace(System.err);
			tmAssist.shutdown();
			System.exit(1);
		}

		// Register the KeepAliveServicePlugin.
		final KeepAliveServicePlugin keepAliveServicePlugin = new KeepAliveServicePlugin(tmAssist.getTimer(), tmAssist.getServerConnection());
		tmAssist.registerPlugin(keepAliveServicePlugin);

		// Load scheduled events from file
		final File eventsFile = new File(tmAssist.getProperties().getProperty("eventsFilePath"));
		if (eventsFile.exists()) {
			final ScheduledEventParser scheduledEventParser = new ScheduledEventParser();
			final TimeZone serverTimeZone = TimeZone.getTimeZone(serverTimeZoneString);

			ScheduledEvent[] scheduledEvents;
			try {
				scheduledEvents = scheduledEventParser.parseFromFile(eventsFile, serverTimeZone);
			} catch (IOException e) {
				e.printStackTrace(System.err);
				scheduledEvents = new ScheduledEvent[0];
			}
			tmAssist.setScheduledEvents(scheduledEvents);

			// Register ScheduledEventReminderService plugin if enabled in the configuration.
			final boolean enableScheduledEventReminderService = "true".equals(tmAssist.getProperties().getProperty("enableScheduledEventReminderService"));
			if (enableScheduledEventReminderService) {
				final ScheduledEventReminderService scheduledEventReminderService = new ScheduledEventReminderService(scheduledEvents, tmAssist.getServerConnection());
				tmAssist.registerPlugin(scheduledEventReminderService);
			} else {
				System.err.println("Not registering Scheduled Event Reminder Service.");
			}
		} else {
			System.err.println("Not registering Scheduled Event Reminder Service. Events file path does not exist.");
		}

		// Start the registered plugins.
		for (final Plugin plugin : tmAssist.getRegisteredPlugins()) {
			plugin.configure(tmAssist.getProperties());
			plugin.start();
		}

		// Listen for data from the server and parse it accordingly
		final Thread serverReaderThread = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						final String fullyRead = tmAssist.getServerConnection().readFully().trim();
						//System.out.println(fullyRead);
						final String[] lines = fullyRead.split(tmAssist.getServerConnection().getDefaultPrompt());
						for(final String line : lines) {
							tmAssist.parseLine(line.trim());
						}
					} catch (IOException | ServerConnectionClosedException e) {
						e.printStackTrace(System.err);
						tmAssist.shutdown();
						System.exit(1);
					}
				}
			}
		});
		serverReaderThread.start();

		// Read from System.in and write to the ServerConnection
		final Thread systemInReader = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						final byte[] inBytes = new byte[1024];
						int bytesRead = System.in.read(inBytes);
						if (bytesRead > -1) {
							final byte[] relevantBytes = Arrays.copyOfRange(inBytes, 0, bytesRead);
							if (!tmAssist.getServerConnection().socket.isClosed()) {
								tmAssist.getServerConnection().write(new String(relevantBytes).trim());
							}
						}
					} catch (IOException e) {
						e.printStackTrace(System.err);
					}
				}
			}
		});
		// This thread should not keep the program from exiting
		systemInReader.setDaemon(true);
		systemInReader.start();

		// Start communicating with the web interface to pull tournaments from the web interface.
		if (webInterfaceUrl != null && !webInterfaceUrl.isEmpty()) {
			final TournamentReceivedRunnable tournamentJsonReceivedRunnable = new TournamentReceivedRunnable() {
				@Override
				public void run() {
					try {
						final Tournament tournament = this.getTournament();
						if (tournament != null) {
							System.out.printf("Created Tournament: %s%n", tournament.getName());

							final String[] commands = FicsTournamentCreator.getCommands(tournament);
							tmAssist.getServerConnection().writeBatch(commands);
						}
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			};

			final TournamentRequestSocketCommunicator tournamentRequestSocketCommunicator = new TournamentRequestSocketCommunicator(webInterfaceUrl, tournamentJsonReceivedRunnable);
			try {
				tournamentRequestSocketCommunicator.connect();
			} catch (URISyntaxException e) {
				e.printStackTrace(System.err);
				System.err.println("Unable to start TournamentRequestSocketCommunicator");
			}
		}
	}
}
