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
- ✅ WhatsApp notification when a student is added 📲
- ✅ Email notification sent to the student on registration 📧
- ✅ Email & duplicate validation (DB + UI)
- ✅ Status feedback inside the UI (success / error messages)

---

## 🧰 Tech Stack

- **Java**: JDK 21+
- **JavaFX**: 21.0.6
- **Build Tool**: Maven
- **Database**: MariaDB (XAMPP)
- **JDBC Driver**: MariaDB Connector/J
- **UI Builder**: Scene Builder
- **Messaging API**: CallMeBot (WhatsApp)
- **Email API**: EmailJS

---

## 📁 Project Structure

```

src/main/java/com/esprit/studentcrud/
├── controller/        # JavaFX controllers
├── dao/               # DAO interface + implementation
├── model/             # Entity classes (Student)
├── util/              # DBConnection, WhatsAppService, EmailJsService
├── MainApp.java       # JavaFX Application entry
└── Launcher.java      # main() entry

src/main/resources/com/esprit/studentcrud/
├── students-view.fxml # JavaFX view (Scene Builder)
├── style.css          # Application styling

````

---

## 🗄️ Database Setup

### Start XAMPP
Start **MySQL / MariaDB** from XAMPP Control Panel.

### Create database and table

```sql
CREATE DATABASE IF NOT EXISTS school;
USE school;

CREATE TABLE IF NOT EXISTS students (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE
);
````

---

## 🧠 Architecture Overview

```
FXML → Controller → DAO → Database
                        ↳ WhatsApp API
                        ↳ EmailJS API
```

* Clean separation of concerns
* External APIs isolated in `util`
* Easily extensible (login, roles, SMTP, REST…)

---

## 📲 WhatsApp API Integration (CallMeBot)

![CallMeBot Logo](callmebot.png)

A WhatsApp message is sent automatically when a new student is added.

### Phone Activation

1. Add this number to contacts:
   **+34 623 76 13 63**

2. Send this WhatsApp message:

   ```
   I allow callmebot to send me messages
   ```

3. Receive your API key.

---

## 📧 Email Notification (EmailJS)

![EmailJS Logo](EmailJS.png)

When a student is successfully added, a **confirmation email** is sent to the student.

### EmailJS Setup

1. Create an account at [https://www.emailjs.com](https://www.emailjs.com)
2. Create:

   * Email Service
   * Email Template
3. Template variables:

   * `{{student_name}}`
   * `{{student_age}}`
   * `{{student_email}}`

---

## 🔐 Environment Variables (.env)

Sensitive credentials are stored in a `.env` file and **ignored by Git**.

```env
EMAILJS_SERVICE_ID=your_service_id
EMAILJS_TEMPLATE_ID=your_template_id
EMAILJS_PUBLIC_KEY=your_public_key

WHATSAPP_PHONE=216XXXXXXXX
WHATSAPP_API_KEY=XXXXXXXX
```

> `.env` is listed in `.gitignore` to prevent credential leaks.

---

## ▶️ Run the Application

```bash
mvn clean javafx:run
```

---

## 🧪 Common Issues

### Table shows rows but cells are empty

* Ensure `cellValueFactory` is set
* Ensure getters exist in `Student`

### Update/Delete buttons disabled

* Enabled only when a row is selected

### Email not sent

* Check EmailJS credentials
* Verify template variable names

---

## 👤 Author

**Ghaith — ESPRIT**

---

## 🔥 Future Improvements

* In-app notifications
* Search & filters
* Export data (CSV / PDF)
