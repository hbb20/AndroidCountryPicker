import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import datagenerator.CPFilePath
import java.nio.file.Files
import java.nio.file.Paths

class PhoneCodeReader(val dataGeneratorRootDir: String) {
    companion object{
        object Header {
            const val ALPHA_2 = "alpha_2"
            const val PHONE_CODE = "phone_code"
        }
    }
    fun read(filePath: String = "$dataGeneratorRootDir/${CPFilePath.phoneCodesCsv}"): Map<String, Int> {
        val reader = Files.newBufferedReader(Paths.get(filePath))
        // parse the file into csv values
        val csvParser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        val result = mutableMapOf<String, Int>()
        for (record in csvParser) {
            result[record[Header.ALPHA_2]] = record[Header.PHONE_CODE].toInt()
        }
        return result
    }
}