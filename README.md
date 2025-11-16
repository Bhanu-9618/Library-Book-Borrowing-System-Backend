# 📚 Library Book Borrowing System Backend

## Project Description

The Library Book Borrowing System is a fully functional backend application designed to manage a library's books, users, and borrowing records efficiently. This system allows administrators to perform CRUD operations on books and users, track borrowing and returning of books, and enforce validations to ensure data integrity. By leveraging Spring Boot for the backend, MySQL for the database, and Postman for API testing, the system provides a robust and scalable solution for library management. It maintains proper relationships between entities such as books, users, and borrow records, ensuring that only valid books can be borrowed by registered users. The application is ideal for learning enterprise backend development and creating production-ready REST APIs with Spring Boot.

---

## Tech Stack

- **Backend Framework:** Spring Boot  
- **Database:** MySQL  
- **ORM:** Spring Data JPA / Hibernate  
- **API Testing:** Postman  
- **IDE:** IntelliJ IDEA  
- **Programming Language:** Java 17+  

---

## Features

- **Book Management**
  - Add, update, delete, and search books.
  - Track availability status.
  
- **User Management**
  - Add new library users.
  - Retrieve and manage user details.
  
- **Borrowing Management**
  - Record borrowing and returning of books.
  - Validate existence of book and user before creating borrow records.
  - Track borrow date, due date, return date, and status ("BORROWED" / "RETURNED").
  
- **Validations**
  - Prevent borrowing if book does not exist or is unavailable.
  - Prevent borrowing if user does not exist.
