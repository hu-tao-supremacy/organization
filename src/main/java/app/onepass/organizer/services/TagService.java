package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateTagRequest;
import app.onepass.apis.GetByIdRequest;
import app.onepass.apis.GetTagByIdResponse;
import app.onepass.apis.GetTagResponse;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Tag;
import app.onepass.apis.UpdateTagRequest;
import app.onepass.organizer.entities.EventTagEntity;
import app.onepass.organizer.entities.TagEntity;
import app.onepass.organizer.messages.TagMessage;
import app.onepass.organizer.repositories.EventTagRepository;
import app.onepass.organizer.repositories.TagRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class TagService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	TagRepository tagRepository;

	@Autowired
	EventTagRepository eventTagRepository;

	@Override
	@Transactional
	public void createTag(CreateTagRequest request, StreamObserver<Empty> responseObserver) {

		if (tagRepository.findById(request.getTag().getId()).isPresent()) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "A tag with this ID already exists.");

			return;
		}

		TagMessage tagMessage = new TagMessage(request.getTag());

		ServiceUtil.saveEntity(tagMessage, tagRepository);

		responseObserver.onCompleted();
	}

	@Override
	@Transactional
	public void addTag(UpdateTagRequest request, StreamObserver<Empty> responseObserver) {

		List<EventTagEntity> eventTagEntities = new ArrayList<>();

		for (int index = 0; index < request.getTagIdsCount(); index++) {

			EventTagEntity eventTagEntity = EventTagEntity.builder()
					.eventId(request.getEventId())
					.tagId(request.getTagIds(index))
					.build();

			eventTagEntities.add(eventTagEntity);
		}

		eventTagRepository.saveAll(eventTagEntities);

		responseObserver.onCompleted();
	}

	@Override
	@Transactional
	public void removeTag(UpdateTagRequest request, StreamObserver<Empty> responseObserver) {

		List<Long> tagIds = request.getTagIdsList();

		List<EventTagEntity> eventTagEntities = eventTagRepository
				.findByEventId(request.getEventId());

		List<EventTagEntity> entitiesToDelete = new ArrayList<>();

		//TODO: Optimize!

		for (EventTagEntity eventTagEntity : eventTagEntities) {

			for (Long tagId : tagIds) {

				if (eventTagEntity.getTagId() == tagId) {

					entitiesToDelete.add(eventTagEntity);
				}
			}
		}

		eventTagRepository.deleteAll(entitiesToDelete);

		responseObserver.onCompleted();
	}

	@Override
	public void getTag(Empty request, StreamObserver<GetTagResponse> responseObserver) {

		List<TagEntity> allTagEntities = tagRepository.findAll();

		List<Tag> allTags = allTagEntities.stream()
				.map(tagEntity -> tagEntity.parseEntity().getTag())
				.collect(Collectors.toList());

		GetTagResponse getTagResponse = GetTagResponse.newBuilder()
				.addAllTags(allTags).build();

		ServiceUtil.returnObject(responseObserver, getTagResponse);
	}

	@Override
	public void getTagById(GetByIdRequest request, StreamObserver<GetTagByIdResponse> responseObserver) {

		TagEntity tagEntity;

		try {

			tagEntity = tagRepository
					.findById(request.getId())
					.orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			GetTagByIdResponse getTagByIdResponse = GetTagByIdResponse
					.newBuilder()
					.build();

			ServiceUtil.returnObject(responseObserver, getTagByIdResponse);

			return;
		}

		Tag tag = tagEntity.parseEntity().getTag();

		GetTagByIdResponse getTagByIdResponse = GetTagByIdResponse
				.newBuilder()
				.setTag(tag)
				.build();

		ServiceUtil.returnObject(responseObserver, getTagByIdResponse);

	}
}
