package etl.main

import etl.core.dsl.etl
import etl.core.engine.EtlRunner

fun main() {
    val job = etl {
        log("etl.log")

        extract {
            csv("flights_small_CA.csv")
            csv("flights_20260328_CA.csv")
            json("flights_small_WS.json")
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
                //toUpperCase("status")
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