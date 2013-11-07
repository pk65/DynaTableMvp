package com.google.gwt.sample.dynatablemvp.shared;

import java.io.Serializable;

public class WeekDayStorage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7499545334299390591L;

	public static byte ALL_DAYS=127;
	
	private byte weekDayBits;

	public WeekDayStorage() {
		this.weekDayBits = 0;
	}

	public WeekDayStorage(byte weekDayBits) {
		this.weekDayBits = weekDayBits;
	}
	
	public byte getWeekDayBits() {
		return weekDayBits;
	}

	public void setWeekDayBits(byte weekDayBits) {
		this.weekDayBits = weekDayBits;
	}

	public void setWeekDayValue(int day, boolean value) {
		byte mask = 1;
		if (day > 0)
			mask <<= day;
		if (value)
			weekDayBits |= mask;
		else {
			mask ^= ALL_DAYS;
			weekDayBits &= mask;
		}
	}
	
	public boolean isWeekDayChecked(int day) {
		byte mask = 1;
		byte result = weekDayBits;
		if (day > 0)
			mask <<= day;
		result &= mask;
		return (result > 0);
	}
	
	public void setAllDaysChecked(){
		this.weekDayBits = ALL_DAYS;
	}
}
