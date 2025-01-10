# CUtils: A Comprehensive Java Utility Library

CUtils is a versatile and lightweight utility library designed to enhance productivity in Java projects by providing intuitive and reusable utility classes for common tasks.

## Key Features

### Date and Time Utilities
- **`CDate`**: A powerful wrapper for date manipulations with support for custom formatting, comparisons, and more.
- **`CPeriod`**: Simplifies working with periods (days, months, years) with utility methods for addition, subtraction, and formatting.
- **`CDuration`**: A robust tool for managing and manipulating time durations (hours, minutes, seconds).

### Number Utilities
- **`CDecimal`**: A precise and extensible wrapper for `BigDecimal` with convenient arithmetic, scaling, and comparison methods.

### String Utilities
- **`CStrings`**: A collection of methods for padding, trimming, splitting, and more, making string operations easier and safer.

## Why Use CUtils?
- **Ease of Use**: Simplifies tedious, boilerplate-heavy tasks with concise and expressive APIs.
- **Reliability**: Built with immutability and strong typing to minimize errors.
- **Flexibility**: Adapts seamlessly to a variety of use cases in enterprise and personal projects.

## Planned Features
- Additional utility classes for collections, file I/O, and more.
- Extended functionality for existing classes based on community feedback.

## Installation
### Step 1: Configure `pom.xml`
1. **Add the GitHub Packages Repository:**
In your `pom.xml`, add the following under the `<repositories>` section to point Maven to the GitHub Packages repository:
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/ChristianHosh/c-utills</url>
    </repository>
</repositories>
```

2. **Add the Dependency:**
Add the following dependency to use c-utils:
```xml
<dependencies>
    <dependency>
        <groupId>com.chris</groupId>
        <artifactId>c-utils</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

### Step 2: Generate a Personal Access Token (PAT)
1. Go to GitHub:
    - Navigate to GitHub and go to `Settings > Developer settings > Personal access tokens`.
2. Generate a New Token:
    - Click on `Generate new token`.
    - Provide a descriptive name for the token.
    - Select the `read:packages` scope
3. Save the Token:
    - Copy the generated token. You will use it in the next step.
  
### Step 3: Configure `settings.xml`
1. Locate or Create `settings.xml`:
    - The file is typically located at `~/.m2/settings.xml`. If it doesn't exists, create it.
2. Add Server Authentication:
    - Add the following configuration to your `settings.xml`:
      ```xml
      <servers>
          <server>
              <id>github</id>
              <username>your-github-username</username>
              <password>your-personal-access-token</password>
          </server>
      </servers>
      ```
    - Replace `your-github-username` with your GitHub username.
    - Replace `your-personal-access-token` with the token you generated.

### Step 4: Install the Package
1. Run Maven Command:
```bash
mvn clean install
```
2. Maven will now download the package from GitHub Packages using the provided credentials
---

Whether you're building a large-scale enterprise system or a simple project, CUtils provides the tools you need to streamline development and enhance code readability.
