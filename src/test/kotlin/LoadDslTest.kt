import etl.dsl.DslException
import etl.dsl.etl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoadDslTest {
    @Test
    fun `should give error if there is no load part`() {
        assertFailsWith<DslException> {
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
                }
            }
        }
    }

    @Test
    fun `should give error if there is no csv part`() {
        assertFailsWith<DslException> {
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
                }
                load { }
            }
        }
    }

    @Test
    fun `should give error if there are more than one csv`() {
        assertFailsWith<DslException> {
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
                }
                load {
                    csv("filename") {
                        overwrite()
                    }
                    csv("filename2") {
                    }
                }
            }
        }
    }

    @Test
    fun `should allow load and csv`() {
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
            load {
                csv("filename") {
                    //overwrite()
                }
            }
        }
        val load = job.load
        assertEquals("filename", load.csv.fileName)
    }

    @Test
    fun `should give error in case of missing filename in csv part`() {
        assertFailsWith<DslException> {
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
                }
                load {
                    csv
                }
            }
        }
    }

    @Test
    fun `should allow overwrite in load`() {
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
            load {
                csv("filename") {
                    overwrite()
                }
            }
        }
        assertEquals(true, job.load.csv.overwrite)
    }

    @Test
    fun `should allow without overwrite in load`() {
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
            load {
                csv("filename") {
                }
            }
        }
        assertEquals(false, job.load.csv.overwrite)
    }
}
