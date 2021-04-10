import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.file.Files
import java.nio.file.Paths

private const val ALPHA_2 = "alpha_2"
private const val PHONE_CODE = "phone_code"

class PhoneCodeReader(val dataGeneratorRootDir: String) {
    fun read(filePath: String = "$dataGeneratorRootDir/data/PhoneCodes.CSV"): Map<String, Int> {
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
            result[record[ALPHA_2]] = record[PHONE_CODE].toInt()
        }
        return result
    }
}