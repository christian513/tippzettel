package cs.tippzettel.model;

public class Team extends IdObjekt {

	private final String beschreibung;
	private String kurzBeschreibung;

	public Team(String id, String kurzBeschreibung, String beschreibung) {
		super(id);
		this.kurzBeschreibung = kurzBeschreibung;
		this.beschreibung = beschreibung;
	}

	public String getKurzBeschreibung() {
		return kurzBeschreibung;
	}

	public String getBeschreibung() {
		return this.beschreibung;
	}

}
