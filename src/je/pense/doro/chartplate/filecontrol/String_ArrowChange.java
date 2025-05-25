package je.pense.doro.chartplate.filecontrol;

public class String_ArrowChange {

	public static String compareOriginAndLrange(String origin, double Lrange) {
	    double Corigin = Double.parseDouble(origin.trim());
	    if (Lrange < Corigin) {
	        return origin + " \u2191 ";
	    } else {
	        return origin;
	    }
	}
	
	public static String compareOriginAndLrange(String origin, double Lrange, double Hrange) {
	    double Corigin = Double.parseDouble(origin.trim());
	    if (Lrange > Corigin) {
	        return origin + "  \u2193 ";
	    } else if (Hrange < Corigin) {
	        return origin + " \u2191 ";
	    } else {
	        return origin;
	    }
	}
	public static String compareOriginAndLrangeH(String origin, double Hrange) {
	    double Corigin = Double.parseDouble(origin.trim());
	    if (Hrange > Corigin) {
	        return origin + " \u2193 ";
	    } else {
	        return origin;
	    }
	}
}