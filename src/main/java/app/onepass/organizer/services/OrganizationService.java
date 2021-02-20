package app.onepass.organizer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.onepass.organizer.entities.OrganizationEntity;
import app.onepass.organizer.repositories.OrganizationRepository;

@Service
public class OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;

	public List<OrganizationEntity> getAllOrganizations() {
		return organizationRepository.findAll();
	}
}
