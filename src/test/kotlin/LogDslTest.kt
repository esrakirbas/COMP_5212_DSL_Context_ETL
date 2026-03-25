import etl.core.dsl.etl
import kotlin.test.Test
import kotlin.test.assertEquals

class LogDslTest {
    @Test
    fun `should allow log`() {
        val job = etl {
            log("path")
            extract { csv("data.csv") }
            schema {
                field("fieldName") {
                    notEmpty()
                }
            }
            transform {
                clean {
                    trim("fieldName")
                }
            }
            load {
                csv("filename.csv") {
                    //overwrite()
                }
            }
        }
        val load = job.load
        assertEquals("filename.csv", load.csv.fileName)
    }
}
