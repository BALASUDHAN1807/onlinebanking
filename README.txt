KDBank - Full Project (OTP login, deposit, withdraw, transfer)
--------------------------------------------------------------

Setup:
1. Import database:
   - Open MySQL Workbench, connect to your server.
   - Run the file kdbank.sql to create schema and sample data.

2. Configure DB.java:
   - Open src/main/java/dao/DB.java and change username/password to your MySQL credentials.

3. Build & Run (requires Maven & JDK 17):
   mvn clean package
   mvn jetty:run

4. Open in browser:
   http://localhost:8080/kdbank/login.jsp

Demo:
 - Register a new user or use sample user phone: 9876543210
 - Click Get OTP (OTP is shown on screen in demo mode)
 - Enter OTP → dashboard shows accounts, deposit/withdraw/transfer.

Notes:
 - OTP is simulated for demo (displayed on screen). For production, integrate SMS API.
 - Do not commit DB credentials to public repos.
