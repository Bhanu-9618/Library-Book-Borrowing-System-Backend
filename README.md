# 📚 Enterprise Book Borrowing System

A robust, fully automated, and enterprise-grade Library Management System built with **Spring Boot 4** and **Java 21**. This system provides a comprehensive API for managing library inventories, processing smart book borrowing workflows, and handling automated background tasks like fine calculations and email notifications.

---

## ✨ Key Features

### 🔐 Security & Access Control
* **JWT Authentication:** Highly secure, stateless API authentication.
* **Role-Based Access Control (RBAC):** Distinct `ADMIN` and `USER` privileges.
* **Auto-Initialization:** Automatically provisions a default Admin account (`admin@gmail.com`) upon initial system startup.

### 📖 Smart Inventory Management
* **Full CRUD Operations:** Seamlessly add, view, update, and delete books.
* **Advanced Pagination & Search:** Search the catalog by title, author, or category with optimized paginated responses.
* **Real-time Stock Tracking:** Automatically adjusts `availableCopies` and updates book status to "unavailable" when out of stock.

### 🔄 Intelligent Borrowing Workflow
* **State Machine Logic:** Books follow a strict `REQUESTED` ➔ `ISSUED` ➔ `RETURNED` lifecycle.
* **Automated Due Dates:** Automatically calculates and assigns a 14-day borrowing period upon admin issuance.
* **Personalized History:** Users can view their own borrowing history, while Admins can monitor the global ledger.

### 🤖 Background Automations (Schedulers)
* **Nightly Fine Processor:** A `@Scheduled` Cron job wakes up daily at Midnight (12:00 AM) to identify overdue books and automatically apply a late fine of Rs. 50/day.
* **Automated Email Reminders:** A secondary Cron job runs at 8:00 AM daily, emailing users who have books due the following day.
* **Welcome Notifications:** Automatically dispatches a welcome email to newly registered users.

### ⚙️ Technical Highlights
* **JPA Auditing:** Automatically tracks `createdAt` and `updatedAt` timestamps for all database records.
* **DTO Mapping:** Clean data transfer objects powered by `ModelMapper`.
* **Global Exception Handling:** Custom `@RestControllerAdvice` for standardizing API error responses.
* **Docker Ready:** Includes a `Dockerfile` for seamless containerized deployment.

---

## 🛠️ Technology Stack

* **Core:** Java 21, Spring Boot 3.x
* **Security:** Spring Security, JSON Web Tokens (JWT)
* **Database:** MySQL, Spring Data JPA, Hibernate
* **Mail Service:** Spring Boot Starter Mail (JavaMailSender)
* **Documentation:** Swagger UI
* **Utilities:** Lombok, ModelMapper
