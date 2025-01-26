package com.example.jobportal.dao;

import com.example.jobportal.models.JobDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDao extends JpaRepository<JobDetails,Integer> {
}
