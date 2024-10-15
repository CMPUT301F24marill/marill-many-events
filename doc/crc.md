## CRC Cards for Event Registration App

### Entrant
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
| - Join or leave a waiting list                                                                         | Event                          |
| - Provide and update personal information                                                              |                                 |
| - Upload or remove profile picture                                                                     |                                 |
| - Receive notifications about lottery results                                                          | Notification                    |
| - Accept or decline an event invitation                                                                | Event                           |

### Event
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
| - Create and manage event details                                                                      |                                 |
| - Add entrants to the waiting list                                                                     | Entrant                         |
| - Randomly select entrants for event participation                                                      |                      |
| - Handle cancellations and replacements                                                                |                       |
| - Generate QR code for event registration                                                              | QRCode                          |

### EventList
| **Responsibilities**                                                                                      | **Collaborators**               |
|-----------------------------------------------------------------------------------------------------------|---------------------------------|
| - Add a new event to the list                                                                              | Event                           |
| - Remove an event from the list                                                                            |                         |
| - Retrieve an event by its ID                                                                              |                         |
| - Get a list of all events                                                                                 |                        |

### QRCode
| **Responsibilities**                                                                                  | **Collaborators**               |
|-------------------------------------------------------------------------------------------------------|---------------------------------|
| - Generate a unique code for each event                                                                | Event                           |
| - Provide event details when scanned                                                                   |                          |

### Notification
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Send notifications to entrants about lottery results, updates, and event details                 |Event                             |
| - Allow entrants to opt out of receiving notifications                                              | Entrant                         |
| - Notify organizers about cancellations and replacements                                            |                                  |

### Geolocation
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - Collect and verify the location of users when they join the waiting list                          | Entrant                        |
| - Display a map view of where entrants joined from                                                  |                                |

### Facility
| **Responsibilities**                                                                               | **Collaborators**               |
|----------------------------------------------------------------------------------------------------|---------------------------------|
| - contains facility information with getters and setters                                         | organizer                          | 
| - Contains facility name                                                                           | administrator                  |
| - linked to events                                                                                | Event                          |