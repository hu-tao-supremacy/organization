package app.onepass.organizer.utilities;

import app.onepass.apis.Status;

public class StatusUtil {

	public static Status toStatus(String status) {

		switch (status) {
		case "PENDING":
			return Status.PENDING;
		case "APPROVED":
			return Status.APPROVED;
		case "REJECTED":
			return Status.REJECTED;
		}

		return Status.PENDING;
	}
}
