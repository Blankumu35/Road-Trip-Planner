# csu33012-2425-project08

# Running the Application

## Frontend Setup Instructions

### Version requirements

You must have the following installed before continuing:

|   Dependency   | Version  |                   Install                   |
| :------------: | :------: | :-----------------------------------------: |
|    Node.js     | 18.20.2.\* |   [docs](https://nodejs.org/en/download)    |
|      pnpm      | 8.15.1.\*  |    [docs](https://pnpm.io/installation)     |

---

### First steps

After cloning the repostitory do the following:

```bash
# Move to directory 'flight'
cd frontend/flights

# Install the package manager
npm install pnpm

# Run the application
pnpm run dev
```

The application should now be available to view in your browser at http://localhost:3000/

You can use ctrl-C in the terminal to quit the application.

---

## Running the frontend tests

Run the following command to run tests

```bash
cd frontend/flights
npm run test
```

---

### Troubleshooting

If the above instructions do not work consider using `npm` instead.

```bash
# Move to directory 'flight'
cd frontend/flights

# Install the dependencies locally
npm install --legacy-peer-dep

# Run the application
npm run dev
```

---

## Backend Setup Instructions

This guide provides instructions for downloading, setting up, and running our Spring Boot application using Maven.

---

### Prerequisites

Before setting up the application, ensure you have the following installed on your machine:

1. **Java Development Kit (JDK)**
   - Minimum Version: **Java 17** (or as specified in your `pom.xml`)
   - [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or use a package manager like `sdkman` for Linux/MacOS or `choco` for Windows.

   **Verify Installation:**
   ```bash
   java -version
   ```

2. **Apache Maven**
   - Minimum Version: **3.8.6**
   - [Download Maven](https://maven.apache.org/download.cgi)

   **Verify Installation:**
   ```bash
   mvn -v
   ```

3. **Git**
   - [Download Git](https://git-scm.com/)

   **Verify Installation:**
   ```bash
   git --version
   ```

---

### Steps to Set Up the Spring Boot Application

#### 1. Clone the Repository
Use `git` to clone the project repository. If you already have the project files, skip cloning the repository.

```bash
git clone https://gitlab.scss.tcd.ie/csu33012-2425-group08/csu33012-2425-project08.git
cd csu33012-2425-project08/backend/trip
```

#### 2. Configure the API Keys
The default configuration is located in `src/main/resources/application.properties`. Modify the `openai.api.key` to your own OpenAI API key.

#### 3. Build the Project
Run the following Maven command to download dependencies and build the application.

```bash
mvn clean install
```

> This command will:
> - Download all required dependencies specified in `pom.xml`.
> - Compile the source code.
> - Run any tests.

Run the following Maven command to only run tests

```bash
mvn test
```

#### 4. Run the Application
Start the Spring Boot application by executing:

```bash
mvn spring-boot:run
```

---

### Dependency Management

Dependencies are defined in the `pom.xml` file.

Run the following command to add new dependencies:
```bash
mvn clean install
```

---

### Troubleshooting

- **Port Conflicts:**
  If the default port (`8080`) is in use, please make sure to free the default port.

- **Dependency Issues:**
  If Maven fails to resolve dependencies, clear the cache and retry:
  ```bash
  mvn dependency:purge-local-repository
  mvn clean install
  ```

---

### Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/)

---

# Testing

The Spring Boot tests can be found in `backend/trip/src/test/java/csu33012/group08/trip/controller`.

## Running the backend tests

Run the following Maven command to run tests

```bash
cd backend/trip
mvn test
```

## Running the frontend tests

Run the following command to run tests

```bash
cd frontend/flights
npm run test
```

---

# Diary

You can find the diary in the root directory of our project.

---

# Video

You can find a video demonstrating our application [here](https://media.heanet.ie/page/ac397877495d4ccbaa3d45d116bf9068
):

https://media.heanet.ie/page/ac397877495d4ccbaa3d45d116bf9068

---

# Jira Board

You can find our Jira Board [here](https://tcd-team-bkgfut2l.atlassian.net/jira/software/projects/SCRUM/boards/1/backlog
):

https://tcd-team-bkgfut2l.atlassian.net/jira/software/projects/SCRUM/boards/1/backlog

---

# Testing Map and Planner generator

To test some feature, you have to use your own openAPI key(put it in the applications.properties file in the backend/src/main/resources) and googleAPI key(in the home.jsx file and the index.html file in the flights/src folder)
