import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.file.Files
import java.nio.file.Paths


private const val ALPHA_2 = "country_code"
private const val NAME = "country_name"
private const val ALPHA_3 = "country_alpha3_code"
private const val NUMERIC_CODE = "country_numeric_code"
private const val CAPITAL_ENGLISH_NAME = "capital"
private const val DEMONYM = "country_demonym"
private const val AREA = "total_area"
private const val POPULATION = "population"
private const val IDD_CODE = "idd_code"
private const val CURRENCY_CODE = "currency_code"
private const val CURRENCY_NAME = "currency_name"
private const val CURRENCY_SYMBOL = "currency_symbol"
private const val LANG_CODE = "lang_code"
private const val LANG_NAME = "lang_name"
private const val CCTLD = "cctld"

class IP2LocationInfoReader(val rootDir: String) {

    fun read(infoFilePath: String = "$rootDir/data/ip2location/IP2LOCATION-COUNTRY-INFORMATION.CSV"): MutableMap<String, IP2LocationInfoCountry> {
        val reader = Files.newBufferedReader(Paths.get(infoFilePath))
        // parse the file into csv values
        val csvParser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )
        val result = mutableMapOf<String, IP2LocationInfoCountry>()
        for (csvRecord in csvParser) {
            val country =
                IP2LocationInfoCountry(
                    alpha2 = csvRecord[ALPHA_2],
                    alpha3 = csvRecord[ALPHA_3],
                    englishName = csvRecord[NAME],
                    demonym = csvRecord[DEMONYM],
                    numericCode = csvRecord[NUMERIC_CODE].toInt(),
                    capitalEnglishName = csvRecord[CAPITAL_ENGLISH_NAME],
                    areaKM2 = csvRecord[AREA],
                    population = csvRecord[POPULATION],
                    currencyCode = csvRecord[CURRENCY_CODE],
                    currencyName = csvRecord[CURRENCY_NAME],
                    currencySymbol = csvRecord[CURRENCY_SYMBOL],
                    cctld = csvRecord[CCTLD]
                )
            result.put(country.alpha2, country)
        }
        return result
    }

}

class IP2LocationInfoCountry(
    val alpha2: String,
    val alpha3: String,
    val englishName: String,
    val demonym: String,
    val numericCode: Int,
    val capitalEnglishName: String,
    val areaKM2: String,
    val population: String,
    val currencyCode: String,
    val currencyName: String,
    val currencySymbol: String,
    val cctld: String
)

