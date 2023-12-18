## WeatherForcast
The WeatherForcast Android application retrieves real-time weather details according to the user's present location and showcases the current weather detail and weather history for the logged-in user. 

### **Features** 
- Splash
- Login
- Signup - The Signup screen for Account Registration.
- Current Weather - Display weather information on the Weather screen, including the current location (city and country), temperature in Celsius, weather status, sunrise, and sunset time etc.
- Weather history - Show previously retrieved weather data as a history for the user who is logged in.


### **Technical Description** 

- **Programming Language** - Kotlin (1.8.0)


- **Android Studio** - Giraffe 2022.3.1
- **Architecture Design pattern** -  MVVM (Model-View-ViewModel)
- **Libraries Used** -  Navigation Component, ViewModel,LiveData, Room Database, Encrypted SharedPreference, SQLCipher, Retrofit, Gson, Glide, Material Design etc.
- **WeatherApi Url** - https://openweathermap.org
- **Device Supported** - Android Phone (Portrait Orientation)
  

### **Security Measures** 

- **Proguard Rule** - For the **release** build, enable **minifyEnabled** and set **shrinkResources** to true to **obfuscate** code and reduce the application size. Additionally, incorporate **Proguard rules** to preserve **Data classes** and prevent app crashes.

- **Encrypted SharedPreference** - Utilized Encrypted SharedPreference to securely store sensitive data within the app locally.
- **Database Encryption** - Utilized **SQLCipher** for encrypting Room Databases.
- **Secured API keys** - API keys and other sensitive data were stored and accessed using `buildConfigField`, reading from properties files that were added to the **gitignore**.
  
**NOTE:** Update `DB_PassPhrase` and `API_KEY` in `local.properties` file with your own Database Encryption Key and Open Weather API key 

 
  
### **Project Structure**

- **common** -  This directory contains a set of commonly used utility files and  validation helper class for form validation purposes. 

- **data** - This folder has different kinds of files like data models, repositories, DTOs, APIs, and other files related to Model Layer.
- **di** - This folder containes Hilt Dependency Injection's module classes.
- **ui** - This folder has various UI screens, including Activity, Fragments, and ViewModel classes.

### **Unit Test Case Report** 

Above 95 % Test Coverage.

<img width="691" alt="unit_test_case_report" src="https://github.com/Mohammad-Perennialsys/WeatherForcast/assets/153976694/3385ad29-f28d-4d5b-ac79-61fe326b5cbe">



### **App Screenshot**

![splash_screen](https://github.com/Mohammad-Perennialsys/WeatherForcast/assets/153976694/16db93d6-0946-453a-8347-8d0ffb5bc639)
![login_screen](https://github.com/Mohammad-Perennialsys/WeatherForcast/assets/153976694/952b96bb-827d-4d03-8924-c278709a5265)
![signup_screen](https://github.com/Mohammad-Perennialsys/WeatherForcast/assets/153976694/225ff9bd-d0a3-4fea-ab6f-e467c6139246)
![current_weather](https://github.com/Mohammad-Perennialsys/WeatherForcast/assets/153976694/5221b0b7-738d-484b-b395-e71a96c1f30e)
![weather_history](https://github.com/Mohammad-Perennialsys/WeatherForcast/assets/153976694/2efd5528-701b-48bc-b2ed-d2dd7db9cf08)
![weather_history_empty](https://github.com/Mohammad-Perennialsys/WeatherForcast/assets/153976694/46852bf3-2114-42df-b04b-f99c5be0ca34)


**Note**: Being only contributor I have put all code in main branch. 

