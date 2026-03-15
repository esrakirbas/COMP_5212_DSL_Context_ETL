import etl.dsl.etl
import etl.model.CsvSource
import etl.model.ExcelSource
import etl.model.JsonSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class EtlAndExtractDslTest {
    @Test
    fun `should have single source`() {
        val job = etl {
            extract { csv("data.csv") }
            schema {field("fieldName") {} }
            transform {  }
            load { csv("filename") {}
            }
        }
        assertEquals(1, job.sources.size)
    }

    @Test
    fun `should have csv source as the first file`() {
        val job = etl {
            extract { csv("data.csv") }
            schema {field("fieldName") {}}
            transform {  }
            load { csv("filename") {} }
        }
        val firstSource = job.sources.first()
        assert(firstSource is CsvSource)
    }

    @Test
    fun `should have csv json and excel in order`() {
        val job = etl {
            extract {
                csv("data_csv.csv")
                json("data_json.csv")
                excel("data_excel.csv")
            }
            schema {field("fieldName") {}}
            transform {  }
            load { csv("filename") {} }
        }
        assertEquals(3, job.sources.size)
        val firstSource = job.sources.first()
        assert(firstSource is CsvSource)
        val secondSource = job.sources[1]
        assert(secondSource is JsonSource)
        val thirdSource = job.sources[2]
        assert(thirdSource is ExcelSource)
    }

    @Test
    fun `should create json source with correct path`() {
        val job = etl {
            extract { json("data_json.json") }
            schema {field("fieldName") {}}
            transform {  }
            load { csv("filename") {} }
        }
        val firstSource = job.sources.first()
        assertIs<JsonSource>(firstSource)
        assertEquals("data_json.json", firstSource.path)
    }

    @Test
    fun `should create csv source with correct path`() {
        val job = etl {
            extract { csv("data.csv") }
            schema {field("fieldName") {}}
            transform {  }
            load { csv("filename") {} }
        }
        val source = job.sources.first()
        assertIs<CsvSource>(source)
        assertEquals("data.csv", source.path)
    }

    @Test
    fun `should create excel source with correct path`() {
        val job = etl {
            extract { excel("data_xsl.xsl") }
            schema {field("fieldName") {}}
            transform {  }
            load { csv("filename") {} }
        }
        val source = job.sources.first()
        assertIs<ExcelSource>(source)
        assertEquals("data_xsl.xsl", source.path)
    }
}