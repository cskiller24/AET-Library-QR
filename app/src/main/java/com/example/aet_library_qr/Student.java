package com.example.aet_library_qr;

public class Student {

    private String email;
    private String lname;
    private String fname;
    private String mname;
    private String cdept;
    private String yrlevel;
    private String stdnum;
    private String age;

    public Student() {
    }

    public Student(String email, String lname, String fname, String mname, String cdept, String yrlevel, String stdnum, String age) {
        this.email = email;
        this.lname = lname;
        this.fname = fname;
        this.mname = mname;
        this.cdept = cdept;
        this.yrlevel = yrlevel;
        this.stdnum = stdnum;
        this.age = age;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail() {
        this.email = email;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getCdept() {
        return cdept;
    }

    public void setCdept(String cdept) {
        this.cdept = cdept;
    }

    public String getYrlevel() {
        return yrlevel;
    }

    public void setYrlevel(String yrlevel) {
        this.yrlevel = yrlevel;
    }

    public String getStdnum() {
        return stdnum;
    }

    public void setStdnum(String stdnum) {
        this.stdnum = stdnum;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
