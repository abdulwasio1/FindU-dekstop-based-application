package com.example.lost_and_found;

public class usersModel {
    private int user_id;
//    private String student_id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String s_id;
    private String profile_image;

    public usersModel(int user_id, String name, String email, String password, String phone, String role ,  String s_id) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.s_id = s_id;
    }

    public usersModel(String name ,  String s_id , String phone , String email , String password ) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.s_id = s_id;
    }

    public usersModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public usersModel(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getProfile_image() { return profile_image; }
    public void setProfile_image(String profile_image) { this.profile_image = profile_image; }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }
}
