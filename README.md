# Calendar

##Description
This is a calendar application that will help it's user organize their schedule 
for the day, week, and month. The user will be able to add events with a 
name, duration, and category. 

## Intended Users

This application is meant to be used by a student from high school, 
university, or college. However, this application can also be used by any 
working/studying individual.

## My Interest

This project is of interest to me because this is an application I use 
personally use on a daily basis. The calendar application I currently use
does not have certain functionality that would be very helpful to me. So,
I will be including features that are important to myself (and presumably 
other students).

## Types of Duration
- All day (spans the entire day)
- Multi-day (spans multiple days)

## Categories
- Family
- School
- Work
- Friends
- Personal (personal time)

## User Stories 
- As a user, I want to be able to add an event to my calendar with a title, duration, and category
- As a user, I want to be able to select a day to see what events are planned
- As a user, I want to be able to edit the name and duration of an event
- As a user, I want to be able to delete an event
- As a user, I want to be able to save all events onto the calendar
- As a user, I want to be able to load all events saved to the calendar

## Phase 4: Task 2
I have loaded the calendar from the month of MARCH,2022 from ./data/calendar.json
Event created with... 	Title: hi	Start date: 10	End date: 20	Category: work
Event titled hi: Category changed from work to family
Event created with... 	Title: Hang out	Start date: 27	End date: 27	Category: friends
Event created with... 	Title: Boring	Start date: 26	End date: 26	Category: work


## Phase 4: Task 3
One of the main problems I had when making my project was the "disconnect" between the GUI and the underlying data. The
ways I can improve this "disconnect" is by:
- Refactoring Calendar so that Calendar extends JFrame
- Refactoring CalendarEvent so that CalendarEvent extends JButton
- Another possibility is by having an abstract CalendarEvent class with multiple classes extending this new abstract class
that correspond to the different event buttons