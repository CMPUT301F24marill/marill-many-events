## CRC Cards for Event Registration App

### Entrant
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
| - Join or leave a waiting list                                                                         | Event                          |
| - Provide and update personal information (name, email, profile picture, etc.)                         |UserProfile, Authentication                                    |                                 
| - Upload or remove profile picture                                                                     | UserProfile                     |                      
| - Receive notifications about lottery results, event updates, and invitations                          | Notification                    |
| - Accept or decline an event invitations                                                               | Event                           |
| - Have a facility if entrant is an organiser                                                           | Event                           |
| - View upcoming and past events                                                                        | Event                           |
| - View event details (date, location, participants)                                                    | Event                           |
| - Opt in or out of receiving notifications                                                             | Notification, Settings          |
| - Log in or log out of the app                                                                         | Authentication                  |                                                    
| - View event details within the app by scanning QR code                              | QRCode    |
| - Receive another chance to participate if a selected entrant cancels                | Event, Notification |
| - Be warned before joining a waiting list requiring geolocation                      | Geolocation |
| - Register for an event by scanning a QR code                                                         |QRCode|




### Event
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
| - Create and manage event details(name, date, location, etc)                                          | com.example.marill_many_events.Facility                        |
| - Manage the waiting list(add/remove entrants)                                                        | Entrant                         |
| - Randomly select entrants for event participation(lottery system)                                    | Entrant                         |
| - Handle cancellations and replacements                                                               | Entrant, Notification           |
| - Allow organizers to publish event details and QR codes                                                | QRCode, Organizer                          |
| - Track event statistics (capacity, attendees, cancellations, etc.)                                   | QRCode, com.example.marill_many_events.Facility                |
| - Manage post-event follow-up (feedback, notifications)                                               | Notification.                   |
| - Display event information to entrants (details, updates)                                            | Entrant                         |
                              



### EventList
| **Responsibilities**                                                                                      | **Collaborators**               |
|-----------------------------------------------------------------------------------------------------------|---------------------------------|
| - Add a new event to the event list                                                                              | Event                           |
| - Remove an event from the event list                                                                            |    Event                     |
| - Retrieve an event details by event ID                                                                           |     Event                    |
| - Get a list of all upcoming and past events                                                               |   Event                     |
| - Filter events by criteria (e.g., location, date, capacity)                                               |   Event                     |
| - Display events to users in a list or calendar view.                                                      |   Event, Entrant            |
| - Track available capacity and status (full, open)                                                         |   Event             |
| - Handle pagination and sorting of event lists                                                             |   Event           |



### QRCode
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
| - Generate a unique QR code for each event                                                                | Event                           |
| - Provide event details when a QR code is scanned                                                      | Event                          |
| - Validate QR code during event check-in                                                               |Entrant, Event                     | - Store and manage hash data of generated QR codes                                                     |Event, Administrator|


### Notification
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Send notifications to entrants about lottery results, updates, and event details                 |Event                             |
| - Allow entrants to opt out of receiving notifications                                              | Setting                         |
| - Notify organizers about cancellations and replacements                                            |Organizer                         |
| - Manage notification preferences (email, push notifications, SMS)                                  | Setting, Entrant                 |
| - Notify entrants about changes to event status (acceptance, cancellation)                          | Entrant                  |




### Geolocation
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Collect and verify the location of users when they join the waiting list                          | Entrant                        |
| - Display a map view of where entrants joined from                                                  | Entrant                         | 
| - Track and store geolocation data for event analytics                                              | Database                         |
| - Warn entrants about events requiring geolocation                                   | Entrant           |
| - Provide geolocation-based event recommendations                                    | EventManager      |

       

### com.example.marill_many_events.Facility
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - contains facility information with getters and setters                                         | organizer                          | 
| - Contains facility name                                                                           | administrator                  |
| - linked to events                                                                                | Event                          |
| - Provide facilities for event                                                                    | Event                          |
| - Handle booking and availability of facilities                                                      | Event, Organizer                  |


### Authentication
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Validate entrant credentials (login)                                                             | Entrant, Database               |
| - Handle account creation (sign-up)                                                                | Entrant, Database               |
| - Manage entrant sessions (session tokens, logout)                                      | Session    |
| - Provide password recovery functionality                                            | Email      |



### UserProfile

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Manage entrant profile details (name, email, profile picture, etc.)                   | Entrant, Database    |
| - Upload or remove profile picture                                                   | Database          |
| - Handle updates to personal information (e.g., email, phone number)                 | Database          |


### Settings

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Manage app settings (e.g., notification preferences, themes)                       | Entrant, Notification |
| - Store and retrieve settings from local storage or database                         | Database          |
| - Manage opt-in/opt-out settings for notifications                                   | Notification             |



### Session

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Manage entrant session tokens                                                         | Authentication    |
| - Ensure session expiration and automatic logout                                     | Entrant, Authentication |
| - Validate session state (active, expired)                                           | Entrant              |


### Administrator

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Assist with managing multiple events                                               | Event      |
| - Manage facility booking and logistics                                              | com.example.marill_many_events.Facility   |
| - Track event-related budgets and expenses                                           | Event    |
| - Ensure compliance with event guidelines                                            | Event      |
| - Remove events that violate policies                                                | Event      |
| - Remove profiles that violate policies                                              | UserProfile |
| - Remove hashed QR code data                                                         | QRCode    |
| - Browse events, profiles, and images                                                | Event, UserProfile |
| - Remove facilities that violate app policy                                          | com.example.marill_many_events.Facility  |





### Organizer

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Create and manage events                                                           | Event      |
| - Generate unique promotional QR codes for each event                                | QRCode     |
| - Set event capacity and waiting list limits                                         | Event      |
| - View waiting list details and entrant locations                                    | Entrant, Geolocation |
| - Optionally enable or disable geolocation verification                              | Geolocation |
| - Draw replacements for cancelled entrants                                           | Event      |
| - Send notifications to selected and cancelled entrants                              | Notification |
| - View lists of all selected and cancelled entrants                                  | Entrant, Notification |



### Email

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Send email notifications to entrants regarding event updates, invitations, and reminders | Notification, Event |
| - Handle password recovery emails and account-related communications                | Authentication    |
| - Send confirmation emails for event registrations and cancellations                | Event, Entrant |
| - Manage email preferences for users (opt-in/opt-out of email notifications)        | Settings   |
| - Track email delivery status and handle failures (e.g., undelivered, bounced)      | Notification |
| - Provide email templates for various types of communications (invitations, reminders, updates) | Event, Notification |
