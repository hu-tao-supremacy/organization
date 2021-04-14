package app.onepass.organizer.messages;

import app.onepass.apis.User;
import app.onepass.organizer.entities.UserEntity;
import lombok.Getter;

public class UserMessage implements BaseMessage<UserMessage, UserEntity> {

	@Getter
	User user;

	public UserMessage(User user) {
		this.user = user;
	}

	@Override
	public UserEntity parseMessage() {

		return UserEntity.builder()
				.id(user.getId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.nickname(user.hasNickname() ? user.getNickname().getValue() : null)
				.chulaId(user.hasChulaId() ? user.getChulaId().getValue() : null)
				.isChulaStudent(user.getIsChulaStudent())
				.gender(user.getGender().toString())
				.address(user.hasAddress() ? user.getAddress().getValue() : null)
				.profilePictureUrl(user.hasProfilePictureUrl() ? user.getProfilePictureUrl().getValue() : null)
				.didSetup(user.getDidSetup())
				.district(user.hasDistrict() ? user.getDistrict().getValue() : null)
				.zipCode(user.hasZipCode() ? user.getZipCode().getValue() : null)
				.phoneNumber(user.hasPhoneNumber() ? user.getPhoneNumber().getValue() : null)
				.province(user.hasProvince() ? user.getProvince().getValue() : null)
				.academicYear(user.hasAcademicYear() ? user.getAcademicYear().getValue() : null)
				.build();
	}
}
