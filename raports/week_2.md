# HW2 - Tatu Tallavaara, 2208317


## What are the essential parts for implementing navigation?

I had to recreate my project due to IDE errors, so I just added all the required imports and other essentials. However, in the Main file I used NaviagtionBar class for the job. I have declared a AppDestinations enum where I have "HOME", "SETTINGS" and "ADD":

```kotlin
enum class AppDestinations(
    val label: String,
    var icon: Int
) {
    SETTINGS("Settings", R.drawable.icon_settings_light),
    HOME("Home", R.drawable.icon_home_light),
    ADD("Add", R.drawable.icon_add_light),
}
```

 I also have boolean variable currentLocation (start location is "HOME"):

```kotlin
var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

```

The AppDestinations are looped inside the NavigationBar using NavigationBarItem

```kotlin
NavigationBar(
        containerColor = appColors.secondary,
    ) {
        AppDestinations.entries.forEach {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = it.label,
                        modifier = Modifier.width(24.dp),
                        tint = Color.Unspecified
                    )
                },
                label = { Text(
                    text = it.label,
                    color = appColors.primaryText,
                ) },
...
```



## How can you make a button that takes you to another view in the app?

I just need to change the location with navController in the onClick event:
```kotlin
val navController = rememberNavController()
...

onClick = {

    // Switch the destination (if it is new one)
    if (navController.currentDestination?.route != it.label) {
        navController.navigate(it.label)
```

I'm not sure, what the '?' marks in the if-statement, but it was given me as an autofix from my IDE. 



## How can you prevent circular navigation?

The circular navigation can be prevented using popUpTo() function, which pops the navigation stack until the given destination is reached. Meaning, that only the "HOME" in my case is in the stack and no further back navigating is possible and the next step would be to exit the application (inclusive = true):

```kotlin
    if (it == AppDestinations.HOME) {
        popUpTo(AppDestinations.HOME.label) { inclusive = true }
    }
```

To prevent accidental exiting the additional if-statement is necessary, so pressing the "HOME" button won't pop the stack, if currentDestination == HOME.
