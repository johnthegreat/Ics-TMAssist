package tmassist;

import java.util.ArrayList;
import java.util.List;

import tmassist.enums.Variant;

public class FicsTournamentCreator {
	private static final String BotCommand = "mam";
	
	public static String[] getCommands(final Tournament tournament) {
		final List<String> commands = new ArrayList<>();

		commands.add(String.format("%s create", BotCommand));
		
		if (tournament.getName() != null && tournament.getName().length() > 0) {
			commands.add(String.format("%s stv -d name %s", BotCommand, tournament.getName()));
		}
		
		commands.add(String.format("%s stv -d time %d", BotCommand, tournament.getTime()));
		commands.add(String.format("%s stv -d inc %d", BotCommand, tournament.getInc()));
		commands.add(String.format("%s stv -d mode %s", BotCommand, tournament.getMode()));
		commands.add(String.format("%s stv -d variant %s", BotCommand, Variant.isChess(tournament.getVariant()) ? "chess" : tournament.getVariant()));
		
		if (tournament.getWild() != 0) {
			commands.add(String.format("%s stv -d wild %d", BotCommand, tournament.getWild()));
		}
		
		if (tournament.getBoard() != null && tournament.getBoard().length() > 0) {
			commands.add(String.format("%s stv -d board %s", BotCommand, tournament.getBoard()));
		}
		
		commands.add(String.format("%s stv -d style %s", BotCommand, tournament.getStyle()));
		
		if (tournament.isTiebreaker()) {
			commands.add(String.format("%s stv -d tiebreaker 1", BotCommand));
		}
		
		if (tournament.isNoEscape()) {
			commands.add(String.format("%s stv -d noescape 1", BotCommand));
		}
		
		if (tournament.getNumRounds() > 0) {
			commands.add(String.format("%s stv -d rounds %d", BotCommand, tournament.getNumRounds()));
		}
		
		if (tournament.getPlayerLimit() != null && tournament.getPlayerLimit() > 0) {
			commands.add(String.format("%s stv -d playerlimit %d", BotCommand, tournament.getPlayerLimit()));
		}
		
		if (tournament.getMaxRating() != null && tournament.getMaxRating() != 0) {
			commands.add(String.format("%s stv -d maxrating %d", BotCommand, tournament.getMaxRating()));
		}
		
		if (tournament.getMinRating() != null && tournament.getMinRating() != 0) {
			commands.add(String.format("%s stv -d minrating %d", BotCommand, tournament.getMinRating()));
		}
		
		if (tournament.getAllowedList() != null && tournament.getAllowedList().length() > 0) {
			commands.add(String.format("%s stv -d allowedlist %s", BotCommand, tournament.getAllowedList()));
		}
		
		if (tournament.isCompsAllowed()) {
			commands.add(String.format("%s stv -d computers 1", BotCommand));
		}
		
		if (tournament.getAutoAnnounce() != null && tournament.getAutoAnnounce() > 0) {
			commands.add(String.format("%s stv -d autoannounce %d", BotCommand, tournament.getAutoAnnounce()));
		}
		
		if (tournament.isKeep()) {
			commands.add(String.format("%s stv -d keep 1", BotCommand));
		}
		
		if (tournament.getLateJoins() > 0) {
			commands.add(String.format("%s stv -d latejoin %d", BotCommand, tournament.getLateJoins()));
		}
		
		if (tournament.getHalfJoins() > 0) {
			commands.add(String.format("%s stv -d halfjoin %d", BotCommand, tournament.getHalfJoins()));
		}
		
		if (tournament.getByes() > 0) {
			commands.add(String.format("%s stv -d byes %d", BotCommand, tournament.getByes()));
		}
		
		if (tournament.getMaxByes() > 0) {
			commands.add(String.format("%s stv -d maxbyes %d", BotCommand, tournament.getMaxByes()));
		}
		
		if (tournament.getEntryMessage() != null && tournament.getEntryMessage().length() > 0) {
			commands.add(String.format("%s stv -d entrymessage %s", BotCommand, tournament.getEntryMessage()));
		}
		
		if (tournament.getExtraInfo() != null && tournament.getExtraInfo().length() > 0) {
			commands.add(String.format("%s stv -d extrainfo %s", BotCommand, tournament.getExtraInfo()));
		}
		
		if (tournament.getComment() != null && tournament.getComment().length() > 0) {
			commands.add(String.format("%s stv -d comment %s", BotCommand, tournament.getComment()));
		}

		return commands.toArray(new String[0]);
	}
}
