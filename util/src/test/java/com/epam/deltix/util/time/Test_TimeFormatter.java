package com.epam.deltix.util.time;

import org.junit.Assert;
import org.junit.Test;

import java.util.StringTokenizer;

import static org.junit.Assert.assertEquals;

public class Test_TimeFormatter {

	private final static long MILLIS_PER_DAY = 24*60*60*1000L;
	
	@Test
	public void simpleTest() {
		assertFormat(0 * 1000, "00:00:00");
		assertFormat(1 * 1000, "00:00:01");
		assertFormat(10 * 1000, "00:00:10");
		assertFormat(60 * 1000, "00:01:00");
		assertFormat(3600 * 1000, "01:00:00");
		assertFormat(3601 * 1000, "01:00:01");
		assertFormat(3601 * 1000, "01:00:01");
		
        assertFormat(MILLIS_PER_DAY - 1, "23:59:59");
	}

	@Test
	public void specialFormat() {
		assertEquals(28800000000000L, TimeFormatter.parseTimeOfDay("08:00:00", 1_000_000_000));
	}

	@Test
	public void simpleMSTest() {
		assertFormatMS(0, "00:00:00.000");
		assertFormatMS(1, "00:00:00.001");
		assertFormatMS(123, "00:00:00.123");
		assertFormatMS(1234, "00:00:01.234");
		assertFormatMS(12345, "00:00:12.345");
		
		
	}
	
    @Test
	public void simpleOneDayTruncation() {
		assertFormat(-1L, "00:00:00");
		assertFormat(MILLIS_PER_DAY, "00:00:00");
		assertFormat(MILLIS_PER_DAY+1, "00:00:00");
		assertFormat(MILLIS_PER_DAY+1000, "00:00:01");
		assertFormat(5*MILLIS_PER_DAY+1000, "00:00:01");
	}
	

	private static void assertFormat(long time, String expected) {
		String actual = TimeFormatter.formatTimeOfDay(time);
		assertEquals(expected, actual);
	}

	private static void assertFormatMS(long time, String expected) {
		String actual = TimeFormatter.formatDurationWithMilliseconds(time);
		assertEquals(expected, actual);
	}	
	@Test
	public void testBadCasesTime() {
		assertBad(null);
		assertBad("");
		assertBad(":");
		assertBad(":10");
		assertBad(":10 am");
		assertBad("10 amABC");

		// extra : character
		assertBad("1:");
		assertBad("1: am");
		assertBad("11:");
		assertBad("11: am");
		assertBad("1:1:");
		assertBad("1:1:am");
		assertBad("11:11:");
		assertBad("11:11:am");
		assertBad("1:1:1:");
		assertBad("1:1:1: am");
		assertBad("11:11:11:");
		assertBad("11:11:11: am");

		// h/m/s exceed max allowed
		assertBad("24:00:00");
		assertBad("00:60:00");
		assertBad("00:00:60");

		// illegal am-pm hours
		assertBad("00:00 am");
		assertBad("00:00 pm");
		assertBad("13:00 pm");
		assertBad("13:00 am");

		// maximum two digits per group
		assertBad("001:01:01");
		assertBad("01:001:01");
		assertBad("01:01:001");

		// no traling/leading spaces and space separators
		assertBad(" 11:11:11");
		assertBad("11: 11:11");
		assertBad("11:11: 11");
		assertBad("11:11:11 ");

		// uneterminated string
		assertBad("1:");
		assertBad("1:1:");
		assertBad("1:1:1 ");
		assertBad("1:1:1 a");

	}

	@Test
	public void testOneDigitCasesTime() {
		assertTimeParsed("0", 0);
		assertTimeParsed("0:1", 1 * 60);
		assertTimeParsed("1:1", 1 * 60 * 60 + 1 * 60);
		assertTimeParsed("0:0:1", 1);
		assertTimeParsed("0:1:1", 1 * 60 + 1);
		assertTimeParsed("1:1:1", 1 * 60 * 60 + 1 * 60 + 1);
	}

	@Test
	public void testMilitaryFormatTime() {
		assertTimeParsed("0", 0);
		assertTimeParsed("1", 1 * 60 * 60);
		assertTimeParsed("12", 12 * 60 * 60);
		assertTimeParsed("12:3", 12 * 60 * 60 + 3 * 60);
		assertTimeParsed("12:34", 12 * 60 * 60 + 34 * 60);
		assertTimeParsed("12:34:5", 12 * 60 * 60 + 34 * 60 + 5);
		assertTimeParsed("12:34:56", 12 * 60 * 60 + 34 * 60 + 56);
	}

	@Test
	public void testAmPmFormatTime() {
		assertTimeParsed("1 am", 1 * 60 * 60);
		assertTimeParsed("11 AM", 11 * 60 * 60);
		assertTimeParsed("11:3 am", 11 * 60 * 60 + 3 * 60);
		assertTimeParsed("11:34 AM", 11 * 60 * 60 + 34 * 60);
		assertTimeParsed("11:34:5 am", 11 * 60 * 60 + 34 * 60 + 5);
		assertTimeParsed("11:34:56   \t  \t  \t     AM", 11 * 60 * 60 + 34 * 60
				+ 56);

		assertTimeParsed("1 pm", 13 * 60 * 60);
		assertTimeParsed("11 Pm", 23 * 60 * 60);
		assertTimeParsed("11:3 pM", 23 * 60 * 60 + 3 * 60);
		assertTimeParsed("11:34 PM", 23 * 60 * 60 + 34 * 60);
		assertTimeParsed("11:34:5 pm", 23 * 60 * 60 + 34 * 60 + 5);
		assertTimeParsed("11:34:56   \t  \t  \t     pm", 23 * 60 * 60 + 34 * 60
				+ 56);
	}

	@Test
	public void testHoursOnlyTime() {
		assertTimeParsed("0", 0);
		assertTimeParsed("1", 1 * 60 * 60);
		assertTimeParsed("23", 23 * 60 * 60);
		assertTimeParsed("1 am", 1 * 60 * 60);
		assertTimeParsed("1 pm", 13 * 60 * 60);
		assertTimeParsed("11 am", 11 * 60 * 60);
		assertTimeParsed("12 am", 0 * 60 * 60);
		assertTimeParsed("12 pm", 12 * 60 * 60);
		assertTimeParsed("11 pm", 23 * 60 * 60);
	}

	// MMM
	@Test
	public void testOneDigitGroupsDuration() {
		assertDurationParse("0", 0);
		assertDurationParse("1", 1 * 60);
		assertDurationParse("10", 10 * 60);
		assertDurationParse("100", 100 * 60); // can be more than 60
	}

	// MMM:SS
	@Test
	public void testTwoDigitGroupsDuration() {
		assertDurationParse("0:0", 0);
		assertDurationParse("1:1", 1 * 60 + 1);
		assertDurationParse("10:10", 10 * 60 + 10);
		assertDurationParse("100:10", 100 * 60 + 10);

		assertBadDuration("10:100"); // secounds > 59
		assertBadDuration("10:10:");
		assertBadDuration("10:");
	}

	// HHH:MM:SS
	@Test
	public void testThreeDigitGroupsDuration() {
		assertDurationParse("0:0:0", 0);
		assertDurationParse("1:1:1", 1 * 3600 + 1 * 60 + 1);
		assertDurationParse("10:10:10", 10 * 3600 + 10 * 60 + 10);
		assertDurationParse("100:10:10", 100 * 3600 + 10 * 60 + 10); // duration can exceed 24h

		assertBadDuration("10:10:100"); // secounds > 59
		assertBadDuration("10:100:10"); // minutes > 59
	}

	@Test
	public void testOneDigitCasesDuration() {
		assertDurationParse("0", 0);
		assertDurationParse("0:1", 1);
		assertDurationParse("1:1", 1 * 60 + 1);
		assertDurationParse("0:0:1", 1);
		assertDurationParse("0:1:1", 1 * 60 + 1);
		assertDurationParse("1:1:1", 1 * 60 * 60 + 1 * 60 + 1);
	}

	@Test
	public void test12345Duration() {
		assertDurationParse("0", 0);
		assertDurationParse("1", 1 * 60);
		assertDurationParse("12", 12 * 60);
		assertDurationParse("12:3", 12 * 60 + 3);
		assertDurationParse("12:34", 12 * 60 + 34);
		assertDurationParse("12:34:5", 12 * 60 * 60 + 34 * 60 + 5);
		assertDurationParse("12:34:56", 12 * 60 * 60 + 34 * 60 + 56);
	}

	@Test
	public void testBadCasesDuration() {
		assertBadDuration(null);
		assertBadDuration("");
		assertBadDuration(":");
		assertBadDuration(":10");
		assertBadDuration(":10 am");
		assertBadDuration("10 amABC");

		// extra : character
		assertBadDuration("1:");
		assertBadDuration("11:");
		assertBadDuration("1:1:");
		assertBadDuration("11:11:");
		assertBadDuration("1:1:1:");
		assertBadDuration("11:11:11:");

		// h/m/s exceed max allowed
		//assertBad ("24:00:00");
		assertBadDuration("00:60:00");
		assertBadDuration("00:00:60");

		// maximum two digits per group (except first group)
		assertBadDuration("01:001:01");
		assertBadDuration("01:01:001");
		assertBadDuration("01:001");

		// no traling/leading spaces and space separators
		assertBadDuration(" 11:11:11");
		assertBadDuration("11: 11:11");
		assertBadDuration("11:11: 11");
		assertBadDuration("11:11:11 ");

		// uneterminated string
		assertBadDuration("1:");
		assertBadDuration("1:1:");
		assertBadDuration("1:1:1 ");
		assertBadDuration("1:1:1 am");

	}

	private static void assertDurationParse(String input,
			int expectedNumberOfSeconds) {
		try {
			int actualNumberOfSeconds = TimeFormatter
					.parseDurationInSeconds(input);
			assertEquals("Number of seconds in '" + input + '\'',
					expectedNumberOfSeconds, actualNumberOfSeconds);
		} catch (NumberFormatException ex) {
			Assert.fail("Parsing of '" + input + "' failed with message ["
					+ ex.getMessage() + ']');
		}
	}

	private static void assertBadDuration(String time) {
		try {
			TimeFormatter.parseDurationInSeconds(time);
			Assert.fail("Expected to detect a problem in time string \"" + time
					+ '"');
		} catch (NumberFormatException expected) {
		}
	}

	private static int standardParse(String value) {

		StringTokenizer t = new StringTokenizer(value, ":", false);

		int numTokens = t.countTokens();
		int seconds = 0;

		if (numTokens > 2)
			seconds += Integer.parseInt(t.nextToken()) * 3600;

		if (numTokens > 1)
			seconds += Integer.parseInt(t.nextToken()) * 60
					+ Integer.parseInt(t.nextToken());
		else if (numTokens > 0)
			seconds += Integer.parseInt(t.nextToken()) * 60;

		return seconds;
	}


	//This is no longer automatic test since in context of CI build our code is instrumented by Cobertura and runs much slower than non-instrumented Java library code  
	public void testParsingPerformance() {
		validate (24);
		//warmup
		parse(15, true);
		parse(15, false);

		final long startTime1 = System.nanoTime();
		parse(24, true);
		final long startTime2 = System.nanoTime();
		parse(24, false);
		final long endTime = System.nanoTime();
		
		long customRunTime = startTime2 - startTime1;
		long standardRunTime = endTime - startTime2;
		
		

		Assert.assertTrue("Scanner parsing is expected to be at least 3 times faster. Actual: " + (double) standardRunTime/ customRunTime,
				customRunTime * 3 < standardRunTime);

	}

	private static void parse (int numberOfHours, boolean useTimeFormatter) {
		StringBuilder sb = new StringBuilder(64);
		for (int i = 0; i < 10; i++) { // extra cycle
			for (int hr = 0; hr < numberOfHours; hr++) {
				if (hr < 10)
					sb.append('0');
				sb.append(hr);
				
				for (int min = 0; min < 60; min++) {
					sb.setLength(2); //hh
					sb.append(':');
					if (min < 10)
						sb.append('0');
					sb.append(min);

					for (int sec = 0; sec < 60; sec++) {
						sb.setLength(5); //hh:mm
						sb.append(':');
						if (sec < 10)
							sb.append('0');
						sb.append(sec);
						
						int actual;
						if (useTimeFormatter)
							actual = TimeFormatter.parseDurationInSeconds(sb.toString());
						else
							actual = standardParse(sb.toString());

						Assert.assertTrue(actual >= 0); //just dummy check that uses 'text' to trick optimizer
					}
				}
			}
		}
	}
	
	private static void validate (int numberOfHours) {
		for (int hr = 0; hr < numberOfHours; hr++) {
			final String hours = (hr < 10) ? "0" + hr : Integer.toString(hr);
			final int hoursInSec = hr * 3600;
			for (int min = 0; min < 60; min++) {
				final String minutes = (min < 10) ? ":0" + min : ":" + min;
				final int minInSec = min * 60;

				for (int sec = 0; sec < 60; sec++) {
					String seconds = (sec < 10) ? ":0" + sec : ":" + sec;
					String text = hours + minutes + seconds;

					int actual = TimeFormatter.parseDurationInSeconds(text);
					int expected = hoursInSec + minInSec + sec;
					assertEquals(actual, expected);
				}
			}
		}
	}
	
	//This is no longer automatic test since in context of CI build our code is instrumented by Cobertura and runs much slower than non-instrumented Java library code  
	public void testFormattingPerformance() {
		//warmup
		format(10, true);
		format(10, false);

		final long startTime1 = System.nanoTime();
		format(24, true);
		final long startTime2 = System.nanoTime();
		format(24, false);
		final long endTime = System.nanoTime();
		
		long customRunTime = startTime2 - startTime1;
		long standardRunTime = endTime - startTime2;
		

		Assert.assertTrue("TimeFormatter is expected to be at least 2.5 times faster. Actual: " + (double) standardRunTime/ customRunTime,
				customRunTime * 2.5 < standardRunTime);

	}	
	
	private static void format(int numberOfHours, boolean useTimeFormatter) {
		for (int i = 0; i < 10; i++) { // extra cycle
			for (int hr = 0; hr < numberOfHours; hr++) {
				
				final int hoursInSec = hr * 3600;
				for (int min = 0; min < 60; min++) {
					final int minInSec = min * 60;

					for (int sec = 0; sec < 60; sec++) {
						int time = hoursInSec + minInSec + sec;

						String text;
						if (useTimeFormatter)
							text = TimeFormatter.formatTimeOfDayFromSeconds(time);
						else
							text = naiveFormat(time);

						Assert.assertTrue (text.length() > 0); //just dummy check that uses 'text' to trick optimizer
					}
				}
			}
		}
	}
	
	
	private static String naiveFormat(int time) {
        int         secComp = time % 60;
        
        time /= 60;
        
        int         minComp = time % 60;
        
        time /= 60;
        
        int         hourComp = time;
        
        StringBuffer    sb = new StringBuffer ();
        
        sb.append (hourComp);
        sb.append (":");
        if (minComp < 10)
            sb.append ("0");
        
        sb.append (minComp);
        
        sb.append (":");
        if (secComp < 10)
            sb.append ("0");
        
        sb.append (secComp);
        
        return (sb.toString ());
	}

	private static void assertTimeParsed(String input, int expectedSeconds) {
		try {
			int actualSeconds = TimeFormatter.parseTimeOfDay(input);
			assertEquals("Number of seconds in '" + input + '\'',
					expectedSeconds, actualSeconds);
		} catch (NumberFormatException ex) {
			Assert.fail("Parsing of '" + input + "' failed with message ["
					+ ex.getMessage() + ']');
		}
	}

	private static void assertBad(String time) {
		try {
			TimeFormatter.parseTimeOfDay(time);
			Assert.fail("Expected to detect a problem in time string \"" + time
					+ '"');
		} catch (NumberFormatException expected) {
		}
	}

        
        public void main (String [] args) {
            System.out.print("hey");
        }
}
