package cs.tippzettel;

import java.util.HashMap;
import java.util.Map;

public class TeamsIcons {

	static boolean initialized = false;

	private static Map<String, Integer> iconMap = new HashMap<String, Integer>();

	public static int getIcon(String kurzBeschreibung) {
		if (!initialized) {
			iconMap.put("bvb", R.drawable.bvb);
			iconMap.put("bre", R.drawable.bremen);
			iconMap.put("fra", R.drawable.frankfurt);
			iconMap.put("bmg", R.drawable.gladbach);
			iconMap.put("hsv", R.drawable.hamburg);
			iconMap.put("h96", R.drawable.hannover);
			iconMap.put("lev", R.drawable.leverkusen);
			iconMap.put("mai", R.drawable.mainz);
			iconMap.put("fcb", R.drawable.muenchen);
			iconMap.put("nue", R.drawable.nuernberg);
			iconMap.put("s04", R.drawable.schalke);
			iconMap.put("vfb", R.drawable.stuttgart);
			iconMap.put("wob", R.drawable.wolfsburg);
			iconMap.put("due", R.drawable.duesseldorf);
			iconMap.put("fre", R.drawable.freiburg);
			iconMap.put("hof", R.drawable.hoffenheim);
			iconMap.put("aug", R.drawable.augsburg);
			iconMap.put("fur", R.drawable.fuerth);

			initialized = true;
		}
		if (iconMap.containsKey(kurzBeschreibung)) {
			return iconMap.get(kurzBeschreibung);
		} else {
			return R.drawable.wappen_dummy;
		}
	}
}
