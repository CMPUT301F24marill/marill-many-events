## CRC Cards for Event Registration App

### Entrant
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
| - Join or leave a waiting list                                                                         | Event                          |
| - Provide and update personal information (name, email, profile picture, etc.)                         |UserProfile                                     |                                 
| - Upload or remove profile picture                                                                     | Authentication           |                      
| - Receive notifications about lottery results, event updates, and invitationsnin a list                          | NotificationList                    |
| - Accept or decline an event invitations                                                               |  Geolocation                          |
| - Have a facility if entrant is an organiser                                                           |  Settings                          |
| - View upcoming and past events                                                                        |   QRCode                         |
| - View event details (date, location, participants)                                                    | Eventlist                      |
| - Opt in or out of receiving notifications                                                             |           |
| - Log in or log out of the app                                                                         |                   |                                                    
| - View event details within the app by scanning QR code                                                 |     |
| - Receive another chance to participate if a selected entrant cancels                                      |  |
| - Be warned before joining a waiting list requiring geolocation                                            |  |
| - Register for an event by scanning a QR code                                                         ||
| - Can create a facility. This is how they become an organizer.                                                        ||





### Event
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
| - Create and manage event details(name, date, location, etc)                                          | Facility                        |
| - Manage the waiting list(add/remove entrants)                                                        | EntrantList                        |
| - Randomly select entrants for event participation(lottery system)                                    | QRCode                         |
| - Handle cancellations and replacements                                                               | Messagefragment          |
| - Allow organizers to publish event details and QR codes                                                | Organizer                          |
| - Track event statistics (capacity, attendees, cancellations, etc.)                                   | Geolocation               |
| - Manage post-event follow-up (feedback, notifications)                                               | Image                  |
| - Display event information to entrants (details, updates)                                            ||
| - Generate a unique QR code for each event                                                         |                          |
| - Optionally create and hold geolocation location a                                                    |                          |          
| - Track available capacity and status (full, open)                                                    |                          |      
| - Manage poster image                                                   |                          | 
| - Allow organizer to send messages to entrants                                                  |                          |
| - Hold Entrant List                                                  |                          |
                              
### EntrantList
| **Responsibilities**                                                                                      | **Collaborators**               |
|-----------------------------------------------------------------------------------------------------------|---------------------------------|
| - Allow organizer to filter through entrants by wishlisted, enrolled, invited, and cancelled                             | Event                           |
| - Hold Entrants and their status                                                                           | Entrant                 |
| - Manage Entrant status                                                                          |                     |


### EventList
| **Responsibilities**                                                                                      | **Collaborators**               |
|-----------------------------------------------------------------------------------------------------------|---------------------------------|
| - Add a new event to the event list                                                                              | Event                           |
| - Remove an event from the event list                                                                            |                      |
| - Retrieve an event details by event ID                                                                           |                     |
| - Get a list of all upcoming and past events                                                               |                        |
| - Filter events by criteria (e.g., location, date, capacity)                                               |                        |
| - Display events to users in a list or calendar view.                                                      |                |
| - Handle pagination and sorting of event lists                                                             |              |



### QRCode
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
|  - Store and manage hash data of generated QR codes                                                    | Event                           |
| - Provide event details when a QR code is scanned                                                      | Administrator                      |
|                                                                                                    |Entrant                     | 


### Notification
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Send notifications to entrants about lottery results and messages from the organizer                 |Event                             |
| - Hold notification status, if its been viewed or not                                                | Setting                         |
| - Notify organizers about cancellations and replacements                                            |Organizer                         |
| - Manage notification preferences (email, push notifications, SMS)                                  | NotificationList                 |
| - Notify entrants about changes to event status (acceptance, cancellation)                          | Entrant                  |
| - Contain status if notification is a lottery result or a message from the organizer                         |                   |
| - Hold Notification message if any                        |                   |
| - Become viewed when opened                         |                   |


### NotificationList
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Holds list of notifications                                                                       | Entrant                  |
| - Allow entrants to opt out of receiving notifications                                              | Notification                    |
| - Notify entrant of new notifications                                           |                         |
| - Manage notification preferences (email, push notifications, SMS)                                  |                  |
| - Remove viewed lottery results notifications                                  |                  |
| - Check notifications if viewed                                  |                  |


### Geolocation
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Collect and verify the location of users when they join the waiting list                          | Entrant                        |
| - Display a map view of where entrants joined from                                                  | EventManager                         | 
| - Track and store geolocation data for event analytics                                              | Database                         |
| - Warn entrants about events requiring geolocation                                   |            |
| - Provide geolocation-based event recommendations                                    |       |

       

### Facility
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - contains facility information with getters and setters                                         | organizer                          | 
| - Contains facility name                                                                           | administrator                  |
| - Associated to events in a list                                                                 | EventList                          |
| - Provide facilities for event                                                                    |                           |
| - Handle booking and availability of facilities                                                      |                    |
| - Delete associated events when deleted                                                     |                    |


### Authentication
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Validate entrant credentials (Check device identification)                                                             | Entrant,                |
| - Handle account creation (If username already exist)                                                                | Database               |
| - Manage entrant sessions (session tokens, logout)                                      | Session    |

### UserProfile

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Manage entrant profile details (name, email, profile picture, etc.)                   | Entrant    |
| - Upload or remove profile picture                                                   | Database        |
| - Associated with profile picture image                | Image         |
| - Generated image deterministically based on username                |          |


### Settings

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Manage app settings (e.g., notification preferences, themes)                       | Entrant |
| - Store and retrieve settings from local storage or database                         | Database          |
| - Manage opt-in/opt-out settings for notifications                                   | Notification             |



### Session

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Manage entrant session tokens                                                         | Authentication       |
| - Ensure session expiration and automatic logout                                     | Entrant         |
| - Validate session state (active, expired)                                           |                |


### Administrator

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Assist with managing multiple eventswithin a facility                               | Event      |
| - Manage facility booking and logistics                                              | Facility   |
| - Track event-related budgets and expenses                                           | UserProfile    |
| - Ensure compliance with event guidelines                                            | QRCode        |
| - Remove events that violate policies                                                | ImageList        |
| - Remove profiles that violate policies                                              |                 |
| - Remove hashed QR code data                                                         |                 |
| - Browse all events, profiles, and images                                                |                 |
| - Remove facilities that violate app policy                                          |             |
| - Be able to remove events, facilities, profiles, and images                          |                |

### Organizer

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Create and manage events under facilities                                          | Event      |
| - Generate unique promotional QR codes for each event                                | QRCode     |
| - Set event capacity and waiting list limits                                         | Geolocation      |
| - View waiting list details and entrant locations                                    | Entrant  |
| - Optionally enable or disable geolocation verification                              |  |
| - Draw replacements for cancelled entrants                                           | Facility           |
| - Send notifications to selected and cancelled entrants                              | Eventlist         |
| - View lists of all selected and cancelled entrants                                  |                 |
| - Manage their facility                                                               |                 |
| - View list of events created for facility                                           |                 |
| - Delete facility when deleted                                                       |                 |


### Image

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Hold Image                                                                       | ImageList      |
| - Get entrants in event                                                                 | UserProfile    |
| - Filter through entrants by invited, enrolled, cancelled, and waitlisted                  | Event    |
| - Add self to overall image list when created                                              |       |
| - Track if uploaded or generated deterministically                                         |       |
| - Track if a poster or a profile picture                                            |       |
| - Have access to associated event or userprofile                                         |       |

### ImageList

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Add and Remove Image                                                                 |Image      |
| - Sort images by poster or profile picture                                                       |      |
| - Replace deleted profile pictures with ones generated by profile username                        |      |

### Messagefragment

| **Responsibilities**                                                                 | **Collaborators**  |
|--------------------------------------------------------------------------------------|-------------------|
| - Window showing ui for organizer to send notifications to entrants in event   | Event      |
| - Get entrants in event                                                           | Entrant     |
| - Filter through entrants by invited, enrolled, cancelled, and waitlisted      | Organizer      |
| - Create message Notification to send to entrants Notificationlist     | Notification      |
|                                                                        | NotificationList      |