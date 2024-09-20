# Bank Card Searching

This web application allows users to enter a bank card number and retrieve information about it, such as BIN, alpha code, and bank name.

## Features

- Input bank card number
- Display card information
- Error handling for invalid input

## Technology Stack

- **Frontend:**
  - HTML
  - CSS
  - JavaScript

- **Backend:**
  - Java 22
  - Spring Boot
  - PostgreSQL
    
 - **Testing:**
   - Postman

## Dependencies

### Backend Dependencies
- `spring-boot-starter-data-jpa`: Spring Data JPA for database interaction
- `postgresql`: PostgreSQL JDBC driver (runtime)
- `hibernate-validator`: Bean validation for Hibernate
- `spring-boot-starter-web`: Starter for building web applications
- `lombok`: Reduces boilerplate code (optional)
- `gson`: For JSON serialization and deserialization
- `spring-boot-starter-thymeleaf`: Thymeleaf templating engine
- `spring-boot-starter-test`: Testing framework for Spring Boot applications

## File Handling

The application generates file paths based on configuration properties defined in the `application.properties` file. This includes paths for:

- Base folder
- JSON files
- ZIP files
- Online catalog URI

The paths are constructed using system-dependent file separators to ensure compatibility across different operating systems.

## Installation

1. Clone the repository or download the files.
2. Open the `search.html` file in your browser.
3. Ensure the API server is running at `http://localhost:8089`.
4. Make sure PostgreSQL is installed and a database is set up according to the application requirements.

## Usage

1. Enter the card number in the text field.
2. Click the "Search" button.
3. The results will be displayed in the table below.

## Example

![image](https://github.com/user-attachments/assets/d3f6811f-7317-4134-bbda-f30625c4e80a)


## Notes

- Ensure that your API server supports the POST method for the `/api/v1/card` endpoint.
- You can use fake card numbers for testing if your API allows it.

## Useful links
Zip handling: https://www.baeldung.com/java-compress-and-uncompres

Gsoup parsing: https://github.com/google/gson/blob/main/UserGuide.md
