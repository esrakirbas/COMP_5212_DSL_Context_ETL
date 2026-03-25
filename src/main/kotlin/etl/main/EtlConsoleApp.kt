package etl.main

import etl.core.dsl.etl
import etl.core.engine.EtlRunner

fun main() {
    val job = etl {
        log("etl.log")

        extract {
            csv("people.csv")
        }

        schema {
            field("name") {
                notEmpty()
            }
            field(name = "city") {
                notEmpty()
                rejectIfInvalid()
            }
        }

        load {
            csv("output.csv") {}
        }
    }

    EtlRunner().run(job)
}