package com.example.lost_and_found;

public class adminSession {
    private static int adminId;
    private static int totalItems;
    private static int totalUsers;
    private static int totalMatches;
    private static int pendingItems;
    private static String adminName;
    private static String email;
    private static String password;
    private static String phone;
    private static String role;

    private static String profileImagePath;

    public static void setAdmin(int id, String name, String email, String password, String phone, String role) {
        adminId = id;
        adminName = name;
        adminSession.email = email;
        adminSession.password = password;
        adminSession.phone = phone;
        adminSession.role = role;
    }

    public static String getProfileImagePath() {
        return profileImagePath;
    }

    public static void setProfileImagePath(String profileImagePath) {
        adminSession.profileImagePath = profileImagePath;
    }

    public static int getAdminId() {
        return adminId;
    }

    public static void setAdminId(int adminId) {
        adminSession.adminId = adminId;
    }

    public static int getTotalItems() {
        return totalItems;
    }

    public static void setTotalItems(int totalItems) {
        adminSession.totalItems = totalItems;
    }

    public static int getTotalUsers() {
        return totalUsers;
    }

    public static void setTotalUsers(int totalUsers) {
        adminSession.totalUsers = totalUsers;
    }

    public static int getTotalMatches() {
        return totalMatches;
    }

    public static void setTotalMatches(int totalMatches) {
        adminSession.totalMatches = totalMatches;
    }

    public static int getPendingItems() {
        return pendingItems;
    }

    public static void setPendingItems(int pendingItems) {
        adminSession.pendingItems = pendingItems;
    }

    public static String getAdminName() {
        return adminName;
    }

    public static void setAdminName(String adminName) {
        adminSession.adminName = adminName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        adminSession.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        adminSession.password = password;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        adminSession.phone = phone;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        adminSession.role = role;
    }

    public static void clear() {
        adminId = 0;
        adminName = null;
        email = null;
        password = null;
        phone = null;
        role = null;
        totalItems = 0;
        totalUsers = 0;
        totalMatches = 0;
        pendingItems = 0;
    }

    // Check login
    public static boolean isLoggedInAdmin() {
        return adminId != 0;
    }
}
