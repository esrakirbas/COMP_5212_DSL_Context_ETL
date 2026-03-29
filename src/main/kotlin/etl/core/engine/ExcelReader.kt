package etl.core.engine

import etl.core.model.Record
import etl.util.Logger
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File

object ExcelReader {

    private val logger = Logger.forClass(ExcelReader::class)

    fun readExcel(path: String): List<Record> {
        logger.info("Opening Excel File : $path")

        val file = File(path)
        if (!file.exists()) {
            logger.error("Excel file not found: $path")
            return emptyList()
        }

        WorkbookFactory.create(file).use { workbook ->

            val sheet = workbook.getSheetAt(0)
            val rowCount = sheet.physicalNumberOfRows
            logger.info("Excel File Opened: $path Extracting Excel Records. Row count: $rowCount")

            val headerRow = sheet.getRow(0) ?: return emptyList()
            //assuming that headers are strings
            val headers = headerRow.map { cell -> cell.stringCellValue }

            val records = mutableListOf<Record>()

            for (i in 1..sheet.lastRowNum) {
                val row = sheet.getRow(i) ?: continue

                val record: Record = mutableMapOf()

                for ((index, header) in headers.withIndex()) {
                    val cell = row.getCell(index)

                    val value = when (cell?.cellType) {
                        CellType.STRING -> cell.stringCellValue
                        CellType.NUMERIC -> {
                            val num = cell.numericCellValue
                            //sometimes integer values come as a double that's why we are doing this
                            if (num % 1 == 0.0) num.toInt().toString() else num.toString()
                        }
                        CellType.BOOLEAN -> cell.booleanCellValue.toString()
                        CellType.BLANK, null -> ""
                        else -> cell.toString()
                    }

                    record[header] = value
                }

                record["_source"] = path
                record["_recIndex"] = i.toString()
                records.add(record)
            }

            return records
        }
    }
}