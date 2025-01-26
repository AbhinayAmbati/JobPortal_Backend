package com.example.jobportal.controllers;


import com.example.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
