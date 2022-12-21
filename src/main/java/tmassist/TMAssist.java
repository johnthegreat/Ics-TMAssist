package tmassist;

import tmassist.plugin.Plugin;
import tmassist.plugin.PluginStatus;

import java.io.IOException;
import java.util.*;

public class TMAssist {
	/**
	 * This has to be manually updated
	 */
	public static final String compileDate = "12/21/2022";
	
	public static void handleError(Throwable t) {
		t.printStackTrace(System.err);
	}

	private Properties properties;
	private final Timer timer;
	private ServerConnection serverConnection;
	private ScheduledEvent[] scheduledEvents;
	private final Set<Plugin> registeredPlugins;
	private final ServerInputHandler inputHandler;
	protected final Set<String> adminUsernames;
	
	//
	// TMAssist Constructor.
	//
	public TMAssist() {
		timer = new Timer();
		adminUsernames = new TreeSet<>();
		inputHandler = new ServerInputHandler(this);
		registeredPlugins = new HashSet<>();
	}

	public void shutdown() {
		System.out.println("Beginning shutdown sequence");
		for (final Plugin plugin : registeredPlugins) {
			if (plugin.getStatus() == PluginStatus.STARTED) {
				plugin.stop();
			}
		}
		timer.cancel();
		try {
			serverConnection.socket.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	public void parseLine(String input) {
		this.inputHandler.parseAndHandleInput(input);
	}
	
	public ServerConnection getServerConnection() {
		return serverConnection;
	}
	
	public void setServerConnection(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

	public ScheduledEvent[] getScheduledEvents() {
		return scheduledEvents;
	}

	public void setScheduledEvents(ScheduledEvent[] scheduledEvents) {
		this.scheduledEvents = scheduledEvents;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public Set<String> getAdmins() {
		return adminUsernames;
	}
	
	public void addAdmin(String username) {
		adminUsernames.add(username);
	}
	
	public boolean isAdmin(String username) {
		return adminUsernames.contains(username);
	}

	public void registerPlugin(final Plugin plugin) {
		registeredPlugins.add(plugin);
	}

	public void deregisterPlugin(final Plugin plugin) {
		registeredPlugins.remove(plugin);
	}

	public Set<Plugin> getRegisteredPlugins() {
		return Collections.unmodifiableSet(registeredPlugins);
	}

	public Timer getTimer() {
		return timer;
	}
}
