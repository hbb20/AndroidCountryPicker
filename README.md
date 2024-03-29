![visitors](https://visitor-badge.laobi.icu/badge?page_id=AndroidCountryPicker.readme)
[![Add](https://maven-badges.herokuapp.com/maven-central/com.hbb20/android-country-picker/badge.svg?style={style}) ](https://github.com/hbb20/AndroidCountryPicker/blob/develop/CHANGELOG.md) 


<img height=500 src="https://user-images.githubusercontent.com/4918760/90301130-32916100-de5b-11ea-8238-3f1e03ef325c.png"/>


| Country Picker View | Country Picker Dialog |
| --- | ----------- |
| <img src="https://user-images.githubusercontent.com/4918760/133714158-7f5f7d49-def9-483c-bc3b-8302965b7a0b.gif" width="200"> | <img src="https://user-images.githubusercontent.com/4918760/154889351-687907cb-ee9b-4e90-b276-afc157459f13.gif" width="200"> |

### 1. Add dependency
   - ```groovy
         dependencies {
           implementation 'com.hbb20:android-country-picker:X.Y.Z'
          }
      ```
   - For latest version, [![Add](https://maven-badges.herokuapp.com/maven-central/com.hbb20/android-country-picker/badge.svg?style={style}) ](https://github.com/hbb20/AndroidCountryPicker/blob/develop/CHANGELOG.md)


### 2. Decide your use-case


<details>
<summary><b>Default Country Picker View</b></summary>
i. add following to your XML layout    

```xml
   <com.hbb20.CountryPickerView
   android:id="@+id/countryPicker"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content" />
```
    
ii. modify view / dialog / list config in activity or fragment
```kotlin
private fun setupCountryPickerView() {
        val countryPicker = findViewById<CountryPickerView>(R.id.countryPicker)

        // Modify CPViewConfig if you need. Access cpViewConfig through `cpViewHelper`
        countryPicker.cpViewHelper.cpViewConfig.viewTextGenerator = { cpCountry: CPCountry ->
            "${cpCountry.name} (${cpCountry.alpha2})"
        }
        // make sure to refresh view once view configuration is changed
        countryPicker.cpViewHelper.refreshView()

        // Modify CPDialogConfig if you need. Access cpDialogConfig through `countryPicker.cpViewHelper`
        // countryPicker.cpViewHelper.cpDialogConfig.

        // Modify CPListConfig if you need. Access cpListConfig through `countryPicker.cpViewHelper`
        // countryPicker.cpViewHelper.cpListConfig.

        // Modify CPRowConfig if you need. Access cpRowConfig through `countryPicker.cpViewHelper`
        // countryPicker.cpViewHelper.cpRowConfig.
    }
```

ii. [Read More](https://github.com/hbb20/AndroidCountryPicker/wiki/Country-Picker-View) about Country Picker View and available configuration 
      
</details>
    
<details>
<summary><b>Custom Country Picker View</b></summary>

i. Read how to create your [Custom Country Picker View](https://github.com/hbb20/AndroidCountryPicker/wiki/Custom-Country-Picker-View)

</details>


<details>
<summary><b>Launch Country Picker Dialog</b></summary>
i. add following to your Activity/Fragment    

```kotlin
   context.launchCountryPickerDialog { selectedCountry: CPCountry? ->
     // your code to handle selected country
   }
```

ii. [Read More](https://github.com/hbb20/AndroidCountryPicker/wiki/Country-Picker-Dialog) about CountryPicker Dialog and available configuration 
</details>
    
<details>
<summary><b>Load countries in RecyclerView</b></summary>
i. add following to your Activity/Fragment    

```kotlin
   recyclerView.loadCountries { selectedCountry: CPCountry -> 
     // your code to handle selected country
   }
```

ii. [Read More](https://github.com/hbb20/AndroidCountryPicker/wiki/Country-List-(RecyclerView)) about Country List and available configuration 
 
</details>

### Credits
- [ip2Location](https://www.ip2location.com/) for country data
- Airbnb for [Epoxy Recycler View](https://github.com/airbnb/epoxy) library


