import etl.core.dsl.etl
import etl.core.engine.SchemaEngine
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SchemaEngineTest {
    @Test
    fun `should reject invalid records in case of rejectIfInvalid`() {

        val records = listOf(
            mutableMapOf("name" to "Oz", "city" to "Athens"),   // valid
            mutableMapOf("name" to "Esra", "city" to "a")     // invalid
        )

        // Schema rules
        val etl = etl {
            log("etl.log")
            extract { csv("customers.csv") }
            schema {
                field("city") {
                    notEmpty()
                    maxLength(15)
                    minLength(3)
                    rejectIfInvalid()
                }
            }
            transform { }
            load { csv("filename.csv") {} }
        }

        val result = SchemaEngine.validate(records, etl.schema)
        assertEquals(1, result.acceptedRecords.size)
    }

    @Test
    fun `should accept invalid records if rejectIfInvalid is not declared`() {

        val records = listOf(
            mutableMapOf("name" to "Oz", "city" to "Athens", "age" to "44"),   // valid
            mutableMapOf("name" to "Esra", "city" to "a", "age" to "")     // invalid
        )

        // Schema rules
        val etl = etl {
            log("etl.log")
            extract { csv("customers.csv") }
            schema {
                field("city") {
                    notEmpty()
                    maxLength(15)
                    minLength(3)
                }
                field("age") {
                    isNumber()
                    notEmpty()
                }
            }
            transform { }
            load { csv("filename.csv") {} }
        }

        val result = SchemaEngine.validate(records, etl.schema)
        assertEquals(2, result.acceptedRecords.size)
    }
}