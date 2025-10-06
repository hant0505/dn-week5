# Spring Boot + Keycloak OAuth2 Demo

This project demonstrates integration of **Spring Boot** with **Keycloak** using **OAuth2 / OpenID Connect**, including:
- User authentication via Keycloak
- Realm roles (e.g., `NICE`)
- Automatic **Refresh Token** support
---

## **Truy cập vào app http://localhost:8081/**

- Điều hướng tới Keycloak để sign in
![alt text](<Screenshot 2025-10-06 113152.png>)
- Điền thông tin yêu cầu về mail, họ tên
![alt text](<Screenshot 2025-10-06 112917.png>)
- Đăng nhập thành công, xác thực ROLE: NICE nên chuyển hướng tới nice.html
![alt text](<Screenshot 2025-10-06 113141.png>)
---

## **REFRESH TOKEN**
- Kéo dài session của user mà không cần login lại
- Đăng nhập vào admin console, kích hoạt 
![alt text](image.png)

--> Mở tab mới vẫn còn ở trạng thái login

