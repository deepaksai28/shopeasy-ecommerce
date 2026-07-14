# ShopEasy — Simple E-Commerce Web App

A small, complete e-commerce demo: browse products → add to cart → checkout → order confirmation.

Built with **Spring Boot 3, Thymeleaf, Spring Data JPA, H2 (in-memory database)**.

This project is intentionally small. It's meant to be something you fully understand and
can walk through in an interview — not a feature-packed app you can't explain.

---

## 1. How to run it

Requirements: **Java 17+** and **Maven** installed on your machine (`java -version`, `mvn -version`).

```bash
cd ecommerce
mvn spring-boot:run
```

Then open **http://localhost:8080** in your browser. That's it — no database installation
needed, because it uses H2 (an in-memory database that resets each time you restart the app).

To inspect the database tables directly while it's running, visit **http://localhost:8080/h2-console**
(JDBC URL: `jdbc:h2:mem:ecommercedb`, user: `sa`, no password).

---

## 2. Project structure

```
src/main/java/com/deepak/ecommerce/
├── EcommerceApplication.java   # entry point + seeds sample products on startup
├── model/                      # data classes
│   ├── Product.java            # @Entity - a store item
│   ├── Order.java              # @Entity - a completed purchase
│   ├── OrderItem.java          # @Entity - one line inside an order
│   └── CartItem.java           # plain object, NOT saved to DB (lives in session)
├── repository/                 # Spring Data JPA interfaces (auto-generated CRUD)
│   ├── ProductRepository.java
│   └── OrderRepository.java
├── service/
│   └── Cart.java                # cart logic - add/update/remove/total, held in session
└── controller/                 # handle HTTP requests, return Thymeleaf view names
    ├── ProductController.java  # browse products
    ├── CartController.java     # add/update/remove cart items
    └── OrderController.java    # checkout form + place order

src/main/resources/
├── templates/                  # Thymeleaf HTML pages
└── application.properties      # DB config
```

---

## 3. The request flow (this is the part to be able to draw on a whiteboard)

```
Browser  →  Controller  →  Repository / Service  →  Database
   ↑                                                     │
   └──────────────── Thymeleaf renders HTML ◄────────────┘
```

**Browsing:** `GET /` → `ProductController.listProducts()` → `ProductRepository.findAll()`
→ Thymeleaf renders `index.html` with the product list.

**Adding to cart:** `POST /cart/add/{id}` → `CartController` fetches the `Product` from the
repository, then calls `Cart.addProduct()`. The `Cart` object lives in the **HTTP session**,
not the database — it's temporary, one per browser/user, and disappears if the session ends.

**Checkout:** `POST /checkout` → `OrderController.placeOrder()` reads the session `Cart`,
builds an `Order` entity with one `OrderItem` per cart line, and saves it via
`OrderRepository.save()`. This is the moment temporary cart data becomes a **permanent**
database record. The cart is then cleared.

---

## 4. Design decisions worth explaining in an interview

**Q: Why is the cart stored in the session instead of the database?**
Because it's temporary. Most people who add something to a cart never complete checkout —
writing every cart action to the database would be wasteful. Only a completed order needs
to be permanent.

**Q: Why does `OrderItem` store `productName` and `priceAtPurchase` instead of just linking
to the `Product`?**
Because prices change and products get discontinued. If we only stored a reference to
`Product`, a price change next month would silently rewrite last month's order history.
Storing a snapshot at the time of purchase is standard e-commerce practice.

**Q: What does `@Entity` actually do?**
It tells Spring Data JPA / Hibernate to map the Java class to a database table automatically
— field names become column names, and JPA generates the SQL for you.

**Q: What does extending `JpaRepository<Product, Long>` give you?**
Methods like `findAll()`, `findById()`, `save()`, `deleteById()` with zero implementation
code — Spring generates them at runtime based on the interface.

**Q: Why H2 instead of MySQL, if the resume says MySQL?**
H2 is used here for zero-setup local demos. Switching to MySQL only requires changing the
JDBC URL, driver, and adding the MySQL connector dependency (see the commented-out block in
`pom.xml` and `application.properties`) — the rest of the code (entities, repositories,
controllers) doesn't change at all. That's the point of using Spring Data JPA: the
persistence logic is decoupled from the specific database.

**Q: What would you add if you had more time?**
User authentication/login, admin panel to manage products, payment gateway integration,
order history page per user, product search/filtering.

---

## 5. Push this to your GitHub

```bash
cd ecommerce
git init
git add .
git commit -m "Initial commit: ShopEasy e-commerce app"
git branch -M main
git remote add origin https://github.com/deepaksai28/shopeasy-ecommerce.git
git push -u origin main
```

(Create the empty `shopeasy-ecommerce` repo on github.com first, without a README, then run
the above from inside the `ecommerce` folder.)

---

## 6. Before your interview

Actually run it locally, click through every page yourself, and open each file at least once.
If a small change is asked for on the spot (e.g. "add a discount field to Product"), you
should be able to find where to add it: the field in `Product.java`, then the display in
`index.html`. That's the level of familiarity worth having before you list this on a resume.
