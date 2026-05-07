package com.example.lost_and_found;

public class itemModel {

        private int item_id;
        private int user_id;
        private String item;
        private String category;
        private String description;
        private String location;
        private String date_lost_found;
        private String type;
        private String status;
        private String contact;
        private String image_path;

    public itemModel(String item, String category, String location,  String type, String status, String image_path , String date_lost_found , String contact , String description) {
        this.item = item;
        this.category = category;
        this.location = location;
        this.type = type;
        this.status = status;
        this.image_path = image_path;
        this.date_lost_found = date_lost_found;
        this.contact = contact;
        this.description = description;
    }

    public itemModel(int item_id, int user_id, String item, String category, String description, String location, String date_lost_found, String type, String status, String contact, String image_path) {
        this.item_id = item_id;
        this.user_id = user_id;
        this.item = item;
        this.category = category;
        this.description = description;
        this.location = location;
        this.date_lost_found = date_lost_found;
        this.type = type;
        this.status = status;
        this.contact = contact;
        this.image_path = image_path;
    }

    public itemModel(String item, String category, String location, String type, String status, String image_path ) {
        this.item = item;
        this.category = category;
        this.location = location;
        this.type = type;
        this.status = status;
        this.image_path = image_path;
    }

    public itemModel(String item, String category, String location, String type, String status, String imagePath, String dateLostFound) {
        this.item_id = item_id;
        this.user_id = user_id;
        this.item = item;
        this.category = category;
        this.location = location;
        this.date_lost_found = dateLostFound;
        this.type = type;
        this.status = status;
        this.image_path = imagePath;
    }

    public itemModel(int itemId, String item, String category, String location, String type, String status, String imagePath, String dateLostFound) {
        this.item = item;
        this.item_id  = itemId ;
        this.category = category;
        this.location = location;
        this.date_lost_found = dateLostFound;
        this.type = type;
        this.status = status;
        this.image_path = imagePath;

    }

    public itemModel(int user_id , int itemId, String item, String category, String location, String type, String status, String imagePath, String dateLostFound) {
        this.user_id = user_id;
        this.item = item;
        this.item_id  = itemId ;
        this.category = category;
        this.location = location;
        this.date_lost_found = dateLostFound;
        this.type = type;
        this.status = status;
        this.image_path = imagePath;

    }
    // Getters and Setters

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDate_lost_found() {
            return date_lost_found;
        }

        public void setDate_lost_found(String date_lost_found) {
            this.date_lost_found = date_lost_found;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

}
