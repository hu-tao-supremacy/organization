package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateTagRequest;
import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Permission;
import app.onepass.apis.Tag;
import app.onepass.apis.UpdateTagRequest;
import app.onepass.organizer.entities.EventTagEntity;
import app.onepass.organizer.messages.TagMessage;
import app.onepass.organizer.repositories.EventRepository;
import app.onepass.organizer.repositories.EventTagRepository;
import app.onepass.organizer.repositories.TagRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class TagService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EventTagRepository eventTagRepository;

    @Override
    public void createTag(CreateTagRequest request, StreamObserver<Tag> responseObserver) {

        HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(),
                request.getOrganizationId(), Permission.TAG_CREATE);

        if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

            ServiceUtil.returnPermissionDeniedError(responseObserver);

            return;
        }

        if (tagRepository.findById(request.getTag().getId()).isPresent()) {

            ServiceUtil.returnInvalidArgumentError(responseObserver, "A tag with this ID already exists.");

            return;
        }

        TagMessage tagMessage = new TagMessage(request.getTag());

        ServiceUtil.saveEntity(tagMessage, tagRepository);

        ServiceUtil.returnObject(responseObserver, request.getTag());
    }

    @Override
    public void addTags(UpdateTagRequest request, StreamObserver<Empty> responseObserver) {

        if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(),
                request.getEventId(), Permission.EVENT_TAG_UPDATE)) {

            return;
        }

        List<EventTagEntity> eventTagEntities = new ArrayList<>();

        for (int index = 0; index < request.getTagIdsCount(); index++) {

            EventTagEntity eventTagEntity = EventTagEntity.builder()
                    .eventId(request.getEventId())
                    .tagId(request.getTagIds(index))
                    .build();

            eventTagEntities.add(eventTagEntity);
        }

        eventTagRepository.saveAll(eventTagEntities);

        ServiceUtil.returnEmpty(responseObserver);
    }

    @Override
    public void removeTags(UpdateTagRequest request, StreamObserver<Empty> responseObserver) {

        if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(),
                request.getEventId(), Permission.EVENT_TAG_UPDATE)) {

            return;
        }

        int eventId = request.getEventId();

        List<Integer> tagIds = request.getTagIdsList();

        List<EventTagEntity> entitiesToDelete = new ArrayList<>();

        for (int tagId : tagIds) {

            EventTagEntity eventTagEntity = eventTagRepository.findByEventIdAndTagId(eventId, tagId);

            if (eventTagEntity != null) {

                entitiesToDelete.add(eventTagEntity);
            }
        }

        eventTagRepository.deleteAll(entitiesToDelete);

        ServiceUtil.returnEmpty(responseObserver);
    }
}
