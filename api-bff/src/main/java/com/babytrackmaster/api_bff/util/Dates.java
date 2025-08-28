package com.babytrackmaster.api_bff.util;

import java.time.LocalDate;

public class Dates {
	  public static String mesActual() {
	    LocalDate now = LocalDate.now();
	    return now.getYear() + "-" + (now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue()));
	  }
	}