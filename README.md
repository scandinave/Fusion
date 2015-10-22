# Fusion
Never get on your nerves with your test !

## Goal
Fusion simplifies the use of commonly tools dedicated to integration tests. With Fusion you can start writing BDD(Behavior Driven Development) tests with minimal configuration.
Fusion preconfigure tools like JUnit, Cucumber and Selenium so that they work together in a fashion way. This project provide common architecture that you can use across multiple projects to industrialize your developpement.

## Features
Fusion uses the paradigme "convention over configuration". You can still choose to configure Fusion in detail to match your needs but a simple configuration is sufficient to start. Fusion can be used embedded in your project if you use tools like Gradle to test your project for example or in a dedicated project. 

### Data generator.
The big deal when creating tests is the creation and use of well-known and stabilized data. If your data are moving, your tests will continually in fail. To help you achieve this goal, Fusion provide Java generators, that will construct for you xml files used by DBUnit to manage your database. With this feature you can easily generate predefined or random data in your database.

### Commons Cucumber steps definitions
Fusion provides you with commons steps definitions so you can start writing your test quickly. Actions like click on button, select an item in a list or refresh a page are already defined. Some other actions specific to your project like connection are defined but not implemented. You just need to provide one in some line of code.

