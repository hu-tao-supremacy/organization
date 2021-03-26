package app.onepass.organizer.utilities;

import java.time.Instant;

import app.onepass.apis.AnswerType;
import app.onepass.apis.QuestionGroupType;

public class TypeUtil {

	public static QuestionGroupType toQuestionGroupType(String questionGroupType) {

		switch (questionGroupType) {
		case "PRE_EVENT":
			return QuestionGroupType.PRE_EVENT;
		case "POSY_EVENT":
			return QuestionGroupType.POST_EVENT;
		}

		return QuestionGroupType.POST_EVENT;
	}

	public static AnswerType toAnswerType(String answerType) {

		switch (answerType) {
		case "SCALE":
			return AnswerType.SCALE;
		case "TEXT":
			return AnswerType.TEXT;
		}

		return AnswerType.TEXT;
	}

	public static java.sql.Timestamp toSqlTimestamp(com.google.protobuf.Timestamp timestamp) {

		return java.sql.Timestamp.from(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()));
	}

	public static com.google.protobuf.Timestamp toProtobufTimestamp(java.sql.Timestamp timestamp) {

		return com.google.protobuf.Timestamp.newBuilder()
				.setSeconds(timestamp.getTime())
				.setNanos(timestamp.getNanos())
				.build();
	}
}
