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
- Vehicle title is being fetched from the `Auction` page insted of 
  from within the CR window. Thus eliminating the need to additional
  maintenance on the CR window.
  - `Vehicle` to String method is updated accordingly.
  - Code cleanup conducted within the `CRAnalyzer` class.
- Added logic to fetch the vehicle odometer information.
  - Introduced new logic to eliminate vehicles with odometer readings higer
    than certain parameter defined in `application.config`

---

## Future Improvements / Work-in-Progress

> Vehicle VIN

- Add logic to fetch the vehicle VIN information.
  Current logic does not have any consideration for this.
  
> Vehicle status analysis

- Add logic to validate whether the vehicle is already `SOLD`.
  Current logic does not have any consideration for this.
  
> Auction analysis

- Add logic to limit an auction per its run date.
  Current logic only limits an auction by its names and states.
  
> Vehicle CR analysis

- Add logic to eliminate certain keywords from announcements.
  Current logic does not have elimination clause, only inclusion.
  
> Establish a database

- Information to store:
  - Vehicle unique ID (primary key, auto-increment)
  - Vehicle year and make/model
  - Vehicle VIN
  - Vehicle odometer reading
  - Vehicle auction
  - Vehicle run lane
  - Vehicle run date
  - Vehicle announcements
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
