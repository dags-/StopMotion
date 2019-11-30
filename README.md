# Animations

### Quickstart

#### Step 1. Create an animation timeline

The timeline is a series of frames that play one after another, making up the animation.

1. `/frame pos1` & `/frame pos2` - Selects two corners of the frame
2. `/frame add 1` - Records the frame and sets the duration to 1 tick (Repeat as necessary)
3. `/frame save example` - Create a timeline from the recorded frames, called `"example"`

#### Step 2. Create an animation trigger

Triggers are used to determine when an animation should start playing, & consist of 1 or more rules.

Different rule types can be combined to create a specific set of circumstances for the animation to play.

In this example, we want want the words "open sesame" to trigger the animation, but only when we're with 25 blocks
 of the animation position.

1. `/trigger add distance 50 65 50 25` - Creates a distance rule using `Position(50, 65, 50)` & `Radius(25)`
2. `/trigger add message open sesame` - Creates a message rule using `Message("open sesame")`
3. `/trigger save sesame` - Creates a trigger combining the above two rules, with the name `"sesame"`

#### Step 3. Create an animation instance

An animation instance combines the timeline created in step 1, and the trigger(s) created in step 2.
1. `/anim origin 50 65 50` - Sets the paste position for the animation at `Position(50, 65, 50)`
2. `/anim animation example` - Set the animation to the `"example"` animation created in step 1
3. `/anim trigger sesame` - Add the `"sesame"` trigger created in step 2
4. `/anim direction FORWARD BACKWARD` - Set the animation to play forward once, then backward once
5. `/anim save magic_door` - Create an animation instance with the name `"magic_door"`
6. `/anim reload` - Reload the animations plugin so that the new animation becomes active