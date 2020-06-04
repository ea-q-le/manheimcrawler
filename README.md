# The Manheim Crawler Application

> A solution for specialized car buyers who have to search thousands of vehicles to find a specific one

---

## Authors

* **Shahin 'Sean' Gadimov** - *Idea and initial work* - [ea-q-le](https://github.com/ea-q-le)

---

> **IMPORTANT Usage and Distribution Notes**

This application is NOT designed for commercial use and should NOT be copied for any purposes except by obtaining a written permission. Please see the default copyright laws for more information. Please contact the owner for special requests.

---

## Features

- Consumes information from Manheim Simulcast
- Searches for vehicles per the criteria specified below:
  - vehicle year
  - vehicle make and/or model
  - auction name and/or state
- Executes further analysis of selected vehicle(s) for the CR (Condition Report), if available
- Analyzes the CR per the criteria specified below:
  - Announcement(s) content, if available, based on the provided keywords
- Final output is emailed
- Email preferences can be modified per the criteria below:
  - recipient(s) information
  - subject line keywords
  - content limitation (i.e. how many search results per email)
  
---

## Version History

> **Release 0.1** on 5/10/2020

**Release Notes:**

- The complete end-to-end run achieved
- Email received per given specifications and limitations
- Common errors and exceptions are handled

> **Release 0.1.1** on 5/12/2020

**Release Notes:**

- Introduced new logic to eliminate vehicles younger than certain year
  - The parameter can be adjusted from `application.config`
  - Additional check was placed to eliminate potential code failure
    in case year of the vehicle is not advertised.
    Such vehicles are skipped so that the year is assigned as `-1`.
- Vehicle title is being fetched from the `Auction` page instead of 
  from within the CR window. Thus eliminating the need for additional
  maintenance on the CR window.
  - `Vehicle` toString method is updated accordingly.
  - Code cleanup conducted within the `CRAnalyzer` class.
- Added logic to fetch the vehicle odometer information.
  - Introduced new logic to eliminate vehicles with odometer readings higher
    than certain parameter defined in `application.config`
- Added logic to fetch the vehicle VIN and parse it with a special design
  if the VIN is of correct length.
- Added logic to analyze vehicle status (whether it is sold or still 
  available) based on the presence of the 'Proxy bid' element.
  Currently, vehicles with 'SOLD' status are ignored and no further CR
  analysis is conducted. This logic can be adjusted in the future.
  
> **Release 0.1.2** on 5/14/2020

**Release Notes:**

- Performance improvements:
  - Storing CR links in an ArrayList a size of which is reduced with each iteration
    to decrease the array complexity in order to achieve future performance enhancements.
  - Fetching the CR link element only once from the List and reusing it, 
    thus removing the redundant call to get the element multiple times.
- Added simple run time calculator to record the time it took for the whole
  process to complete. This information is being sent out as a part of the subject line
  of the final email.
  
> **Release.0.2.1** on 5/17/2020

**Release Notes:**

- Established connection with a remote MySQL database.
  - DB connection credentials should be stored as environment variables.
  - MySQL server time-zone is set to 'MSK' which needs to defined by driver configuration
    (see `DBUtils.createDBConnection()`).
- Created `vehicles` table in the server database
  (see `resources/sql/vehicles.ddl` for table description)
- Created `insertIntoVehicles` function add `Vehicle` object into
  the `vehicles` table within the DB.
- Added logic to fetch and record the current Timestamp the vehicle was
  found by the `crawler` the very first time. This will allow future
  DB based manipulations and extractions
- Added logic to fetch and record the Timestamp the vehicle is going to be
  auctioned at under `run_date` column.
- Added logic to check whether a vehicle already exists in the DB by the
  given VIN: the vehicle details are not considered to be sent via email 
  if exists, they are otherwise.
  
> **Releases.0.2.2-0.2.6** from 5/18 to 6/04/2020

**Release Notes:**
- Configured DataBase timeout settings to allow multi-hour program run.
- Performance enhancements:
  - Validating vehicle against the database records 
    before trying to fetch the vehicle sales status;
  - Enhancing the Driver singleton to be truly thread safe;
  - Updates to parameters;
  - Fetching all the VINs from the database only once to store it as
    a list, and only then checking whether a current vehicle VIN exists
    within this list, rather than querying the database every time.

---

## Future Improvements / Work-in-Progress
 
> Vehicle CR analysis

- Add logic to eliminate certain keywords from announcements.
  Current logic does not have elimination clause, only inclusion.
  
> Database Enhancements

- Logic to implement:
  - If a vehicle is discovered to already exist in the DB, build logic
    and possibly additional tables to store the changes to the vehicle
    data, if any. *Current implementation* simply skips the vehicle from
    further consideration.
- Database maintenance > the following need to be considered before each
  run in order to maintenance the health and size of the DB:
  - Removing vehicles `run_date` of which is older than 7 days from today
  - Removing vehicles that are not `available` more than 3 days from today
  - Creating additional tables to and storing commonly queried data there
    for lookups instead of loading a single `vehicles` table
  
> Advanced features

- Ability to fetch MMR information
- Ability to place proxy bid(s)
- Additional thought needs to be given into potentially removing data
  analysis logic into **another independent service**.
  
---

> Copyright 2020 © <a href="https://ums.global/">UMS Global</a>.
