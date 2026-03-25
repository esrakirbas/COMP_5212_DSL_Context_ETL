package etl.main

import etl.core.dsl.etl

fun main() {
    val etlJob = etl {
        extract {
            csv("orders.csv")
            json("orders_from_BC.csv")
        }
        schema {
            field("orderId") {}
            field("orderDate") {}
            field("customerName") {}
            field("orderedItem") {}
            field("orderedItemCount") {}
            field("price") {}
            field("deliveryAddress") {}
        }
        transform {
            filter { empty("orderId") }
            filter { empty("customerName") }
            clean { toTitleCase("customerName") }
            clean { trim("price") }
        }
        load { csv("orders_unified.csv") { overwrite() } }
    }
    //EtlRunner().run(etlJob)
}