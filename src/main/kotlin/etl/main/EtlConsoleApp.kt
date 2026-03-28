package etl.main

import etl.core.dsl.etl
import etl.core.engine.EtlRunner

fun main() {
    val job = etl {
        log("etl.log")
        extract { csv("customers.csv") }
        schema {
            field("city") {
                notEmpty()
                maxLength(15)
                minLength(3)
            }
        }
        transform {
            clean {
                trim("name")
                toLowerCase("email")
                defaultIfEmpty("city", "Unknown")
                replace("city", "NYC", "New York")
                toTitleCase("city")
            }

            filter {
                equals("city", "Thunderbay")
            }
        }
        load { csv("filename.csv") {overwrite()} }
    }

    EtlRunner().run(job)
}