import etl.core.dsl.etl
import etl.core.model.MaxLength
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SchemaDslTest {
    @Test
    fun `should create schema with one field`() {
        val job =  etl {
            extract { csv("data.csv") }
            schema {
                field("fieldName") {
                    notEmpty()
                }
            }
            transform { }
            load { csv("filename.csv") {} }
        }
        val schema = job.schema
        assertEquals(1, schema.fields.size)
        assertEquals("fieldName", schema.fields.first().name)
    }

    @Test
    fun `should assign validation rules to field`() {
        val job =  etl {
            extract { csv("data.csv") }
            schema {
                field("fieldName") {
                    notEmpty()
                    isNumber()
                }
            }
            transform { }
            load { csv("filename.csv") {} }
        }
        val field = job.schema.fields.first()
        assertEquals(3, field.rules.size)
    }

    @Test
    fun `should capture parametrized validation rules`() {
        val job =  etl {
            extract { csv("data.csv") }
            schema {
                field("fieldName") {
                    notEmpty()
                    maxLength(15)
                    minLength(1)
                }
            }
            transform { }
            load { csv("filename.csv") {} }
        }
        val maxLengthRule = job.schema.fields.first().rules.get(1) //maxLength rule
        if (maxLengthRule is MaxLength) {
            assertEquals(15, maxLengthRule.length)
        }
    }

    @Test
    fun `should fail in case of duplicate rules defined`() {
        assertFailsWith<IllegalStateException> {
             etl {
                extract {
                    csv("data.csv")
                }
                schema {
                    field("fieldName") {
                        notEmpty()
                    }
                    field("fieldName") {
                        notEmpty()
                    }
                }
                transform { }
                load { csv("filename.csv") {} }
            }
        }
    }

    @Test
    fun `should fail if no fields defined in schema`() {
        assertFailsWith<IllegalStateException> {
             etl {
                extract {
                    csv("data.csv")
                }
                schema {
                }
                transform { }
                load { csv("filename.csv") {} }
            }
        }
    }

    @Test
    fun `should fail if no schema definition`() {
        assertFailsWith<etl.core.dsl.DslException> {
             etl {
                extract {
                    csv("data.csv")
                }
                transform { }
                load { csv("filename.csv") {} }
            }
        }
    }

}