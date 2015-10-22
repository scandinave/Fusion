# Fusion
Never get on your nerves with your test !

## Goal
Fusion simplifies the use of commonly tools dedicated to integration tests. With Fusion you can start writing BDD(Behavior Driven Development) tests with minimal configuration.
Fusion preconfigure tools like JUnit, Cucumber and Selenium so that they work together in a fashion way. This project provide common architecture that you can use across multiple projects to industrialize your developpement.

## Getting Start
Fusion uses the paradigme "convention over configuration". You can still choose to configure Fusion in detail to match your needs but a simple configuration is sufficient to start. Fusion can be used embedded in your project if you use tools like Gradle to test your project for example or in a dedicated project. So let's see how to use Fusion with your project.

### Embedded Project Structure
To use Fusion in embedded mode, you need to create a fusion.properties file in the root of your project and add  the fusion-x.x.x.jar as dependency. The following will describe the use of Fusion with a graddle project. If you use another build tool to launch your tests, the only things that will change are the source directory for your fusion files. By default Fusion will search for a resources directory call flatXmlDataSet to store et read datas test. This directory contain init, common and distinct directory. You will write your tests in a directory called scenarii. This directory will contain .features files used by Cucumber. We will see later the role of these directories and how to change their names or their paths.

![](https://github.com/scandinave/Fusion/blob/scandinave-readme/resources/gradleStructure.png)
