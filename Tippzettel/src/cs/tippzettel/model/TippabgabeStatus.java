package cs.tippzettel.model;

public class TippabgabeStatus {

	boolean getippt;

	String spieltag;

	String tage;

	String stunden;

	public TippabgabeStatus(boolean getippt, String spieltag, String tage, String stunden) {
		this.getippt = getippt;
		this.spieltag = spieltag;
		this.tage = tage;
		this.stunden = stunden;
	}

	public boolean isGetippt() {
		return getippt;
	}

	public String getSpieltag() {
		return spieltag;
	}

	public String getTage() {
		return tage;
	}

	public String getStunden() {
		return stunden;
	}

}
