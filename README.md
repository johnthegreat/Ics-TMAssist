# TMAssist

This is a bot that runs on [FICS](https://freechess.org) as `TMAssist(TD)(TM)` that provides assistance to Tournament Managers.

Currently, it provides three primary features:

- Reminds tournament managers to open scheduled events when due.
- Allows users to see a list of when the next event(s) are.
- Allows tournament managers to use a user-friendly web form to create tournaments.

## Configuration

Create a file (e.g. `TMAssist.properties`) with the following parameters:

`host`: Ip Address or Hostname of the Internet Chess Server to connect to. (e.g. `freechess.org`)  
`port`: Port number to connect to. (e.g. `5000`)  
`username`: Username to use when logging in.  
`password`: Password to use when logging in.  
`adminList`: A comma-seperated list of usernames who can manage the bot (e.g. `usernameA,usernameB`)  
`enableScheduledEventReminderService`: Should the Scheduled Events Reminder Service should be enabled; expects `true` or `false`.  
`eventsFilePath`: Path to the file containing the scheduled events.   
`serverTimeZone`: The Time Zone that the Internet Chess Server is located in (default is `America/New_York`).  
`webInterfaceUrl`: The URL of the web interface providing the tournament creation form. (e.g. starting with `http://` or `https://`)

Now pass the path to your created file (e.g. `TMAssist.properties`) as the first and only argument to the TMAssist application or jar file.

## License

Copyright &copy; 2022 John Nahlen.

Licensed under the MIT License. See `LICENSE.txt` for full license text.