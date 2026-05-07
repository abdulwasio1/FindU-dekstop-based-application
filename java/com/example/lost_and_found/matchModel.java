package com.example.lost_and_found;

public class matchModel {

        private int matchId;
        private int lostItemId;
        private int foundItemId;
        private String matchDate;
        private int score;
        private String lostItemName;
        private String foundItemName;
        private String lostLocation;
        private String foundLocation;
        private String lostContact;
        private String foundContact;
        private String status;
        private String lost_student_Id;
        private String found_student_Id;
        private String lostImagePath;   // ← add kiya
        private String foundImagePath;  // ← add kiya

        public matchModel() {}

        public matchModel(int lostItemId, int foundItemId) {
                this.lostItemId  = lostItemId;
                this.foundItemId = foundItemId;
        }

        public String getFound_student_Id() { return found_student_Id; }
        public void setFound_student_Id(String found_student_Id) { this.found_student_Id = found_student_Id; }

        public int getMatchId() { return matchId; }
        public void setMatchId(int matchId) { this.matchId = matchId; }

        public int getLostItemId() { return lostItemId; }
        public void setLostItemId(int lostItemId) { this.lostItemId = lostItemId; }

        public int getFoundItemId() { return foundItemId; }
        public void setFoundItemId(int foundItemId) { this.foundItemId = foundItemId; }

        public String getMatchDate() { return matchDate; }
        public void setMatchDate(String matchDate) { this.matchDate = matchDate; }

        public String getLostItemName() { return lostItemName; }
        public void setLostItemName(String lostItemName) { this.lostItemName = lostItemName; }

        public String getFoundItemName() { return foundItemName; }
        public void setFoundItemName(String foundItemName) { this.foundItemName = foundItemName; }

        public String getLostLocation() { return lostLocation; }
        public void setLostLocation(String lostLocation) { this.lostLocation = lostLocation; }

        public String getFoundLocation() { return foundLocation; }
        public void setFoundLocation(String foundLocation) { this.foundLocation = foundLocation; }

        public String getLostContact() { return lostContact; }
        public void setLostContact(String lostContact) { this.lostContact = lostContact; }

        public String getFoundContact() { return foundContact; }
        public void setFoundContact(String foundContact) { this.foundContact = foundContact; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }

        public String getLost_student_Id() { return lost_student_Id; }
        public void setLost_student_Id(String lost_student_Id) { this.lost_student_Id = lost_student_Id; }

        // ← yeh add kiye
        public String getLostImagePath() { return lostImagePath; }
        public void setLostImagePath(String lostImagePath) { this.lostImagePath = lostImagePath; }

        public String getFoundImagePath() { return foundImagePath; }
        public void setFoundImagePath(String foundImagePath) { this.foundImagePath = foundImagePath; }
}