package cs.tippzettel.model;

public class FussballTipp extends Tipp {

	private boolean risiko;

	public FussballTipp(Spiel spiel) {
		super(spiel);
	}

	public boolean isRisiko() {
		return this.risiko;
	}

	public void setRisiko(boolean risiko) {
		this.risiko = risiko;
	}

}
