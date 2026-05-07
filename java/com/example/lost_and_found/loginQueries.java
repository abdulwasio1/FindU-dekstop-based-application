package com.example.lost_and_found;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import java.sql.Connection;

import java.sql.*;
import java.util.ArrayList;

public class loginQueries {
    private dbConnection connection = new dbConnection();

    public boolean loginCheck(usersModel model) throws Exception {
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement("SELECT  user_id, s_id , name, email , password,  phone , role, profile_image FROM lost_found_db.users where BINARY email = ?  AND BINARY password = ?");
        ps.setString(1 , model.getEmail());
        ps.setString(2 ,model.getPassword());
        ResultSet rs = ps.executeQuery();
       if (rs.next()){
           if (rs.getString("role").equalsIgnoreCase("user")){
               Session.setUser(rs.getInt("user_id") , rs.getString("s_id") , rs.getString("name") , rs.getString("email") , rs.getString("password") , rs.getString("phone") , rs.getString("role") , rs.getString("profile_image"));
               displayMatches(Session.getUserId());
           }else {
               Session.setRole("admin");
//               adminSession.setAdmin(rs.getInt("user_id") , rs.getString("name") ,rs.getString("email") , rs.getString("password") , rs.getString("phone") , rs.getString("role"));
           queriesAdmin admin = new queriesAdmin();
           admin.loginCheck(model);
           }
           ps.close();
           connection.closeCon();
           return true;

       }
       ps.close();
       connection.closeCon();
       return false;
    }

    public int addUser(usersModel model) throws Exception{
        connection.setUpCon();
        try {
            PreparedStatement ps = connection.getCon().prepareStatement("Insert into users (name , s_id , phone , email , password )" +
                    "values (? , ? , ? , ? , ?)");
            ps.setString(1 , model.getName());
            ps.setString(2 , model.getS_id());
            ps.setString(3 , model.getPhone());
            ps.setString(4 , model.getEmail());
            ps.setString(5 , model.getPassword());
            int value = ps.executeUpdate();
            ps.close();
            connection.closeCon();
            return value;

        }catch (SQLIntegrityConstraintViolationException e){
            Alert alert  = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Email or Student Id already registered");
            alert.showAndWait();
            return 0;
        }catch (SQLException e){
            Alert alert  = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Email  format (e.g., alice@iba-suk.edu.pk)");
            alert.showAndWait();
            return 0;
        }


    }
    public int insertItem(itemModel model  , int userId)throws Exception{
        connection.setUpCon();
       try {
           PreparedStatement ps = connection.getCon().prepareStatement("insert into lost_found_db.items (user_id , item ,  category , location , type , image_path , date_lost_found , contact , description) values (? ,  ?, ? , ?,  ?, ? , str_to_date(? , '%Y-%m-%d') , ? , ?)" , Statement.RETURN_GENERATED_KEYS);
           ps.setString(1, Integer.toString(Session.getUserId()));
           ps.setString(2, model.getItem());
           ps.setString(3, model.getCategory());
           ps.setString(4, model.getLocation());
           ps.setString(5, model.getType());
           ps.setString(6, model.getImage_path());
           ps.setString(7 ,  model.getDate_lost_found());
           ps.setString(8 , model.getContact());
           ps.setString(9 , model.getDescription());

           int value = ps.executeUpdate(); // record insert ho gaya, ID bhi generate ho gai database mein

           ResultSet rs = ps.getGeneratedKeys(); //yeh special ResultSet hai sirf generated keys ya default values set by the DB return karta hai as ResultSet
           if (rs.next()) {
               int item_id = rs.getInt(1); // column 1 = generated ID - Give me the value from column number 1 of this ResultSet.
               model.setItem_id(item_id);
           }
           System.out.println(model.getItem_id());
           ps.close();
           connection.closeCon();
           return value;
       }catch (SQLIntegrityConstraintViolationException e){
           Alert alert  = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error!");
           alert.setHeaderText(null);
           alert.setContentText("Required fields must be filled");
           alert.showAndWait();
       }
       return 0;

    }



//    public ObservableList showValues(int userId)throws Exception{
//        ObservableList list = FXCollections.observableArrayList();
//        connection.setUpCon();
//        PreparedStatement ps = connection.getCon().prepareStatement("Select item ,  category , location , type , status  , image_path from lost_found_db.items where user_id = ?");
//        ps.setInt(1 , userId);
//        ResultSet rs = ps.executeQuery();
//        itemModel model;
//        int itemCount = 0;
//        int lostCount = 0 ;
//        int foundCount = 0;
//        int  claimCount = 0;
//        while (rs.next()){
//            model = new itemModel(rs.getString("item") , rs.getString("category"), rs.getString("location"), rs.getString("type") ,  rs.getString("status") , rs.getString("image_path"));
//            list.add(model);
//            if (rs.getString("type").equals("lost")){
//                lostCount++;
//                Session.setLostCount(lostCount);
//            }else {
//                foundCount++;
//                Session.setFoundCount(foundCount);
//            }
//            if (rs.getString("status").equals("verified")){
//                claimCount++;
//                Session.setTotalClaim(claimCount);
//            }
//            System.out.println(lostCount + " " + foundCount + " " + claimCount);
//            itemCount = foundCount + lostCount;
//        }
//        System.out.println(itemCount);
//        Session.setItemCount(itemCount);
//        ps.close();
//        rs.close();
//        connection.closeCon();
//        return list;
//
//
//    }

    public ObservableList showValues(int userId)throws Exception{
        ObservableList list = FXCollections.observableArrayList();
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement("Select item_id , item ,  category , location , type , status  , image_path , date_lost_found from lost_found_db.items where user_id = ?" );
        ps.setInt(1 , userId);
        ResultSet rs = ps.executeQuery();
        itemModel model;
        int itemCount = 0;
        int lostCount = 0 ;
        int foundCount = 0;
        int  claimCount = 0;
        while (rs.next()){
            model = new itemModel(rs.getInt("item_id"), rs.getString("item") , rs.getString("category"), rs.getString("location"), rs.getString("type") ,  rs.getString("status") , rs.getString("image_path") , rs.getString("date_lost_found"));
            list.add(model);
            if (rs.getString("type").equalsIgnoreCase("lost")){
                lostCount++;
            }else if (rs.getString("type").equalsIgnoreCase("found")){
                foundCount++;
            }
            if (rs.getString("status").equalsIgnoreCase("verified") || rs.getString("status").equalsIgnoreCase("claimed") ){
                claimCount++;

            }

            System.out.println(lostCount + " " + foundCount + " " + claimCount);
            itemCount = foundCount + lostCount;
        }
        Session.setTotalClaim(claimCount);
        Session.setFoundCount(foundCount);
        Session.setLostCount(lostCount);
        Session.setItemCount(itemCount);

        System.out.println(itemCount);
        ps.close();
        rs.close();
        connection.closeCon();
        return list;


    }
    public int saveChanges()throws Exception{
        connection.setUpCon();
        int value = 0;
        PreparedStatement ps = connection.getCon().prepareStatement("Update lost_found_db.users SET name =?,  s_id = ? , email  = ?  , phone  = ? , password = ?  where user_id = ?");
        ps.setString(1, Session.getUserName());
        ps.setString(2, Session.getS_id());
        ps.setString(3, Session.getEmail());
        String phoneStr = Session.getPhone();
        try {
            if (phoneStr != null && !phoneStr.isEmpty()) {
                ps.setLong(4, Long.parseLong(phoneStr));
            } else {
                ps.setNull(4, java.sql.Types.BIGINT);
            }
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Field Formats");
            alert.showAndWait();
        }
        ps.setString(5, Session.getPassword());
        ps.setString(6 , Integer.toString(Session.getUserId()));
        try {
            value = ps.executeUpdate();
            ps.close();
           connection.closeCon();
           System.out.println(value);
           return value;

       }catch (SQLIntegrityConstraintViolationException e){
            ps.close();
            connection.closeCon();
           return -1;
       }
//       catch (SQLException e){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText(null);
//            alert.setContentText("Invalid Field Formats");
//            alert.showAndWait();
//            ps.close();
//            connection.closeCon();
//            return 0;
//        }

    }
    public int saveProfileImage(String imageName, int userId) throws Exception {
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement(
                "UPDATE lost_found_db.users SET profile_image = ? WHERE user_id = ?"
        );
        ps.setString(1, imageName);
        ps.setInt(2, userId);
        int value = ps.executeUpdate();
        ps.close();
        connection.closeCon();
        return value;
    }
    public boolean deleteAccount(int user_id) throws Exception{
        connection.setUpCon();
        PreparedStatement psItems = connection.getCon().prepareStatement("DELETE FROM lost_found_db.items WHERE user_id = ?");
        psItems.setString(1 , Integer.toString(user_id));
        int itemValue = psItems.executeUpdate();
        PreparedStatement psUsers = connection.getCon().prepareStatement("Delete from lost_found_db.users where user_id = ?;");
        psUsers.setString(1 , Integer.toString(user_id));
        int userValue = psUsers.executeUpdate();
        Session.clear();
        /*
         //yaha par or operator isi waja sa use kia ha for example agr ak user ha but us userid pa kio
         item hi nhi ha to ItemValue to zero return kare ga but ya possibility bilkul
         valid ha to agar ma and operator use krta to ya if condition chlte hi nhi  ha infact
         ya possibility valid so isi waja sa mane OR opertor use kia ha ab dekho ab asa to possible
         hi nhi ha ka item exist krta ho or userid nhi ku ka userid foreign key ki tarh
         use hue ha to ab uski reverse condition possible ha ak user exits krta ho or use
         userid pa item exist kre ya na kre condition is valid
         */
        if (userValue >0  || itemValue>0){
            return true;
        }
        return false;
    }

    public ArrayList<itemModel> getRemainingItems(String type) throws Exception {
        ArrayList list = new ArrayList<itemModel>();
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement("Select * from lost_found_db.items where type = ? ");
        ps.setString(1 , type);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            itemModel item  = new itemModel(rs.getInt(1), rs.getInt(2) , rs. getString(3) , rs. getString(4) ,  rs. getString(5) ,  rs. getString(6) ,  rs. getString(7) ,  rs. getString(8) ,  rs. getString(9) ,  rs. getString(10) ,  rs. getString(11)   );
            list.add(item);
        }
        return list;
    }

    public void addMatchItem(int score, itemModel item1, itemModel item2, String type) throws Exception {
        connection.setUpCon();

        // ✅ Pehle check karo — duplicate toh nahi?
        PreparedStatement check = connection.getCon().prepareStatement(
                "SELECT match_id FROM lost_found_db.matches " +
                        "WHERE (lost_item_id = ? AND found_item_id = ?) " +
                        "OR   (lost_item_id = ? AND found_item_id = ?)"
        );

        int id1 = item1.getItem_id();
        int id2 = item2.getItem_id();

        check.setInt(1, id1); check.setInt(2, id2);
        check.setInt(3, id2); check.setInt(4, id1);

        ResultSet rs = check.executeQuery();

        if (rs.next()) {
            // ✅ Already exists — kuch mat karo
            System.out.println("Match already exists, skipping.");
            check.close();
            connection.closeCon();
            return;
        }
        check.close();

        // ✅ Naya match insert karo
        PreparedStatement ps;
        if (type.equalsIgnoreCase("found")) {
            ps = connection.getCon().prepareStatement(
                    "INSERT INTO lost_found_db.matches (lost_item_id, found_item_id, score) VALUES (?, ?, ?)"
            );
            ps.setInt(1, item1.getItem_id());
            ps.setInt(2, item2.getItem_id());
            ps.setInt(3, score);
        } else {
            ps = connection.getCon().prepareStatement(
                    "INSERT INTO lost_found_db.matches (found_item_id, lost_item_id, score) VALUES (?, ?, ?)"
            );
            ps.setInt(1, item1.getItem_id());
            ps.setInt(2, item2.getItem_id());
            ps.setInt(3, score);
        }
        ps.executeUpdate();
        ps.close();
        connection.closeCon();
    }

    public ObservableList<matchModel> displayMatches(int currentUserId) throws Exception {
        ObservableList list = FXCollections.observableArrayList();
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement(
                "SELECT m.score, m.status, m.match_id, m.match_date, " +
                        "lost.item AS lost_item_name, " +
                        "lost.location AS lost_location, " +
                        "lost.contact AS lost_contact, " +
                        "lost.image_path AS lost_image_path, " +   // ← add kiya
                        "found.item AS found_item_name, " +
                        "found.location AS found_location, " +
                        "found.contact AS found_contact, " +
                        "found.image_path AS found_image_path " +   // ← add kiya
                        "FROM lost_found_db.matches m " +
                        "JOIN items lost ON m.lost_item_id = lost.item_id " +
                        "JOIN items found ON m.found_item_id = found.item_id " +
                        "WHERE lost.user_id = ? OR found.user_id = ?"
        );
        ps.setInt(1 ,currentUserId);
        ps.setInt(2 ,currentUserId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            matchModel match = new matchModel();
            match.setScore(rs.getInt("score"));
            match.setStatus(rs.getString("status"));
            match.setMatchId(rs.getInt("match_id"));
            match.setMatchDate(rs.getString("match_date"));
            match.setLostItemName(rs.getString("lost_item_name"));
            match.setLostLocation(rs.getString("lost_location"));
            match.setLostContact(rs.getString("lost_contact"));
            match.setLostImagePath(rs.getString("lost_image_path"));    // ← add karo
            match.setFoundItemName(rs.getString("found_item_name"));
            match.setFoundLocation(rs.getString("found_location"));
            match.setFoundContact(rs.getString("found_contact"));
            match.setFoundImagePath(rs.getString("found_image_path")); // ← add karo
            list.add(match);

        }
        ps.close();
        connection.closeCon();
        return list;
    }
    public ObservableList<itemModel> displaySearch(String searchType , String searchFieldText , int userId) throws Exception{
        ObservableList<itemModel> list = FXCollections.observableArrayList();
        connection.setUpCon();
        PreparedStatement ps = null;
        if (searchType.equals("category")){
            ps = connection.getCon().prepareStatement("Select item ,  category , location , type , status  , image_path ,date_lost_found from lost_found_db.items where user_id = ? and category = ?");
            ps.setInt(1 , userId);
            ps.setString(2,searchFieldText.toLowerCase());
        }else if (searchType.equals("location")){
            ps = connection.getCon().prepareStatement("Select item ,  category , location , type , status  , image_path,date_lost_found from lost_found_db.items where user_id = ? and location = ?");
            ps.setInt(1 , userId);
            ps.setString(2,searchFieldText.toLowerCase() );
        } else if (searchType.equals("status")) {
            ps = connection.getCon().prepareStatement("Select item ,  category , location , type , status  , image_path,date_lost_found from lost_found_db.items where user_id = ? and status = ?");
            ps.setInt(1 , userId);
            ps.setString(2,searchFieldText.toLowerCase() );
        } else if (searchType.equals("type")) {
            ps = connection.getCon().prepareStatement("Select item ,  category , location , type , status  , image_path , date_lost_found from lost_found_db.items where user_id = ? and type = ?");
            ps.setInt(1 , userId);
            ps.setString(2,searchFieldText.toLowerCase() );
        } else if (searchType.equals("All")) {
            ps = connection.getCon().prepareStatement("Select item ,  category , location , type , status  , image_path,date_lost_found from lost_found_db.items where user_id = ?");
            ps.setInt(1 , userId);
        }
        ResultSet rs = ps.executeQuery();
        itemModel model;
        while (rs.next()){
            model = new itemModel(rs.getString("item") , rs.getString("category"), rs.getString("location"), rs.getString("type") ,  rs.getString("status") , rs.getString("image_path") , rs.getString("date_lost_found"));
            list.add(model);
        }
        return list;
    }
    public int deleteItem(itemModel model)throws Exception{
        connection.setUpCon();
        PreparedStatement ps = connection.getCon().prepareStatement("Delete from lost_found_db.matches where lost_item_id = ? or found_item_id = ?");
        ps.setInt(1, model.getItem_id());
        ps.setInt(2, model.getItem_id());
        ps.executeUpdate();
        PreparedStatement ps2 = connection.getCon().prepareStatement("DELETE FROM lost_found_db.items where item_id= ?");
        ps2.setInt(1, model.getItem_id());
        int value = ps2.executeUpdate();
        return value;

    }



}
