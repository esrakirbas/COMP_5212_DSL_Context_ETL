import etl.dsl.DslException
import etl.dsl.etl
import etl.model.MaxLength
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SchemaDslTest {
    @Test
    fun `should create schema with one field`() {
        val job = etl {
            extract { csv("data.csv") }
            schema {
                field("fieldName") {
                    notEmpty()
                }
            }
        }
        val schema = job.schema
        assertEquals(1, schema.fields.size)
        assertEquals("fieldName", schema.fields.first().name)
    }

    @Test
    fun `should assign validation rules to field`() {
        val job = etl {
            extract { csv("data.csv") }
            schema {
                field("fieldName") {
                    notEmpty()
                    isNumber()
                    rejectIfInvalid()
                }
            }
        }
        val field = job.schema.fields.first()
        assertEquals(3, field.rules.size)
    }

    @Test
    fun `should capture parametrized validation rules`() {
        val job = etl {
            extract { csv("data.csv") }
            schema {
                field("fieldName") {
                    notEmpty()
                    maxLength(15)
                    minLength(1)
                }
            }
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
            }
        }
    }

    @Test
    fun `should fail if no schema definition`() {
        assertFailsWith<DslException> {
            etl {
                extract {
                    csv("data.csv")
                }
            }
        }
    }

}