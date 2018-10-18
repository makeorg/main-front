## Make Front

This application is an integration of the make.org site using scala-js and react
 
### Setting up the dev environment
 *  install nodejs >= 6 and npm >= 3
 *  Install the scalafmt IntelliJ plugin
 *  Install the scala IntelliJ plugin
  
### Running the app
  
````
make package-docker-image
docker run -p 9009:80 -e API_URL=https://api.preprod.makeorg.tech nexus.prod.makeorg.tech/make-front:latest

````

After that you can watch you work at the address [http://localhost:9009/webpack-dev-server/](http://localhost:9009/webpack-dev-server/)

### Force country selection

You should first install ModHeader extension to be able to add request header :

 - [ModHeader for Chrome](https://chrome.google.com/webstore/detail/modheader/idgpnmonknjnojddfkpgkljpfnnfcklj)
 - [ModHeader for Firefox](https://addons.mozilla.org/en-US/firefox/addon/modheader-firefox/)

Open it and add an request header named x-forced-country with the country code desired as value.

### License

This project is licenced under the GNU GPL license version 3 or later. See the [license](LICENSE.md)