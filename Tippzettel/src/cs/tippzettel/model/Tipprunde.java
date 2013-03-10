package cs.tippzettel.model;

public class Tipprunde extends IdObjekt {

	public static Tipprunde instance;

	private String beschreibung;

	private final NamedList<Tipper> tippers = new NamedList<Tipper>();

	private final NamedList<Spieltag> spieltage = new NamedList<Spieltag>();

	private final Forum forum;

	private Tipper angemeldeterTipper;

	public Tipprunde(String id, String beschreibung) {
		super(id);
		this.beschreibung = beschreibung;
		this.forum = new Forum();
	}

	public void addTipper(Tipper tipper) {
		this.tippers.add(tipper);
	}

	public void addSpieltag(Spieltag tag) {
		this.spieltage.add(tag);
	}

	public Spieltag getSpieltag(String id) {
		return this.spieltage.get(id);
	}

	public NamedList<Spieltag> getSpieltage() {
		return this.spieltage;
	}

	public NamedList<Tipper> getTippers() {
		return this.tippers;
	}

	public Tipper getTipper(String id) {
		return this.tippers.get(id);
	}

	public int getTipperCount() {
		return this.tippers.size();
	}

	public String getBeschreibung() {
		return this.beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public Forum getForum() {
		return this.forum;
	}

	public void setAngemeldeterTipper(String angemeldeterTipper, String password) {
		this.angemeldeterTipper = getTipper(angemeldeterTipper);
		this.angemeldeterTipper.setPasswort(password);
	}

	public Tipper getAngemeldeterTipper() {
		return angemeldeterTipper;
	}

}
