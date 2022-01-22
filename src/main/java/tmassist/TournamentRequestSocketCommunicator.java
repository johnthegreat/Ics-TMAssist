package tmassist;

import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.Arrays;

public class TournamentRequestSocketCommunicator {
	private final String host;
	private final TournamentReceivedRunnable runnable;
	private Socket socket;

	public TournamentRequestSocketCommunicator(final String host, final TournamentReceivedRunnable runnable) {
		this.host = host;
		this.runnable = runnable;
	}
	
	public void connect() throws URISyntaxException {
		socket = IO.socket(this.host);
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("Socket connected");

				socket.emit("botConnect", new Ack() {
					@Override
					public void call(Object... arg0) {
						System.out.println("Server has acknowledged connection");
					}
				});
			}
		});

		socket.on("botCreateTournament", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println(Arrays.toString(args));

				if (args[0] instanceof JSONObject) {
					final JSONObject jsonObject = (JSONObject) args[0];
					final String jsonString = jsonObject.toString();
					try {
						final Tournament tournament = TournamentFromJsonFactory.createTournament(jsonString);
						runnable.setTournament(tournament);
						runnable.run();
					} catch (Exception e) {
						System.err.printf("Unable to create Tournament from JSON: %s%n", jsonString);
						e.printStackTrace(System.err);
					}
				}

				if (args[args.length -1] instanceof Ack) {
					final Ack ack = (Ack) args[args.length-1];
					ack.call();
				}
			}
		});

		socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("Socket disconnected");
			}
		});

		socket.connect();
	}
}
