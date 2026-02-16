# HW3 - Tatu Tallavaara, 2208317


## How did you implement picking an image?

I only displayed images on first week, but will explain it now more in detail. Images from directort "drawable/" can be implemented the following way:
```kotlin
Image(
    painter = painterResource(R.drawable.<image_name>),
    contentDescription = null,
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop
)
```

Other options for user given images is to use AsyncImage:
```kotlin
AsyncImage(
    model = imageUri,
    contentDescription = null,
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop
)
```

I personally store user given images to "filesDir/" and the image info (title, description and imageName) to the database, so to display the stored image file I had to first transform it to File the following way:
```
val imageFile = remember(it.imageName) {
    File(context.filesDir, it.imageName)
}
```
The "remember" keyword is used to ensure that the "imageFile" object isn't recreated on every recomposition. My database just stores images and the amount can be large, so without the keyword navigating back and forth between app destinations could cause app to slow down.



## How did you implement text input?

I used OutlinedTextField to get user's input: 
```kotlin
OutlinedTextField(
        modifier = Modifier.fillMaxWidth(0.9f).height(height.dp),
        value = value,
        onValueChange = toggle,             // Changes the given variable to be the user's input
        maxLines = maxLines,
        label = { Text(                     // The input box title
            text = label,
            color = appColors.secondary3
        )},
        isError = error != null,            // I had an error message (String?), so when the error message is null, then 'error' occurs in the input field
        placeholder = { Text(               // Text displayed, when the field is clicked, but no text is written
            text = placeholder,
            color = appColors.secondaryText,
            fontSize = 15.sp
        )},
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = appColors.secondaryText
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = appColors.secondary2.copy(alpha = 0.6f),
            errorBorderColor = Color.Red,   // When error occurs, the border becomes red
        ),
        supportingText = {                  // Display the error message, when necessary (error != null)
            error?.let { Text(
                text = it,
                color = Color.Red,
                fontSize = 15.sp
            ) }
        }
    )
```

The variables were defined:
```kotlin
var title: String by remember { mutableStateOf("") }
var titleError: String? by remember { mutableStateOf<String?>(null) }    // Variables error message

// I passed this straight to the function, which will handle the input's, but it's easier to show this way
val toggleTitle: (String) -> Unit = { title = it; titleError = null }
```


## How can you store the data such that the application can display your chosen image and text even when you change views or restart the application?

I used the RoomDatabase to store the information and when the navigation hits the 'HOME' destination, then all database entries are loaded. I created class DiaryItem for those entries, DiaryItemDAO to handle the database manipulation (e.g. INSERT and UPDATE). I lastly created a 'DatabaseMethods(...) : ViewModel()' to get the values elsewhere with one line.



## Specifically for the image, what is a good way to store the data such that you can load it as soon as the app starts?
I basically answered to this previously, but it's best to store the image file to some directory, where you can access. Then just store the images path (name is usually enough) to the database.



## Links
GitHub: https://github.com/turvasa/mobile-computing
