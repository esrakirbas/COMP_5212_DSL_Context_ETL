import etl.core.dsl.DslException
import etl.core.dsl.etl
import etl.core.model.CsvSource
import etl.core.model.ExcelSource
import etl.core.model.JsonSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs


class EtlAndExtractDslTest {
    @Test
    fun `should give error if there is no extract part`() {
        assertFailsWith<DslException> {
            etl {
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
                    csv("data.csv") {}
                }
            }
        }
    }

    @Test
    fun `should give error if there are duplicate sources csv`() {
        assertFailsWith<DslException> {
            etl {
                extract {
                    csv("a.csv")
                    csv("a.csv")
                }
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
                    csv("data.csv") {}
                }
            }
        }
    }

    @Test
    fun `should give error if there are duplicate sources json`() {
        assertFailsWith<DslException> {
            etl {
                extract {
                    json("a.json")
                    json("a.json")
                }
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
                    csv("data.csv") {}
                }
            }
        }
    }

    @Test
    fun `should give error if there are duplicate sources excel`() {
        assertFailsWith<DslException> {
            etl {
                extract {
                    excel("a.xsl")
                    excel("a.xsl")
                }
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
                    csv("data.csv") {}
                }
            }
        }
    }

    @Test
    fun `should give error if file extension is invalid excel`() {
        assertFailsWith<DslException> {
            etl {
                extract {
                    excel("a.csv")
                }
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
                    csv("data.csv") {}
                }
            }
        }
    }

    @Test
    fun `should give error if file extension is invalid csv`() {
        assertFailsWith<DslException> {
            etl {
                extract {
                    csv("a.txt")
                }
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
                    csv("data.csv") {}
                }
            }
        }
    }

    @Test
    fun `should give error if file extension is invalid JSON`() {
        assertFailsWith<DslException> {
            etl {
                extract {
                    json("a.html")
                }
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
                    csv("data.csv") {}
                }
            }
        }
    }

    @Test
    fun `should give error if there is no source`() {
        assertFailsWith<DslException> {
            val etl = etl {
                extract { }
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
                    csv("data.csv") {}
                }
            }
        }
    }

    @Test
    fun `should have single source`() {
        val job = etl {
            extract { csv("data.csv") }
            schema { field("fieldName") {} }
            transform { }
            load {
                csv("filename.csv") {}
            }
        }
        assertEquals(1, job.extract.sources.size)
    }

    @Test
    fun `should have csv source as the first file`() {
        val job = etl {
            extract { csv("data.csv") }
            schema { field("fieldName") {} }
            transform { }
            load { csv("filename.csv") {} }
        }
        val firstSource = job.extract.sources.first()
        assert(firstSource is CsvSource)
    }

    @Test
    fun `should have csv json and excel in order`() {
        val job = etl {
            extract {
                csv("data_csv.csv")
                json("data_json.json")
                excel("data_excel.xlsx")
            }
            schema { field("fieldName") {} }
            transform { }
            load { csv("filename.csv") {} }
        }
        assertEquals(3, job.extract.sources.size)
        val firstSource = job.extract.sources.first()
        assert(firstSource is CsvSource)
        val secondSource = job.extract.sources[1]
        assert(secondSource is JsonSource)
        val thirdSource = job.extract.sources[2]
        assert(thirdSource is ExcelSource)
    }

    @Test
    fun `should create json source with correct path`() {
        val job = etl {
            extract { json("data_json.json") }
            schema { field("fieldName") {} }
            transform { }
            load { csv("filename.csv") {} }
        }
        val firstSource = job.extract.sources.first()
        assertIs<JsonSource>(firstSource)
        assertEquals("data_json.json", firstSource.path)
    }

    @Test
    fun `should create csv source with correct path`() {
        val job = etl {
            extract { csv("data.csv") }
            schema { field("fieldName") {} }
            transform { }
            load { csv("filename.csv") {} }
        }
        val source = job.extract.sources.first()
        assertIs<CsvSource>(source)
        assertEquals("data.csv", source.path)
    }

    @Test
    fun `should create excel source with correct path`() {
        val job = etl {
            extract { excel("data.xls") }
            schema { field("fieldName") {} }
            transform { }
            load { csv("filename.csv") {} }
        }
        val source = job.extract.sources.first()
        assertIs<ExcelSource>(source)
        assertEquals("data.xls", source.path)
    }
}