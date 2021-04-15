package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.protobuf.StringValue;

import app.onepass.apis.Location;
import app.onepass.organizer.messages.LocationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity implements BaseEntity<LocationMessage, LocationEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	private String name;
	@NotNull
	private String googleMapUrl;
	private String description;
	private String travelInformationImageUrl;
	private String travelInformationImageHash;

	@Override
	public LocationMessage parseEntity() {

		Location location = Location.newBuilder()
				.setId(id)
				.setName(name)
				.setGoogleMapUrl(googleMapUrl)
				.build();

		if (description != null) {
			location = location.toBuilder().setDescription(StringValue.of(description)).build();
		}

		if (travelInformationImageUrl != null) {
			location = location.toBuilder().setTravelInformationImageUrl(StringValue.of(travelInformationImageUrl)).build();
		}

		if (travelInformationImageHash != null) {
			location = location.toBuilder().setTravelInformationImageHash(StringValue.of(travelInformationImageHash)).build();
		}

		return new LocationMessage(location);
	}
}
