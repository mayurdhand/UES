package com.db.riskit.enums;

public enum ReviewMailType {
	 APPROVED(1),REJECTED(0),UPDATE_REQUEST(2),UPDATE_BOOKING(3);
	private int value;

	ReviewMailType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public static ReviewMailType valueOfMethod(int value) {
		ReviewMailType type = null;
		switch (value) {		
		case 1:
			type = APPROVED;
			break;
		case 0:
			type = REJECTED;
			break;
		case 2:
			type = UPDATE_REQUEST;
			break;
		case 3:
			type = UPDATE_BOOKING;
			break;
		}
		return type;
	}
}
