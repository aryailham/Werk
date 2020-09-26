package com.example.werk.model;

public class Job {
    private int jobID;
    private String jobTitle;
    private String companyName;
    private String companyAddress;
    private String jobCategory;
    private String employentType;
    private int salaryLower;
    private int salaryUpper;
    private String jobDescription;
    private String qualification;
    private String benefits;
    private String startPeriod;
    private String endPeriod;

    public Job(int jobID, String jobTitle, String companyName, String companyAddress, String jobCategory, String employentType, int salaryLower, int salaryUpper, String jobDescription, String qualification, String benefits, String startPeriod, String endPeriod) {
        this.jobID = jobID;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.jobCategory = jobCategory;
        this.employentType = employentType;
        this.salaryLower = salaryLower;
        this.salaryUpper = salaryUpper;
        this.jobDescription = jobDescription;
        this.qualification = qualification;
        this.benefits = benefits;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getEmployentType() {
        return employentType;
    }

    public void setEmployentType(String employentType) {
        this.employentType = employentType;
    }

    public int getSalaryLower() {
        return salaryLower;
    }

    public void setSalaryLower(int salaryLower) {
        this.salaryLower = salaryLower;
    }

    public int getSalaryUpper() {
        return salaryUpper;
    }

    public void setSalaryUpper(int salaryUpper) {
        this.salaryUpper = salaryUpper;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }
}
