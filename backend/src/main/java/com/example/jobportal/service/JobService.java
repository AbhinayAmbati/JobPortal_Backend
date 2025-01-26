package com.example.jobportal.service;


import com.example.jobportal.dao.JobDao;
import com.example.jobportal.models.JobDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {


    @Autowired
    JobDao jobDao;

    public Object getAllJobDetails() {
        List<JobDetails> data =  jobDao.findAll();
        return data;
    }
}
