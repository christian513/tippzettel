package cs.tippzettel.model;

public class SpieltagPosition {

	int punkte;

	int position;

	String nummer;

	private int schnitt;

	private String sieger;

	private String max;

	public SpieltagPosition(String spieltag, int punkte, int position, int schnitt, String max, String sieger) {
		super();
		this.nummer = spieltag;
		this.punkte = punkte;
		this.position = position;
		this.schnitt = schnitt;
		this.max = max;
		this.sieger = sieger;
	}

	public String getMax() {
		return max;
	}

	public String getSieger() {
		return sieger;
	}

	public int getSchnitt() {
		return schnitt;
	}

	public String getNummer() {
		return nummer;
	}

	public Integer getPosition() {
		return position;
	}

	public Integer getPunkte() {
		return punkte;
	}
}
