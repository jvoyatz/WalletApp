# assignment Wallet

| Accounts                                                                    | Details                                                                   |
|-----------------------------------------------------------------------------|---------------------------------------------------------------------------|
| <img src ="./screenshots/portfolio_dark.png" height = "75%" width ="75%" /> | <img src ="./screenshots/details_dark.png" height = "80%" width ="80%" /> |

## Technologies
- Multiple modules
- Clean Architecture
- MVVM+ (mix of MVVM with MVI)
- Single Activity
- Navigation Component
- Material UI
- Kotlin 
  - Coroutines
  - Flows
- Dependency Injection **(Dagger Hilt)**
- Networking **(Retrofit)** 
- Database **(Room)**
- Unit Testing & UI Testing
  - JUnit4
  - Truth
  - Turbine
  - Mockito/MockK
  - Espresso
- Gradle
  - using Kotlin (.kts) instead of Groovy (autocompletion)
  - Version Catalog (single source of truth for dependencies and plugins versions)
    - with a drawaback (unfortunately), cannot use precompiled script plugins for the several modules.
    - The solution is the **Convention Plugins**


**This app has been designed, built and implemented having in the multi-module and clean approach in mind**

### Modules

**App Module**
> App module is the start point of this project, it is aware of the other features modules provided. It contains the one Activity used in this app and it contains the navigation graph, which is needed to navigate through the features screens of this app. It also depends on the **:data** modules as well as other modules.

**Features Modules**
> These modules are essentially representing the User Interface of this app. They are indendent of each other and they are not aware of the :app module. 
> Specifically, they are aware of ther own navigation graph if exists, of the corresponding :domain module plus other, such as :data module.
> > **Portfolio** :
> > Display user's accounts found in his wallet.
> 
> **Account Details** :
> > Displays extra details for the selected account

**Domain Module**
> Contains the **UseCases** needed by the feature modules to execute certain actions. Apart from that, provide the models which they are provided to the UI as part of the UseCase's response. Finally, the contract (eg AccountRepository) is being defined here (dependency inversion principle).


**Data Module**
> This module is responsible to fetch and return us, the data received/fetched/stored for the user. It knows what are the data sources that can be used to achieve that.

**Core Module**
> Contains code and implementations that they are used throughout the app
> > **Common** Common classes and utilities used throughout out the app -- [it is not android dependent]



### References
- Gradle
  - https://proandroiddev.com/using-version-catalog-on-android-projects-82d88d2f79e5
  - https://proandroiddev.com/single-source-of-truth-for-your-build-scripts-72d584534949
  - https://stackoverflow.com/questions/70550883/warning-the-following-options-were-not-recognized-by-any-processor-dagger-f
  - https://github.com/JakeWharton/SdkSearch/blob/master/gradle/projectDependencyGraph.gradle (needs GraphViz program to be installed in order to run successfully)
  - more TBA