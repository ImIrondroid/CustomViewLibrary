# Android Custom View (Contributions are welcome)

## 1. PointSeekBar

![pointseekbar](https://user-images.githubusercontent.com/48594786/166239904-0e66429d-30b1-45f8-ac62-c73c6aade6ae.gif)

## Description

This PointSeekbar is simple to use and is made to display information that can be used basically.

## XML attributes

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

## XML attributes

| Name | Format | Default | Description |
|:----:|:----:|:-------:|:-----------:|
|lineColor|color/refernce|#F8FAFC|unselected progressbar color(Background)|
|progressColor|color/refernce|#FDC6CE|selected progressbar color|
|thumbMarkTextColor|color/refernce|Color.WHITE|thumbmark text color|
|tickMarkCount|integer|Color.BLUE|5|number of tickmarks you want to display|

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
