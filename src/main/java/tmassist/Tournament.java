package tmassist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tmassist.enums.RatedMode;
import tmassist.enums.Variant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tournament {
	//
	// CONSTRUCTORS
	//
	
	public Tournament() { }
	
	//
	// VARIABLES
	//
	
	protected String name;
	protected Integer event;
	protected int time;
	protected int inc;
	protected RatedMode mode;
	protected Variant variant;
	protected int wild;
	protected String board;
	protected String style;
	protected boolean tiebreaker;
	protected boolean noEscape;
	protected int numRounds;
	protected Integer playerLimit;
	protected Integer maxRating;
	protected Integer minRating;
	protected String allowedList;
	protected boolean compsAllowed;
	protected Integer autoAnnounce;
	protected boolean keep;
	protected int lateJoins;
	protected int halfJoins;
	protected int byes;
	protected int maxByes;
	protected String entryMessage;
	protected String extraInfo;
	protected String comment = null;
	
	//
	// GETTERS / SETTERS
	//
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getEvent() {
		return event;
	}
	public void setEvent(Integer event) {
		this.event = event;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getInc() {
		return inc;
	}
	public void setInc(int inc) {
		this.inc = inc;
	}
	public RatedMode getMode() {
		return mode;
	}
	public void setMode(RatedMode mode) {
		this.mode = mode;
	}
	public Variant getVariant() {
		return variant;
	}
	public void setVariant(Variant variant) {
		this.variant = variant;
	}
	public int getWild() {
		return wild;
	}
	public void setWild(int wild) {
		this.wild = wild;
	}
	public String getBoard() {
		return board;
	}
	public void setBoard(String board) {
		this.board = board;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public boolean isTiebreaker() {
		return tiebreaker;
	}
	public void setTiebreaker(boolean tiebreaker) {
		this.tiebreaker = tiebreaker;
	}
	public boolean isNoEscape() {
		return noEscape;
	}
	public void setNoEscape(boolean noEscape) {
		this.noEscape = noEscape;
	}
	public int getNumRounds() {
		return numRounds;
	}
	public void setNumRounds(int numRounds) {
		this.numRounds = numRounds;
	}
	public Integer getPlayerLimit() {
		return playerLimit;
	}
	public void setPlayerLimit(Integer playerLimit) {
		this.playerLimit = playerLimit;
	}
	public Integer getMaxRating() {
		return maxRating;
	}
	public void setMaxRating(Integer maxRating) {
		this.maxRating = maxRating;
	}
	public Integer getMinRating() {
		return minRating;
	}
	public void setMinRating(Integer minRating) {
		this.minRating = minRating;
	}
	public String getAllowedList() {
		return allowedList;
	}
	public void setAllowedList(String allowedList) {
		this.allowedList = allowedList;
	}
	public void setCompsAllowed(boolean compsAllowed) {
		this.compsAllowed = compsAllowed;
	}
	public boolean isCompsAllowed() {
		return compsAllowed;
	}
	public void setAutoAnnounce(Integer autoAnnounce) {
		this.autoAnnounce = autoAnnounce;
	}
	public Integer getAutoAnnounce() {
		return autoAnnounce;
	}
	public boolean isKeep() {
		return keep;
	}
	public void setKeep(boolean keep) {
		this.keep = keep;
	}
	public int getLateJoins() {
		return lateJoins;
	}
	public void setLateJoins(int lateJoins) {
		this.lateJoins = lateJoins;
	}
	public int getHalfJoins() {
		return halfJoins;
	}
	public void setHalfJoins(int halfJoins) {
		this.halfJoins = halfJoins;
	}
	public int getByes() {
		return byes;
	}
	public void setByes(int byes) {
		this.byes = byes;
	}
	public int getMaxByes() {
		return maxByes;
	}
	public void setMaxByes(int maxByes) {
		this.maxByes = maxByes;
	}
	public String getEntryMessage() {
		return entryMessage;
	}
	public void setEntryMessage(String entryMessage) {
		this.entryMessage = entryMessage;
	}
	public String getExtraInfo() {
		return extraInfo;
	}
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
