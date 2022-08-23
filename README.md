**English** | [简体中文](README_CN.md)

# sample-gradle-plugin

[![version](https://img.shields.io/maven-central/v/com.airsaid/sample-plugin)](https://search.maven.org/artifact/com.airsaid/sample-plugin)
[![license](https://img.shields.io/github/license/airsaid/statelayout)](LICENSE)

:rocket: A powerful Gradle Plugin to help you demonstrate your android app.

We often write demo applications that contain a lot of template code, such as lists of different
demo cases, jump logic, application permissions and fake data, and more.

SamplePlugin provides various components through annotations and automatically generates this
template code so that people viewing the demo application can understand the application more
easily.

> Download the sample apk to see more: [Sample APK](sample.apk).

# Minimal supported versions

Due to limited effort, SamplePlugin is currently supported up to version 7.x:

- Minimal supported [Gradle](https://www.gradle.org) version: `7.0.2`.
- Minimal
  supported [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin)
  version: `7.0.0`.

# Setup

1. Add the following code to the root project `build.gradle.kts` file:

```kotlin
plugins {
  id("com.airsaid.sample") version "<latest_version>" apply false
}
```

2. Add the following code to the android application project (app by default) `build.gradle.kts`
   file:

```kotlin
plugins {
  id("com.airsaid.sample")
}

dependencies {
  implementation("com.airsaid:sample-extension:<latest_version>")
}
```

<details>
<summary>Old setup method</summary>

1. Add the following code to the root project `build.gradle.kts` file:

```kotlin
buildscript {
  dependencies {
    classpath("com.airsaid:sample-gradle:<latest_version>")
  }
}
```

2. Add the following code to the android application project (app by default) `build.gradle.kts`
   file:

```kotlin
plugins {
  id("com.airsaid.sample")
}

dependencies {
  implementation("com.airsaid:sample-extension:<latest_version>")
}
```

</details>

# How to use

Register as an example by adding the `@Register` annotation to the `activity` or `fragment` class (
or other supported component) for which the example code is written. For example:

```kotlin
@Register
class DemoFragment : Fragment() {
  ...
}
```

Then we run the application and we can see that the example we just registered has appeared in the
automatically generated list.

By default, the title of each example item in the list is the class name of the example. If you want
to change the title or add a description, you can add the `title` and `desc` attributes to the
annotation. For example:

```kotlin
@Register(title = "Demo", desc = "Demonstrate how to use.")
class DemoFragment : Fragment() {
  ...
}
```

For the case of multiple examples, we organize the relationship between the examples by package
name.

For example, suppose this is the structure of your project:

```
com.airsaid.demo.fragment
    FragmentDemo1.kt
    FragmentDemo2.kt
com.airsaid.demo.ui
    ViewDemo1.kt
    ViewDemo2.kt
```

The result of the automatic generation of the demo list is:

```
|-- fragment
  |-- FragmentDemo1
  |-- FragmentDemo2
|-- ui
  |-- ViewDemo1
  |-- ViewDemo2
```

That's all, You don't need to do anything. Or you can specify it manually using the `path` attribute
of the `@Register` annotation. For example:

```kotlin
@Register(path = "android/view")
class DemoFragment : Fragment() {
  ...
}
```

## Working with testcase

Imagine you have a vast demonstration app, and finding one demo is hard. So you may want to open it
directly whenever you launch your app.

The solution is to use the `@TestCase` annotation, which specifies a test case that will be
automatically opened when you start the application. In the case of specifying multiple test cases,
a dialog will alert for you to select:

```kotlin
@TestCase
@Register
class DemoFragment : Fragment() {
  ...
}
```

## Working with extension

We provide a number of extension annotations to easily add additional demos to the sample.

For example, When you use `@SampleSourceCode`, it will help you generate a ViewPager and put your
source code as a list:

```kotlin
@SampleSourceCode
@Register
class DemoFragment : Fragment() {
  ...
}
```

Currently, the sample framework itself provides the following extension annotations. Or you can
extend it yourself, as seen in the sample application.

### @SampleDocument

Display the related document. For example:

```kotlin
// You can specify the documents in the assets directory:
@SampleDocument("assets://sample_document.md")
// Or you can set up an online documentation address:
// @SampleDocument("https://raw.githubusercontent.com/Airsaid/sample-gradle-plugin/master/README.md")
// Or you can specify documents in any directory, e.g. under the same level as the current class:
// @SampleDocument("sample_document.md")
@Register(
  title = "SampleDocument",
  desc = "Use @SampleDocument to associate documents to sample."
)
class SampleDocumentFragment : Fragment() {
  ...
}
```

<img width="300" height="630" src="preview/document.png"/>

### @SampleSourceCode

Display the source file. For example:

```kotlin
// By default, all source classes of the package where the current class is located are displayed.
@SampleSourceCode
// Or you can use regular expressions that filter only the required classes:
// @SampleSourceCode("SampleSourceCodeFragment.kt")
@Register(
  title = "SampleSourceCode",
  desc = "Use @SampleSourceCode to associate source code to sample."
)
class SampleSourceCodeFragment : Fragment() {
  ...
}
```

<img width="300" height="630" src="preview/source_code.png"/>

### @SampleMessage

Display the test message. For example:

```kotlin
@SampleMessage
@Register(
  title = "SampleMessage",
  desc = "Use @SampleMessage to take the messages output by System.out " +
      "and display them on the page."
)
class SampleMessageFragment : Fragment() {
  ...
}
```

<img width="300" height="630" src="preview/message.png"/>

### @SampleMemory

Display the runtime memory. For example:

```kotlin
@SampleMemory
@Register(
  title = "SampleMemory",
  desc = "Use @SampleMemory to quickly see the current memory usage on the page."
)
class SampleMemoryFragment : Fragment() {
  ...
}
```

<img width="300" height="630" src="preview/memory.png"/>

### @SamplePermission

Automatically request specified permissions. For example:

```kotlin
@SamplePermission(
  Manifest.permission.CAMERA,
  Manifest.permission.READ_EXTERNAL_STORAGE
)
@Register(
  title = "SamplePermission",
  desc = "Use @SamplePermission and specify the permissions to be requested to " +
      "automatically request permissions when the page is first opened."
)
class SamplePermissionFragment : Fragment() {

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    addPermissionObserver { result ->
      if (result.granted) {
        Toast.makeText(context, "${result.name} Permission request successful!", Toast.LENGTH_SHORT)
          .show()
      } else {
        Toast.makeText(context, "${result.name} Permission request failed!", Toast.LENGTH_SHORT)
          .show()
      }
    }
  }
  ...
}
```

# Project structure

- **app**: the demo app.
- **sample-api**: common api for both library and plugin.
- **sample-core**: the core library. Support for core APIs such as the base action processor.
- **sample-extension**: the extension library. Based on the core library, it provides all kinds of
  extended functions.
- **sample-plugin**: GradlePlugin for collecting annotation information at compile time and
  modifying bytecode files.

# License

```
Copyright 2022 Airsaid. https://github.com/airsaid

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
