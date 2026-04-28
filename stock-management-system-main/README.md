# Stock Management System

This repository contains the cumulative project submission for the Stock Management System. The project includes a JavaFX desktop inventory application and a Spring Boot web dashboard for reporting.

## Modules

- **Stock_Management_System** - desktop inventory operations
- **stock-report-web** - web-based analytics dashboard

## Key Improvements Added

- Search and low-stock filtering in the desktop product view
- Stronger product input validation before records are saved
- Clearer README and testing workflow for instructors and teammates
- Password hashing support for dashboard accounts
- Basic authentication guard on report API endpoints
- Uploaded database validation for required tables
- Team testing and improvement documentation included in the repository

## Desktop Application

Folder: `Stock_Management_System`

### Main functions

- Add products
- View all products as cards
- Search by product name or ID
- Filter only low-stock products
- Sell products
- Restock products
- Delete products
- Generate inventory and profit reports
- Reset the demo database

### Requirements

- Java 17
- Maven
- JavaFX-compatible environment

### Run

```bash
mvn clean javafx:run
```

## Web Dashboard

Folder: `stock-report-web`

### Main functions

- Login and registration
- Upload a desktop-generated `.db` file
- Validate required report tables before dashboard use
- View summary cards, sales tables, and charts
- Logout and clear selected dashboard database

### Requirements

- Java 17
- Maven

### Run

```bash
mvn spring-boot:run
```

Then open `http://localhost:8080`

### Default dashboard login

- username: `admin`
- password: `admin123`

## Recommended Demo Workflow

1. Run the desktop application.
2. Add products or use existing records.
3. Perform a few sell and restock actions.
4. Open the report window and confirm totals.
5. Run the web dashboard.
6. Log in and upload the generated `.db` file.
7. Review summary cards, trend chart, and top-product chart.

## Repository Documents

- `PROJECT_IMPROVEMENTS.md` - summary of all changes made in this improved version
- `TEAM_TEST_CHECKLIST.md` - step-by-step testing guide for teammates

## Notes

- The desktop module remains the main system of record.
- The web module is intended for report visualization.
- Runtime-generated database files are not required in the source submission.
