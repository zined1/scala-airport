 _____           _         _____            ______
/  ___|         | |       /  __ \           | ___ \
\ `--.  ___ __ _| | __ _  | /  \/_____   __ | |_/ /_ _ _ __ ___  ___ _ __
 `--. \/ __/ _` | |/ _` | | |   / __\ \ / / |  __/ _` | '__/ __|/ _ \ '__|
/\__/ / (_| (_| | | (_| | | \__/\__ \\ V /  | | | (_| | |  \__ \  __/ |
\____/ \___\__,_|_|\__,_|  \____/___/ \_/   \_|  \__,_|_|  |___/\___|_|


Scala CSVParser is a program written in Scala which aims to parse 3 csv files in order to
extract informations from theses files and do some statistics.

### INFORMATIONS ABOUT CSV FILES
 - Countries.csv contains informations about countries (country_code, name, continent...)
 - Airports.csv contains several airports and informations about this.
 - Runways.csv contains all runways of the airports in airports.csv

### HOW TO LAUNCH
 1) You must have :
  - Sbt which is a simple build tool for scala.
  - MongoDB which is a document database.
 2) Open a terminal and open Mongo, for this, type "mongod". Now your "server" is open.
 3) Now type "sbt compile" in the same directory as this README.
 4) Finally, type "sbt run" and the program should be open.

### HOW TO USE
 DISCLAIMER : It is important to note that the INITIALIZATION will be "slow" the first time you launch our program.
			  It is due to database creation and insertions.

 Now you have a menu with 3 options (Query, Report and Exit).
 The input can be String or Int. That is to say you can type either Query/Report/Exit or 1/2/3.

 (1) Query : You must enter a Country_code/Country/Beginning of the Country name (e.g: FR/France/Fran is the same).
             The program gives you all the airports in the country and their runways (Number, Surface, Width).

 (2) Report :
     (1) Highest/Lowest number of airports : Gives the 10 first countries with the highest/lowest
      number of airports.
     (2) Type of runways : Shows all the countries with all types of their runways.
     (3) 10 most common runway latitude : Gives the 10 most commons runways latitude.

 (3) Exit: Exits the program.

### TEST
 Tests were created in order to test the program. You can type "sbt test" in order to launch them.
 Some tests are "slow" (1s) because there are insertions in database.

### BONUS
 - Name matching partial/fuzzy.
 - Use database Mongo.

### AUTHORS
 REBIAI Zinedine
 DEBRENNE Antoine
