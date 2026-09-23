# v0.5.1 build fix

The failed build was caused by AndroidX dependencies being present while
`android.useAndroidX` was not enabled.

The important fix is in the repository root:

android.useAndroidX=true

`android.enableJetifier=true` is also enabled for compatibility with any
legacy third-party dependency.

After copying these files into the existing `sarvato-playstore` repository,
commit/push and run the GitHub Actions workflow again.
