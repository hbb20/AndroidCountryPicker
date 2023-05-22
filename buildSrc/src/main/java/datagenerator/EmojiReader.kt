import datagenerator.CPFilePath
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.file.Files
import java.nio.file.Paths

class EmojiReader(val dataGeneratorRootDir: String) {
    companion object{
        object Header {
            const val ALPHA_2 = "alpha_2"
            const val FLAG_EMOJI = "flag_emoji"
        }
    }
    fun read(filePath: String = "$dataGeneratorRootDir/${CPFilePath.flagEmojiCsv}"): Map<String, String> {
        val reader = Files.newBufferedReader(Paths.get(filePath))
        // parse the file into csv values
        val csvParser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        val result = mutableMapOf<String, String>()
        for (record in csvParser) {
            result[record[Header.ALPHA_2]] = record[Header.FLAG_EMOJI]
        }
        return result
    }
}