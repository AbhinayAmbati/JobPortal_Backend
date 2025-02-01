package com.example.jobportal.service;


import com.example.jobportal.dao.JobDao;
import com.example.jobportal.models.JobDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {


    @Autowired
    JobDao jobDao;

    public Object getAllJobDetails() {
        List<JobDetails> data =  jobDao.findAll();
        return data;
    }

    public Object getJobDetails(Integer id) {

        return jobDao.findById(id);

    }

    public Object addJob(String jobName, String jobCompanyName, String jobDescription, String jobLocation, String jobType, String jobContactEMail, String jobSalaryRange, String jobRequirements, String jobApplyLink) {
        JobDetails jobDetails = new JobDetails();
        jobDetails.setJobName(jobName);
        jobDetails.setJobCompanyName(jobCompanyName);
        jobDetails.setJobDescription(jobDescription);
        jobDetails.setJobLocation(jobLocation);
        jobDetails.setJobType(jobType);
        jobDetails.setJobContactEMail(jobContactEMail);
        jobDetails.setJobSalaryRange(jobSalaryRange);
        jobDetails.setJobRequirements(jobRequirements);
        jobDetails.setJobApplyLink(jobApplyLink);
        jobDao.save(jobDetails);
        return jobDetails;
    }
}
