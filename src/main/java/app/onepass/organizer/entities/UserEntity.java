package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;

import app.onepass.apis.User;
import app.onepass.organizer.messages.UserMessage;
import app.onepass.organizer.utilities.TypeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements BaseEntity<UserMessage, UserEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String email;
	private String nickname;
	private String chulaId;
	private boolean isChulaStudent;
	@NotNull
	private String gender;
	private String address;
	private String profilePictureUrl;
	private boolean didSetup;
	private String district;
	private String zipCode;
	private String phoneNumber;
	private String province;
	private Integer academicYear;

	@Override
	public UserMessage parseEntity() {

		User user = User.newBuilder()
				.setId(id)
				.setFirstName(firstName)
				.setLastName(lastName)
				.setEmail(email)
				.setIsChulaStudent(isChulaStudent)
				.setGender(TypeUtil.toGender(gender))
				.setDidSetup(didSetup)
				.build();

		if (nickname != null) {
			user = user.toBuilder().setNickname(StringValue.of(nickname)).build();
		}

		if (chulaId != null) {
			user = user.toBuilder().setChulaId(StringValue.of(chulaId)).build();
		}

		if (address != null) {
			user = user.toBuilder().setAddress(StringValue.of(address)).build();
		}

		if (profilePictureUrl != null) {
			user = user.toBuilder().setProfilePictureUrl(StringValue.of(profilePictureUrl)).build();
		}

		if (district != null) {
			user = user.toBuilder().setDistrict(StringValue.of(district)).build();
		}

		if (zipCode != null) {
			user = user.toBuilder().setZipCode(StringValue.of(zipCode)).build();
		}

		if (phoneNumber != null) {
			user = user.toBuilder().setPhoneNumber(StringValue.of(phoneNumber)).build();
		}

		if (province != null) {
			user = user.toBuilder().setProvince(StringValue.of(province)).build();
		}

		if (academicYear != null) {
			user = user.toBuilder().setAcademicYear(Int32Value.of(academicYear)).build();
		}

		return new UserMessage(user);

	}
}
