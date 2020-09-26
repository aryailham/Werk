package com.example.werk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.werk.HistoryItem;
import com.example.werk.model.AvailableJobs;
import com.example.werk.model.Candidates;
import com.example.werk.model.Job;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper OpenHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess Instance;
    Cursor c = null;

    private DatabaseAccess(Context context){
        this.OpenHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if(Instance == null){
            Instance = new DatabaseAccess(context);
        }
        return Instance;
    }

    //buka database

    public void open(){
        this.db = OpenHelper.getWritableDatabase();
    }

    //tutup koneksi database

    public void close(){
        if(db != null){
            this.db.close();
        }
    }

    //function ini dipakai pada saat login
    public boolean checkExist(String email, String password){
        try{
            String query = "select * from MsJobSeeker where email=?";
            c = db.rawQuery(query, new String[]{email});
        }
        catch(Exception e){
            e.printStackTrace();
        }
        c.moveToFirst();

        if(c.getCount() > 0 && BCrypt.checkpw(password,c.getString(10))) return true;

        return false;
    }

    //function ini dipakai untuk mengecek bahwa provider ada atau tidak
    public boolean checkProviderExist(String email, String password){
        try{
            String query = "select * from MsJobProvider where email=? AND password = ?";
            c = db.rawQuery(query, new String[]{email,password});
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(c.getCount() > 0) return true;

        return false;
    }

    //function ini dipakai pada saat register dan edit profile
    public boolean checkExist(String email){

        try{
            String query = "SELECT * FROM MsJobSeeker WHERE email = '" +email+"'";
            c = db.rawQuery(query, new String[]{});
        }
        catch (Exception e){
            e.printStackTrace();
        }

//        StringBuffer buffer = new StringBuffer();

        if(c.getCount() > 0){
            return true;
        }
        return false;
    }

    //untuk mendaftarkan pencari kerja
    public boolean registerSeeker(String email, String password, String gender, String dob, String address, String cv, String linkedInUrl, String name, String phoneNo){
        password = BCrypt.hashpw(password, BCrypt.gensalt(12));

        try {
            String query = "INSERT INTO MsJobSeeker(nama, gender, dob, alamat, noHp, linkedInUrl, email, password) VALUES('"+name+"','"
                    +gender+"','"
                    +dob+"','"
                    +address+"','"
                    +phoneNo+"','"
//                        +cv+"','"
                    +linkedInUrl+"','"
                    +email+"','"
                    +password+"')";

            c = db.rawQuery(query, new String[]{});
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(c.getCount() > 0){
            return true;
        }
        return false;

    }

    //untuk mengupdate profile dari pekerja
    public boolean updateSeeker(String oldEmail, String email, String password, String gender, String dob, String address, String cv, String linkedInUrl, String name, String phoneNo){
        password = BCrypt.hashpw(password, BCrypt.gensalt(12));

        try {
            String query = "UPDATE MsJobSeeker SET email=?, password=?, gender=?, dob=?, alamat=?, linkedInUrl=?, nama=?, noHp=? WHERE email=?";

            c = db.rawQuery(query, new String[]{email,password,gender,dob,address,linkedInUrl,name,phoneNo,oldEmail});
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(c.getCount() > 0){
            return true;
        }
        return false;

    }

    //mendapatkan data dari pekerja di dalam database
    public Cursor getUserData(String oldEmail){

        try{
            String query = "SELECT * FROM MsJobSeeker WHERE email = ?";
            c = db.rawQuery(query, new String[]{oldEmail});
        }
        catch (Exception e){
            e.printStackTrace();
        }

//        StringBuffer buffer = new StringBuffer();

        return c;

    }

    //Stanley
    public List<AvailableJobs> getJobsByCategory(String category)
    {
        List<AvailableJobs> jobs = new ArrayList<>();
        String query = "SELECT jobTitle, namaPerusahaan, alamatPerusahaan, salaryRangeLower, salaryRangeUpper, COUNT(ja.idJobSeeker) AS applicantCount, jv.idJobVacancy " +
                        "FROM TrJobVacancyForm jv JOIN MsJobProvider jp ON jv.idJobProvider = jp.idJobProvider " +
                        "JOIN MsCategory c ON jv.idCategory = c.idCategory LEFT JOIN TrJobApplicationForm ja ON jv.idJobVacancy = ja.idJobVacancy " +
                        "WHERE c.categoryName = ? GROUP BY jv.idJobVacancy";
        c = db.rawQuery(query, new String[] {category});

        //c.moveToFirst();
        if( c != null && c.moveToFirst() ){
            do{
                AvailableJobs availJob = new AvailableJobs();
                availJob.setJobTitle(c.getString(0));
                availJob.setCompanyName(c.getString(1));
                availJob.setCompanyAddress(c.getString(2));
                availJob.setSalaryLower(c.getInt(3));
                availJob.setSalaryUpper(c.getInt(4));
                availJob.setApplicantCount(c.getInt(5));
                availJob.setJobVacancyId(c.getInt(6));
                jobs.add(availJob);

            }while (c.moveToNext());
            c.close();
        }

        return jobs;
    }

    public List<AvailableJobs> getJobsByPopular()
    {
        List<AvailableJobs> jobs = new ArrayList<>();
        String query = "SELECT jobTitle, namaPerusahaan, alamatPerusahaan, salaryRangeLower, salaryRangeUpper, COUNT(ja.idJobSeeker) AS applicantCount , jv.idJobVacancy " +
                        "FROM TrJobVacancyForm jv JOIN MsJobProvider jp ON jv.idJobProvider = jp.idJobProvider LEFT JOIN TrJobApplicationForm ja ON jv.idJobVacancy = ja.idJobVacancy " +
                        "GROUP BY jv.idJobVacancy ORDER BY applicantCount DESC";
        c = db.rawQuery(query, null);

        //c.moveToFirst();
        if( c != null && c.moveToFirst() ){
            do{
                AvailableJobs availJob = new AvailableJobs();
                availJob.setJobTitle(c.getString(0));
                availJob.setCompanyName(c.getString(1));
                availJob.setCompanyAddress(c.getString(2));
                availJob.setSalaryLower(c.getInt(3));
                availJob.setSalaryUpper(c.getInt(4));
                availJob.setApplicantCount(c.getInt(5));
                availJob.setJobVacancyId(c.getInt(6));
                jobs.add(availJob);

            }while (c.moveToNext());
            c.close();
        }

        return jobs;
    }

    public List<AvailableJobs> getTopJobs()
    {
        List<AvailableJobs> jobs = new ArrayList<>();
        String query = "SELECT jobTitle, namaPerusahaan, alamatPerusahaan, salaryRangeLower, salaryRangeUpper, COUNT(ja.idJobSeeker) AS applicantCount , jv.idJobVacancy " +
                "FROM TrJobVacancyForm jv JOIN MsJobProvider jp ON jv.idJobProvider = jp.idJobProvider LEFT JOIN TrJobApplicationForm ja ON jv.idJobVacancy = ja.idJobVacancy " +
                "GROUP BY jv.idJobVacancy ORDER BY applicantCount DESC LIMIT 2";
        c = db.rawQuery(query, null);

        //c.moveToFirst();
        if( c != null && c.moveToFirst() ){
            do{
                AvailableJobs availJob = new AvailableJobs();
                availJob.setJobTitle(c.getString(0));
                availJob.setCompanyName(c.getString(1));
                availJob.setCompanyAddress(c.getString(2));
                availJob.setSalaryLower(c.getInt(3));
                availJob.setSalaryUpper(c.getInt(4));
                availJob.setApplicantCount(c.getInt(5));
                availJob.setJobVacancyId(c.getInt(6));
                jobs.add(availJob);

            }while (c.moveToNext());
            c.close();
        }

        return jobs;
    }

    public List<AvailableJobs> searchJobs(String search)
    {
        List<AvailableJobs> jobs = new ArrayList<>();
        String query = "SELECT jobTitle, namaPerusahaan, alamatPerusahaan, salaryRangeLower, salaryRangeUpper, COUNT(ja.idJobSeeker) AS applicantCount , jv.idJobVacancy " +
                        "FROM TrJobVacancyForm jv JOIN MsJobProvider jp ON jv.idJobProvider = jp.idJobProvider LEFT JOIN TrJobApplicationForm ja ON jv.idJobVacancy = ja.idJobVacancy " +
                        "WHERE jobTitle LIKE ? GROUP BY jv.idJobVacancy";
        c = db.rawQuery(query, new String[] {"%" + search + "%"});

        //c.moveToFirst();
        if( c != null && c.moveToFirst() ){
            do{
                AvailableJobs availJob = new AvailableJobs();
                availJob.setJobTitle(c.getString(0));
                availJob.setCompanyName(c.getString(1));
                availJob.setCompanyAddress(c.getString(2));
                availJob.setSalaryLower(c.getInt(3));
                availJob.setSalaryUpper(c.getInt(4));
                availJob.setApplicantCount(c.getInt(5));
                availJob.setJobVacancyId(c.getInt(6));
                jobs.add(availJob);

            }while (c.moveToNext());
            c.close();
        }


        return jobs;
    }


    //Russell
    public void applyJob(Integer idJobSeeker, int idJobVacancy, Integer desiredSalary, String pitch){

        String applyDate;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        month = month + 1;
        applyDate = String.format("%d/%02d/%02d", year, month, day);

        String query = "INSERT INTO TrJobApplicationForm(idJobSeeker, idJobVacancy, applyDate, " +
                "desiredSalary, pitch, status) VALUES(?, ?, ?, ?, ?, 'Waiting')";
        db.execSQL(query, new Object[]{idJobSeeker, idJobVacancy, applyDate, desiredSalary, pitch});
    }

    public Job getJobData(String jobTitle, String companyName){
        try {
            String query =
                    "SELECT idJobVacancy,  alamatPerusahaan, categoryName, employmentTypeName, salaryRangeLower, salaryRangeUpper, " +
                            "jobDescription, qualification, benefits, startPeriod, endPeriod " +
                            "FROM TrJobVacancyForm a " +
                            "JOIN MsJobProvider b ON a.idJobProvider = b.idJobProvider " +
                            "JOIN MsCategory c ON a.idCategory = c.idCategory " +
                            "JOIN MsEmploymentType d ON a.idEmploymentType = d.idEmploymentType " +
                            "WHERE jobTitle = ? AND namaPerusahaan = ?";
            c = db.rawQuery(query, new String[]{jobTitle, companyName});
        }catch (Exception e){
            e.printStackTrace();
        }
        c.moveToFirst();
        Job job = new Job(
                c.getInt   (0),//JobID
                jobTitle,                 //jobTitle
                companyName,              //companyName
                c.getString(1),//companyAddress
                c.getString(2),//jobCategory
                c.getString(3),//employmentType
                c.getInt   (4),//salaryLower
                c.getInt   (5),//salaryUpper
                c.getString(6),//jobDescription
                c.getString(7),//qualification
                c.getString(8),//benefits
                c.getString(9),//startPeriod
                c.getString(10)//endPeriod
        );
        return job;
    }

    public List<AvailableJobs> getFewJobsByCategory(String category)
    {
        List<AvailableJobs> jobs = new ArrayList<>();
        try {
            String query =
                    "SELECT jobTitle, namaPerusahaan, alamatPerusahaan, salaryRangeLower, salaryRangeUpper " +
                            "FROM TrJobVacancyForm a JOIN MsJobProvider b ON a.idJobProvider = b.idJobProvider " +
                            "JOIN MsCategory c ON a.idCategory = c.idCategory WHERE c.categoryName = ? LIMIT 5";
            c = db.rawQuery(query, new String[] {category});
        } catch(Exception e){

            e.printStackTrace();
        }

        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                AvailableJobs availJob = new AvailableJobs();
                availJob.setJobTitle(c.getString(0));
                availJob.setCompanyName(c.getString(1));
                availJob.setCompanyAddress(c.getString(2));
                availJob.setSalaryLower(c.getInt(3));
                availJob.setSalaryUpper(c.getInt(4));
                jobs.add(availJob);
            }while (c.moveToNext());
        }else{

        }
        return jobs;
    }

//    public List<AvailableJobs> getJobsByCategory(String category)
//    {
//        List<AvailableJobs> jobs = new ArrayList<>();
//        try {
//            String query =
//                    "SELECT jobTitle, namaPerusahaan, alamatPerusahaan, salaryRangeLower, salaryRangeUpper " +
//                            "FROM TrJobVacancyForm a JOIN MsJobProvider b ON a.idJobProvider = b.idJobProvider " +
//                            "JOIN MsCategory c ON a.idCategory = c.idCategory WHERE c.categoryName = ?";
//            c = db.rawQuery(query, new String[] {category});
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//
//        if(c.getCount() > 0){
//            c.moveToFirst();
//            do{
//                AvailableJobs availJob = new AvailableJobs();
//                availJob.setJobTitle(c.getString(0));
//                availJob.setCompanyName(c.getString(1));
//                availJob.setCompanyAddress(c.getString(2));
//                availJob.setSalaryLower(c.getInt(3));
//                availJob.setSalaryUpper(c.getInt(4));
//                jobs.add(availJob);
//            }while (c.moveToNext());
//        }else{
//
//        }
//        return jobs;
//    }

    public List<Candidates> getCandidates(String companyName, String jobTitle){
        List<Candidates> candidatesList = new ArrayList<>();
        return candidatesList;
    }

    public void respondCandidate(String companyName, String jobTitle, String status){
//        String query =
//                "UPDATE TrJobApplicationForm" +
//                "JOIN TrJobApplicationFormSET status = '?' WHERE " idk how
//        c = db.rawQuery(query, new String[]{companyName, jobTitle, status});
    }


    //Hardy
    public int checkIdJobSeeker(String email){
        String query = "select idJobSeeker from MsJobSeeker where email=?";
        c = db.rawQuery(query, new String[]{email}); //change to email
        c.moveToFirst();
//        String query = "select * from MsJobSeeker where email=?";
//        c = db.rawQuery(query, new String[]{email});

//        StringBuffer buffer = new StringBuffer();

        if(c.getCount() > 0 ){
            return c.getInt(0);

        }
        return -1;
    }

    public List<HistoryItem> getAppHistory(String email) {
        int id = checkIdJobSeeker(email);

        List<HistoryItem> historyItems = new ArrayList<>();
        try {
            String query = "SELECT jobTitle, namaPerusahaan, applyDate, status FROM TrJobApplicationForm ja LEFT JOIN TrJobVacancyForm jv ON ja.idJobVacancy = jv.idJobVacancy JOIN MsJobProvider jp ON jv.idJobProvider = jp.idJobProvider WHERE ja.idJobSeeker =" + id;
            c = db.rawQuery(query, null);

            if (c != null && c.moveToFirst()) {
                do {
                    HistoryItem item = new HistoryItem();
                    item.setJobTitle(c.getString(0));
                    item.setNamaPerusahahaan(c.getString(1));
                    item.setApplyDate(c.getString(2));
                    item.setStatus(c.getString(3));
                    historyItems.add(item);
                } while (c.moveToNext());

                c.close();

            }
        } catch (Exception e) {

            e.printStackTrace();

        }

        return historyItems;

    }
}
