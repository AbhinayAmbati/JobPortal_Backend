package com.example.jobportal.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class JobDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer jobId;
    private String jobName;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobCompanyName() {
        return jobCompanyName;
    }

    public void setJobCompanyName(String jobCompanyName) {
        this.jobCompanyName = jobCompanyName;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobSalaryRange() {
        return jobSalaryRange;
    }

    public void setJobSalaryRange(String jobSalaryRange) {
        this.jobSalaryRange = jobSalaryRange;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getJobRequirements() {
        return jobRequirements;
    }

    public void setJobRequirements(String jobRequirements) {
        this.jobRequirements = jobRequirements;
    }

    public String getJobContactEMail() {
        return jobContactEMail;
    }

    public void setJobContactEMail(String jobContactEMail) {
        this.jobContactEMail = jobContactEMail;
    }

    private String jobDescription;
    private String jobCompanyName;
    private String jobLocation;
    private String jobSalaryRange;
    private String jobType;
    private LocalDateTime createdOn;
    private String jobRequirements;
    private String jobContactEMail;

}
