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
				.nickname(user.getNickname().getValue())
				.chulaId(user.getChulaId().getValue())
				.isChulaStudent((user.getIsChulaStudent()))
				.gender(user.getGender().toString())
				.address(user.getAddress().getValue())
				.profilePictureUrl(user.getProfilePictureUrl().getValue())
				.didSetup(user.getDidSetup())
				.district(user.getDistrict().getValue())
				.zipCode(user.getZipCode().getValue())
				.phoneNumber(user.getPhoneNumber().getValue())
				.province(user.getProvince().getValue())
				.academicYear(user.getAcademicYear().getValue())
				.build();
	}
}
