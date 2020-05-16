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
  
> **Release.0.2.1** started on 5/15/2020

**Release Notes:**

- Establish connection with a remote MySQL database
  - DB connection credentials should be stored as environment variables
  - MySQL server time-zone is set to 'MSK' which needs to identified by driver configuration
    (see `DBUtils.createDBConnection()`)
  - Created `vehicles` table in the server database with the following columns:
    - id (int, primary key, auto-increment) 
    - year (int)
    - make_model (varchar(128))
    - vin (varchar(32))
    - odometer (int)
    - auction (varchar(32))
    - lane (varchar(16))
    - run_date (datetime)
    - announcements (varchar(256))
    - available (bool)
    
---

## Future Improvements / Work-in-Progress
  
> Auction analysis

- Add logic to limit an auction per its run date.
  Current logic only limits an auction by its names and states.
  
> Vehicle CR analysis

- Add logic to eliminate certain keywords from announcements.
  Current logic does not have elimination clause, only inclusion.
  
> Establish a database

- Logic to implement:
  - Before analyzing the CR of a vehicle, determine
    whether the vehicle is already in the DB (i.e. it has been analyzed before),
    if yes, then ignore it,
    else, analyze the vehicle and add it to the DB.
  - Fetch the information on stored vehicles upon request.
  
> Advanced features

  - Ability to fetch MMR information
  - Ability to place proxy bid(s)
  
---

> Copyright 2020 © <a href="https://ums.global/">UMS Global</a>.
