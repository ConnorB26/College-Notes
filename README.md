# [College Notes](https://play.google.com/store/apps/details?id=com.connorb26.notesapp) - Android Application

This project was developed to fill a personal need for an app that combined note taking functionality with the ability to see/edit/add events to the calendar, with extra functionality to input class information and have class times and exams also be added to the calendar. Essentially, this app provides CRUD functionality for notes, classes, and calendar events to manage everything a college student might need, all within a single application instead of separate ones.

Not only was this a project done for practical purposes, it was also done so that I could learn about Android application development along the way. Here's some of the stuff that I learned during development:
* Kotlin
* Jetpack Compose
* Hilt-Dagger
* Android Room
* Software architecture styles (this project uses clean architecture)
* Software architecture design patterns (this project uses MVVM)

### Problems Encountered & Solutions
Since Jetpack Compose is a pretty new technology, there isn't as much documentation and tutorials out there, in addition to basic, expected features.

For example, I wanted to have my notes contain a rich text field, but such a thing doesn't exist natively, and there also isn't any good public solutions out there as well. I create a workaround by creating and implementing my own markdown-style language that visually transformed the text based on the tags you write, but later scrapped that in favor of only having bullet point functionality, which was as easy as doing a simple regex search/replace for dashes at the beginning of a newline, replacing them with a bullet point unicode character (although not a perfect solution).

Overall, a lot of solutions and tutorials I found online were for the outdated, xml system, which made it difficult to learn some things with this new technology.
