# CurrencyRateApp
This project demonstrate currency rates conversion app.</br>
Every 1 second new rates information is fetched from the free rates remote api at https://revolut.duckdns.org/</br>
You can click a rate from the list and it becomes your base rate for the conversion.
Use your keyboard to convert the selected base rate to all other currencies LIVE!</br></br>
<a href="url"><img src="https://github.com/RonyBrosh/CurrencyRateApp/blob/master/images/CurrencyRateApp.gif" align="left" height="480" width="270" ></a>
</br></br>

In this project I used:
* Clean architecture principles to separate the app layers dependencies and abstractions 
* MVVM pattern to connect the domain layer with presentation layer
* RxJava and retrofit for Back-end communication
* Junit4 and Espresso for Instrumentation tests (UI-testing and AndroidTest)
* Junit5 and Mockito for Local unit tests

### Running/Testing the app
Checkout the repository (or download), sync and deploy to an Android emulator or real device.
