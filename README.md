# 📚 Lumina Library - Enterprise Backend

A robust, enterprise-grade Library Management System built with **Spring Boot** and **Java 21**. This system handles complex library workflows, automated fine calculations, and real-time notifications with a focus on scalability and security.

---

## 🚀 Live Deployment
* **Backend:** Successfully hosted on **Render**
* **Frontend:** Seamlessly deployed on **Vercel**
* **Database:** Managed via **Aiven (PostgreSQL/MySQL)**

---

## ✨ Key Features

### 🔐 Security & Access Control
* **JWT Authentication:** Stateless and secure API access.
* **Role-Based Access Control (RBAC):** Distinct permissions for `ADMIN` and `USER`.
* **Auto-Provisioning:** Automatically creates a default Admin account on initial startup.

### 📖 Inventory & Borrowing
* **Smart Catalog:** Advanced search and pagination for books, authors, and categories.
* **Real-time Tracking:** Automated stock management and availability updates.
* **Lifecycle Management:** Intelligent book state machine (`REQUESTED` ➔ `ISSUED` ➔ `RETURNED`).

### 🤖 Automated Background Tasks
* **Fine Engine:** Daily scheduled job to calculate overdue fines (Rs. 50/day).
* **Smart Notifications:** Automated email reminders for due dates and welcome emails for new users.
* **Audit Logging:** JPA auditing to track every record’s creation and modification.

---

## 🛠️ Technology Stack

* **Framework:** Java 21, Spring Boot 3.x
* **Security:** Spring Security, JWT
* **Persistence:** Spring Data JPA, Hibernate
* **Database:** Managed Cloud Database (Aiven)
* **API Docs:** Swagger UI / OpenAPI
* **Utilities:** Lombok, ModelMapper, Docker
