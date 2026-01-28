# 1. Project Overview

Name: Movie Ticket Booking System
Goal: A simple web app where users can(Frontend):

List of movies with posters
Separate page for show timings of a movie
Separate page for seat selection & booking
Popup form for name & phone number
Booking confirmation popup
My Bookings list with cancel option
Search bar for movies
Loading messages while data loads
Backend (server + database):

Stores movies, show timings, seats, bookings
Provides REST APIs to get movies, shows, seats, create/cancel bookings
Saves data permanently in MySQL database

# 2. Technologies Used

Frontend -> "HTML5, CSS3, JavaScript","User interface, pages, popup, fetch data"
Backend -> Spring Boot (Java),"Server, REST APIs, business logic"
Database -> MySQL,Store all data permanently
ORM -> Hibernate (via Spring Data JPA),Connect Java to MySQL easily
Build Tool -> Maven,Download libraries & build backend
Tools -> "Eclipse / IntelliJ, VS Code, Postman, MySQL Workbench",Development & testing 

# 3.How We Created the Project (Build Process) 

# A. Backend Creation (Spring Boot)

# 1. Create project in Eclipse / IntelliJ
New → Spring Starter Project
Name: MovieTicketBookingSystem
Dependencies:
Spring Web
Spring Data JPA
MySQL Driver
Spring Boot DevTools (optional)

# 2.Configure database connection
File: src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/movie_db
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080

# 3.Create Entity classes (database tables)
Movie.java
Show.java
Seat.java
Booking.java

# 4.Create Repository interfaces 
MovieRepository, ShowRepository, SeatRepository, BookingRepository

# 5.Create Service classes
MovieService, ShowService, SeatService, BookingService 

# 6.Create Controller classes
MovieController
ShowController
SeatController
BookingController 

# 7.Add CORS (so frontend can call backend)
Create file CorsConfig.java

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}

# 8.Run backend
Right click main class → Run As → Spring Boot App
Wait for "Tomcat started on port(s): 8080"

# B.Frontend Creation (HTML + CSS + JS)

1. Create folder: frontend (inside project or separate)
2. Create 3 files:
index.html — main page (movie list)
shows.html — show timings page
seats.html — seat selection & booking page
3. Create 2 more files:
style.css — all design
script.js — all JavaScript logic
4. Link script.js and style.css in all HTML files:
<link rel="stylesheet" href="style.css">
<script src="script.js"></script>
5. Run frontend
Double-click index.html → opens in browser
Or use VS Code → right-click index.html → Open with Live Server (recommended) 

# How to Run the Complete Project

# A. Start the backend (very important)

Open project in Eclipse / IntelliJ
Make sure MySQL is running
Run the main Spring Boot class
Wait for message:
Tomcat started on port(s): 8080 
Backend is now live at: http://localhost:8080

# B. Start the frontend 

Go to frontend folder
Double-click index.html (opens in browser)
OR
Use VS Code Live Server:
Install Live Server extension
Right-click index.html → Open with Live Server
It opens at http://127.0.0.1:5500 (or similar)
Now you can:

See movies
Click → go to shows page
Click show → go to seats page
Select seats → book → see confirmation → auto back to home

# Summary Table — How to Achieve It

Goal   How we did it    Where it happens
See movie list -> Fetch from /movies API -> index.html + script.js
Click movie → show timings -> Redirect to shows.html?id=... -> script.js
See show timings -> Fetch from /shows/movie/{id} API -> shows.html
Click show → select seats -> Redirect to seats.html?showId=... -> shows.html
Select & book seats -> Popup form → POST to /bookings -> seats.html
See my bookings -> Fetch from /bookings API -> index.html
Cancel booking -> DELETE /bookings/{id} -> index.html
Data saved permanently -> MySQL database + JPA -> Backend

# Final Tips
Always start backend first (Spring Boot)
Then open frontend in browser
Use Ctrl + F5 to refresh frontend if changes don't appear
Check browser console (F12) and Spring Boot console for errors


