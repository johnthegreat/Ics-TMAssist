package tmassist.plugin;

import java.util.Properties;

public interface Plugin {
	void start();
	void stop();
	PluginStatus getStatus();
	String getName();
	long getStartTime();
	boolean isStopped();
	void configure(Properties properties);
}
