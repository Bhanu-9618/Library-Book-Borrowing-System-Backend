# 📚 LibManager - Backend (Staff Portal)

LibManager Backend is a robust RESTful API built with **Spring Boot** and **MySQL**. It handles the core business logic for book inventory, user memberships, and the circulation desk (borrowing/returning logic).

## 🚀 Tech Stack
* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Database:** MySQL
* **ORM:** Spring Data JPA / Hibernate
* **Tools:** Postman (API Testing), IntelliJ IDEA, Maven

## ✨ Core Features
- **Book Management:** Full CRUD (Create, Read, Update, Delete) and availability tracking.
- **User Management:** Member registration and profile management.
- **Borrowing Logic:** - Real-time validation (checks if User and Book IDs exist).
  - Availability enforcement (prevents borrowing "Out of Stock" books).
  - Status tracking (`BORROWED` vs `RETURNED`).
- **Data Integrity:** Enforced database relationships between Books, Users, and Borrow Records.

## 🛠️ Database Schema
The system maintains three primary entities:
1.  **Books:** `id, title, author, category, availableCopies, availability`
2.  **Users:** `id, name, email, phone, address, membershipdate`
3.  **BorrowRecords:** `borrowid, userid, bookid, borrowdate, dueDate, returnDate, status`

## 🔌 API Endpoints (Quick Reference)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/book/all` | Retrieve all books |
| `POST` | `/book/save` | Add a new book |
| `GET` | `/book/id/{id}` | Search book by ID |
| `POST` | `/borrow/save` | Create a new rent record |
| `GET` | `/borrow/history` | Get all transaction logs |
