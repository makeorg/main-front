## Make Front

This application is an integration of the make.org site using scala-js and react
 
### Setting up the dev environment
 *  install nodejs >= 6 and npm >= 3
 *  Install the scalafmt IntelliJ plugin
 *  Install the scala IntelliJ plugin
  
### Running the app
  
  In order to run the front app, you should launch `sbt` 
  and than launch the webpack dev server
  
  * fastOptJS::startWebpackDevServer 

you can lauch the server in incremental way by adding `~`

After that you can watch you work at the address [http://localhost:9009/webpack-dev-server/](http://localhost:9009/webpack-dev-server/)

### Force country selection

You should first install ModHeader extension to be able to add request header :

 - [ModHeader for Chrome](https://chrome.google.com/webstore/detail/modheader/idgpnmonknjnojddfkpgkljpfnnfcklj)
 - [ModHeader for Firefox](https://addons.mozilla.org/en-US/firefox/addon/modheader-firefox/)

Open it and add an request header named x-forced-country with the country code desired as value.
