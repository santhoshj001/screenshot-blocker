# Contributing to Screenshot Blocker

Thank you for your interest in contributing to Screenshot Blocker! This document provides guidelines for contributing to this project while respecting the project's governance model.

## 🏛️ Project Governance

**Important**: This project is maintained by [@santhoshj001](https://github.com/santhoshj001) as the sole maintainer. All contributions are welcome, but:

- ✅ **You can**: Fork, create issues, submit pull requests, suggest features
- ❌ **You cannot**: Push directly to main branch, merge PRs, publish releases
- 🔍 **All PRs**: Require explicit approval from the maintainer before merging
- 📋 **CODEOWNERS**: Ensures maintainer review on all critical files

This governance model ensures library stability and security while welcoming community contributions.

---

## 🚀 Getting Started

### Fork and Clone
1. **Fork the repository** on GitHub to your account
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/screenshot-blocker.git
   cd screenshot-blocker
   ```
3. **Add upstream remote** (to sync with main repo):
   ```bash
   git remote add upstream https://github.com/santhoshj001/screenshot-blocker.git
   ```

### Development Setup

#### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 17 or later
- **Android SDK**: API 21+ (Android 5.0 Lollipop)
- **Git**: For version control

#### Building the Project
```bash
# Build the library
./gradlew :screenshotblocker:build

# Build sample app
./gradlew :sample:build

# Build everything
./gradlew build
```

#### Running Tests
```bash
# Unit tests only
./gradlew test

# Instrumentation tests (requires device/emulator)
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

---

## 🎯 Types of Contributions

### 🐛 Bug Reports
- Use GitHub Issues with the "bug" label
- Include Android version, device info, and reproduction steps
- Provide stack traces and relevant logs
- Search existing issues first to avoid duplicates

### ✨ Feature Requests
- Use GitHub Issues with the "enhancement" label
- Clearly describe the problem your feature solves
- Provide use cases and examples
- Consider if the feature aligns with the library's core purpose

### 📖 Documentation Improvements
- Fix typos, improve clarity, add examples
- Update API documentation and code comments
- Improve README and contributing guides

### 🧪 Test Improvements
- Add missing test coverage
- Improve existing tests
- Add integration or performance tests

### 🔧 Code Contributions
- Bug fixes and performance improvements
- New features (discuss in issues first)
- Code refactoring and optimization

---

## 🛠️ Development Guidelines

### Code Style & Standards
- **Kotlin**: Follow [official conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Formatting**: Use Android Studio default formatter
- **Documentation**: Add KDoc for all public APIs
- **Naming**: Use clear, descriptive names
- **Functions**: Keep small and focused (< 20 lines preferred)

### API Design Principles
- **Backward compatibility**: Don't break existing APIs
- **Simplicity**: Keep APIs simple and intuitive  
- **Safety**: Fail gracefully, handle edge cases
- **Performance**: Minimal overhead and memory usage
- **Testing**: All public APIs must be testable

### Security Considerations
- **No sensitive data logging**
- **Validate all inputs**
- **Handle permissions safely**
- **Consider edge cases and malicious usage**

---

## 📋 Submission Process

### 1. Before You Start
- **Check existing issues** to see if someone is already working on it
- **Create an issue** for new features to discuss the approach
- **Keep changes focused** - one PR per feature/fix

### 2. Development Workflow
```bash
# Sync with upstream
git fetch upstream
git checkout main
git merge upstream/main

# Create feature branch
git checkout -b feature/your-feature-name

# Make your changes, commit frequently
git add .
git commit -m "Clear, descriptive commit message"

# Push to your fork
git push origin feature/your-feature-name
```

### 3. Pull Request Requirements

#### Must Have:
- [ ] **Clear description** of changes and motivation
- [ ] **Tests added/updated** for new functionality
- [ ] **Documentation updated** if API changes
- [ ] **All existing tests pass**
- [ ] **No breaking changes** (unless explicitly discussed)
- [ ] **CHANGELOG.md updated** for user-facing changes

#### Code Quality:
- [ ] **Self-reviewed** your own code
- [ ] **Comments added** for complex logic
- [ ] **No compiler warnings** introduced
- [ ] **Follows project conventions**

#### Testing:
- [ ] **Unit tests pass**: `./gradlew test`
- [ ] **Integration tests pass**: `./gradlew connectedAndroidTest`
- [ ] **Manual testing completed** on real device
- [ ] **Multiple API levels tested** (21, 28, 33+)

### 4. PR Review Process
1. **Automated checks** must pass (CI/CD)
2. **Code review** by maintainer (@santhoshj001)
3. **Feedback addressed** and changes requested implemented
4. **Final approval** and merge by maintainer

---

## ⚠️ Important Notes

### Response Times
- **Issues**: Typically acknowledged within 48 hours
- **Pull Requests**: Initial review within 1 week
- **Complex PRs**: May take longer for thorough review

### What Gets Priority
1. **Critical bug fixes** affecting security or stability
2. **Well-documented features** with clear use cases
3. **Performance improvements** with benchmarks
4. **Documentation improvements**
5. **Test coverage improvements**

### What Might Be Rejected
- **Breaking changes** without compelling reason
- **Features outside core scope** (screenshot blocking)
- **Poorly tested code**
- **Code that doesn't follow project conventions**
- **Duplicate functionality**

---

## 🤝 Code of Conduct

### Be Respectful
- **Constructive feedback** only
- **Professional communication**
- **Inclusive language**
- **No harassment or discrimination**

### Be Collaborative
- **Help others** in issues and discussions
- **Share knowledge** and best practices
- **Credit contributors** appropriately

### Be Patient
- **Maintainer is human** with limited time
- **Quality takes time** - thorough reviews prevent bugs
- **Open source is volunteer work**

---

## 🆘 Getting Help

### Questions About:
- **Usage**: Check README.md and sample app first
- **Contributing**: Create an issue with "question" label
- **Bugs**: Follow bug report template
- **Features**: Create feature request issue

### Resources:
- 📖 **Documentation**: [README.md](README.md)
- 🏗️ **Sample App**: `/sample` directory
- 🧪 **Tests**: `/screenshotblocker/src/test`
- 🤖 **CI/CD**: `.github/workflows/`

---

## 📜 Release Process (Maintainer Only)

For transparency, here's how releases work:

1. **Version bump** in relevant files
2. **CHANGELOG.md** updated with changes
3. **Git tag** created (`v1.x.x`)
4. **GitHub release** published
5. **JitPack** automatically builds and publishes

Contributors cannot perform releases, but can suggest version bumps in PRs.

---

## 🙏 Recognition

All contributors are recognized in:
- **GitHub contributors** page
- **CHANGELOG.md** for significant contributions
- **Special thanks** for major features/fixes

---

**Thank you for helping make Screenshot Blocker better! 🛡️📱**

Questions? Feel free to open an issue or start a discussion! 