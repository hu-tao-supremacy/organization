package app.onepass.organizer.utilities;

import java.time.Instant;

import app.onepass.apis.AnswerType;
import app.onepass.apis.QuestionType;

public class TypeUtil {

	public static QuestionType toQuestionType(String questionType) {

		switch (questionType) {
		case "PRE_EVENT":
			return QuestionType.PRE_EVENT;
		case "POSY_EVENT":
			return QuestionType.POST_EVENT;
		}

		return QuestionType.POST_EVENT;
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
