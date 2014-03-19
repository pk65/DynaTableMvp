package com.google.gwt.sample.dynatablemvp.shared;

public class TimeTools {

	private static final transient String[] DAYS = new String[] { "Sun", "Mon",
			"Tues", "Wed", "Thurs", "Fri", "Sat" };

	private int endMinutes;

	private int startMinutes;

	public TimeTools(int zeroBasedDayOfWeek, int startMinutes,int endMinutes ) {
		super();
		this.endMinutes = endMinutes;
		this.startMinutes = startMinutes;
		this.zeroBasedDayOfWeek = zeroBasedDayOfWeek;
	}

	private int zeroBasedDayOfWeek;

	
	public String getDescription() {
		return DAYS[zeroBasedDayOfWeek] + " " + getHrsMins(startMinutes) + "-"
				+ getHrsMins(endMinutes);
	}
/*
	public void setDescription(String description) {
		Pattern pattern = Pattern.compile("(\\w+) (\\w+)-(\\w+)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(description);
		if (m.matches()) {
			for (int d = 0; d < DAYS.length; d++)
				if (DAYS[d].equalsIgnoreCase(m.group(1))) {
					zeroBasedDayOfWeek = d;
					break;
				}
			for (int m1 = 0; m1 < 60; m1++)
				if (getHrsMins(m1).equalsIgnoreCase(m.group(2))) {
					startMinutes = m1;
					break;
				}
			for (int e = 0; e < 24; e++)
				if (getHrsMins(e).equalsIgnoreCase(m.group(3))) {
					endMinutes = e;
					break;
				}
		}
	}
*/

	private String getHrsMins(int mins) {
		int hrs = mins / 60;
		if (hrs > 12) {
			hrs -= 12;
		}

		int remainder = mins % 60;

		return hrs
				+ ":"
				+ (remainder < 10 ? "0" + remainder : String.valueOf(remainder));
	}

	@Override
	public int hashCode() {
		return endMinutes + 7 * startMinutes + 31 * zeroBasedDayOfWeek;
	}

	public int getEndMinutes() {
		return endMinutes;
	}

	public void setEndMinutes(int endMinutes) {
		this.endMinutes = endMinutes;
	}

	public int getStartMinutes() {
		return startMinutes;
	}

	public void setStartMinutes(int startMinutes) {
		this.startMinutes = startMinutes;
	}

	public int getZeroBasedDayOfWeek() {
		return zeroBasedDayOfWeek;
	}

	public void setZeroBasedDayOfWeek(int zeroBasedDayOfWeek) {
		this.zeroBasedDayOfWeek = zeroBasedDayOfWeek;
	}

}
