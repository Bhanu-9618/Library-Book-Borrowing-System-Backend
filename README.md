# 📚 Lumina Library - Enterprise Backend

A robust, enterprise-grade Spring Boot backend designed to automate library operations, including inventory management, borrowing workflows, and automated fine calculations.

---

## Implemented Features

### 1. Advanced Security & Authentication
* **JWT Authentication:** Secure stateless authentication using JSON Web Tokens.
* **Role-Based Access Control (RBAC):** Distinct permissions for `ADMIN` and `USER` roles.
* **Password Security:** Industry-standard password hashing using `BCrypt`.
* **Secure Sign-up:** New users are automatically assigned roles and verified through the system.

### 2. Comprehensive Book Management
* **Full CRUD:** Create, Read, Update, and Delete books.
* **Real-time Inventory:** Automatically tracks `availableCopies` and toggles book availability status.
* **Paginated Search:** Efficient retrieval of book records with advanced search filters.

### 3. Core Borrowing Logic
* **Request Workflow:** Users can request books; admins approve and issue them.
* **Status Tracking:** Precise tracking through `REQUESTED`, `ISSUED`, `RETURNED`, and `OVERDUE` states.
* **Automated Date Handling:** System automatically sets 14-day due dates upon issuance and logs return dates.

### 4. Automated Fine Management
* **Dynamic Calculation:** System automatically detects late returns and calculates fines at a rate of Rs. 50.00 per day.
* **Payment Integration Logic:** Admins can mark fines as `PAID`, which automatically triggers inventory restock and status updates.
* **Fine History:** Detailed tracking of paid/unpaid penalties per user.

### 5. Automated Background Tasks (Schedulers)
* **Daily Fine Updates:** A background cron job runs every midnight to identify overdue books and increment fines.
* **Email Reminders:** Automated email service that sends return reminders to users 24 hours before their book is due.
* **Notification System:** Sends "Welcome" emails to new users and "Fine Notifications" upon late returns.

### 6. Professional-Grade Quality Assurance
* **76% Test Coverage:** Verified through `JUnit 5` and `Mockito`.
* **Performance Optimized:** Paginated API responses to ensure fast performance even with thousands of records.
* **Soft Delete:** Implemented soft-deletion for users to maintain historical data integrity.

---

## 🛠 Tech Stack

| Technology / Tool | Description |
| :--- | :--- |
| **Framework** | Spring Boot 3.4.1 |
| **Language** | Java 17+ |
| **Security** | Spring Security 6 (with JWT) |
| **Database** | Spring Data JPA |
| **Testing** | JUnit 5, Mockito, MockMvc, JaCoCo |
| **Utilities** | ModelMapper, Lombok, JavaMailSender |
