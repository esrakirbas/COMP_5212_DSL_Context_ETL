package etl.core.engine

import etl.core.dsl.etl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransformEngineTest {

    @Test
    fun `test toTitleCase and trim`() {
        val records = listOf(
            mutableMapOf("name" to "esra kirdi   "),
            mutableMapOf("name" to "BAHAR KIRDI"),
            mutableMapOf("name" to "askjh gnjh   "),
            mutableMapOf("name" to "eSrA kIrDi")
        )

        val etl = etl {
            log("etl.log")
            extract { csv("customers.csv") }
            schema {
                field("city") {
                    notEmpty()
                }
            }
            transform {
                clean {
                    trim("name")
                    toTitleCase("name")
                }
            }
            load { csv("customer_out.csv") {} }
        }

        val result = TransformEngine.transform(records, etl.transform)

        assertEquals("Esra Kirdi", result[0]["name"])
        assertEquals("Bahar Kirdi", result[1]["name"])
        assertEquals("Askjh Gnjh", result[2]["name"])
        assertEquals("Esra Kirdi", result[3]["name"])
    }

    @Test
    fun `test toLowerCase and DefaultIfEmpty`() {
        val records = listOf(
            mutableMapOf("name" to "esra kirdi   ", "email" to "ekirdi@lakeheaadu.ca", "city" to ""),
            mutableMapOf("name" to "BAHAR KIRDI", "email" to "BABI@gamil.COM", "city" to "thunderbay"),
        )

        val etl = etl {
            log("etl.log")
            extract { csv("customers.csv") }
            schema {
                field("city") {
                    notEmpty()
                }
            }
            transform {
                clean {
                    toLowerCase("email")
                    defaultIfEmpty("city", "Thunderbay")
                }
            }
            load { csv("customer_out.csv") {} }
        }

        val result = TransformEngine.transform(records, etl.transform)

        assertEquals("Thunderbay", result[0]["city"])
        assertEquals("babi@gamil.com", result[1]["email"])
    }

    @Test
    fun `test filter with single condition`() {
        val records = listOf(
            mutableMapOf("name" to "esra kirdi   ", "email" to "ekirdi@lakeheaadu.ca", "city" to ""),
            mutableMapOf("name" to "BAHAR KIRDI", "email" to "BABI@gamil.COM", "city" to "thunderbay"),
        )

        val etl = etl {
            log("etl.log")
            extract { csv("customers.csv") }
            schema {
                field("city") {
                    notEmpty()
                }
            }
            transform {
                clean {
                    toTitleCase("city")
                    defaultIfEmpty("city", "Thunderbay")
                }
                filter { equals("city", "Thunderbay") }
            }
            load { csv("customer_out.csv") {} }
        }

        val result = TransformEngine.transform(records, etl.transform)

        assertEquals(2, result.size)
    }

    @Test
    fun `test filter with multiple conditions (combine with AND)`() {
        val records = listOf(
            mutableMapOf("name" to "esra kirdi   ", "email" to "ekirdi@lakeheaadu.ca", "city" to ""),
            mutableMapOf("name" to "BAHAR KIRDI", "email" to "BABI@gamil.COM", "city" to "thunderbay"),
            mutableMapOf("name" to "aaa KIRDI", "email" to "dddd@gamil.COM", "city" to "NYC"),
            mutableMapOf("name" to "BAHAR cccc", "email" to "BABI@rr.COM", "city" to "Thunderbay"),
        )

        val etl = etl {
            log("etl.log")
            extract { csv("customers.csv") }
            schema {
                field("city") {
                    notEmpty()
                }
            }
            transform {
                clean {
                    replace("city", "NYC",  "New York City")
                    toTitleCase("city")
                    defaultIfEmpty("city", "Thunderbay")
                }
                filter {
                    equals("city", "Thunderbay")
                    equals("email", "ekirdi@lakeheaadu.ca")
                }
            }
            load { csv("customer_out.csv") {} }
        }

        val result = TransformEngine.transform(records, etl.transform)

        assertEquals(1, result.size)
    }
}