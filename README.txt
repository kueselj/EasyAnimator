EasyAnimator

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