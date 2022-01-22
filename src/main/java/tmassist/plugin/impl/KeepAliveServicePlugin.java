package tmassist.plugin.impl;

import tmassist.ServerConnection;
import tmassist.plugin.AbstractPlugin;
import tmassist.plugin.PluginStatus;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class KeepAliveServicePlugin extends AbstractPlugin {
    private final Timer timer;
    private final ServerConnection serverConnection;

    public class KeepAliveTimer extends TimerTask {
        public void run() {
            if (KeepAliveServicePlugin.this.status == PluginStatus.STOPPED) {
                return;
            }

            serverConnection.write("date");
        }
    }

    public KeepAliveServicePlugin(final Timer timer, final ServerConnection serverConnection) {
        this.timer = timer;
        this.serverConnection = serverConnection;
    }

    @Override
    public void start() {
        super.start();
        timer.scheduleAtFixedRate(new KeepAliveTimer(), 0, (long) 1000 * 60 * 50);
    }

    @Override
    public String getName() {
        return "KeepAliveService";
    }

    @Override
    public void configure(Properties properties) {

    }
}
