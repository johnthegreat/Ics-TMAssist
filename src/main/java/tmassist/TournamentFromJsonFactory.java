package tmassist;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TournamentFromJsonFactory {
	private static final ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}
	
	public static Tournament createTournament(final String json) throws Exception {
		return mapper.readValue(json, Tournament.class);
	}
}