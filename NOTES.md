# Notes for Reach Products application
Test Challenge for Reach

## Highlights

* The architecture is MVVM with LiveData.
* I used Retrofit 2, Coroutines, Coroutines Flow, Dagger 2, AndroidX, SqlDelight, Navigation and 
Material components.
* ViewModel and Mapper is Unit tested, full coverage is not guaranteed due to time constraints.
 The Mappers use a functional style.
* Espresso tests to cover some situations. I used Barista to make it more readable.
* Data package contains everything related to networking and data storage. The caching strategy was
network first. This means it tries the network first, if succeeds, it caches and shows the result.
If not, it checks SqlDelight storage and shows the result through OfflineException, together with a 
modal error view. The user can retry the request manually. 
* The cart is also saved into the database and is reloaded on every screen opening. This simplified 
state management and configuration changes.

## Limitations

* I tried to show off as much skills as possible, so I added the detail screen, SqlDelight, etc. But
 I had to draw the line somewhere, so I DO NOT consider the app production ready (such challenges
 never should be). So the app is not fully covered with tests, and there are other limitations.
* Coming back from Details screen the list reloads. It should either load only when the ViewModel
is created (not ideal either), or run the query in the background and use DiffUtil (but I really ran
 out of time).

## Structure

* Note the use of buildSrc, the standard way to add dependencies with Gradle. In a modularized
setting I would also add reusable Gradle plugins here, so that the module build files are tiny.
* Most of the styles, base classes, etc. are coming from BaBeStudiosBase, my base library. I
overrode it in a few instances.
* Data layer goes into the data package. Within that there is a local package for database or 
SharedPreferences access. There is a network package for API calls, data transfer classes and 
mappers. The mappers map DTOs received from APIs to model objects. Finally there is a res package 
for resource access, which contains resource abstractions to ease ViewModel Unit testing.
* Dependency related files go to the 'di' package. Recently started to use Hilt, so this part is
fairly simple now. The heavy lifting is done by Hilt in the background through some annotations on
the App, the Activity, the Fragments and the ViewModel.
* Model package contains classes for the app. Data transfer objects are mapped to these, see above.
* Navigation related files go to the navigation package. This separation was needed for
modularization only, but I kept it for consistency. Not really needed in one/two screen project
although it's useful for testing.
* Views, ViewModels and State go into the ui package. There is one Activity per feature (so one
here), the screens are represented by Fragments. There is one ViewModel per Activity, which also
holds the state. Would add ViewModels when these becoming more complex (this app is on the verge of 
that).
* State is exposed through LiveData.
* I started to use Groupie library for lists with this project.

## Specific for the challenge

The design and the use cases are minimalistic due to time constraints. I omitted cart information on
the main screen, amount details on the cart, etc., so the app is not realistic, it's only an MVP,
focusing on the technical details, maintainability and extensibility.
There is no session, so the cart is not emptied on session expiry. It will load whenever you open
the app again.
The discounts are added to the product mapper, so ultimately attached to the products. In a real app
scenario this might not work like this, depending on how you want to extend this functionality, but
for now this is the best solution I could come up with and it's extensible.
I had no more time to add more tests, sorry. The existing ones are not very solid (depending on what
you want to see), but offer some coverage. I would cover the ViewModel, the Repository and the 
database next.
