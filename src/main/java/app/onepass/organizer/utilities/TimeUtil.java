package app.onepass.organizer.utilities;

import java.time.Instant;

public class TimeUtil {

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
