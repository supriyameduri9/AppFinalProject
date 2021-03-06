# CMPE 277 Final Project

# Team #7
### ✨ Bhavya Tetali (014535144) , ✨Supriya Meduri(015262767)
#
#
![Screen](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/PocketNews-Merge.jpg)
<h1 align="center">Pocket News</h1>
<h4 align="center">
	Discover the top-headlines and general news all around the globe. News data fetched using <a href="https://newsapi.org/">newsapi.org</a> API.
</h4>


## ✨ Screenshots


| Home Screen | Demo |  Detail View |
|:-:|:-:|:-:|
| ![Fist](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/HomePage.png) | ![3](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/DemoGIFVideo.gif) | ![3](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/DetailView.png) |
| Add Story | My Stories Fragment | Saved Fragment
| ![4](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/AddStory.png) | ![5](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/MyStory.png)  | ![6](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/SavedFragment.png)|
| Share Article | All Stories Fragment | Push Notifications
| ![4](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/SharePopUp.png) | ![5](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/AllStories.png)  | ![6](https://github.com/supriyameduri9/AppFinalProject/blob/main/PocketNews-Screenshots/PushNotification.png)|


## ✨ Features

*   Discover the top rated and general news around the world
*   User can signup, login and signout 
*   Shows a list of all the news from different news sources
*   Users can save a news article for future read in the details view by tapping save icon 
*   Users can share news articles using external apps
*   Users can post their own stories they would like to share
*   Users can subscribe to either uplifting/positive or all news, in which case they will receive push notifications for stories shared by any Pocket News user.
*   Advanced uses of Room
*   MVVM with Android Architecture Components(Room, LiveData, ViewModel)
*   Firebase Cloud Messaging for push notifications service
*   Cloud Functions as a server for broadcasting remote notifications
*   Text Classification Tensorflow lite model (toxicity) for sentiment analysis of the stories posted.

## ✨ Technical Details

*   [Firebase Authentication](https://firebase.google.com/docs/auth) - for user login/signup
*   [Glide](https://github.com/bumptech/glide) - for loading and caching images 
*   [Retrofit 2](https://github.com/square/retrofit) - Type-safe HTTP client for Android and Java by Square, Inc. 
*   [Moshi](https://github.com/google/gson) - for serialization/deserialization Java Objects into JSON and back
*   [Room DB](https://developer.android.com/reference/android/arch/persistence/room/RoomDatabase) - for saving news articles
*   [Firestore](https://firebase.google.com/docs/firestore) - for storing user posts
*   [Firebase Storage](https://firebase.google.com/docs/storage/android/create-reference) - for storing images
*   [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/android/client) - for push notifications service
*   [Cloud Functions](https://firebase.google.com/docs/functions) - as a server for broadcasting remote notifications
*   [Text Classification Tensorflow lite Model](https://tfhub.dev/tensorflow/tfjs-model/toxicity/1/default/1) - for sentiment analysis of the stories posted.
*   [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
*   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
*   [DataBinding](https://developer.android.com/topic/libraries/data-binding/)

## ✨ Task Distribution

* Bhavya Lalithya
	* Add Stories, My Stories, and All Stories Fragment. 
	* Push Notifications, Broadcast Server, Tensorflow lite model integration
	* Estimate average read time for news articles and stories.	
	* Detailed View Page.
* Supriya Meduri
	* Login, Sign Out and Signup for users.
	* Get and display news data from multiple sources.
	* Search news functionality.
	* Save the news article .
	* Share the news to multiple apps.
	* Delete the news article from saved article with snackbar and "undo" action




