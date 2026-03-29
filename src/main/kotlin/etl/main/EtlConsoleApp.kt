package etl.main

import etl.core.dsl.etl
import etl.core.engine.EtlRunner

fun main() {
    val basePath = "sample-data/"
    val job = etl {
        log("etl.log")

        extract {
            csv("${basePath}flights_20260328_CA.csv")
            json("${basePath}flights_20260328_WS.json")
            excel("${basePath}flights_20260328_PD.xlsx")
        }

        schema {
            field("flight_id") {
                notEmpty()
            }

            field("origin") {
                notEmpty()
                minLength(3)
                maxLength(3)
            }

            field("destination") {
                notEmpty()
                minLength(3)
                maxLength(3)
            }

            field("departure_hour") {
                min(0)
                max(23)
            }

            field("departure_minute") {
                min(0)
                max(59)
            }

            field("arrival_hour") {
                min(0)
                max(23)
            }

            field("arrival_minute") {
                min(0)
                max(59)
            }

            field("delay_minutes") {
                min(0)
            }

            field("status") {
                notEmpty()
            }

            field("passenger_count") {
                min(0)
            }

            field("load_factor") {
                min(0)
                max(100)
            }
        }

        invalidPolicy("reject")

        transform {
            clean {
                trim("origin")
                trim("destination")
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

    EtlRunner().run(job)
}