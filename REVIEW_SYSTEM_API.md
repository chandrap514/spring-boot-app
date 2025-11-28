# Review System API Documentation

## Overview
This document describes the Review System implementation with complete CRUD operations for managing product reviews.

## Data Model

### Review Entity
| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier (auto-generated) |
| title | String | Review title (required) |
| content | String | Review content (required, max 2000 chars) |
| userId | Long | ID of the user who created the review (required) |
| rating | Integer | Rating value (required, 1-5) |
| moderation | ModerationStatus | Review moderation status (APPROVED/REJECTED/POSTED) |
| productId | Integer | ID of the product being reviewed (required) |
| createdAt | Timestamp | Timestamp when review was created (auto-generated) |
| updatedAt | Timestamp | Timestamp when review was last updated (auto-updated) |

### ModerationStatus Enum
- `POSTED` - Default status when a review is created
- `APPROVED` - Review has been approved by moderator
- `REJECTED` - Review has been rejected by moderator

## API Endpoints

### 1. Get Reviews
Retrieve reviews for a specific product with pagination support.

**Endpoint:** `GET /api/reviews`

**Query Parameters:**
- `productId` (required) - Integer - The product ID to filter reviews
- `page` (optional) - Integer - Page number (default: 0)
- `limit` (optional) - Integer - Number of results per page (default: 10)

**Headers:**
- `userId` (optional) - Long - User ID for potential future filtering

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "title": "Great product!",
    "content": "This product exceeded my expectations...",
    "userId": 123,
    "rating": 5,
    "moderation": "POSTED",
    "productId": 456,
    "createdAt": "2025-11-28T12:00:00.000+00:00",
    "updatedAt": "2025-11-28T12:00:00.000+00:00"
  }
]
```

**Example Request:**
```bash
curl -X GET "http://localhost:8081/api/reviews?productId=456&page=0&limit=10" \
  -H "userId: 123"
```

---

### 2. Create Review
Create a new review for a product.

**Endpoint:** `POST /api/reviews`

**Headers:**
- `userId` (required) - Long - ID of the user creating the review
- `Content-Type: application/json`

**Request Body:**
```json
{
  "title": "Great product!",
  "content": "This product exceeded my expectations. Highly recommended!",
  "rating": 5,
  "productId": 456
}
```

**Validation Rules:**
- `title` - Required, not blank
- `content` - Required, not blank
- `rating` - Required, must be between 1 and 5
- `productId` - Required

**Response:** 201 Created
```json
{
  "id": 1,
  "title": "Great product!",
  "content": "This product exceeded my expectations. Highly recommended!",
  "userId": 123,
  "rating": 5,
  "moderation": "POSTED",
  "productId": 456,
  "createdAt": "2025-11-28T12:00:00.000+00:00",
  "updatedAt": "2025-11-28T12:00:00.000+00:00"
}
```

**Example Request:**
```bash
curl -X POST "http://localhost:8081/api/reviews" \
  -H "userId: 123" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Great product!",
    "content": "This product exceeded my expectations. Highly recommended!",
    "rating": 5,
    "productId": 456
  }'
```

---

### 3. Update Review
Update an existing review. Only the review owner can update their review. **Supports partial updates** - only include the fields you want to update.

**Endpoint:** `PUT /api/reviews/{id}`

**Path Parameters:**
- `id` (required) - Long - The review ID to update

**Headers:**
- `userId` (required) - Long - ID of the user updating the review
- `Content-Type: application/json`

**Request Body (all fields optional):**
```json
{
  "title": "Updated title",
  "content": "Updated content with more details",
  "rating": 4
}
```

**Validation Rules:**
- `title` - Optional, if provided must not be blank
- `content` - Optional, if provided must not be blank
- `rating` - Optional, if provided must be between 1 and 5

**Note:** Only the fields included in the request body will be updated. Fields not included will retain their existing values.

**Response:** 200 OK
```json
{
  "id": 1,
  "title": "Updated title",
  "content": "Updated content with more details",
  "userId": 123,
  "rating": 4,
  "moderation": "POSTED",
  "productId": 456,
  "createdAt": "2025-11-28T12:00:00.000+00:00",
  "updatedAt": "2025-11-28T12:30:00.000+00:00"
}
```

**Error Responses:**
- `403 Forbidden` - User is not authorized to update this review
- `404 Not Found` - Review not found

**Example Requests:**

Update all fields:
```bash
curl -X PUT "http://localhost:8081/api/reviews/1" \
  -H "userId: 123" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated title",
    "content": "Updated content with more details",
    "rating": 4
  }'
```

Update only the rating:
```bash
curl -X PUT "http://localhost:8081/api/reviews/1" \
  -H "userId: 123" \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 5
  }'
```

Update only title and content:
```bash
curl -X PUT "http://localhost:8081/api/reviews/1" \
  -H "userId: 123" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New title",
    "content": "New content"
  }'
```

---

### 4. Delete Review
Delete a review. Only the review owner can delete their review.

**Endpoint:** `DELETE /api/reviews/{id}`

**Path Parameters:**
- `id` (required) - Long - The review ID to delete

**Headers:**
- `userId` (required) - Long - ID of the user deleting the review

**Response:** 200 OK
```json
{
  "message": "Review deleted successfully"
}
```

**Error Responses:**
- `403 Forbidden` - User is not authorized to delete this review
- `404 Not Found` - Review not found

**Example Request:**
```bash
curl -X DELETE "http://localhost:8081/api/reviews/1" \
  -H "userId: 123"
```

---

## Project Structure

```
src/main/java/com/example/springbootapp/
├── controller/
│   └── ReviewController.java       # REST API endpoints
├── dto/
│   ├── CreateReviewRequest.java    # Request DTO for creating reviews
│   ├── UpdateReviewRequest.java    # Request DTO for updating reviews
│   └── ReviewResponse.java         # Response DTO for reviews
├── model/
│   ├── ModerationStatus.java       # Enum for review moderation status
│   └── Review.java                 # Review entity
├── repository/
│   └── ReviewRepository.java       # JPA repository interface
└── service/
    └── ReviewService.java          # Business logic layer
```

## Running the Application

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the H2 Console (for development):**
   - URL: http://localhost:8081/h2-console
   - JDBC URL: jdbc:h2:file:./data/testdb
   - Username: sa
   - Password: (leave empty)

## Features

✅ **Create Reviews** - Users can post reviews for products
✅ **Read Reviews** - Get paginated reviews filtered by product ID
✅ **Update Reviews** - Users can edit their own reviews
✅ **Delete Reviews** - Users can delete their own reviews
✅ **Authorization** - Users can only modify their own reviews
✅ **Validation** - Input validation for all fields
✅ **Pagination** - Support for paginated review listing
✅ **Timestamps** - Automatic creation and update timestamps
✅ **Moderation Status** - Track review moderation state

## Security Notes

- The current implementation uses a simple header-based `userId` for authentication
- In a production environment, you should implement proper authentication (JWT, OAuth2, etc.)
- Add role-based access control for moderation features
- Implement rate limiting to prevent spam

## Future Enhancements

- [ ] Add user authentication and authorization (JWT/OAuth2)
- [ ] Implement review moderation workflow
- [ ] Add support for review images/media
- [ ] Implement review voting (helpful/not helpful)
- [ ] Add reporting functionality for inappropriate reviews
- [ ] Implement search and filtering capabilities
- [ ] Add review analytics and statistics
- [ ] Email notifications for review activities

