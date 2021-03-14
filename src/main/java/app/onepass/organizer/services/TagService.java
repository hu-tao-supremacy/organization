package app.onepass.organizer.services;

import org.springframework.stereotype.Service;

import app.onepass.apis.CreateTagRequest;
import app.onepass.apis.GetByIdRequest;
import app.onepass.apis.GetTagByIdResponse;
import app.onepass.apis.GetTagResponse;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Result;
import app.onepass.apis.UpdateTagRequest;
import app.onepass.apis.UserRequest;
import io.grpc.stub.StreamObserver;

@Service
public class TagService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Override
	public void createTag(CreateTagRequest request, StreamObserver<Result> responseObserver) {
		super.createTag(request, responseObserver);
	}

	@Override
	public void addTag(UpdateTagRequest request, StreamObserver<Result> responseObserver) {
		super.addTag(request, responseObserver);
	}

	@Override
	public void removeTag(UpdateTagRequest request, StreamObserver<Result> responseObserver) {
		super.removeTag(request, responseObserver);
	}

	@Override
	public void getTag(UserRequest request, StreamObserver<GetTagResponse> responseObserver) {
		super.getTag(request, responseObserver);
	}

	@Override
	public void getTagById(GetByIdRequest request, StreamObserver<GetTagByIdResponse> responseObserver) {
		super.getTagById(request, responseObserver);
	}
}
