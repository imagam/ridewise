# Setup Instructions

## Prerequisites

- Java 21
- Maven 3.9+
- Git

## Clone

```bash
git clone <your-github-repository-url>
cd ridewise
```

## Build and test

```bash
mvn clean test
```

A successful build should report all tests passing.

## Run the console application

```bash
mvn compile
java -cp target/classes com.ridewise.Main
```

## Suggested Git workflow

The assignment was developed on a feature branch and can be merged into `main` through a pull request.

```bash
git checkout main
git pull
git checkout -b feature/ridewise-v1
# make changes
git add .
git commit -m "Implement RideWise V1"
git push -u origin feature/ridewise-v1
```

Then open a pull request from `feature/ridewise-v1` to `main`.
