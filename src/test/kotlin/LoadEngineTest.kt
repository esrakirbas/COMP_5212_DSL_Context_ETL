import etl.core.dsl.DslException
import etl.core.engine.LoadEngine
import etl.core.model.Csv
import etl.core.model.Load
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoadEngineTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `should write records to csv file`() {

        val filePath = tempDir.resolve("output.csv").toString()

        val records = listOf(
            mutableMapOf("name" to "Esra Kirdi", "city" to "Thunder Bay"),
            mutableMapOf("name" to "Bahar Kirdi", "city" to "Burnaby")
        )

        val load = Load(
            Csv(
                fileName = filePath,
                overwrite = true
            )
        )
        //if needed ... println(tempDir.toAbsolutePath())
        LoadEngine.load(records, load)

        val file = File(filePath)

        // file exists
        assertTrue(file.exists())

        val lines = file.readLines()

        // header + 2 rows
        assertEquals(3, lines.size)

        // header check
        assertEquals("name,city", lines[0])

        // content check
        assertTrue(lines.contains("Esra Kirdi,Thunder Bay"))
        assertTrue(lines.contains("Bahar Kirdi,Burnaby"))
    }

    @Test
    fun `should throw exception when file exists and overwrite is false`() {

        val file = tempDir.resolve("output.csv").toFile()

        // create existing file
        file.writeText("old content")

        val records = listOf(
            mutableMapOf("name" to "Oz")
        )

        val load = Load(Csv(
            fileName = file.absolutePath,
            overwrite = false)
        )

        assertThrows<DslException> {
            LoadEngine.load(records, load)
        }
    }

    @Test
    fun `should overwrite when file exists and overwrite is true`() {

        val file = tempDir.resolve("output.csv").toFile()

        // create existing file
        file.writeText("old content")

        val records = listOf(
            mutableMapOf("name" to "Oz")
        )

        val load = Load(Csv(
            fileName = file.absolutePath,
            overwrite = true)
        )
        LoadEngine.load(records, load)

        val lines = file.readLines()

        // header + 1 row
        assertEquals(2, lines.size)

        // header check
        assertEquals("name", lines[0])

        // content check
        assertTrue(lines.contains("Oz"))
    }

    @Test
    fun `should not to any file process if the records are empty`() {

        val file = tempDir.resolve("output.csv").toFile()

        val records = emptyList<MutableMap<String, String>>()

        val load = Load(Csv(
            fileName = file.absolutePath,
            overwrite = true)
        )
        LoadEngine.load(records, load)

        // file should not exist
        assertTrue(!file.exists())
    }
}
