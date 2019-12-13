# StopMotion

### Terminology

- FRAME - a snapshot of blocks/entities/tile-entities that is placed during the animation for a given duration
- TIMELINE - a uniquely named ordered list of FRAMES
- RULE - a condition that is tested when determining whether an ANIMATION should play or not
- TRIGGER - a uniquely named list of RULES which must all be active for an ANIMATION to play
- ANIMATION - combines a TIMELINE and one or more TRIGGERS, and is configured to play somewhere in the world

### Overview

The process of setting up an animation consists of:
1. Building a series of frames & saving them as a timeline
2. Building a set of rules & saving them as a trigger
3. Building the animation by specifying a timeline, trigger(s), a paste position (origin), the animation 'mode', and
 the animation direction(s)

### Quick Start

#### Step 1. Create a Timeline

1. Bind a selection wand to the item you're holding using `/sm wand`
2. Select 2 points (left & right click respectively) that contain the area you want to save as a frame
3. Add the frame to your timeline using `/timeline add <duration>` - **NOTE** the frame will be pasted relative to the position you are standing when using the command, much like WorldEdit's copy/paste behaviour.
4. Repeat the above steps for each frame you want to add to the timeline
5. Save the timeline using `/timeline save <timeline_name>`

#### Step 2. Create a Trigger

1. Add a rule to the trigger using `/trigger add ...` (see the section on triggers for each sub-command)
2. Save the trigger using `/trigger save <trigger_name>`

#### Step 3. Create the Animation

1. Set timeline that should be used with `/anim timeline <timeline_name>`
2. Add one or more triggers using `/anim trigger <trigger_name>`
3. Set the paste position to your current location using `/anim origin`
4. Set the 'mode' for the animation with `/anim mode <mode>`
5. Set the direction(s) that the frames should play in `/anim direction <direction(s)>`
6. Save the animation using `/anim save <animation_name>`

#### 4. Reload the StopMotion Plugin & Test

Newly created animations will not become active until the StopMotion plugin has been reloaded.  

Use `/sm reload` then try make the animation play by satisfying the trigger that you set up in step 2

### Documentation

#### Rules & Triggers

When creating a trigger, every rule that you add to it must be active for the trigger itself to be active.

An animation can have one or more triggers. If the animation is given multiple triggers, only one trigger needs to be active to cause the animation to play.

- `/trigger add message <message>` - listen to chat for the `<message>` in order to activate the rule
- `/trigger add distance <radius>` - a player must be within the `<radius>` of your position (at the time of running the command) in order to activate the rule
- `/trigger add time <min> <max>` - the world time must be within the provided bounds in order to activate the rule
- `/trigger add interact` - a player must interact with a block within your current selection (using the selection wand) in order to active the rule
- `/trigger add permission <node>` - a player must have the permission `stopmotion.rule.<node>` to be able to activate the rule

#### Animation Mode & Direction

Directions:
- FORWARDS - the timeline will play in the order that frames were added to it
- BACKWARDS - the timeline will play in reverse order

When creating an animation you can specify more than one direction for the timeline to play in during the animation.  
For example, you can create a timeline of a door opening and use the command `/anim direction FORWARDS BACKWARDS` to have the door close during the second half of the animation.

Animation Modes:
- SINGLE - the animation will play through it's timeline in the same manner each time it is triggered
- PUSH_PULL - the animation will alternate between a 'push' and 'pull' playback mode each time it is triggered (where the 'pull' mode plays the animation in reverse)
