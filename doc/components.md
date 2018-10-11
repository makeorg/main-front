```
Owner: Charley

* routes
* package organization
* facades
* data constraints & validation
```

# Routes

The [Container.scala](src/main/scala/org/make/front/components/Container.scala) file define the routes.
All the routes contains the country code. If one route is not available
in that country, user will be redirect to the `/:country/soon` page.

### Routes list

* `/:country` -> Homepage
* `/:country/soon` -> Display the current operation in `country`
* `/:country/search` -> Search result page according to the `country`
* `/:country/proposal/:proposalId/:proposalSlug` -> Proposal page according to the `proposalSlug` and the `country`
* `/:country/theme/:themeSlug` -> Theme page if this `theme` if available in the `country`
* `/:country/consultation/:operationSlug` -> Operation page according to `operationSlug` if available in the `country`
* `/:country/consultation/:operationSlug/selection` -> Sequence slider of the `operationSlug` if available in the `country`
* `/:country/profile` -> user Page
* `/:country/account-activation/:userId/:verificationToken` -> route use to activate the user account (via the makeAPI)
* `/:country/password-recovery/:userId/:resetToken` -> `Forgot your password ?` page
* `/404`-> 404 error page
* `/maintenance` -> maintenance page

# Packages organization

The code is divided into 4 main packages:

* client -> more information at [client.md](doc/client.md)
* core -> This package contains some utilities (URI formatter, data validation, counter)
* front -> See bellow for more details.
* services -> This package contains the different methods used to call the MakeAPI. It is organized by resource

### front package

This package contains the differents components ([react-components.md](doc/react-components.md)), the redux logic
([middlewres package](src/main/scala/org/make/front/middlewares) and [reducer package](src/main/scala/org/make/front/reducers)),
the data models ([models package](src/main/scala/org/make/front/models)) as well as the styles ([styles.md](doc/styles.md))