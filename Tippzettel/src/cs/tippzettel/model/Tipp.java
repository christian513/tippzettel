package cs.tippzettel.model;

public class Tipp {

	private Tipper tipper;

	private Ergebnis ergebnis;

	private final Spiel spiel;

	public Tipp(Spiel spiel) {
		this.spiel = spiel;
	}

	public Tipper getTipper() {
		return this.tipper;
	}

	public void setTipper(Tipper tipper) {
		this.tipper = tipper;
	}

	public Ergebnis getErgebnis() {
		return this.ergebnis;
	}

	public void setErgebnis(Ergebnis ergebnis) {
		this.ergebnis = ergebnis;
	}

	public Spiel getSpiel() {
		return this.spiel;
	}

}
