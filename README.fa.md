# spring-data-jpa-restful


### تفاوت REST vs RESTful (فرقشون چیه؟)
- درحقیقت REST → از HTTP استفاده می‌کنی، JSON برمی‌گردونی
- در حقیقت RESTful → به معنای واقعی از اصول REST پیروی می‌کنی

### 1️⃣ در حقیقت Resource-based design (بزرگ‌ترین تفاوت)
### مثلا پروژه هایی که داشته ام

```http request
/account/save.do
/account/findByEmail.do
/account/getAccounts.do

```

این‌ها:

- Action-based هستن

- شبیه RPC هستن

### ✅ و RESTful واقعی
Resource = Account
```http request
POST   /accounts
GET    /accounts
GET    /accounts/{id}
GET    /accounts?email=x

```

📌 در RESTful:

- اسم Resource مهمه

- فعل نداریم (save, get, find ❌)

### 2️⃣در واقع HTTP Method Semantics (الان نقض شده)

❌ الان
```java
@GetMapping("/save.do")

```
GET = تغییر state ❌


✅ اما RESTful


```java
@PostMapping("/accounts")

```
| Method | معنی           |
| ------ | -------------- |
| GET    | Read           |
| POST   | Create         |
| PUT    | Replace        |
| PATCH  | Partial update |
| DELETE | Remove         |

### 3️⃣ درواقع  URIها معنی‌دار نیستند

```http request
/account/fullFetch.do

```

-  و fullFetch یعنی چی؟

- و  Client باید منطق داخلی بداند

```http request
GET /accounts?balanceLessThan=1000

```

- با Query param

- با Self-descriptive

### 4️⃣ معمولا HTTP Status Codes استفاده نمی‌کنی
❌ الان
```java

return account;

```

همیشه 200 OK


✅ اما RESTful

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(savedAccount);

```

| وضعیت | کاربرد      |
| ----- | ----------- |
| 200   | OK          |
| 201   | Created     |
| 204   | No Content  |
| 400   | Bad Request |
| 404   | Not Found   |

### 5️⃣ درواقع Validation در Layer اشتباه استفاده می‌شود
```java
public Object fullFetch(@Valid Account account)

```
Account = Entity
❌ نباید مستقیم API Contract باشد

✅ اما RESTful

```java
public AccountDTO create(@Valid @RequestBody AccountDTO dto)
```

### 6️⃣ و Content Negotiation وجود ندارد
RESTful یعنی:
```http request
Accept: application/json
Content-Type: application/json

```

### 7️⃣ و Error Handling استاندارد نداری

RESTful API باید:

- Error Response مشخص

- Message

- Timestamp

- Path

### 🧠 جمع‌بندی خیلی شفاف

| معیار            | پروژه تو |
| ---------------- | -------- |
| HTTP             | ✅        |
| JSON             | ✅        |
| Controller       | ✅        |
| Resource-based   | ❌        |
| Method semantics | ❌        |
| Status codes     | ❌        |
| DTO              | ❌        |
| Error model      | ❌        |

<hr/>

### 1️⃣ درحقیقت Resource-based URLها

❌ فعلی یا قبلی ها:
```http request
/account/save.do
/account/findByEmail.do

```

✅ RESTful:
- Resource = accounts

- استفاده از جمع برای منابع

- QueryParam برای فیلترها

مثال:

```http request
POST   /accounts           → Create new account
GET    /accounts           → Get all accounts (optionally filter)
GET    /accounts/{id}      → Get account by ID
GET    /accounts?email=x   → Filter by email
GET    /accounts?balanceMin=1000   → Filter by balance

```

### 2️⃣در واقع HTTP Method Semantics

| Action | HTTP Method |
| ------ | ----------- |
| Create | POST        |
| Read   | GET         |
| Update | PUT / PATCH |
| Delete | DELETE      |

### 3️⃣ Request Body / Path / Query
- POST/PUT/PATCH → @RequestBody

- GET → @PathVariable یا @RequestParam

### 4️⃣ Status Codes

| Status          | Meaning                             |
| --------------- | ----------------------------------- |
| 200 OK          | موفقیت برای GET                     |
| 201 Created     | ایجاد موفقیت‌آمیز                   |
| 204 No Content  | Delete موفق یا Update بدون Response |
| 400 Bad Request | Validation Error                    |
| 404 Not Found   | Resource پیدا نشد                   |

<hr/>

## اینکه چرا RestControllerAdvice رو توی پکیج api.error ساختم و توی پکیج exception نگذاشتم

خلاصه‌ی خیلی کوتاه

👉 چون @RestControllerAdvice بخشی از لایه‌ی API است، نه Domain.

اما Exceptionها متعلق به Domain / Application هستند.

حالا بازش می‌کنیم 👇

### 1️⃣ اصل طلایی: هر چیز در لایه‌ی خودش
در معماری تمیز (Clean / Layered):
| لایه             | مسئولیت                 |
| ---------------- | ----------------------- |
| API (Controller) | HTTP, Status Code, JSON |
| Service          | Business Logic          |
| Domain           | Model + Rules           |
| Persistence      | DB                      |

❓ واقعا @RestControllerAdvice ً چیکار می‌کند؟

- در واقع HTTP Status تعیین می‌کند

- و Response JSON می‌سازد

- و Request URI را می‌خواند

- و به Web Layer وابسته است (HttpServletRequest)

📌 یعنی:

❌ بدون وب معنی ندارد

❌ بدون REST بی‌استفاده است

پس:

این یک Web / API concern است

### 2️⃣ و Exceptionها چرا در پکیج exception؟

Exception چیست؟

- بیان یک وضعیت غیرعادی در Domain یا Application

- مستقل از این‌که:

  - REST باشیم

  - GraphQL باشیم

  - Batch job باشیم

مثلاً:

```java
throw new AccountNotFoundException(...)

```

📌 این:

نه HTTP می‌شناسد

نه Status Code

نه JSON

✔ فقط می‌گوید: «Account وجود ندارد»

### 3️⃣ اگر ControllerAdvice را در exception بگذاریم چه می‌شود؟

❌ مشکل معماری
```text
exception
 ├── AccountNotFoundException
 └── GlobalExceptionHandler   ❌

```

چرا بده؟

- Web layer به Domain قاطی می‌شود

- بعداً اگر:

  - API عوض شود

  - gRPC اضافه شود

  - REST حذف شود

همه‌چیز به هم می‌ریزد.


### 4️⃣ ساختار پیشنهادی حرفه‌ای

```text
org.j2os
 ├── api
 │   ├── controller
 │   └── error
 │       ├── ApiError
 │       └── GlobalExceptionHandler
 │
 ├── service
 │
 ├── exception
 │   └── AccountNotFoundException
 │
 └── repository

```



📌 خیلی تمیز
📌 قابل توسعه
📌 قابل تست
📌 مصاحبه‌پسند