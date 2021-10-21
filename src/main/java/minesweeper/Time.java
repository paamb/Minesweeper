package minesweeper;

public class Time{
	private long startTime;
	private long elapsedSeconds = 0;
	private long elapsedMinutes = 0;

	public Time() {
		this.startTime = System.currentTimeMillis();
	}
	public void updateTime() {
		long startTime = getStartTime();
		long currentTime = System.currentTimeMillis();
		long timeElapsed = currentTime - startTime;
		
		long totalSeconds = timeElapsed / 1000;

		long seconds = totalSeconds % 60;
		long minutes = totalSeconds / 60;
		if(minutes > 99) {
			minutes = 99;
			seconds = 99;
		}
		this.elapsedSeconds = seconds;
		this.elapsedMinutes = minutes;
	}
	
	public long getStartTime() {
		return this.startTime;
	}

	public long getElapsedSeconds() {
		return this.elapsedSeconds;
	}

	public long getElapsedMinutes() {
		return this.elapsedMinutes;
	}
	
	public static String flattenTime(String timeString) {
		String [] timeList = timeString.split(":");
		int minutes = Integer.parseInt(timeList[0].strip());
		int seconds = Integer.parseInt(timeList[1].strip());
		String secondsString = String.valueOf(seconds);
		String minutesString = String.valueOf(minutes);
		if (seconds < 10){
			secondsString = 0 + String.valueOf(seconds);
		}
		if (minutes < 10) {
			minutesString = 0 + String.valueOf(minutes);
		}
		return minutesString + secondsString;
	}
}
