package tmassist.plugin;

public abstract class AbstractPlugin implements Plugin {
    protected PluginStatus status = PluginStatus.STOPPED;
    protected long startTime = 0L;

    @Override
    public void start() {
        if (getStatus() == PluginStatus.DISABLED) {
            System.out.printf("Unable to start plugin %s. Plugin is disabled.%n", getName());
            return;
        }

        status = PluginStatus.STARTED;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void stop() {
        status = PluginStatus.STOPPED;
        startTime = 0L;
    }

    @Override
    public PluginStatus getStatus() {
        return status;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public boolean isStopped() {
        return status == PluginStatus.STOPPED;
    }
}
