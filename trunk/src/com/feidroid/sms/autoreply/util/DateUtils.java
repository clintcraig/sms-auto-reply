package com.feidroid.sms.autoreply.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	public static final String SIMPLE_DATE_FORMAT_CN = "yyyy-MM-dd";
	public static final String LONG_DATE_FORMAT_CN = "yyyy-MM-dd HH:mm:ss";
	public static final String SHORT_DATE_FORMAT_CN = "yyyy-MM-dd HH:mm";
	public static final String MILLIS_DATE_FORMAT_EN = "yyyy-MM-dd kk:mm:ss.SSS";
	
	public static final String TRADITIONAL_DATE_FORMAT = "MM月dd日 ,E";//1月26号，周六
	
	public static String long2String(long longDate,String dateFormat) {
		SimpleDateFormat sdFormat = new SimpleDateFormat(dateFormat);
		Date date = new Date(longDate);
		return sdFormat.format(date);
		
	}

	// 获取某年某月的最大天數
	/**
	 * @param date
	 * @return int
	 */
	public static int getMaxDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	// 将某种格式的日期字符串转化为Date
	/**
	 * @param strDate
	 * @param strDateFormat
	 * @return Date
	 */
	public static Date convertStringToDate(String strDate, String strDateFormat) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	// 将Date转化为某种格式的日期字符串
	/**
	 * @param date
	 * @param strDateFormat
	 * @return String
	 */
	public static String convertDateToString(Date date, String strDateFormat) {

		return new SimpleDateFormat(strDateFormat).format(date);

	}

	// 获取某一天是这一年的第几周
	/**
	 * @param date
	 * @return
	 */
	public static int getWeekNum(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, date.getYear());
		cal.set(Calendar.MONTH, date.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, date.getDay());
		return cal.get(Calendar.WEEK_OF_YEAR);

	}

	// 获取两个日期的时间间隔天数
	/**
	 * @param startDay
	 * @param endDay
	 * @return int
	 */
	public static int getIntervalDays(Date startDay, Date endDay) {
		if (startDay.after(endDay)) {
			Date d = startDay;
			startDay = endDay;
			endDay = d;
		}
		long l1 = startDay.getTime();
		long l2 = endDay.getTime();
		long lc = l2 - l1;
		return (int) (lc / (1000 * 60 * 60 * 24));
	}

	// 获取一年前的日期
	/**
	 * @return
	 */
	public static Date getOneYearAgo() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	/**
	 * @param now
	 * @return
	 */
	public static Date getOneYearAgo(Date now) {
		return new Date(((now.getTime() / 1000) - 60 * 60 * 24 * 365) * 1000);
	}

	// 获取3周前的日期
	public static Date getThreeWeeksAgo() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		return cal.getTime();
	}

	// 获取某个日期的星期数
	public static String getWeek(Date d) {
		return new SimpleDateFormat("E").format(d);
	}

	/**
	 * 在开发web应用中针对不同的数据库日期类型我们需要在我们的程序中对日期类型做各种不同 的转换。
	 * 若对应数据库数据是oracle的Date类型即只需要年月日的可以选择使用java.sql.Date类型 若对应的是 MSsqlserver
	 * 数据库的DateTime类型即需要年月日时分秒的 选择java.sql.Timestamp类型
	 * 
	 * @throws ParseException
	 */
	/**
	 * @param strDate
	 * @return 数据库类型  java.sql.Timestamp(MS SQL SERVER)
	 * @throws ParseException
	 */
	public static java.sql.Timestamp string2Timestamp(String strDate)
			throws ParseException {
		// SimpleDateFormat sdf = new
		// SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",
		// Locale.ENGLISH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
				Locale.ENGLISH);
		sdf.setLenient(false);
		return new java.sql.Timestamp(sdf.parse(strDate).getTime());
	}

	/**
	 * @param strDate
	 * @return 数据库类型  java.sql.Date(Oracle)
	 * @throws ParseException
	 */
	public static java.sql.Date string2Date(String strDate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT_CN,
				Locale.ENGLISH);
		sdf.setLenient(false);
		return new java.sql.Date(sdf.parse(strDate).getTime());
	}

	// 输出某种货币格式的字符串
	/**
	 * @param defaultFormatPattern
	 * @param money
	 * @return String
	 */
	public static String getDefaultNumberFormat(String defaultFormatPattern,
			double money) {
		return (new DecimalFormat(defaultFormatPattern)).format(money);
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(getMaxDayOfMonth(new Date()));
		System.out.println(java.sql.Date.valueOf("2012-9-9"));
		System.out.println(convertStringToDate("2001-07-04 12:08",
				SHORT_DATE_FORMAT_CN));
		System.out
				.println(convertDateToString(new Date(), LONG_DATE_FORMAT_CN));
		System.out.println(convertDateToString(
				Calendar.getInstance().getTime(), LONG_DATE_FORMAT_CN));
		System.out.println(convertDateToString(
				new Date(System.currentTimeMillis()), LONG_DATE_FORMAT_CN));

		System.out.println("现在是北京时间第" + getWeekNum(new Date()) + "周");
		System.out.println("间隔："
				+ getIntervalDays(
						convertStringToDate("2002-07-04 12:08",
								SHORT_DATE_FORMAT_CN),
						convertStringToDate("2001-07-04 12:08",
								SHORT_DATE_FORMAT_CN)) + "天");
		// 获取一年前的日期
		System.out.println(getOneYearAgo());
		System.out.println(getOneYearAgo(new Date()));
		System.out.println(getThreeWeeksAgo());
		System.out.println(getWeek(new Date()));

		System.out
				.println("===============Oracle Date MS SQL SERVER Timestamp==================");
		System.out.println(string2Date("2012-1-1"));
		System.out.println(string2Timestamp("2012-1-1 12:12:12"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",
				Locale.ENGLISH);
		System.out.println(sdf.format(new Date()));
		System.out.println(new Timestamp(System.currentTimeMillis()));
		System.out.println(getDefaultNumberFormat("###,###.00",
				1231313.34534535));
		System.out.println(getDefaultNumberFormat("#0.00%", 0.0123));
	}

	public static long getTodayZeroLong() {
		// TODO Auto-generated method stub
		Date today = new Date();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	
		String longString = simpleDateFormat.format(today);
		
		return Long.valueOf(longString);
	}

}
