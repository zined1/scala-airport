
# Scala Airport

Resume
------

Scala Airport is a program written in Scala which aims to parse 3 csv files in order to extract informations from theses files, use MongoDB and do some statistics.


 * *Countries.csv* contains informations about countries (country_code, name, continent...)
 * *Airports.csv* contains several airports and informations about this.
 * *Runways.csv* contains all runways of the airports in airports.csv

This project is using a MongoDB to store all theses informations.

___
Execution
---------

To launch the project:

    > mongod
    > sbt run
    < 1- Query
      2- Reports
      3- Exit
      
 *Query : You must enter a Country_code/Country/Beginning of the Country name (e.g: FR/France/Fran is the same).
             The program gives you all the airports in the country and their runways (Number, Surface, Width).

 *Report :
     *(1) Highest/Lowest number of airports : Gives the 10 first countries with the highest/lowest
      number of airports.
     *(2) Type of runways : Shows all the countries with all types of their runways.
     *(3) 10 most common runway latitude : Gives the 10 most commons runways latitude.
     
___
Results
-------
Highest/Lowest number of airports:

    > HIGHEST :
    	United States (US) => 21501 airports
    	Brazil (BR) => 3839 airports
    	Canada (CA) => 2454 airports
    	Australia (AU) => 1908 airports
    	Russia (RU) => 920 airports
    	France (FR) => 789 airports
    	Argentina (AR) => 713 airports
    	Germany (DE) => 703 airports
    	Colombia (CO) => 700 airports
    	Venezuela (VE) => 592 airports
      LOWEST :
    	South Georgia and the South Sandwich Islands (GS) => 0 airport
    	Pitcairn (PN) => 0 airport
    	Tokelau (TK) => 0 airport
    	Andorra (AD) => 1 airport
    	Anguilla (AI) => 1 airport
    	Aruba (AW) => 1 airport
    	Saint Barthélemy (BL) => 1 airport
    	Cocos (Keeling) Islands (CC) => 1 airport
    	Curaçao (CW) => 1 airport
    	Christmas Island (CX) => 1 airport

Query with "Japan":

     > Japan (JP):
     ...
     Futenma Marine Corps Air Station (ROTM) (1 runway) => [Surface: ASP, Length(ft): 9000, Width(ft): 150] | 
     Yonaguni Airport (ROYN) (1 runway) => [Surface: ASP, Length(ft): 4920, Width(ft): 150] | 
     Satsuma Iwo-jima Airport (ZZZZ) (1 runway) => [Surface: concrete, Length(ft): 1713, Width(ft): 82] | 
     ----
