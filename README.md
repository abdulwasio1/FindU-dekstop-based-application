<div align="center">

# 🔍 FindU — Lost & Found Management System

A full-stack desktop application that helps communities report, track, and recover lost items through an intelligent automated matching engine.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.13-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

[Demo Video](#demo) · [Features](#features) · [How It Works](#how-the-matching-works) · [Getting Started](#getting-started)

</div>

---

## What is FindU?

FindU is a desktop application built with Java and JavaFX that solves a simple but frustrating problem — when people lose something, there is no easy way to check if someone else has already found and reported it.

FindU gives users a personal dashboard to report lost or found items with photos, descriptions, and location details. The moment a report is submitted, a **custom scoring algorithm runs in the background**, comparing the new item against every opposite-type report in the database and surfacing potential matches ranked by confidence score.

Administrators get a separate panel to oversee all items, manage claims, review matches, and handle user accounts.

---

## Features

### User Side
- Register and log in with a personal account
- Report a **lost** or **found** item with name, category, location, date, description, and photo
- View **auto-generated matches** with a percentage confidence score and progress bar
- Browse all found items in a searchable table
- View and manage your own submitted reports
- Personal profile management

### Admin Side
- Full dashboard with system-wide item and match statistics
- View, search, and delete all items across all users
- Review all matches and manage their status
- User management — view, edit, and remove accounts
- Match view with detailed score breakdown per pair

---

## How the Matching Works

This is the core feature of FindU. Every time a user submits a report, the system:

**1. Fetches all items of the opposite type from the database**
> Lost report submitted → fetch all found items. Found report submitted → fetch all lost items.

**2. Pre-filters to reduce unnecessary comparisons**
> Only items in the same category proceed to scoring.

**3. Scores each candidate pair out of 100**

| Factor | Weight | Method |
|--------|--------|--------|
| Item name similarity | 40 pts | Exact match = 40, partial = 25, common words × 10 |
| Category match | 25 pts | Exact match only |
| Location similarity | 20 pts | Exact = 20, partial = 10 |
| Date proximity | 10 pts | Same day = 10, within 2 days = 5, 60+ days = −15 |
| Description similarity | variable | Common meaningful words × 5 (stop words filtered) |

**4. Saves any pair scoring ≥ 50 as a match**
> The match is stored in the database and immediately visible to the user on their matches screen.

**5. Prevents duplicate matches**
> Before inserting, the system checks if a match between those two item IDs already exists.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| UI Framework | JavaFX 21.0.6 + FXML |
| Database | MySQL 8.0 |
| DB Driver | MySQL Connector/J (`com.mysql.cj.jdbc.Driver`) |
| Build Tool | Maven 3.13 |
| Image Storage | Local filesystem (`~/FindU/uploads/`) |

---

## Project Structure

```
src/main/java/com/example/lost_and_found/
│
├── Models
│   ├── itemModel.java          # Item entity (name, category, location, date, image...)
│   ├── matchModel.java         # Match entity (item pair + score)
│   └── usersModel.java         # User entity
│
├── Database
│   ├── dbConnection.java       # MySQL connection setup
│   ├── loginQueries.java       # All user-side queries
│   └── queriesAdmin.java       # All admin-side queries
│
├── Session
│   ├── Session.java            # Logged-in user state
│   └── adminSession.java       # Logged-in admin state
│
├── Controllers — User
│   ├── loginController.java    # Login & registration
│   ├── regController.java      # Sign up form
│   ├── userDashboardControl.java  # User home (base controller)
│   ├── reportController.java   # Report form + match trigger
│   ├── matchControl.java       # View my matches
│   ├── userViewControl.java    # View my items
│   ├── matchViewControl.java   # Match detail view
│   └── profileController.java  # User profile
│
└── Controllers — Admin
    ├── adminDashboardControl.java  # Admin home
    ├── adminDisplayItems.java      # All items management
    ├── adminMatchControl.java      # All matches management
    ├── adminProfileControl.java    # Admin profile
    ├── manageUsersAdmin.java       # User management
    ├── matchViewAdminControl.java  # Match detail (admin)
    └── allItemDisplayControl.java  # Item browser
```

---

## Getting Started

### Prerequisites
- Java JDK 21+
- JavaFX SDK 21+
- MySQL 8.0+
- Maven 3.8+

### 1. Clone the repository
```bash
git clone https://github.com/username/FindU.git
cd FindU
```

### 2. Set up the database
```sql
CREATE DATABASE lost_found_db;
```
Then import the schema:
```bash
mysql -u root -p lost_found_db < schema.sql
```

### 3. Configure your DB credentials
Open `dbConnection.java` and update:
```java
con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/lost_found_db",
    "your_username",
    "your_password"
);
```

### 4. Run the app
```bash
./mvnw clean javafx:run
```
On Windows:
```bash
mvnw.cmd clean javafx:run
```

> Uploaded item images are saved automatically to `~/FindU/uploads/` on first run.

---

## Demo

[Watch on YouTube](https://www.youtube.com/watch?v=your-link-here)

---

## Known Limitations

- Desktop only — no web or mobile version yet
- Images stored locally, not in the cloud
- Matching is text-based — no AI/vector embeddings yet
- No email notifications when a match is found

---

## Roadmap

- [ ] REST API backend (Spring Boot)
- [ ] Web frontend (React)
- [ ] Email/SMS notifications on match found
- [ ] Cloud image storage (AWS S3)
- [ ] Semantic matching using sentence embeddings
- [ ] Mobile app (Android/iOS)

---

## Author

**Abdul Wasio**
[GitHub](https://github.com/abdulwasio1) · [LinkedIn](https://www.linkedin.com/in/abdul-wasio-15750327a/)

---

## License

This project is licensed under the MIT License.
