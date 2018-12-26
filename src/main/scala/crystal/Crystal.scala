package crystal

import java.io._

import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}

import scala.io.Source
import scala.util.Random

object Crystal extends App {

    override def main(args: Array[String]): Unit = {
        println("============== START")
        val files = List("female", "male")
        val langs = List(
            new Lang("en", 5),
            new Lang("ko", 1),
            new Lang("ja", 1),
            )
        files.foreach(update(_, langs))
        println("============== DONE")
    }

    private def update(fileName: String, langs: List[Lang]): Unit = {
        val file = new File(s"src/main/resources/csv/$fileName.xlsx")
        val fis = new FileInputStream(file)
        val wb = new XSSFWorkbook(fis)
        val sheet = wb.getSheetAt(0)
        val rowIterator = sheet.iterator
        while (rowIterator.hasNext) {
            val row = rowIterator.next
            if (row.getCell(0) != null && row.getCell(1) == null) {
                val name = row.getCell(0).toString
                for ((lang, i) <- langs.view.zipWithIndex) {
                    row.createCell(i + 1).setCellValue(getMeaning(lang, name))
                }
            }
        }

        convertToCsv(sheet, fileName)

        fis.close()
        val fos = new FileOutputStream(file)
        wb.write(fos)
        fos.close()
    }

    private def getMeaning(lang: Lang, name: String): String = {
        val file = s"src/main/resources/meaning-templates/${lang.code}-template-${Random.nextInt(lang.templateCount) + 1}.txt"
        val line = Source.fromFile(new File(file)).getLines.next()
        line.replaceAllLiterally("{name}", name)
    }

    private def convertToCsv(sheet: XSSFSheet, fileName: String): Unit = {
        val rowIterator = sheet.iterator()
        val sb: StringBuffer = new StringBuffer
        while (rowIterator.hasNext) {
            val row = rowIterator.next
            val cellIterator = row.cellIterator
            while (cellIterator.hasNext) {
                val cell = cellIterator.next
                sb.append(s""""${cell.getStringCellValue}"""")
                sb.append(",")
            }
            sb.append("\n")
        }
        val file = new File(s"src/main/resources/csv/$fileName.csv")
        val bw = new BufferedWriter(new FileWriter(file))
        bw.write(sb.toString)
        bw.flush()
        bw.close()
    }
}

class Lang(
    val code: String,
    val templateCount: Int,
)
