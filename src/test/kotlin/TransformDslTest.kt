import  etl.core.dsl.DslException
import  etl.core.dsl.etl
import etl.core.model.DataCleaningOperation
import etl.core.model.DataFilteringCondition
import kotlin.test.*

class TransformDslTest {
    @Test
    fun `should not give error if there is no transform part`() {
        etl {
            extract { csv("data.csv") }
            schema {
                field("fieldName") {
                    notEmpty()
                }
            }
            load { csv("filename.csv") {} }
        }
        assert(true) //no error till here
    }

    @Test
    fun `should allow clean and transform`() {
        val job = etl {
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
            load { csv("filename.csv") {} }
        }
        val transform = job.transform
        assertNotNull(transform!!.clean?.operations)
        assertEquals(1, transform.clean.operations.size)
    }

    @Test
    fun `should allow titleCase`() {
        val etl = etl {
            extract {
                json("products.json")
            }
            schema {
                field("productName") {}
                field("brand") {}
                field("price") {}
            }
            transform {
                clean {
                    toTitleCase("brand")
                }
            }
            load { csv("filename.csv") {} }
        }
        val operation = etl.transform!!.clean?.operations?.first()
        assertEquals(true, operation is DataCleaningOperation.ToTitleCase)
    }

    @Test
    fun `should allow lowerCase`() {
        val etl = etl {
            extract {
                json("products.json")
            }
            schema {
                field("productName") {}
                field("brand") {}
                field("price") {}
                field("description") {}
            }
            transform {
                clean {
                    toLowerCase("description")
                }
            }
            load { csv("filename.csv") {} }
        }
        val operation = etl.transform!!.clean?.operations?.first()
        assertEquals(true, operation is DataCleaningOperation.ToLowerCase)
    }

    @Test
    fun `should allow defaultIfEmpty`() {
        val etl = etl {
            extract {
                json("products.json")
            }
            schema {
                field("productName") {}
                field("brand") {}
                field("price") {}
                field("description") {}
            }
            transform {
                clean {
                    defaultIfEmpty("price", "0.00")
                }
            }
            load { csv("filename.csv") {} }
        }
        val operation = etl.transform!!.clean?.operations?.first()
        assertEquals(true, operation is DataCleaningOperation.DefaultIfEmpty)
        if (operation is DataCleaningOperation.DefaultIfEmpty) {
            assertEquals("0.00", operation.defaultValue)
        }
    }

    @Test
    fun `should allow replace`() {
        val etl = etl {
            extract {
                json("products.json")
            }
            schema {
                field("productName") {}
                field("brand") {}
                field("price") {}
                field("description") {}
            }
            transform {
                clean {
                    replace("price", "0", "0.00")
                }
            }
            load { csv("filename.csv") {} }
        }
        val operation = etl.transform!!.clean?.operations?.first()
        assertEquals(true, operation is DataCleaningOperation.Replace)
        if (operation is DataCleaningOperation.Replace) {
            assertEquals("0.00", operation.replace)
        }
    }

    @Test
    fun `should give error if there are duplicate clean parts`() {
        assertFailsWith<DslException> {
            etl {
                extract { csv("data.csv") }
                schema {
                    field("fieldName") {
                        notEmpty()
                    }
                }
                transform {
                    clean { trim("fieldName") }
                    clean { toTitleCase("fieldName") }
                }
                load { csv("filename.csv") {} }
            }
        }
    }

    @Test
    fun `should allow filter in transform`() {
        etl {
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
                filter { }
            }
            load { csv("filename.csv") {} }
        }
        assertTrue { true }
    }

    @Test
    fun `should give error if there are duplicate filter parts`() {
        assertFailsWith<DslException> {
            etl {
                extract { csv("data.csv") }
                schema {
                    field("fieldName") {
                        notEmpty()
                    }
                }
                transform {
                    filter { }
                    filter { }
                }
                load { csv("filename.csv") {} }
            }
        }
    }

    @Test
    fun `should allow Equals in filter`() {
        val job = etl {
            extract { csv("customer_address.csv") }
            schema {
                field("street") {
                    notEmpty()
                }
            }
            transform {
                filter { equals("city", "Thunder Bay") }
            }
            load { csv("filename.csv") {} }
        }
        assertEquals(1, job.transform!!.filter?.conditions?.size)
        val equalsCond = job.transform.filter?.conditions?.first()
        if (equalsCond is DataFilteringCondition.Equals) {
            assertEquals("Thunder Bay", equalsCond.value)
        } else {
            fail("")
        }
    }

    @Test
    fun `should allow NotEquals in filter`() {
        val job = etl {
            extract { csv("customer_address.csv") }
            schema {
                field("street") {
                    notEmpty()
                }
            }
            transform {
                filter { notEquals("city", "Vancouver") }
            }
            load { csv("filename.csv") {} }
        }
        assertEquals(1, job.transform!!.filter?.conditions?.size)
        val notEqualsCond = job.transform.filter?.conditions?.first()
        if (notEqualsCond is DataFilteringCondition.NotEquals) {
            assertEquals("Vancouver", notEqualsCond.value)
        } else {
            fail("")
        }
    }

    @Test
    fun `should allow Empty in filter`() {
        val job = etl {
            extract { csv("customer_address.csv") }
            schema {
                field("street") {
                    notEmpty()
                }
            }
            transform {
                filter { empty("city") }
            }
            load { csv("filename.csv") {} }
        }
        assertEquals(1, job.transform!!.filter?.conditions?.size)
        val emptyCond = job.transform.filter?.conditions?.first()
        if (emptyCond is DataFilteringCondition.Empty) {
            assertEquals("city", emptyCond.field)
        } else {
            fail("")
        }
    }

    @Test
    fun `should allow NotEmpty in filter`() {
        val job = etl {
            extract { csv("customer_address.csv") }
            schema {
                field("street") {
                    notEmpty()
                }
            }
            transform {
                filter { notEmpty("city") }
            }
            load { csv("filename.csv") {} }
        }
        assertEquals(1, job.transform!!.filter?.conditions?.size)
        val notEmptyCond = job.transform.filter?.conditions?.first()
        if (notEmptyCond is DataFilteringCondition.NotEmpty) {
            assertEquals("city", notEmptyCond.field)
        } else {
            fail("")
        }
    }

    @Test
    fun `should allow GreaterThan in filter`() {
        val job = etl {
            extract { csv("customer_address.csv") }
            schema {
                field("street") {
                    notEmpty()
                }
            }
            transform {
                filter { greaterThan("last_update_date", "20260101") }
            }
            load { csv("filename.csv") {} }
        }
        assertEquals(1, job.transform!!.filter?.conditions?.size)
        val greaterThanCond = job.transform.filter?.conditions?.first()
        if (greaterThanCond is DataFilteringCondition.GreaterThan) {
            assertEquals("20260101", greaterThanCond.value)
        } else {
            fail("")
        }
    }

    @Test
    fun `should allow LessThan in filter`() {
        val job = etl {
            extract { csv("customer_address.csv") }
            schema {
                field("street") {
                    notEmpty()
                }
            }
            transform {
                filter { lessThan("last_update_date", "20260101") }
            }
            load { csv("filename.csv") {} }
        }
        assertEquals(1, job.transform!!.filter?.conditions?.size)
        val lessThanCond = job.transform.filter?.conditions?.first()
        if (lessThanCond is DataFilteringCondition.LessThan) {
            assertEquals("20260101", lessThanCond.value)
        } else {
            fail("")
        }
    }

    @Test
    fun `should allow Contains in filter`() {
        val job = etl {
            extract { csv("customer_address.csv") }
            schema {
                field("street") {
                    notEmpty()
                }
            }
            transform {
                filter { contains("address_line", "Oliver") }
            }
            load { csv("filename.csv") {} }
        }
        assertEquals(1, job.transform!!.filter?.conditions?.size)
        val containsCond = job.transform.filter?.conditions?.first()
        if (containsCond is DataFilteringCondition.Contains) {
            assertEquals("address_line", containsCond.field)
            assertEquals("Oliver", containsCond.value)
        } else {
            fail("")
        }
    }

    @Test
    fun `should allow multiple conditions in filter`() {
        val job = etl {
            extract { csv("customer_address.csv") }
            schema {
                field("street") {
                    notEmpty()
                }
            }
            transform {
                filter {
                    equals("city", "Thunder Bay")
                    contains("address_line", "Oliver")
                }
            }
            load { csv("filename.csv") {} }
        }
        assertEquals(2, job.transform!!.filter?.conditions?.size)
    }
}
