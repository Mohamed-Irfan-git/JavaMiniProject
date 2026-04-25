<div align="center">

<br/>

```
██╗     ███╗   ███╗███████╗
██║     ████╗ ████║██╔════╝
██║     ██╔████╔██║███████╗
██║     ██║╚██╔╝██║╚════██║
███████╗██║ ╚═╝ ██║███████║
╚══════╝╚═╝     ╚═╝╚══════╝
```

# Faculty Learning Management System

**A production-grade academic platform built on Java Sockets + JavaFX**

<br/>

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-UI_Layer-2d6a9f?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Socket](https://img.shields.io/badge/Socket-Networking-22c55e?style=for-the-badge&logo=socket.io&logoColor=white)

<br/>

> *Real academic rules. Real eligibility logic. Real-world architecture.*

<br/>

</div>

---

## Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Feature Breakdown](#-feature-breakdown)
- [Academic Logic](#-academic-logic)
- [Database Schema](#-database-schema)
- [Backend Commands](#-backend-commands)
- [Setup & Installation](#-setup--installation)
- [Authentication Flow](#-authentication-flow)
- [Avatar System](#-avatar-system)
- [Roadmap](#-roadmap)
- [Author](#-author)

---

## 🎯 Overview

The **Faculty LMS** is a full-stack desktop application that replicates real-world academic management workflows — from course registration gating to eligibility enforcement and medical overrides. Built entirely without a web framework, it uses raw **Java TCP Sockets** for client-server communication, a **Command Pattern** backend, and a **JavaFX** GUI layer.

| Layer      | Technology            |
|------------|-----------------------|
| Frontend   | JavaFX                |
| Transport  | Java Socket (TCP/JSON)|
| Backend    | Java — Command Pattern|
| Database   | MySQL                 |
| Auth       | JWT Tokens            |

---

## 🏗 Architecture

```
┌─────────────────────────────────────┐
│           JavaFX Client             │
│  (Controllers → SocketService)      │
└──────────────┬──────────────────────┘
               │  JSON over TCP Socket
┌──────────────▼──────────────────────┐
│         MultiServer.java            │
│  CommandRouter → Command Handler    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│           Service Layer             │
│  (Business Logic + Eligibility)     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│             DAO Layer               │
│  (PreparedStatements + ResultSets)  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│           MySQL Database            │
└─────────────────────────────────────┘
```

Every client request is a **JSON command object** dispatched through the Command Pattern — clean, extensible, and easy to unit-test.

---

## 🧩 Feature Breakdown

### 👨‍💼 Admin Panel
- Open / close course registration periods by department and academic level
- Manage all users (create, update, deactivate)
- Post and manage notices

### 👨‍🏫 Lecturer Panel
- View assigned courses for current semester
- Upload final exam marks
- Manage CA (Continuous Assessment) scores
- Check and review student eligibility
- Dashboard with key statistics
- Update designation and profile picture

### 👨‍🎓 Student Panel
- Register for courses during active registration periods
- View SGPA, CGPA, and attendance on dashboard
- Check exam results
- View personal eligibility status

---

## 📐 Academic Logic

This system implements the actual eligibility and registration rules used in academic institutions — not a simplified mock.

### Course Registration Flow

```
Admin opens registration_period
        │
        ▼
Student registers courses
(blocked if period is closed)
        │
        ▼
course_registration becomes single source of truth
for all eligibility, result, and reporting logic
```

### Registration Types

| Type      | Meaning                              |
|-----------|--------------------------------------|
| `Proper`  | First-time attempt at the course     |
| `Repeat`  | Retaking a previously failed course  |
| `Suspend` | Student temporarily inactive         |

### Eligibility Rules

| Registration Type | Attendance Required | CA Required | Eligible? |
|-------------------|---------------------|-------------|-----------|
| `Proper`          | ≥ 80%               | ≥ 50%       | ✅ Both must pass |
| `Repeat`          | Not checked         | ≥ 50%       | ✅ CA only |
| `Suspend`         | —                   | —           | ❌ Never eligible |

### Medical Handling

| Medical Type        | System Effect                         |
|---------------------|---------------------------------------|
| Attendance Medical  | Compensating attendance hours added   |
| Exam Medical        | Result recorded as `WH` (Withheld)    |

---

## 🗄 Database Schema

Key tables and their primary columns:

```sql
-- Core user store
users (
  user_id, username, email, password,
  contact_number, profile_picture, role
)

-- Lecturer-specific data
lecturers (
  user_id, specialization, designation
)

-- Student enrollments per term
course_registration (
  student_id, course_id, academic_year,
  semester, registration_type
)

-- Controls when students can register
registration_period (
  department_id, academic_level, semester,
  academic_year, start_at, end_at, status
)
```

> **Rule:** Always filter queries by `academic_year` + `semester`. Never query without temporal scope.

---

## 🔌 Backend Commands

Commands are registered in the `CommandRouter` and dispatched by name from the client.

| Command                   | Description                              |
|---------------------------|------------------------------------------|
| `LOGIN`                   | Authenticate and return JWT token        |
| `GET_LECTURER_PROFILE`    | Fetch lecturer details by user ID        |
| `UPDATE_LECTURER_PROFILE` | Update designation and profile picture   |
| `REGISTER_COURSE`         | Enroll student in a course               |
| `GET_ELIGIBILITY`         | Evaluate and return eligibility status   |
| `GET_DASHBOARD_STATS`     | Aggregate stats for dashboard view       |
| `UPLOAD_MARKS`            | Submit final marks for a course          |

Adding new functionality = adding one new `Command` implementation. Zero changes to routing logic.

---

## ⚙️ Setup & Installation

### Prerequisites

- Java 17+
- JavaFX SDK
- MySQL 8.0+
- IDE: IntelliJ IDEA or Eclipse

---

### 1 — Clone the Repository

```bash
git clone https://github.com/Mohamed-Irfan-git/lms-project.git
cd lms-project
```

### 2 — Configure the Database

```sql
CREATE DATABASE lms_db;
-- Then import the provided schema:
-- database/schema.sql
```

### 3 — Update DB Connection

Edit your database config file:

```java
// src/config/DBConfig.java
String URL  = "jdbc:mysql://localhost:3306/lms_db";
String USER = "root";
String PASS = "your_password";
```

### 4 — Start the Backend Server

```bash
# Run from your IDE or:
javac MultiServer.java && java MultiServer
```

Server listens on `localhost:5000` by default.

### 5 — Launch the Frontend

Run the JavaFX application from your IDE with the JavaFX SDK configured in the module path.

---

## 🔐 Authentication Flow

```
Client sends LOGIN command
         │
         ▼
Server validates credentials against DB
         │
         ▼
JWT token generated (signed with secret key)
         │
         ▼
Token returned → stored in SessionManager
         │
         ▼
All subsequent requests include token in header
         │
         ▼
Server validates token on every command
```

Tokens are stateless — no server-side session storage required.

---

## 🖼 Avatar System

Profile images are displayed as circular avatars. If no image is set, the user's initial is rendered as a styled fallback.

```java
File file = new File(profilePicturePath);

if (file.exists()) {
    // Load and clip the real profile image
    avatarImage.setImage(new Image(file.toURI().toString()));
    avatarImage.setClip(new Circle(centerX, centerY, radius));
    avatarImage.setVisible(true);
    avatarInitial.setVisible(false);
} else {
    // Show styled initial fallback
    avatarInitial.setText(String.valueOf(username.charAt(0)).toUpperCase());
    avatarInitial.setVisible(true);
    avatarImage.setVisible(false);
}
```

---

## 🗺 Roadmap

| Feature                         | Status      |
|---------------------------------|-------------|
| Core LMS (marks, eligibility)   | ✅ Complete  |
| JWT Authentication              | ✅ Complete  |
| Profile picture + avatar system | ✅ Complete  |
| In-app notification system      | 🔲 Planned  |
| Assignment file uploads         | 🔲 Planned  |
| Real-time updates via WebSocket | 🔲 Planned  |
| Flutter mobile companion app    | 🔲 Planned  |

---

## 👨‍💻 Author

**Mohamed Irfan**
BICT Undergraduate · Java Developer

[![GitHub](https://img.shields.io/badge/GitHub-Mohamed--Irfan--git-181717?style=for-the-badge&logo=github)](https://github.com/Mohamed-Irfan-git)

---

<div align="center">

*Built with real academic rules, not mock logic.*

</div>
