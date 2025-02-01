package com.example.jobportal.controllers;


import com.example.jobportal.models.JobDetails;
import com.example.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/user/job")
public class JobController {

    @Autowired
    JobService jobService;

    @GetMapping("all-jobs")
    public Object getAllJobDetails(){
        return jobService.getAllJobDetails();
    }

    @GetMapping("jobs/{id}")
    public Object getJobDetailsById(@PathVariable Integer id){
        return jobService.getJobDetails(id);
    }

    @PostMapping("create")
    public Object addJob(@RequestBody JobDetails jobDetails){
        return jobService.addJob(
                jobDetails.getJobName(),
                jobDetails.getJobCompanyName(),
                jobDetails.getJobDescription(),
                jobDetails.getJobLocation(),
                jobDetails.getJobType(),
                jobDetails.getJobContactEMail(),
                jobDetails.getJobSalaryRange(),
                jobDetails.getJobRequirements(),
                jobDetails.getJobApplyLink()
        );
    }

}
