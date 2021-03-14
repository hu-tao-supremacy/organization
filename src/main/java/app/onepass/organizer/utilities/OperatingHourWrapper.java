package app.onepass.organizer.utilities;

import org.json.JSONObject;

import app.onepass.apis.DayOfWeek;
import app.onepass.apis.OperatingHour;

public class OperatingHourWrapper {

	public OperatingHourWrapper(JSONObject operatingHour) {
		this.operatingHour = operatingHour;
	}

	JSONObject operatingHour;

	public OperatingHour parseOperatingHour() {

		return OperatingHour.newBuilder()
				.setStartHour((long) operatingHour.get("start"))
				.setFinishHour((long) operatingHour.get("finish"))
				.setDay((DayOfWeek) operatingHour.get("day"))
				.build();
	}
}
