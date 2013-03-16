package cs.tippzettel.model;

public class Spiel extends IdObjekt {

	private Team heimTeam;

	private Team auswTeam;

	private Ergebnis ergebnis = new Ergebnis();

	public Spiel(String id) {
		super(id);
	}

	public boolean hatErgebnis() {
		return this.ergebnis.getTendenz() != Tendenz.UNDEFINIERT;
	}

	public Ergebnis getErgebnis() {
		return this.ergebnis;
	}

	public Tendenz getErgebnisTendenz() {
		return this.ergebnis.getTendenz();
	}

	public Team getHeimTeam() {
		return this.heimTeam;
	}

	public void setHeimTeam(Team heimTeam) {
		this.heimTeam = heimTeam;
	}

	public Team getAuswTeam() {
		return this.auswTeam;
	}

	public void setAuswTeam(Team auswTeam) {
		this.auswTeam = auswTeam;
	}

	public void setErgebnis(Ergebnis ergebnis) {
		this.ergebnis = ergebnis;
	}

}
