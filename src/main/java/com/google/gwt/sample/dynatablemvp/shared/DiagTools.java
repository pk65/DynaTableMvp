package com.google.gwt.sample.dynatablemvp.shared;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class DiagTools {
	private static final transient String[] DAYS = new String[] { "Sun", "Mon",
			"Tues", "Wed", "Thurs", "Fri", "Sat" };

	public static String getDescription(int zeroBasedDayOfWeek,
			int startMinutes, int endMinutes) {
		return DAYS[zeroBasedDayOfWeek] + " " + getHrsMins(startMinutes) + "-"
				+ getHrsMins(endMinutes);
	}

	private static String getHrsMins(int mins) {
		int hrs = mins / 60;
		if (hrs > 12) {
			hrs -= 12;
		}

		int remainder = mins % 60;

		return hrs
				+ ":"
				+ (remainder < 10 ? "0" + remainder : String.valueOf(remainder));
	}

	public static String getViolationText(String message,
			Set<ConstraintViolation<?>> errors) {
		StringBuilder buffer = new StringBuilder(message);
		Iterator<ConstraintViolation<?>> iter = errors.iterator();
		while (iter.hasNext()) {
			buffer.append("\r\n");
			buffer.append(iter.next().getMessage());
		}
		return buffer.toString();
	}

}
