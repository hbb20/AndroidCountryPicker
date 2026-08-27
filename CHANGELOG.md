# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Next Release]

- Update ip2location data @hbb20
- Search for first letters of country name like UAE shows Unite Arab Emirates and USA shows United
  States of America @hbb20
- Remove preferred countries section if user is entering query. User is searching because they did
  not find required country in the preferred countries so that section is of no use when searching.
  Although, preferred country will continue to show up in regular filtered list if matches for
  query. @hbb20
- Add support to JetPack Compose @hbb20
- Out of box composables for Material2 @hbb20

## v0.0.7 2022-01

- Japanese language support by Harsh Bhakta (@hbb20)
- Print multiple translation errors at once to avoid multiple runs (@hbb20)
- Change library theme to avoid possible issue @hbb20

## v0.0.6 2021-07-31

- Option to pass size mode for dialog

## v0.0.5 2021-07-31

- Fix dependency issue for flag pack

## v0.0.4 2021-07-14

- Option to include flagPack

## v0.0.3 2021-06-23

- Fix crash on non-material theme

## v0.0.2 2021-04-13

- Publish to MavenCentral with artifact id `android-country-picker`
- Remove `jCenter()` usage

## v0.0.1
- Basic functionality

