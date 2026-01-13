# Week 1

## How did you add an image and how did you adjust its size and shape?

My image is defined by the following way:
```kotlin
Image (
    painter = painterResource(R.drawable.you_are_the_meme),
    contentDescription = null,
    contentScale = ContentScale.FillHeight,
    modifier = Modifier
        .height(300.dp)
        .clip(RoundedCornerShape(10.dp))
        .border(
            2.dp,
            if (isDarkMode) Color.White else Color.Black
        )
)
```

The image size (300 Density Independent pixels) is set with ".height" parameter to "Modifier". The image has rounded corners,
that are defined with the ".clip". It also has borders, with color depending of the mode (light / dark)


## In what ways did you change text appearance?

```kotlin
Text(
    "What took you so long?",
    style = MaterialTheme.typography.bodyMedium,
    color = if (isDarkMode) Color.White else Color.Black
)
```

The color of the text depend on the variable "isDarkMode". The application has two text objects. The first one has size of
"titleLarge", while the other displayed above has size "bodyMedium".


## How did you make the content scrollable?
When I created the body of the application inside Column object, I used parameter "verticalScroll(rememberScrollState())"
to make the content scrollable:
```kotlin
Column (
    modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState()) // Makes the column scrollable
        .padding(top = 200.dp, bottom = 200.dp),
    horizontalAlignment = Alignment.CenterHorizontally
)
``` 


## How do you register the user clicking on something?
I made a dark mode -button and it has a parameter "onClick", which defines what happens after the click.
```kotlin
Button(
    onClick = { isDarkMode = !isDarkMode },
    modifier = Modifier
        .padding(top = 40.dp)
)
```


## How can you make that click cause a visible change?
I made a mutable variable that is changed every time the dark mode -button is pressed:
```kotlin
var isDarkMode by remember { mutableStateOf(true) }
```
For this line I used ChatGPT. Just using:
```kotlin
var isDarkMode = true
```
wouldn't work, because the whole "@Composable" function is runned again, so it would just swicth back to its original state.
The "remember" keyword made the variable mutable, but only when it is changed in the code (not during reruns).


## Other descriptions

The custom background was implemented for both modes (light / dark):
```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    Image(
        painter = painterResource(
            if (isDarkMode) R.drawable.background_black else R.drawable.background_white
        ),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    ...
}
```

The Block is fully filled through the screen, and the Image is filled fully inside the Box (whole screen also). The background is
cropped to prevent funny scalings.
