EasyAnimator

//ASSIGNMENT 6//

The implementation of our model (EasyAnimator, which is an implementation of an IAnimatorModel) can
be understood as dealing with two different categories of objects.

Motions which are a way to represent changing properties between different time ranges.

And Shapes which are the objects that Motions are supposed to edit.

Motions are an implementation of IMotion which describes a start time and an end time, and bunch
of properties that change between those times. These properties are packaged in our own custom
classes Point, Color, and WidthHeight.

In anticipation of animations becoming more complex than linearly changing properties we created
a BasicMotion class to be our start implementation with the intention of implementing
more advanced animations in the future (quadratic time, bounce properties, etc.) We plan on using
the square design pattern to get more advanced methods.

To traverse the different implementations of an IMotion we created an IMotionVisitor.

We try something similar for our implementation of shapes. Shapes are a description of some
graphic object like a Rectangle or an Oval (these two classes share a lot of implementation detail
so we use an AbstractShape between them). They have a basic set of properties described by getters
and setters in IShape. To traverse the different implementations we have an IShapeVisitor.

Currently we implement a text view by a method on the model. We plan to re-use our visitor code
to implement in a view.

===

New edits.

We decided to make our views work based of our visitor patterns previously described.

We didn't change much about our model design, but noticed that it would potentially be easier
to animate things with a keyframe based design. That would at least be easier to conform to
in the AnimationBuilder.

The views all use the IAnimatorView interface which describes a setModel method and a makeVisible
method. The second method having a bunch of different interpretations across the diff type
of views. Visual makeVisual means make the window visible, whereas for a text view it prints
out the text. We also have the method setSpeed which is supressed in the text view since all the
other views have an interpretation for it.

The views at the moment are controlled by an "ApplicationBuilder" which has a variety of methods
to add different settings as they come in from the command line parser. Having a builder meant
we didn't have a bunch of static code that went untested. At the end of adding a bunch of
parameters we use the build method which launches the application with those settings applied.

//ASSIGNMENT 7//

Just so ya know. Gotta press start to actually start the animation, not gonna
pop up for no good reason.

This assignment we decided to add keyFrames. However, we still have backwards compatibility with
our motions that we had. Adaptor much?. keyFrames add a lot of flexibility to the model as we can
now add the state of a given shape at a particular moment in time.

Next, we started implementing our new editorView and corresponding controller. This implementation
was a hot debate topic for the team. We on three separate occasions changed where the logic for
the view would be located. It is now all in the view, so ticks, the timer, etc. Piazza said this was
ok but as expected we ran into some major design issues when trying to implement listeners as they
somewhat had to live in the view as opposed to the controller which is not what we wanted.

Our view functionality can be placed into two categories. Playback controls, and our
editor features. The playback controls are all buttons. We think this looks better. KeyBoard buttons
are no fun. We did implement a keyBoard function though as requested to save and load files. The
editor features panel is a combination of JCombo boxes, JButtons, and textFields. A user can
use to JComboBoxes to select from all the available shapes. Another JComboBox is used to select
from all the available ticks for that given shape. Which, happens to be a keyFrame technically. All
of that selected keyFrame's fields are then displayed in the text boxes. The user can edit them and
save that keyFrame. They can also add a new keyframe for that shape. Buttons are used to create a
new shape, save a keyFrame, and delete a keyFrame.

We added some additional un-required features to the the view, such as the ability to pause, then
go through the individual ticks so the user can see a shapes state at an exact keyFrame. A tick
count is displayed for convenience. When selecting add a new keyframe, the textFields will
automatically fill with the state of the selected state at that tick!

Our controller implementation is unfortunately a little thin. Holds a model and a view.
It is itself a listener that the view can use for certain action events.
The controller, once activated by an action event, will then decide what to do. Renaming a shape,
creating a new keyFrame, deleting a keyFrame, modifying keyframe, adding a shape, deleting a shape.

//ASSIGNMENT 7//

All of the features in our code work. All of our views still work. Our editor view has all required
functionality.

//ASSIGNMENT 8//

We were unable to implement the editor features of our providers view for the simple reason that
they are not there. They did not implement these features in their view so it was impossible for
us to support these features. They did implement playback features and we got those to work.
