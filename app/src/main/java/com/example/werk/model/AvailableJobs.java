package com.example.werk.model;

public class AvailableJobs {
    private String jobTitle;
    private String companyName;
    private String companyAddress;
    private int salaryLower;
    private int salaryUpper;
    private int applicantCount;
    private int jobVacancyId;
    private String jobCategory;

    public AvailableJobs() {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.salaryLower = salaryLower;
        this.salaryUpper = salaryUpper;
        this.applicantCount = applicantCount;
        this.jobVacancyId = jobVacancyId;
    }
    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public int getApplicantCount() {
        return applicantCount;
    }

    public void setApplicantCount(int applicantCount) {
        this.applicantCount = applicantCount;
    }

    public int getJobVacancyId() {
        return jobVacancyId;
    }

    public void setJobVacancyId(int jobVacancyId) {
        this.jobVacancyId = jobVacancyId;
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
}
