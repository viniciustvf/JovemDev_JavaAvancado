package br.com.trier.springvespertino.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static DateTimeFormatter dtfBR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	public static ZonedDateTime strToZonedDateTime(String dateStr) {
		return LocalDate.parse(dateStr, dtfBR).atStartOfDay(ZoneId.systemDefault());
	}
	
	public static String zonedDateTimeToStr(ZonedDateTime date) {
		return dtfBR.format(date);
	}
	
}