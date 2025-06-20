# Contributing to Screenshot Blocker

Thank you for your interest in contributing to Screenshot Blocker! We welcome contributions from the community.

## Development Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/sjdroid/screenshot-blocker.git
   cd screenshot-blocker
   ```

2. **Open in Android Studio:**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned repository and select it

3. **Build the project:**
   ```bash
   ./gradlew clean build
   ```

4. **Run tests:**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

## Project Structure

```
ScreenShotBlocker/
├── app/                          # Library module
│   ├── src/main/java/           # Main source code
│   │   └── com/sjdroid/screenshotblocker/
│   │       ├── ScreenshotBlocker.kt           # Main library class
│   │       └── ActivityLifecycleCallbacksAdapter.kt # Utility class
│   ├── src/test/java/           # Unit tests
│   └── src/androidTest/java/    # Instrumented tests
├── build.gradle.kts             # Root build script
├── README.md                    # Project documentation
└── LICENSE                      # Apache 2.0 License
```

## Code Style

This project follows the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).

### Key Guidelines:

- Use 4 spaces for indentation
- Keep lines under 120 characters
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Follow the existing naming conventions

## Testing

### Unit Tests

All new functionality should include comprehensive unit tests:

```kotlin
@Test
fun `test new feature functionality`() {
    // Arrange
    // Act 
    // Assert
}
```

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires connected Android device/emulator)
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

## Submitting Changes

### Before Submitting

1. **Run all tests:** Ensure all tests pass
2. **Check code style:** Follow the project's coding conventions
3. **Update documentation:** Update README.md if needed
4. **Write clear commit messages:** Use descriptive commit messages

### Pull Request Process

1. **Fork the repository** on GitHub
2. **Create a feature branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes** and commit them:
   ```bash
   git commit -m "Add feature: description of what you added"
   ```
4. **Push to your fork:**
   ```bash
   git push origin feature/your-feature-name
   ```
5. **Create a Pull Request** on GitHub

### Pull Request Guidelines

- **Title:** Use a clear, descriptive title
- **Description:** Explain what changes you made and why
- **Testing:** Describe how you tested your changes
- **Breaking Changes:** Clearly indicate any breaking changes

## Types of Contributions

### Bug Reports

When reporting bugs, please include:

- **Description:** Clear description of the issue
- **Steps to reproduce:** Detailed steps to reproduce the bug
- **Expected behavior:** What you expected to happen
- **Actual behavior:** What actually happened
- **Environment:** Android version, device info, library version
- **Code sample:** Minimal code that reproduces the issue

### Feature Requests

When requesting features:

- **Use case:** Describe why this feature would be useful
- **Proposed solution:** How you think it should work
- **Alternatives:** Other solutions you've considered

### Code Contributions

We welcome contributions for:

- **Bug fixes**
- **New features** (please discuss large changes first)
- **Performance improvements**
- **Documentation improvements**
- **Test coverage improvements**

## Development Guidelines

### API Design

- **Keep it simple:** The API should be easy to use
- **Follow Android conventions:** Use familiar Android patterns
- **Backward compatibility:** Avoid breaking changes when possible
- **Thread safety:** Document thread safety requirements

### Performance

- **Minimal overhead:** Keep performance impact minimal
- **Memory efficient:** Avoid memory leaks
- **Battery conscious:** Don't drain battery unnecessarily

### Security

- **No sensitive data logging:** Never log sensitive information
- **Minimal permissions:** Only request necessary permissions
- **Safe defaults:** Use secure defaults

## Release Process

1. **Update CHANGELOG.md** with release notes
2. **Create release tag:** `git tag v1.0.0`
3. **Push tag:** `git push origin v1.0.0`
4. **JitPack will automatically build** from the new tag

## Getting Help

- **Questions:** Open a [GitHub Discussion](https://github.com/sjdroid/screenshot-blocker/discussions)
- **Issues:** Report bugs via [GitHub Issues](https://github.com/sjdroid/screenshot-blocker/issues)
- **Documentation:** Check the [README.md](README.md) first

## Code of Conduct

By participating in this project, you agree to abide by our Code of Conduct:

- **Be respectful** and inclusive
- **Be collaborative** and helpful
- **Be patient** with newcomers
- **Focus on the best outcome** for the community

Thank you for contributing! 🎉 