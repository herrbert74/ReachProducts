# GanBreakingBad
Test Challenge using Breaking Bad API for GAN

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
modal error view. The user can retry the request manually. The cart is also saved into the database.

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
* Dependency related files go to the 'di' package. Recently started to use Hilt, so this part is
fairly simple now. The heavy lifting is done by Hilt in the background through some annotations on
the App, the Activity, the Fragments and the ViewModel.
* Navigation related files go to the navigation package. This separation was needed for
modularization only, but I kept it for consistency. Not really needed in one/two screen project
although it's useful for testing.
* Views, ViewModels and State go into the ui package. There is one Activity per feature (so one
here), the screens are represented by Fragments. There is one ViewModel per Activity, which also
holds the state.
* State is exposed through LiveData.
* I started to use Groupie library for lists with this project.
