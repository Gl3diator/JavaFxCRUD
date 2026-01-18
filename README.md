# StudentCRUD (JavaFX + MariaDB)



A simple **CRUD desktop application** built with **JavaFX (Scene Builder)** and **MariaDB (XAMPP)** using a clean **DAO + JDBC** architecture.


![Application Screenshot](App.png)
---

## ✨ Features

- ✅ Display students in a `TableView`
- ✅ Add / Update / Delete a student
- ✅ Refresh table from database
- ✅ Click a row → form auto-fills
- ✅ Modular JavaFX project (`module-info.java`)
- ✅ DAO layer (clean separation UI ↔ DB)

---

## 🧰 Tech Stack

- **Java**: JDK 21+
- **JavaFX**: 21.0.6
- **Build Tool**: Maven
- **Database**: MariaDB (XAMPP)
- **JDBC Driver**: MariaDB Connector/J
- **UI Builder**: Scene Builder

---

## 📁 Project Structure

```

src/main/java/com/esprit/studentcrud/
├── controller/        # JavaFX controllers
├── dao/               # DAO interface + implementation
├── model/             # Entity classes (Student)
├── util/              # DBConnection
├── MainApp.java       # JavaFX Application entry
└── Launcher.java      # main() entry

src/main/resources/com/esprit/studentcrud/
└── students-view.fxml # JavaFX view (Scene Builder)

````

---

## ✅ Prerequisites

- Java JDK installed
- Maven installed (or IntelliJ Maven support)
- **XAMPP** installed and MySQL/MariaDB service running

---

## 🗄️ Database Setup

### 1️⃣ Start XAMPP
- Open XAMPP Control Panel
- Start **MySQL**

### 2️⃣ Create database and table

```sql
CREATE DATABASE IF NOT EXISTS school;
USE school;

CREATE TABLE IF NOT EXISTS students (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  email VARCHAR(120) NOT NULL
);
````

(Optional) Insert test data:

```sql
INSERT INTO students(name, age, email) VALUES
('Ghaith', 26, 'Ghaith@mail.com'),
('hamma', 22, 'hamma@mail.com');
```

---

## 🔌 Database Connection Configuration

Edit this file if your DB credentials change:

```
src/main/java/com/esprit/studentcrud/util/DBConnection.java
```

```java
private static final String URL = "jdbc:mariadb://localhost:3306/school";
private static final String USER = "root";
private static final String PASSWORD = "";
```

---

## ▶️ Run the Application

### Option A — IntelliJ

Run:

* `Launcher.java`

### Option B — Maven

If `javafx-maven-plugin` is configured:

```bash
mvn clean javafx:run
```

---

## 🧠 Architecture Overview

**FXML View → Controller → DAO → Database**

* `students-view.fxml` defines the UI
* `StudentController` handles user actions and UI logic
* `StudentDAOImpl` contains SQL queries
* `DBConnection` centralizes JDBC access

---

## 🧪 Common Issues

### ❌ “No controller specified”

Ensure the FXML root contains:

```xml
fx:controller="com.esprit.studentcrud.controller.StudentController"
```

### ❌ Table shows rows but cells are empty

* Ensure `cellValueFactory` is set in the controller
* Ensure `Student` getters exist (`getName()`, `getAge()`, etc.)

---

## 👤 Author

* **Ghaith** — ESPRIT

