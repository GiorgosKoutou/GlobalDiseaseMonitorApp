# Global Disease Monitoring and Analysis System 🌍🦠

**University Project** – IVT AKMI

## 📌 Project Overview

This is a Java Swing desktop application for monitoring and analyzing the spread of diseases across the world using a MySQL database. The system provides role-based login, interactive map visualization, dynamic statistics filtering, and data management tools.

## 📌 Features

### 👤 User Authentication
Supports three user roles:
- **Admin**: Full access to all features, including user and data management
- **Analyst**: Can view and analyze data
- **User**: Can view limited statistical data

> 💡 Default login credentials for testing:
>
> - **Admin** → Username: `admin` | Password: `admin`  
> - **Analyst** → Username: `analyst` | Password: `analyst`  
> - **User** → Username: `user` | Password: `user`

### 🗺️ Interactive World Map
- Visualizes countries with markers based on disease data
- Displays tooltips showing number of cases, deaths, and recoveries
- Real-time updates when hovering or filtering data

### 📊 Statistics Dashboard
- Filter by:
  - Disease (via `JComboBox`)
  - Country (via `JTextField`)
  - Time period (via `JDatePicker`)
- Live update of statistics table and graphs

### 🛠️ Admin Tools
- Add, edit, and delete records for:
  - Diseases
  - Countries
  - Disease case reports
- Import bulk data via CSV
- Manage user accounts and roles

### 📄 Reports
- Users can submit comments related to disease reports
- Optional export to CSV

---

## 🗃️ Database Schema (MySQL)

The application uses the following tables:

- `diseases`: `id`, `name`, `description`, `discovery_date`
- `countries`: `id`, `name`, `continent`, `population`
- `disease_cases`: `id`, `disease_id`, `country_id`, `cases`, `deaths`, `recoveries`, `report_date`
- `users`: `id`, `username`, `password`, `role`
- `reports`: `id`, `user_id`, `disease_id`, `country_id`, `comments`, `report_date`

---

## 🛠️ Database Setup

To create the required MySQL tables, use the script included in the `DataBaseSchema` folder.

### Steps:

1. Open your MySQL client (e.g. MySQL Workbench)
2. Navigate to the provided file:
3. Execute the script to generate the necessary tables.

---

## ▶️ How to Run

> ⚠️ This is a desktop application meant to run inside an IDE such as IntelliJ IDEA or NetBeans.

### Requirements:
- Java 8 or newer
- MySQL Server
- JDBC MySQL Connector (`mysql-connector-java-x.x.x.jar`)

### Running the app:
1. Open the project in your IDE
2. Ensure the database is running and configured
3. Update the JDBC connection parameters in `DataBaseConnection` class inside the `DiseaseDb` folder.
4. Run `Main.java`


---

## 📚 Author

**Georgios Koutourinis**  
IVT AKMI – Semester B6  
Department: Software Engineer  
Academic Year: 2024–2025

---

## 📎 License

This project is for academic purposes only and not intended for commercial use.


