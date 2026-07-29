# Banner API Documentation

Base URL: `/api/v1` (adjust based on your configuration)

## Headers

| Header | Type | Required | Description |
|--------|------|----------|-------------|
| `X-Auto-Adda-Id` | Integer | For authenticated endpoints | User ID |
| `X-User-Role` | String | For authenticated endpoints | Comma-separated roles (e.g., `GARAGE_OWNER,CUSTOMER`) |

---

## Public Endpoints

No authentication required.

### Get All Active Banners

```
GET /banners
```

**Response:** `200 OK`
```json
[
  {
    "id": "string",
    "bannerCode": "BNR-20240101-ABC123",
    "title": "Banner Title",
    "description": "Banner description",
    "bannerImage": {
      "id": 1,
      "fileName": "uuid_banner.jpg",
      "originalFileName": "banner.jpg",
      "filePath": "banners/uuid_banner.jpg",
      "mediaType": "image/jpeg",
      "size": 102400,
      "fileUrl": "https://your-domain.com/uploads/banners/uuid_banner.jpg",
      "createdAt": "2024-01-01T00:00:00"
    },
    "targetUrl": "https://example.com/landing",
    "pageLocation": "HOME_PAGE",
    "position": "TOP",
    "bannerType": "IMAGE",
    "status": "ACTIVE",
    "priority": 1,
    "vendorId": 123,
    "startDate": "2024-01-01T00:00:00",
    "endDate": "2024-12-31T23:59:59",
    "totalImpressions": 15000,
    "totalClicks": 450,
    "dailyImpressionLimit": 1000,
    "totalImpressionLimit": 50000,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00",
    "createdBy": 1
  }
]
```

---

### Get Banners by Page Location

```
GET /banners/page/{pageLocation}
```

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `pageLocation` | String | Page location enum value |

**Page Locations:** `HOME_PAGE`, `SEARCH_PAGE`, `LISTING_PAGE`, `DETAIL_PAGE`, `CHECKOUT_PAGE`, `PROFILE_PAGE`

**Response:** `200 OK` - Array of BannerResponse

---

### Get Banners for Specific Slot

```
GET /banners/page/{pageLocation}/position/{position}
```

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `pageLocation` | String | Page location enum value |
| `position` | String | Banner position enum value |

**Positions:** `TOP`, `BOTTOM`, `LEFT_SIDEBAR`, `RIGHT_SIDEBAR`, `INLINE`, `POPUP`, `FLOATING`

**Response:** `200 OK` - Array of BannerResponse

---

### Track Banner Impression

```
POST /banners/{id}/impression
```

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | Banner ID |

**Response:** `200 OK`
```json
{
  "status": "recorded"
}
```

---

### Track Banner Click

```
POST /banners/{id}/click
```

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | Banner ID |

**Response:** `200 OK`
```json
{
  "status": "recorded"
}
```

---

## Vendor Endpoints

Requires `X-Auto-Adda-Id` and `X-User-Role` headers.

Allowed roles: `COMPANY_OWNER`, `GARAGE_OWNER`, `SPARE_PART_OWNER`, `OTHER_SERVICE_OWNER`, `ADMIN`

### Submit Banner Request

```
POST /vendor/banners/requests
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Request Body:**
```json
{
  "vendorName": "My Garage Shop",
  "vendorEmail": "vendor@example.com",
  "vendorPhone": "9876543210",
  "title": "Summer Sale Banner",
  "description": "Promotional banner for summer sale",
  "proposedImageId": 1,
  "requestedPageLocation": "HOME_PAGE",
  "requestedPosition": "TOP",
  "requestedStartDate": "2024-06-01T00:00:00",
  "requestedEndDate": "2024-06-30T23:59:59",
  "requestedPricingType": "DAILY",
  "proposedBudget": 5000.00
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `vendorName` | String | Yes | Vendor's business name |
| `vendorEmail` | String | Yes | Contact email |
| `vendorPhone` | String | Yes | Contact phone |
| `title` | String | Yes | Banner title |
| `description` | String | No | Banner description |
| `proposedImageId` | Long | Yes | ID from `/mediafiles/upload` response |
| `requestedPageLocation` | String | Yes | Where to display banner |
| `requestedPosition` | String | Yes | Position on page |
| `requestedStartDate` | LocalDateTime | Yes | Desired start date |
| `requestedEndDate` | LocalDateTime | Yes | Desired end date |
| `requestedPricingType` | String | Yes | CPM, CPC, FLAT_RATE, DAILY |
| `proposedBudget` | BigDecimal | Yes | Budget in INR |

**Response:** `201 Created`
```json
{
  "id": "string",
  "requestCode": "BR-20240101-ABC123",
  "vendorId": 123,
  "vendorName": "My Garage Shop",
  "vendorEmail": "vendor@example.com",
  "vendorPhone": "9876543210",
  "title": "Summer Sale Banner",
  "description": "Promotional banner for summer sale",
  "proposedImage": {
    "id": 1,
    "fileName": "uuid_banner.jpg",
    "originalFileName": "banner.jpg",
    "filePath": "banners/uuid_banner.jpg",
    "mediaType": "image/jpeg",
    "size": 102400,
    "fileUrl": "https://your-domain.com/uploads/banners/uuid_banner.jpg",
    "createdAt": "2024-01-01T00:00:00"
  },
  "requestedPageLocation": "HOME_PAGE",
  "requestedPosition": "TOP",
  "requestedStartDate": "2024-06-01T00:00:00",
  "requestedEndDate": "2024-06-30T23:59:59",
  "requestedPricingType": "DAILY",
  "proposedBudget": 5000.00,
  "status": "PENDING",
  "adminNotes": null,
  "rejectionReason": null,
  "approvedBannerId": null,
  "reviewedBy": null,
  "reviewedAt": null,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

---

### Get My Banner Requests

```
GET /vendor/banners/requests
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Response:** `200 OK` - Array of BannerRequestResponse

---

### Get Banner Request by ID

```
GET /vendor/banners/requests/{id}
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Response:** `200 OK` - BannerRequestResponse

---

### Update Banner Request

```
PUT /vendor/banners/requests/{id}
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Request Body:** Same as Submit Banner Request

**Response:** `200 OK` - BannerRequestResponse

**Note:** Can only update requests with `PENDING` status.

---

### Cancel Banner Request

```
DELETE /vendor/banners/requests/{id}
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Response:** `204 No Content`

---

### Get My Active Banners

```
GET /vendor/banners/my-banners
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Response:** `200 OK` - Array of BannerResponse

---

### Get My Banner Analytics

```
GET /vendor/banners/my-banners/{id}/analytics
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `startDate` | LocalDate | No | Filter start date (YYYY-MM-DD) |
| `endDate` | LocalDate | No | Filter end date (YYYY-MM-DD) |

**Response:** `200 OK`
```json
[
  {
    "id": "string",
    "bannerId": "string",
    "analyticsDate": "2024-01-15",
    "impressions": 1500,
    "clicks": 45,
    "uniqueImpressions": 1200,
    "uniqueClicks": 40,
    "ctr": 3.0,
    "costIncurred": 750.00
  }
]
```

---

### Get Pricing Estimate

```
GET /vendor/banners/pricing/estimate
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `pageLocation` | String | Yes | Page location |
| `position` | String | Yes | Banner position |
| `pricingType` | String | Yes | CPM, CPC, FLAT_RATE, DAILY |
| `startDate` | LocalDateTime | Yes | Start date-time |
| `endDate` | LocalDateTime | Yes | End date-time |

**Response:** `200 OK`
```json
{
  "pageLocation": "HOME_PAGE",
  "position": "TOP",
  "pricingType": "DAILY",
  "startDate": "2024-06-01T00:00:00",
  "endDate": "2024-06-30T23:59:59",
  "estimatedCost": 15000.00,
  "currency": "INR"
}
```

---

### Get Active Pricing

```
GET /vendor/banners/pricing
```

**Headers:**
```
X-Auto-Adda-Id: 123
X-User-Role: GARAGE_OWNER
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `pageLocation` | String | No | Filter by page location |
| `position` | String | No | Filter by position |

**Response:** `200 OK`
```json
[
  {
    "id": "string",
    "pricingCode": "PRC-20240101-ABC123",
    "pageLocation": "HOME_PAGE",
    "position": "TOP",
    "pricingType": "DAILY",
    "basePrice": 500.00,
    "currency": "INR",
    "minimumSpend": 1000.00,
    "discountPercentage": 10.0,
    "effectiveFrom": "2024-01-01T00:00:00",
    "effectiveUntil": "2024-12-31T23:59:59",
    "isTemplate": true,
    "peakHourMultiplier": 1.5,
    "weekendMultiplier": 1.2,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
]
```

---

## Admin Endpoints

Requires `X-User-Role: ADMIN` header.

### Get All Banners

```
GET /admin/banners
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `status` | String | No | Filter by status |
| `pageLocation` | String | No | Filter by page location |

**Response:** `200 OK` - Array of BannerResponse

---

### Get Banner by ID

```
GET /admin/banners/{id}
```

**Response:** `200 OK` - BannerResponse

---

### Create Banner

```
POST /admin/banners
```

**Headers:**
```
X-Auto-Adda-Id: 1
X-User-Role: ADMIN
```

**Request Body:**
```json
{
  "title": "New Banner",
  "description": "Banner description",
  "bannerImageId": 1,
  "targetUrl": "https://example.com/landing",
  "pageLocation": "HOME_PAGE",
  "position": "TOP",
  "bannerType": "IMAGE",
  "priority": 1,
  "vendorId": 123,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59",
  "dailyImpressionLimit": 1000,
  "totalImpressionLimit": 50000
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `title` | String | Yes | Banner title |
| `description` | String | No | Banner description |
| `bannerImageId` | Long | Yes | ID from `/mediafiles/upload` |
| `targetUrl` | String | Yes | Click-through URL |
| `pageLocation` | String | Yes | Page to display on |
| `position` | String | Yes | Position on page |
| `bannerType` | String | Yes | IMAGE, HTML, VIDEO |
| `priority` | Integer | No | Display priority (higher = first) |
| `vendorId` | Integer | No | Associated vendor ID |
| `startDate` | LocalDateTime | Yes | Start date-time |
| `endDate` | LocalDateTime | Yes | End date-time |
| `dailyImpressionLimit` | Long | No | Max impressions per day |
| `totalImpressionLimit` | Long | No | Max total impressions |

**Response:** `201 Created` - BannerResponse

---

### Update Banner

```
PUT /admin/banners/{id}
```

**Request Body:**
```json
{
  "title": "Updated Banner",
  "description": "Updated description",
  "bannerImageId": 2,
  "targetUrl": "https://example.com/new-landing",
  "pageLocation": "HOME_PAGE",
  "position": "TOP",
  "bannerType": "IMAGE",
  "status": "ACTIVE",
  "priority": 2,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59",
  "dailyImpressionLimit": 2000,
  "totalImpressionLimit": 100000
}
```

**Response:** `200 OK` - BannerResponse

---

### Delete Banner

```
DELETE /admin/banners/{id}
```

**Response:** `204 No Content`

---

### Update Banner Status

```
PATCH /admin/banners/{id}/status
```

**Request Body:**
```json
{
  "status": "ACTIVE"
}
```

**Banner Statuses:** `DRAFT`, `PENDING`, `ACTIVE`, `PAUSED`, `EXPIRED`, `REJECTED`

**Response:** `200 OK` - BannerResponse

---

### Get All Banner Requests

```
GET /admin/banners/requests
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `status` | String | No | Filter by request status |

**Request Statuses:** `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`

**Response:** `200 OK` - Array of BannerRequestResponse

---

### Approve Banner Request

```
POST /admin/banners/requests/{id}/approve
```

**Headers:**
```
X-Auto-Adda-Id: 1
X-User-Role: ADMIN
```

**Request Body (optional):**
```json
{
  "adminNotes": "Approved with modifications"
}
```

**Response:** `200 OK` - BannerResponse (the created banner)

---

### Reject Banner Request

```
POST /admin/banners/requests/{id}/reject
```

**Headers:**
```
X-Auto-Adda-Id: 1
X-User-Role: ADMIN
```

**Request Body:**
```json
{
  "rejectionReason": "Image quality too low"
}
```

**Response:** `200 OK` - BannerRequestResponse

---

### Get All Schedules

```
GET /admin/banners/schedules
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `bannerId` | String | No | Filter by banner ID |
| `status` | String | No | Filter by status |

**Response:** `200 OK` - Array of BannerScheduleResponse

---

### Create Schedule

```
POST /admin/banners/schedules
```

**Request Body:**
```json
{
  "bannerId": "string",
  "startDate": "2024-06-01T00:00:00",
  "endDate": "2024-06-30T23:59:59",
  "dailyStartTime": "09:00:00",
  "dailyEndTime": "18:00:00",
  "daysOfWeek": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
  "timezone": "Asia/Kolkata",
  "isRecurring": true
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `bannerId` | String | Yes | Banner ID to schedule |
| `startDate` | LocalDateTime | Yes | Schedule start date |
| `endDate` | LocalDateTime | Yes | Schedule end date |
| `dailyStartTime` | LocalTime | No | Daily display start time (HH:mm:ss) |
| `dailyEndTime` | LocalTime | No | Daily display end time (HH:mm:ss) |
| `daysOfWeek` | List<String> | No | Days to display (MONDAY, TUESDAY, etc.) |
| `timezone` | String | No | Timezone (default: Asia/Kolkata) |
| `isRecurring` | Boolean | No | Whether schedule repeats |

**Response:** `201 Created` - BannerScheduleResponse

---

### Update Schedule

```
PUT /admin/banners/schedules/{id}
```

**Request Body:** Same as Create Schedule

**Response:** `200 OK` - BannerScheduleResponse

---

### Delete Schedule

```
DELETE /admin/banners/schedules/{id}
```

**Response:** `204 No Content`

---

### Get All Pricing

```
GET /admin/banners/pricing
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `pageLocation` | String | No | Filter by page location |

**Response:** `200 OK` - Array of BannerPricingResponse

---

### Create Pricing

```
POST /admin/banners/pricing
```

**Request Body:**
```json
{
  "pageLocation": "HOME_PAGE",
  "position": "TOP",
  "pricingType": "DAILY",
  "basePrice": 500.00,
  "currency": "INR",
  "minimumSpend": 1000.00,
  "discountPercentage": 10.0,
  "effectiveFrom": "2024-01-01T00:00:00",
  "effectiveUntil": "2024-12-31T23:59:59",
  "isTemplate": true,
  "peakHourMultiplier": 1.5,
  "weekendMultiplier": 1.2
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `pageLocation` | String | Yes | Page location |
| `position` | String | Yes | Banner position |
| `pricingType` | String | Yes | CPM, CPC, FLAT_RATE, DAILY |
| `basePrice` | BigDecimal | Yes | Base price in specified currency |
| `currency` | String | Yes | Currency code (e.g., INR) |
| `minimumSpend` | BigDecimal | No | Minimum spend required |
| `discountPercentage` | BigDecimal | No | Discount percentage |
| `effectiveFrom` | LocalDateTime | No | Pricing effective from |
| `effectiveUntil` | LocalDateTime | No | Pricing effective until |
| `isTemplate` | Boolean | No | Whether this is a template pricing |
| `peakHourMultiplier` | BigDecimal | No | Multiplier for peak hours |
| `weekendMultiplier` | BigDecimal | No | Multiplier for weekends |

**Pricing Types:** `CPM`, `CPC`, `FLAT_RATE`, `DAILY`

**Response:** `201 Created` - BannerPricingResponse

---

### Update Pricing

```
PUT /admin/banners/pricing/{id}
```

**Request Body:** Same as Create Pricing

**Response:** `200 OK` - BannerPricingResponse

---

### Get Banner Analytics

```
GET /admin/banners/analytics
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `bannerId` | String | Yes | Banner ID |
| `startDate` | LocalDate | No | Filter start date |
| `endDate` | LocalDate | No | Filter end date |

**Response:** `200 OK` - Array of BannerAnalyticsResponse

---

## Media File Endpoints

### Upload Files

```
POST /mediafiles/upload
```

**Content-Type:** `multipart/form-data`

**Form Data:**
| Field | Type | Description |
|-------|------|-------------|
| `files` | File[] | Array of files to upload |

**Response:** `201 Created`
```json
[
  {
    "id": 1,
    "fileName": "uuid_original-name.jpg",
    "originalFileName": "original-name.jpg",
    "filePath": "banners/uuid_original-name.jpg",
    "mediaType": "image/jpeg",
    "size": 102400,
    "fileUrl": "https://your-domain.com/uploads/banners/uuid_original-name.jpg",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

**Note:** Use the `id` from this response as `proposedImageId` when submitting a banner request.

---

### Get File by ID

```
GET /mediafiles/{id}
```

**Response:** `200 OK` - MediaFileResponse

---

### Delete File

```
DELETE /mediafiles/{id}
```

**Response:** `204 No Content`

---

## Enums Reference

### BannerPageLocation
- `HOME_PAGE`
- `SEARCH_PAGE`
- `LISTING_PAGE`
- `DETAIL_PAGE`
- `CHECKOUT_PAGE`
- `PROFILE_PAGE`

### BannerPosition
- `TOP`
- `BOTTOM`
- `LEFT_SIDEBAR`
- `RIGHT_SIDEBAR`
- `INLINE`
- `POPUP`
- `FLOATING`

### BannerType
- `IMAGE`
- `HTML`
- `VIDEO`

### BannerStatus
- `DRAFT`
- `PENDING`
- `ACTIVE`
- `PAUSED`
- `EXPIRED`
- `REJECTED`

### BannerRequestStatus
- `PENDING`
- `APPROVED`
- `REJECTED`
- `CANCELLED`

### PricingType
- `CPM` (Cost Per Mille/1000 impressions)
- `CPC` (Cost Per Click)
- `FLAT_RATE`
- `DAILY`

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error message",
  "path": "/api/endpoint"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Admin access required",
  "path": "/api/endpoint"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Banner not found: abc123",
  "path": "/api/endpoint"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/endpoint"
}
```
