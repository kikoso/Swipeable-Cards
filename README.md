Swipeable cards: Tinder-like cards library for Android
=================

Swipeable-cards is a native library for Android that provide a Tinder card like effect. A card can be constructed using an image and displayed with animation effects, dismiss-to-like and dismiss-to-unlike, and use different sorting mechanisms.

The library is compatible for Android versions 3.0 (API Level 11) and upwards.

A [library][1] and a [sample application][2] are provided with the code.

![Example Image][3]
![Example Image][4]


Usage
--------------------
Download the library with git and import it into your project (right now there is only Gradle support, so you need to import it writing in your build.gradle the following:

     compile project(':AndTinder')

and in your settings.gradle

    include 'AndTinder'

When you have included the library in your project, you need to proceeed as follows. First, create a container to store the cards.

    <com.andtinder.view.CardContainer xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/layoutview"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent" />
    
From your Activity, inflate into a CardContainer the container you declared in your XML
    
    mCardContainer = (CardContainer) findViewById(R.id.layoutview);

The card container can sort the cards either ordered or disordered:

     mCardContainer.setOrientation(Orientation.Ordered);
     mCardContainer.setOrientation(Orientation.Disordered);
     
Now you need to create your cards. The procedure is quite simple: you just need to create an object CardView and provide the image resource you want to add:

    CardView card = new CardView(mCardContainer,new CardModel(R.drawable.picture1));
    
Additionally, you can set up a Delegate to be notified when the image is being liked or disliked:
     
     card.setOnCardDimissedDelegate(new CardView.OnCardDimissedDelegate() {
            @Override
            public void onLike(CardView cardView) {
                Log.d("AndTinder", "I liked it");
            }

            @Override
            public void onDislike(CardView cardView) {
                Log.d("AndTinder", "I did not liked it");
            }
        });

Finally, add the cards to the container:

    mCardContainer.addView(card);
   

Version history
--------------------
* 13.05.2014: Published the first version 0.1

Next steps
--------------------
There are many things that can be done with this library. 

* Allow custom templates
* Extend image personalization options
* Recreate the container when it has been emptied

If you want to colaborate with the project or have any idea to be implemented feel free to submit a pull request or to write an issue! 

Also, if you have used AndTinder on your app and you let me know, I can link it from here :)

Developed By
--------------------

Enrique López Mañas - <eenriquelopez@gmail.com>

<a href="https://twitter.com/eenriquelopez">
  <img alt="Follow me on Twitter"
       src="https://raw.github.com/kikoso/android-stackblur/master/art/twitter.png" />
</a>
<a href="https://plus.google.com/103250453274111396206">
  <img alt="Follow me on Google+"
       src="https://raw.github.com/kikoso/android-stackblur/master/art/google-plus.png" />
</a>
<a href="http://de.linkedin.com/pub/enrique-l%C3%B3pez-ma%C3%B1as/15/4a9/876">
  <img alt="Follow me on LinkedIn"
       src="https://raw.github.com/kikoso/android-stackblur/master/art/linkedin.png" />

[1]: https://github.com/kikoso/AndTinder/tree/master/AndTinder
[2]: https://github.com/kikoso/AndTinder/tree/master/AndTinderDemo
[3]: https://raw.github.com/kikoso/AndTinder/master/art/captura1.png
[4]: https://raw.github.com/kikoso/AndTinder/master/art/captura2.png
