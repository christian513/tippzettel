package cs.tippzettel.model;

import java.util.List;

public class Forum {

	private List<String> eintraege;

	public List<String> getEintraege() {
		return this.eintraege;
	}

	public void addEintrag(String s) {
		this.eintraege.add(s);
	}

}
