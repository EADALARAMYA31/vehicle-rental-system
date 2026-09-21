# Vehicle Rental System

Spring Boot 3 + Maven REST API, built and tested by a Jenkins pipeline.

## Prerequisites
- JDK 17+
- Maven 3.9+ (`mvn -v`)
- Git

## Run locally
```bash
mvn clean test          # run unit + integration tests
mvn spring-boot:run     # start on http://localhost:8080
mvn clean package       # builds target/vehicle-rental-system-1.0.0-SNAPSHOT.jar
java -jar target/vehicle-rental-system-1.0.0-SNAPSHOT.jar
```
H2 console: http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:mem:rentaldb`, user `sa`, blank password)

## API
| Method | Path | Purpose |
|--------|------|---------|
| POST | /api/vehicles | Add vehicle |
| GET | /api/vehicles, /api/vehicles/available, /api/vehicles/{id} | List / view |
| DELETE | /api/vehicles/{id} | Remove vehicle |
| POST | /api/customers | Register customer |
| GET | /api/customers, /api/customers/{id} | List / view |
| POST | /api/rentals | Rent a vehicle |
| PUT | /api/rentals/{id}/return | Return a vehicle |
| GET | /api/rentals | List rentals |

Example:
```bash
curl -X POST localhost:8080/api/vehicles -H "Content-Type: application/json" \
  -d '{"registrationNumber":"AP39AB1234","make":"Maruti","model":"Swift","type":"CAR","dailyRate":1200}'

curl -X POST localhost:8080/api/customers -H "Content-Type: application/json" \
  -d '{"name":"Ravi","email":"ravi@example.com","phone":"9999999999","licenseNumber":"DL12345"}'

curl -X POST localhost:8080/api/rentals -H "Content-Type: application/json" \
  -d '{"vehicleId":1,"customerId":1,"startDate":"2026-10-01","endDate":"2026-10-03"}'

curl -X PUT localhost:8080/api/rentals/1/return
```

## Jenkins setup
1. **Install Jenkins** (needs Java 17+) and open http://localhost:8080 (change Jenkins' port if it clashes with the app, e.g. `--httpPort=9090`).
2. **Plugins**: Pipeline, Git, JUnit (all included in the suggested plugins).
3. **Manage Jenkins > Tools**:
   - JDK: name `JDK-17` (auto-install or point to JAVA_HOME)
   - Maven: name `Maven-3.9` (auto-install from Apache)
   These names must match the `tools {}` block in the `Jenkinsfile`.
4. **Push this project to GitHub/GitLab** (`git init`, `git add .`, `git commit`, `git push`).
5. **New Item > Pipeline** (or Multibranch Pipeline):
   - Definition: *Pipeline script from SCM*
   - SCM: Git, repository URL, branch `*/main`
   - Script Path: `Jenkinsfile`
6. **Build Now**. Stages: Checkout > Build > Test (JUnit results published) > Package > Archive jar.
7. **Auto-trigger**: add a GitHub webhook to `http://<jenkins-host>/github-webhook/` and tick *GitHub hook trigger*, or use *Poll SCM* with `H/5 * * * *`.

On a Windows Jenkins agent, change `sh` to `bat` in the Jenkinsfile.

## Ideas for next steps
- Swap H2 for MySQL/PostgreSQL (add driver, change `application.properties`)
- Date-overlap checks / reservations for future dates
- Late-return penalties, payments, Spring Security login
- Jenkins: SonarQube analysis, Docker image build/push, deploy stage
