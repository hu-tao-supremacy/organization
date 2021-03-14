package app.onepass.organizer.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.json.JSONArray;
import org.json.JSONObject;

import app.onepass.apis.Facility;
import app.onepass.apis.OperatingHour;
import app.onepass.organizer.messages.FacilityMessage;
import app.onepass.organizer.utilities.OperatingHourWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "facility")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityEntity implements BaseEntity<FacilityMessage, FacilityEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull
	private long organizationId;
	@NotNull
	private String name;
	@NotNull
	private double latitude;
	@NotNull
	private double longitude;
	@NotNull
	private transient JSONArray operatingHours;
	@NotNull
	private String description;

	@Override
	public FacilityMessage parseEntity() {

		List<OperatingHour> hours = new ArrayList<>();

		for (int index = 0; index < operatingHours.length(); index++) {

			JSONObject operatingHour = operatingHours.getJSONObject(index);

			OperatingHourWrapper operatingHourWrapper = new OperatingHourWrapper(operatingHour);

			OperatingHour hour = operatingHourWrapper.parseOperatingHour();

			hours.add(hour);
		}

		Facility facility = Facility.newBuilder()
				.setId(id)
				.setOrganizationId(organizationId)
				.setName(name)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.addAllOperatingHours(hours)
				.setDescription(description)
				.build();

		return new FacilityMessage(facility);
	}

}
