package gx.etc;

import static java.lang.String.format;

public class Stats {

	private double min = Double.MAX_VALUE;
	private double max = Double.MIN_VALUE;
	private double sum ;
	private long count;
	private int dp = 3;

	public Stats dp(int dp) {
		this.dp = dp;
		return this;
	}

	public void tally(double n) {
		if (n < min) min = n ;
		if (n > max) max = n ;
		sum += n;
		count ++;
	}

	public String toString() {
		if (count == 0) {
			return "<no data>";
		}
		String fmt = "%,." + dp + "f" ;
		return format("n:%,d", count)
				 + format(" min:" + fmt, min)
		  	 + format(" max:" + fmt, max)
				 + format(" avg:" + fmt, sum / count);
	}

}
