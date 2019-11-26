# Animations

### Quickstart

#### Step 1. Create an animation timeline

The timeline is a series of frames that play one after another, making up the animation.

1. `/frame pos1` & `/frame pos2` - Selects two corners of the frame
2. `/frame add 1` - Records the frame and sets the duration to 1 tick (Repeat as necessary)
3. `/frame save example` - Save and registers the timeline as an animation called `"example"`

#### Step 2. Create an animation trigger

Triggers are used to determine when an animation should start playing.

Different trigger types can be combined to create a specific set of circumstances for the animation to play.

In this example, we want want the words "open sesame" to trigger the animation, but only when we're with 25 blocks
 of the animation position.

1. `/trigger distance 50 65 50 25` - Creates a distance trigger using `Position(50, 65, 50)` & `Radius(25)`
2. `/trigger message open sesame` - Creates a message trigger using `Message("open sesame")`
3. `/trigger save sesame` - Combines the above two triggers into one and saves with the name `"sesame"`

#### Step 3. Create an animation instance

An animation instance combines the timeline created in step 1, and the trigger(s) created in step 2.
1. `/anim origin 50 65 50` - Sets the paste position for the animation at `Position(50, 65, 50)`
2. `/anim animation example` - Select the `"example"` animation created in step 1
3. `/anim trigger sesame` - Select the `"sesame"` trigger created in step 2
4. `/anim timeline FORWARD BACKWARD` - Set the animation to play forward once, then backward once
5. `/anim save <name>` - Save & register the animation instance