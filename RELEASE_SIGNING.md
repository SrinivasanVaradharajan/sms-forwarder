# Release signing

This project now builds a release APK in CI.

To sign it with your own release key, add these Gradle properties locally or in CI secrets:

- `RELEASE_KEYSTORE_PATH`
- `RELEASE_STORE_PASSWORD`
- `RELEASE_KEY_ALIAS`
- `RELEASE_KEY_PASSWORD`

Example `gradle.properties` entries:

```properties
RELEASE_KEYSTORE_PATH=/path/to/release.keystore
RELEASE_STORE_PASSWORD=your-store-password
RELEASE_KEY_ALIAS=your-key-alias
RELEASE_KEY_PASSWORD=your-key-password
```

If these properties are not set, the release variant falls back to debug signing so the build remains installable for private testing.
