# VSP Farm

## Overview

VSP Farm is a management system designed to handle customer billing, item management, and loan reports. It has two main user roles: **Admin** and **Cashier**, each with their specific functionalities.

## Functionalities

### Admin
The Admin has full control over users, customers, and items. The Admin functionalities include:

1. **Create User (Admin/Cashier)**
    - Admin can create new users with Admin or Cashier roles.

2. **Create Customer**
    - Only customers registered by the Admin are eligible for discounts and can opt to pay later.
    - Default customers do not receive any discount.

3. **Manage Items and Sub-items**
    - Admin can create, edit, or delete items and their associated sub-items.

4. **View Bill and Loan Reports**
    - Admin can view detailed reports of all bills and loans.

5. **Delete Bills and Users**
    - Admin has the authority to delete bills and users from the system.

### Cashier
The Cashier primarily handles billing and loan payments. The Cashier functionalities include:

1. **Generate Bills for Items**
    - Cashier can generate bills for the items being purchased by customers.

2. **View and Delete Current Day Bills**
    - Cashier can view the bills generated on the current day and has the option to delete them.
    - **Note:** When a bill is deleted, it is not removed from the database. Instead, the status of the bill is updated to **DELETED**.

3. **Process Loan Payments**
    - Cashier can process loan payments from customers.

---

## Installation

1. Clone the repository:
   ```bash
   https://github.com/karththikeyanK/VSP_Farm.git
   ```


### ScreenShots

<p align="center">
  <img src="screenshots%2Flogin_page.jpeg" alt="login_page.jpeg">
  <br>
  <em>Login Page</em>
</p>


// welcome@1234

