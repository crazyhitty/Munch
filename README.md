#Munch
A minimalistic, easy to use Rss reader application.

<a href="https://play.google.com/store/apps/details?id=com.crazyhitty.chdev.ks.munch">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_45.png" />
</a>

#What is Munch ?
Munch is an android app which enable the users to manage their Rss feeds. User can add new sources, manage them and view the article associated with the feeds.

#Permissions:
* Internet
* Access Network State

These both permissions are used to grab the data from the web and to know if user’s internet is available or not so that he can be notified regarding that matter. Also, this app will never snoop your personal information.

#Application features:
* Load Rss feeds quickly
* Add Rss Sources
* Manage Rss Sources
* Archive feeds
* Customizable settings
* Ad free

#How does Munch work ?
Munch uses jsoup xml parser to parse rss feeds and also load the web articles associated with those feeds. It also uses glide to lazy load article images(if available).
