package com.infy.infyinterns.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;

@Service(value = "projectService")
@Transactional
public class ProjectAllocationServiceImpl implements ProjectAllocationService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private MentorRepository mentorRepository;
	
	
	@Override
	public Integer allocateProject(ProjectDTO project) throws InfyInternException {

		Optional<Mentor> optional = mentorRepository.findById(project.getMentorDTO().getMentorId());
		Mentor mentor = optional.orElseThrow(()-> new InfyInternException("Service.MENTOR_NOT_FOUND"));
		Project newProject = new Project();
		
		if(mentor.getNumberOfProjectsMentored() >= 3)
		{
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		
		else {
			Project projectObj = new Project();
			projectObj.setProjectId(project.getProjectId());
			projectObj.setProjectName(project.getProjectName());
			projectObj.setReleaseDate(project.getReleaseDate());
			projectObj.setIdeaOwner(project.getIdeaOwner());
			mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored()+1);
			projectObj.setMentor(mentor);
			newProject = projectRepository.save(projectObj);
			
		}
		
		
		return newProject.getProjectId();
	}

	
	@Override
	public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException{
		
		List<Mentor> mentorList = mentorRepository.getMentorWithNumberOfProject(numberOfProjectsMentored);
		List<MentorDTO> mentorDTOList = new ArrayList<>();
		
		if(mentorList.isEmpty())
		{
			throw new InfyInternException("Service.MENTOR_NOT_FOUND");
		}
		else {
		for(int i=0;i<mentorList.size();i++) {
			Mentor mentor = mentorList.get(i);
			MentorDTO mentorDTO = new MentorDTO();
			mentorDTO.setMentorId(mentor.getMentorId());
			mentorDTO.setMentorName(mentor.getMentorName());
			mentorDTO.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored());
			mentorDTOList.add(mentorDTO);
			
		}
		}
		return mentorDTOList;
	}


	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException {
		
		Optional<Mentor> mentorDetailOptional = mentorRepository.findById(mentorId);
		Mentor mentorDetail = mentorDetailOptional.orElseThrow(()-> new InfyInternException("Service.MENTOR_NOT_FOUND"));
		if(mentorDetail.getNumberOfProjectsMentored()>=3) {
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		Optional<Project> projectDetailOptional = projectRepository.findById(projectId);
		Project projectDetail = projectDetailOptional.orElseThrow(()-> new InfyInternException("Service.PROJECT_NOT_FOUND"));
		mentorDetail.setNumberOfProjectsMentored(mentorDetail.getNumberOfProjectsMentored()+1);
		projectDetail.setMentor(mentorDetail);
		projectDetail=projectRepository.save(projectDetail);
	}

	@Override
	public void deleteProject(Integer projectId) throws InfyInternException {
		
		Optional<Project> projectTOBeDeletedOptional = projectRepository.findById(projectId);
		Project projectTOBeDeleted = projectTOBeDeletedOptional.orElseThrow(()-> new InfyInternException("Service.PROJECT_NOT_FOUND"));
		
		if(projectTOBeDeleted.getMentor()==null) {
			projectRepository.delete(projectTOBeDeleted);
		}
		else {
			projectTOBeDeleted.getMentor().setNumberOfProjectsMentored(projectTOBeDeleted.getMentor().getNumberOfProjectsMentored()-1);
			projectTOBeDeleted.setMentor(null);		
			projectRepository.delete(projectTOBeDeleted);
		}
	}
}