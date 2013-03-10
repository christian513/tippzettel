package cs.tippzettel.model;

public class Tipper extends IdObjekt {

	private String passwort;

	public Tipper(String id, String name) {
		super(id);
		this.name = name;
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

}
