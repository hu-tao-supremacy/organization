package app.onepass.organizer.utilities;

import java.time.Instant;

import app.onepass.apis.AnswerType;
import app.onepass.apis.Gender;
import app.onepass.apis.QuestionGroupType;
import app.onepass.apis.UserEvent.Status;

public class TypeUtil {

	public static QuestionGroupType toQuestionGroupType(String questionGroupType) {

		switch (questionGroupType) {
		case "PRE_EVENT":
			return QuestionGroupType.PRE_EVENT;
		case "POST_EVENT":
			return QuestionGroupType.POST_EVENT;
		}

		return QuestionGroupType.QUESTION_GROUP_TYPE_UNSPECIFIED;
	}

	public static AnswerType toAnswerType(String answerType) {

		switch (answerType) {
		case "SCALE":
			return AnswerType.SCALE;
		case "TEXT":
			return AnswerType.TEXT;
		}

		return AnswerType.ANSWER_TYPE_UNSPECIFIED;
	}

	public static Status toStatus(String status) {

		switch (status) {
		case "PENDING":
			return Status.PENDING;
		case "APPROVED":
			return Status.APPROVED;
		case "REJECTED":
			return Status.REJECTED;
		}

		return Status.STATUS_UNSPECIFIED;
	}

	public static Gender toGender(String gender) {

		switch (gender) {
		case "M":
			return Gender.M;
		case "F":
			return Gender.F;
		case "NS":
			return Gender.NS;
		}

		return Gender.GENDER_UNSPECIFIED;
	}

	public static java.sql.Timestamp toSqlTimestamp(com.google.protobuf.Timestamp timestamp) {

		return java.sql.Timestamp.from(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()));
	}

	public static com.google.protobuf.Timestamp toProtobufTimestamp(java.sql.Timestamp timestamp) {

		return com.google.protobuf.Timestamp.newBuilder().setSeconds(timestamp.getTime()).setNanos(timestamp.getNanos()).build();
	}
}
