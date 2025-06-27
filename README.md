# 🛒 EasyShop E-Commerce Application


A comprehensive Java-based backend Spring Boot API that simulates a fully functional online shopping experience. I was tasked with identifying and fixing critical bugs in the existing codebase to ensure proper functionality across all features. Through systematic debugging and testing, I resolved issues related to data handling, user input validation, and application flow control. The application features role-based access with separate interfaces for customers and administrators.

---

## 🎯 Features

- 🏪 Product Catalog Browsing
  
Browse products organized by categories with detailed information including pricingand descriptions

- 🔍 Product Search & Filtering
  
Search products by color, category, or price range with filtering options for refined results.

- 🛍️ Shopping Cart Management
  
Add, remove, and modify items in your cart with real-time quantity calculations. 

- 👤 Unique Profile login

Username and password authentication to access personalized shopping experience that allows you to store and update personal details including name, contact information, and preferences

---
### PHASE 1: Shopping Cart Controller
---
![Screenshot 2025-06-27 070708](https://github.com/user-attachments/assets/070f2ee4-716c-4d28-a55e-cfd42f875524)

---
### PHASE 2: Bug Fixes

---

![Bug 1](https://github.com/user-attachments/assets/aba19ed3-ef3b-4600-80ad-dd5841c3a225)

Bug 1 (Duplicate Products)

![Bug 1 Fix](https://github.com/user-attachments/assets/a5528f64-8e44-45e4-b8ba-17e8a4fe1aa6)

Bug 1 Fix

![Bug 2 (Price)](https://github.com/user-attachments/assets/ceca21dd-80ca-4780-8422-173c5e2e6c67)

Bug 2 (Price Logic)

![Bug 2 Fix](https://github.com/user-attachments/assets/a4441e86-11d6-4491-910a-fe7d6389e984)

Bug 2 Fix

---
### 🧠 Code I learned the Most From
---

![Screenshot 2025-06-27 073212](https://github.com/user-attachments/assets/2ad25c6a-d9e7-4512-98a6-dd4651d43895)

This snippet from my Shopping Cart Controller taught me the importance of layered error handling and security in REST API development. The method demonstrates how Spring Boot annotations work together to create a secure, robust endpoint that handles cart quantity updates.
What made this particularly valuable was learning about Spring Security's @PreAuthorize annotation for role-based access control, ensuring only authenticated users can modify cart contents. The Principal parameter automatically provides the logged-in user's information, eliminating the need for manual session management.
The multi-layered exception handling was eye-opening - catching specific IllegalArgumentException for business logic violations (negative quantities) and returning appropriate HTTP status codes, while also having a catch-all for unexpected errors. This taught me how proper error handling in REST APIs provides clear feedback to frontend developers and creates a better user experience by returning meaningful HTTP status codes rather than generic server errors.




---
