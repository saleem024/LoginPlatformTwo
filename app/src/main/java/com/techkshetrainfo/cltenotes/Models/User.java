package com.techkshetrainfo.cltenotes.Models;


public class User {

    private String name;
    private String email;
    private String unique_id;
    private String password;
    private String old_password;
    private String new_password;
    private String code;

//    private String fullName;
//    private String contactNumber;
//    private String emailId;
//    private String description;

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public void setContactNumber(String contactNumber) {
//        this.contactNumber = contactNumber;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setEmailId(String emailId) {
//        this.emailId = emailId;
//    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUnique_id() {
        return unique_id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
