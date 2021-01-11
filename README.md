# WeatherAppDemo
This is a simple demo App that displays the weather information based on the user's current location.

# Built with
- Android Studio
- Java
- Retrofit
- Dagger2
- MVVM design pattern
- LiveData & RxJava

# Tested with
Emulator Pixel API 27

# Implementation Flow
On App launch
- Runtime permission for GPS location

- Start Service to get current location in background if below conditions are met:
  - Check for GPS provider availability
  - Check Airplane mode is disable
  - Check Internet available
  - If any of the above is not true -> allow user to easily set feature in Settings with a dialog message
  - User location updated every 5 seconds
  - Notify the user if any location error

- Create a repository to fetch data from
  - Used HttpURLConnection to connect to remote service
  - Used Jackson library for parsing json
  - Implemented Dependency Injection (manual) for loose coupling
  - Used AsyncTask & ProgressBar to make call in background 
  
  # Usage
  Launch the App.  
  The weather information (town, description, temperature, pressure, humidity, and visibility) is then displayed immediately.  
  If this is the first launch, a popup message is prompted to the user to grant the location permission.  
  The update button in the App bar is used to manually update the weather information on the screen.  
  The start button restarts the App. This is used after a location or network error.  
  The exit button in the App bar closes the App.  
 
