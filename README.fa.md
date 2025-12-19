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
