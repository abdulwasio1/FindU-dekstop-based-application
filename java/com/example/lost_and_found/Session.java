package com.example.lost_and_found;

public class Session {

    private static int userId;
    private static String userName;
    private static String s_id;
    private static String email;
    private static String password;
    private static String phone;
    private static String role;
    private static int itemCount;
    private static int foundCount;
    private static int lostCount;
    private static int totalClaim;
    private static String profileImage;

    public static String getProfileImage() { return profileImage; }
    public static void setProfileImage(String profileImage) { Session.profileImage = profileImage; }


    public static void setUser(int id, String  st_id , String name, String userEmail , String passwordValue , String phoneValue ,String roleValue , String profileImage) {
        userId = id;
        s_id = st_id;
        userName = name;
        email = userEmail;
        password = passwordValue;
        phone = phoneValue;
        role = roleValue;
        Session.profileImage = profileImage;
    }

    public static String getS_id() {
        return s_id;
    }

    public static void setS_id(String s_id) {
        Session.s_id = s_id;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        Session.setPhone(phone);
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        Session.role = role;
    }

    public static int getFoundCount() {
        return foundCount;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Session.password = password;
    }

    public static void setFoundCount(int foundCount) {
        Session.foundCount = foundCount;
    }

    public static int getLostCount() {
        return lostCount;
    }

    public static void setLostCount(int lostCount) {
        Session.lostCount = lostCount;
    }

    public static int getTotalClaim() {
        return totalClaim;
    }

    public static void setTotalClaim(int totalClaim) {
        Session.totalClaim = totalClaim;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getEmail() {
        return email;
    }

    public static int getItemCount() {
        return itemCount;
    }

    public static void setItemCount(int itemCount) {
        Session.itemCount = itemCount;
    }

    public static void clear() {
        userId = 0;
        userName = null;
        email = null;
        password = null;
        phone = null;
        role = null;
        itemCount = 0;
        foundCount = 0;
        lostCount = 0;
        totalClaim = 0;
    }

    public static boolean isLoggedIn() {
        return userId != 0;
    }
}
