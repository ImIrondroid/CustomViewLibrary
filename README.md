# Android Custom View (Contributions are welcome)

# 1. PointSeekBar

![pointseekbar](https://user-images.githubusercontent.com/48594786/166239904-0e66429d-30b1-45f8-ac62-c73c6aade6ae.gif)

## Description

This PointSeekbar is simple to use and is made to display information that can be used basically.

## Xml

```xml
    <com.iron.library.PointSeekBar
        android:id="@+id/customSeekBar1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="20"
        app:thumbMarkTextColor="@color/white"
        app:tickMarkCount="6"
        android:paddingHorizontal="24dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintVertical_bias="0.05"
        app:layout_constraintTop_toTopOf="parent" />
```

## Attributes

| Name | Format | Default | Description |
|:----:|:----:|:-------:|:-----------:|
|lineColor|color/refernce|#F8FAFC|Unselected progressbar color(Background)|
|progressColor|color/refernce|#FDC6CE|Selected progressbar color|
|thumbMarkTextColor|color/refernce|Color.WHITE|Thumbmark text color|
|tickMarkCount|integer|5|Number of tickmarks you want to display|

## Public Methods

| Name | Description |
|:----:|:----:|
|setTickMarkCount(Int)| Change number of tickmarks you want to display|


## Note
* This PointSeekBar corresponds to the default setting of width, height, and padding and the user setting of custom correspondence.
* This PointSeekBar exposes the ThumbMark at the appropriate location even if the display resolution of the device is changed.
* The TickMarkCount variable is the number of Drawables that are actually exposed to the SeekBar. For example, if TickMarkCount is set to 5 based on the Max value of 100, each interval becomes 25. Please enter the appropriate number
* The limit of the Max value is the same as Progress, and any value is expressed as a maximum of 100%.


----------------------------------------------------------------------------------------------------------------------------------------------

# 2. ArcProgressBar

![arcprogressbar](https://user-images.githubusercontent.com/48594786/168347241-05a55aec-a35f-4d31-96b1-a037465f2a07.gif)

## Description

Use this when you want to use a ProgressBar composed of semicircles.

## Xml

```xml
    <com.iron.library.ArcProgressBar
        android:id="@+id/arcProgressBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="10000"
        android:padding="@dimen/normal_100"
        android:progress="0"
        app:backgroundProgressColor="@color/purple_200"
        app:borderWidth="30dp"
        app:direction="counterClockwise"
        app:progressColor="@color/purple_500"
        app:startAngle="0"
        app:strokeCap="square"
        app:sweepAngle="180" />
```

## Attributes

| Name | Format | Default | Description |
| --- | --- | --- | --- |
| startAngle | integer | 0   | Starting angle (in degrees) where the arc begins |
| sweepAngle | integer | 360 | Sweep angle (in degrees) measured clockwise or counterclockwise |
| borderWidth | dimension | 16dp | Radius length of each point that draws the ProgressBar |
| direction | integer | clockwise | The direction in which the ProgressBar is drawn. <br/>Available : clockwise, counterclockwise |
| strokeCap | integer | butt | The Cap specifies the treatment for the beginning and ending of stroked lines and paths. <br/> Available : butt, round, square |
| backgroundProgressColor | color/refernce | -0x151516 | Background progress color |
| progressColor | color/refernce | Color.yellow | Progress color |

##

## Note

- This AcrProgressBar properly draws the View based on your preferences of width, height and padding.
- This AcrProgressBar can draw the desired length of ArcProgressBar through startAngle and sweepAngle inputs, but it is not possible to intentionally reduce the height of the unused View.
- This AcrProgressBar can set the direction the Progress is set in.
- This AcrProgressBar can change the shape of the bar being drawn. Also, the background color can be modified to the user's desired color.

----------------------------------------------------------------------------------------------------------------------------------------------

# 3. EventViewFlipper

![EventViewFlipper](https://user-images.githubusercontent.com/48594786/174335370-fda65006-f039-4607-8a53-dda892922f5a.gif)

## Description

Use a ViewFlipper to bind data of any size using 2 TextViews.

## Kotlin

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val messageList = listOf(
            "First Message",
            "Second Message",
            "Third Message",
            "Fourth Message",
            "Fifth Message"
        )

        binding.eventViewFlipper.apply {
            setCount(messageList.size)
            setOnViewChangeListener { view, index ->
                (view as TextView).text = messageList[index]
            }
            flipInterval = 2000
        }

        binding.start.setOnClickListener {
            binding.eventViewFlipper.startFlipping()
        }

        binding.stop.setOnClickListener {
            binding.eventViewFlipper.stopFlipping()
        }

        binding.next.setOnClickListener {
            binding.eventViewFlipper.showNext()
        }

        binding.previous.setOnClickListener {
            binding.eventViewFlipper.showPrevious()
        }
    }
```
## Xml

```xml
    <com.iron.library.EventViewFlipper
        android:id="@+id/eventViewFlipper"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:id="@+id/first_textView"
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:gravity="center"
            android:textColor="@color/black"
            android:textSize="20dp" />

        <TextView
            android:id="@+id/second_textView"
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:gravity="center"
            android:textColor="@color/black"
            android:textSize="20dp" />

    </com.iron.library.EventViewFlipper>
```

## Properties

| Name | Parameter or Type | Default Value | Description |
| --- | --- | --- | --- |
| onViewChangeListener | (View, Int) -> Unit) | null | Listener executed when current view is set to next view by ViewFlipper |
| count | Int? | null | Size of data to bind to View |
| index | Int | 0 | The index of the data referenced for binding to the view |
+ Please check the source code to check member methods except for member variables.

##

## Note

- This EventViewFlipper was created for the purpose of reusing Views. It's the same reason I gave an example using 2 TextViews.
- This EventViewFlipper can be used to sequentially bind data whose size is larger than the size of the childView.
- This EventViewFlipper can be started and stopped automatically, and manually running showNext() or showPrevious() works fine.
