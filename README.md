# Spring Boot Application

A Spring Boot REST API application with CRUD operations for managing posts.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Lombok plugin for your IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
  - IntelliJ IDEA: Install Lombok plugin from Settings > Plugins
  - Eclipse: Download lombok.jar and run it to install
  - VS Code: Install the Lombok Annotations Support extension

## Getting Started

### Build the project

```bash
./mvnw clean install
```

### Run the application

```bash
./mvnw spring-boot:run
```

Or run directly with Java:

```bash
java -jar target/spring-boot-app-0.0.1-SNAPSHOT.jar
```

### Access the application

Once the application is running, you can access it at:

- Base URL: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`

## API Endpoints

### Post Management APIs

#### 1. Create a Post
**POST** `/api/posts`

Create a new post with title and content.

**Request Body:**
```json
{
  "title": "My First Post",
  "content": "This is the content of my first post."
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "title": "My First Post",
  "content": "This is the content of my first post.",
  "createdAt": "2025-11-27T10:30:00",
  "updatedAt": "2025-11-27T10:30:00"
}
```

**Example using cURL:**
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"title":"My First Post","content":"This is the content of my first post."}'
```

#### 2. Update a Post
**PUT** `/api/posts/{id}`

Update an existing post by ID. **Supports partial updates** - you can update only title, only content, or both fields.

**Request Body (Update both fields):**
```json
{
  "title": "Updated Post Title",
  "content": "Updated content for the post."
}
```

**Request Body (Update only title):**
```json
{
  "title": "Updated Post Title"
}
```

**Request Body (Update only content):**
```json
{
  "content": "Updated content for the post."
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Updated Post Title",
  "content": "Updated content for the post.",
  "createdAt": "2025-11-27T10:30:00",
  "updatedAt": "2025-11-27T11:45:00"
}
```

**Example using cURL:**
```bash
# Update both fields
curl -X PUT http://localhost:8080/api/posts/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Post Title","content":"Updated content for the post."}'

# Update only title
curl -X PUT http://localhost:8080/api/posts/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"New Title Only"}'

# Update only content
curl -X PUT http://localhost:8080/api/posts/1 \
  -H "Content-Type: application/json" \
  -d '{"content":"New content only"}'
```

#### 3. Get Post by ID
**GET** `/api/posts/{id}`

Retrieve a specific post by its ID.

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "My First Post",
  "content": "This is the content of my first post.",
  "createdAt": "2025-11-27T10:30:00",
  "updatedAt": "2025-11-27T10:30:00"
}
```

**Example using cURL:**
```bash
curl http://localhost:8080/api/posts/1
```

#### 4. Get All Posts Sorted by Timeline
**GET** `/api/posts/timeline`

Retrieve all posts sorted by creation date (newest first).

**Response:** `200 OK`
```json
[
  {
    "id": 3,
    "title": "Latest Post",
    "content": "This is the most recent post.",
    "createdAt": "2025-11-27T12:00:00",
    "updatedAt": "2025-11-27T12:00:00"
  },
  {
    "id": 2,
    "title": "Second Post",
    "content": "This is the second post.",
    "createdAt": "2025-11-27T11:00:00",
    "updatedAt": "2025-11-27T11:00:00"
  },
  {
    "id": 1,
    "title": "First Post",
    "content": "This is the first post.",
    "createdAt": "2025-11-27T10:30:00",
    "updatedAt": "2025-11-27T10:30:00"
  }
]
```

**Example using cURL:**
```bash
curl http://localhost:8080/api/posts/timeline
```

#### 5. Get All Posts
**GET** `/api/posts`

Retrieve all posts (unsorted).

**Response:** `200 OK` - Returns array of all posts

**Example using cURL:**
```bash
curl http://localhost:8080/api/posts
```

#### 6. Delete a Post
**DELETE** `/api/posts/{id}`

Delete a specific post by ID.

**Response:** `200 OK`
```json
{
  "message": "Post deleted successfully"
}
```

**Example using cURL:**
```bash
curl -X DELETE http://localhost:8080/api/posts/1
```

## Project Structure

```
spring-boot-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/springbootapp/
│   │   │       ├── SpringBootAppApplication.java
│   │   │       ├── controller/
│   │   │       │   ├── HelloController.java
│   │   │       │   └── PostController.java
│   │   │       ├── model/
│   │   │       │   └── Post.java
│   │   │       ├── repository/
│   │   │       │   └── PostRepository.java
│   │   │       └── service/
│   │   │           └── PostService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/example/springbootapp/
│               └── SpringBootAppApplicationTests.java
├── pom.xml
└── README.md
```

## Technologies Used

- Spring Boot 2.7.18
- Spring Web
- Spring Data JPA
- Spring Validation
- Lombok
- H2 Database (In-Memory)
- Spring DevTools
- Maven
- Java 11

## Database

The application uses H2 in-memory database for development and testing purposes. You can access the H2 console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

## Validation Rules

Posts must meet the following requirements:
- **Title**: Required, 3-100 characters
- **Content**: Required, 10-5000 characters

## Error Handling

The API returns appropriate HTTP status codes:
- `200 OK` - Successful GET, PUT, DELETE operations
- `201 Created` - Successful POST operation
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error


