# Automation-TestingTask2 
## Description
Automation framework using TestNG and Apache POI to test the Frontgate login page with multiple user credentials read from an Excel file.

## Test Cases (testdata.xlsx)

| Email | Password | Expected Result |
|-------|----------|----------------|
| invalidemail | *(empty)* | invalid_email |
| chef03@anhmaybietchoi.com | wrongpass123 | wrong_password |
| chef03@anhmaybietchoi.com | Shahd@123456 | success |

## How It Works
1. `ExcelReader.java` reads the 3 rows from `testdata.xlsx`
2. A loop in `LoginDataDrivenTest.java` runs each row as a login attempt
3. For each case, the test verifies either an error or a success welcome message

## Dependencies Added
```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.3</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
</dependency>
```

## Results
```
Test Case 1 – invalid_email     ✅ PASS
Test Case 2 – wrong_password    ✅ PASS
Test Case 3 – success           ✅ PASS

Total tests run: 1, Passes: 1, Failures: 0
```
