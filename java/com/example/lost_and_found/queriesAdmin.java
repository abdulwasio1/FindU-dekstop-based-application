package com.example.lost_and_found;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

public class queriesAdmin {

    private dbConnection connection = new dbConnection();

    public boolean loginCheck(usersModel model) throws Exception {
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement(
                "SELECT user_id, name, email, password, phone, role " +
                        "FROM lost_found_db.users " +
                        "WHERE BINARY email = ? AND BINARY password = ? AND role = 'admin'"
        );
        ps.setString(1, model.getEmail());
        ps.setString(2, model.getPassword());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            adminSession.setAdmin(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("role")
            );
            rs.close();
            ps.close();
            connection.closeCon();
            return true;
        }
        rs.close();
        ps.close();
        connection.closeCon();
        return false;
    }

    public ObservableList<itemModel> getItems() throws Exception {
        ObservableList<itemModel> totalItemsList = FXCollections.observableArrayList();
        connection.setUpCon();
        PreparedStatement psItem = connection.getCon().prepareStatement(
                "SELECT user_id, item_id, item, category, location, type, status, image_path, date_lost_found " +
                        "FROM lost_found_db.items"
        );
        ResultSet rsItem = psItem.executeQuery();

        int pendingCount = 0;
        int itemCount = 0;

        while (rsItem.next()) {
            itemModel model = new itemModel(
                    rsItem.getInt("user_id"),
                    rsItem.getInt("item_id"),
                    rsItem.getString("item"),
                    rsItem.getString("category"),
                    rsItem.getString("location"),
                    rsItem.getString("type"),
                    rsItem.getString("status"),
                    rsItem.getString("image_path"),
                    rsItem.getString("date_lost_found")
            );
            totalItemsList.add(model);
            if (rsItem.getString("status").equalsIgnoreCase("pending")) {
                pendingCount++;
            }
            itemCount++;
        }

        adminSession.setTotalItems(itemCount);
        adminSession.setPendingItems(pendingCount);
        getUsers();
        displayMatchesByAdmin();
        rsItem.close();
        psItem.close();
        connection.closeCon();
        return totalItemsList;
    }

    public ObservableList<usersModel> getUsers() throws Exception {
        int userCount = 0;
        ObservableList<usersModel> totalUserList = FXCollections.observableArrayList();
        connection.setUpCon();
        PreparedStatement psUser = connection.getCon().prepareStatement(
                "SELECT user_id, name, email, password, phone, role, s_id, profile_image " +
                        "FROM lost_found_db.users WHERE user_id != ?"
        );
        psUser.setInt(1, adminSession.getAdminId());
        ResultSet rsUser = psUser.executeQuery();

        while (rsUser.next()) {
            usersModel uModel = new usersModel(
                    rsUser.getInt("user_id"),
                    rsUser.getString("name"),
                    rsUser.getString("email"),
                    rsUser.getString("password"),
                    rsUser.getString("phone"),
                    rsUser.getString("role"),
                    rsUser.getString("s_id")
            );
            uModel.setProfile_image(rsUser.getString("profile_image"));
            totalUserList.add(uModel);
            userCount++;
        }

        rsUser.close();
        psUser.close();
        connection.closeCon();
        adminSession.setTotalUsers(userCount);
        return totalUserList;
    }


    public ObservableList<matchModel> displayMatchesByAdmin() throws Exception {
        int matchCount = 0;
        ObservableList<matchModel> list = FXCollections.observableArrayList();
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement(
                "SELECT " +
                        "m.score, m.status, m.match_id, m.match_date, " +
                        "lost.item AS lost_item_name, " +
                        "lost.location AS lost_location, " +
                        "lost.contact AS lost_contact, " +
                        "lost.image_path AS lost_image_path, " +
                        "found.item AS found_item_name, " +
                        "found.location AS found_location, " +
                        "found.contact AS found_contact, " +
                        "found.image_path AS found_image_path, " +
                        "lu.s_id AS lost_student_id, " +
                        "fu.s_id AS found_student_id " +
                        "FROM lost_found_db.matches m " +
                        "JOIN items lost ON m.lost_item_id = lost.item_id " +
                        "JOIN items found ON m.found_item_id = found.item_id " +
                        "JOIN users lu ON lost.user_id = lu.user_id " +
                        "JOIN users fu ON found.user_id = fu.user_id"
        );
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            matchModel match = new matchModel();
            match.setScore(rs.getInt("score"));
            match.setStatus(rs.getString("status"));
            match.setMatchId(rs.getInt("match_id"));
            match.setMatchDate(rs.getString("match_date"));
            match.setLostItemName(rs.getString("lost_item_name"));
            match.setLostLocation(rs.getString("lost_location"));
            match.setLostContact(rs.getString("lost_contact"));
            match.setLostImagePath(rs.getString("lost_image_path"));    // ← add kiya
            match.setFoundItemName(rs.getString("found_item_name"));
            match.setFoundLocation(rs.getString("found_location"));
            match.setFoundContact(rs.getString("found_contact"));
            match.setFoundImagePath(rs.getString("found_image_path")); // ← add kiya
            match.setLost_student_Id(rs.getString("lost_student_id"));
            match.setFound_student_Id(rs.getString("found_student_id"));
            list.add(match);
            matchCount++;
        }
        adminSession.setTotalMatches(matchCount);
        rs.close();
        ps.close();
        connection.closeCon();
        return list;
    }

    public int statusUpdate(int id, String status) throws Exception {
        connection.setUpCon();

        // 1. Items update
        PreparedStatement ps1 = connection.getCon().prepareStatement(
                "UPDATE items SET status = ? " +
                        "WHERE item_id IN (" +
                        "SELECT lost_item_id FROM matches WHERE match_id = ? " +
                        "UNION " +
                        "SELECT found_item_id FROM matches WHERE match_id = ?)"
        );
        ps1.setString(1, status);
        ps1.setInt(2, id);
        ps1.setInt(3, id);
        ps1.executeUpdate();
        ps1.close();

        // 2. Matches update
        PreparedStatement ps2 = connection.getCon().prepareStatement(
                "UPDATE lost_found_db.matches SET status = ? WHERE match_id = ?"
        );
        ps2.setString(1, status);
        ps2.setInt(2, id);
        int value = ps2.executeUpdate();
        ps2.close();

        connection.closeCon();
        return value;
    }

    public int deleteItemByAdmin(itemModel model) throws Exception {
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement(
                "DELETE FROM lost_found_db.matches WHERE lost_item_id = ? OR found_item_id = ?"
        );
        ps.setInt(1, model.getItem_id());
        ps.setInt(2, model.getItem_id());
        ps.executeUpdate();
        ps.close();

        PreparedStatement ps2 = connection.getCon().prepareStatement(
                "DELETE FROM lost_found_db.items WHERE item_id = ?"
        );
        ps2.setInt(1, model.getItem_id());
        int value = ps2.executeUpdate();
        ps2.close();
        connection.closeCon();
        return value;
    }

    public int updateUserRole(int userId, String role) throws Exception {
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement(
                "UPDATE lost_found_db.users SET role = ? WHERE user_id = ?"
        );
        ps.setString(1, role);
        ps.setInt(2, userId);
        int value = ps.executeUpdate();
        ps.close();
        connection.closeCon();
        return value;
    }

    public int deleteUser(usersModel model) throws Exception {
        connection.setUpCon();
        try {
            // 1. Matches delete karo
            PreparedStatement p0 = connection.getCon().prepareStatement(
                    "DELETE FROM lost_found_db.matches " +
                            "WHERE lost_item_id IN (SELECT item_id FROM lost_found_db.items WHERE user_id = ?) " +
                            "OR found_item_id IN (SELECT item_id FROM lost_found_db.items WHERE user_id = ?)"
            );
            p0.setInt(1, model.getUser_id());
            p0.setInt(2, model.getUser_id());
            p0.executeUpdate();
            p0.close();

            // 2. Items delete karo
            PreparedStatement p1 = connection.getCon().prepareStatement(
                    "DELETE FROM lost_found_db.items WHERE user_id = ?"
            );
            p1.setInt(1, model.getUser_id());
            p1.executeUpdate();
            p1.close();

            // 3. User delete karo
            PreparedStatement p2 = connection.getCon().prepareStatement(
                    "DELETE FROM lost_found_db.users WHERE user_id = ?"
            );
            p2.setInt(1, model.getUser_id());
            int value = p2.executeUpdate();
            p2.close();
            connection.closeCon();
            return value;

        } catch (Exception e) {
            connection.closeCon();
            throw e;
        }
    }

    public int deleteMatchAdmin(matchModel model) throws Exception {
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement(
                "DELETE FROM lost_found_db.matches WHERE match_id = ?"
        );
        ps.setInt(1, model.getMatchId());

        int value = ps.executeUpdate();
        if (value>0){
            PreparedStatement ps1 = connection.getCon().prepareStatement(
                    "UPDATE items SET status = 'Pending' " +
                            "WHERE item_id IN (" +
                            "SELECT lost_item_id FROM matches WHERE match_id = ? " +
                            "UNION " +
                            "SELECT found_item_id FROM matches WHERE match_id = ?)"
            );

            ps1.setInt(1, model.getMatchId());
            ps1.setInt(2, model.getMatchId());
            ps1.executeUpdate();
        }
        ps.close();
        connection.closeCon();
        return value;
    }
    public int saveChanges()throws Exception{
        connection.setUpCon();
        int value = 0;
        PreparedStatement ps = connection.getCon().prepareStatement("UPDATE users SET name=?, email=?, phone=?, password=?, profile_image=? WHERE user_id=?");
        ps.setString(1, adminSession.getAdminName());
        ps.setString(2, adminSession.getEmail());
        String phoneStr = adminSession.getPhone();

        // Phone Validation
        if (phoneStr != null && !phoneStr.isEmpty()) {

            // Digits only validation
            if (!phoneStr.matches("\\d+")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Phone number must contain digits only.");
                alert.showAndWait();

                ps.close();
                connection.closeCon();
                return 0;
            }

            // Optional length validation
            if (phoneStr.length() < 11 || phoneStr.length() > 15) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Phone number length is invalid.");
                alert.showAndWait();

                ps.close();
                connection.closeCon();
                return 0;
            }

            // Store as String to preserve leading zero
            ps.setString(3, phoneStr);

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Field");
            alert.setHeaderText(null);
            alert.setContentText("Phone number cannot be empty.");
            alert.showAndWait();

            ps.close();
            connection.closeCon();
            return 0;
        }
        ps.setString(4, adminSession.getPassword());
        ps.setString(5, adminSession.getProfileImagePath());
        ps.setString(6 , Integer.toString(adminSession.getAdminId()));
        try {
            value = ps.executeUpdate();
            ps.close();
            connection.closeCon();
            return value;

        }catch (SQLIntegrityConstraintViolationException e){
            ps.close();
            connection.closeCon();
            return -1;
        }


    }


}