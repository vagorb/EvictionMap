# Solution

In my solution I decided to use ConcurrentHashMaps and Timer. Main reason for choosing them was thread safety.
It contains 2 public methods(get and put), one private TimerInitializer and constructor.
It is needed to provide time (long) of delay before removing value from Map for constructor initialization.
Put method just checks is given key present, adds or updates map with values from user and creates Timer
with removing entries task for both maps after provided in constructor time for each provided key-value pair which is added to second Map.
I chose to make new Timer for each new entry again because of thread safety. Making one Timer and provide a lot of
timer tasks to it make application faster, but there could occur conflicts in Timer, because TimerTasks are not thread safe.
