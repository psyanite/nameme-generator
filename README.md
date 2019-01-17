# 🎰 nameme-generator

Nameme-generator is a Scala project which takes in a list of names in an `xlsx`, and its corresponding descriptions in Korean, English, etc, and converts it to a `csv` file for the `nameme` app. If there is no descriptions for a given name, a random one will be selected.

## How to generate
* Import project into IntelliJ
* Add new names into `xlsx` files in `src/main/resources/csv/`
* Run `Crystal.scala`
* Copy newly generated `csv` files in `src/main/resources/csv/`into the `nameme` codebase
