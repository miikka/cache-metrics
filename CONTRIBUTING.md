You want to contribute to cache-metrics? That's cool!

If you want to contribute a small change like a small bug fix or a documentation fix,
feel free to just send a pull request. For bigger changes, please open an issue to discuss it first.

If possible, please write an unit test for your change.
You can run the tests with `lein eftest`.

## Pull requests

We use [a Hypothesis-style release process](https://hypothesis.works/articles/continuous-releases/).
This means you get to write a changelog entry for your changes. To do this, create a file named `RELEASE.md`
in the root of the repository. The first line should be one of the following:

* `RELEASE_TYPE: major` – use this if you make breaking changes to the public API.
* `RELEASE_TYPE: minor` - use this if you add new functionality in backwards-compatible manner.
* `RELEASE_TYPE: patch` - use this if you make backwards-compatible fixes.

The rest of the file should be a short summary of your changes.

Once your pull request gets merged, your changelog entry gets automatically added to [the changelog](https://github.com/miikka/cache-metrics/blob/master/CHANGELOG.md)
and a new release will be made.
