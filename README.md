# ETL DSL in Kotlin

This project implements a Domain-Specific Language (DSL) for defining ETL (Extract, Transform, Load) workflows in a declarative way.
The execution engine is built using functional programming principles in Kotlin.

---

## Features

* Supports multiple input formats: CSV, JSON, Excel
* Schema-based validation
* Data cleaning and transformation
* Filtering capabilities
* Unified CSV output
* Rejected records handling

---

## How to Run

The project is self-contained and requires no additional configuration beyond a standard Maven setup.

1. Open the project in IntelliJ IDEA (or any Kotlin-compatible IDE)
2. Ensure JDK 17 is configured
3. Import as a Maven project
4. Run the main function

The application will automatically process the sample input files located in the `sample-data` folder and generate an output CSV file in the project root directory.

---

## Example DSL

```kotlin
etl {
    log("etl.log")

    extract {
        csv("sample-data/flights_small_CA.csv")
        json("sample-data/flights_small_WS.json")
        excel("sample-data/flights_small_PD.xlsx")
    }

    schema {
        field("origin") {
            notEmpty()
            minLength(3)
            maxLength(3)
        }
    }

    transform {
        clean {
            trim("origin")
        }

        filter {
            notEquals("status", "CANCELLED")
        }
    }

    load {
        csv("output.csv") {
            overwrite()
        }
    }
}
```

---

## Project Structure

```
src/                → source code
sample-data/        → input files for testing
pom.xml             → Maven configuration
```

---

## Notes

* All data is processed as strings for simplicity and consistency across different file formats
* Validation and transformation rules are applied during the pipeline execution
* The DSL allows users to define workflows without dealing with low-level implementation details

---