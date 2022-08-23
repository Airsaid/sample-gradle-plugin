[English](README.md) | **简体中文**

# sample-gradle-plugin

[![version](https://img.shields.io/maven-central/v/com.airsaid/sample-plugin)](https://search.maven.org/artifact/com.airsaid/sample-plugin)
[![license](https://img.shields.io/github/license/airsaid/statelayout)](LICENSE)

:rocket: 一个强大的 Gradle Plugin，可以帮助你演示你的 Android Demo 应用。

我们经常会写一些包含大量模版代码的 Demo 应用，例如包含不同演示用例的列表、跳转逻辑、申请权限和假数据等等。

SamplePlugin 通过注解来提供各类组件并自动生成这些模版代码，以便查看 Demo 应用的人能够更容易理解应用。

> 下载示例 APK 查看更多: [Sample APK](sample.apk).

# 最低支持版本

由于精力有限，目前 SamplePlugin 最低支持到 7.x 版本：

- 最低支持的 [Gradle](https://www.gradle.org) 版本: `7.0.2`。
- 最低支持的 [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin)
  版本: `7.0.0`。

# 设置

1. 添加以下代码到根项目的 `build.gradle.kts` 文件:

```kotlin
plugins {
  id("com.airsaid.sample") version "<latest_version>" apply false
}
```

2. 添加以下代码到 android 应用项目（默认是 app）的 `build.gradle.kts` 文件:

```kotlin
plugins {
  id("com.airsaid.sample")
}

dependencies {
  implementation("com.airsaid:sample-extension:<latest_version>")
}
```

<details>
<summary>旧的设置方法</summary>

1. 添加以下代码到根项目的 `build.gradle.kts` 文件:

```kotlin
buildscript {
  dependencies {
    classpath("com.airsaid:sample-gradle:<latest_version>")
  }
}
```

2. 添加以下代码到 android 应用项目（默认是 app）的 `build.gradle.kts` 文件:

```kotlin
plugins {
  id("com.airsaid.sample")
}

dependencies {
  implementation("com.airsaid:sample-extension:<latest_version>")
}
```

</details>

# 如何使用？

在编写了示例代码的 `activity` 或 `fragment` 类（或其他支持的组件）上加上 `@Register` 注解，注册为一个示例。例如：

```kotlin
@Register
class DemoFragment : Fragment() {
  ...
}
```

然后我们运行应用，就可以看到刚刚注册的示例已经出现在了自动生成的列表当中。

默认情况下，列表中每个示例条目的标题是示例的类名，如果想要改变标题或者加上描述，可以在注解上添加 `title` 和 `desc` 属性，例如：

```kotlin
@Register(title = "Demo", desc = "Demonstrate how to use.")
class DemoFragment : Fragment() {
  ...
}
```

对于多个示例的情况，我们通过包名进行组织各个示例之间的关系。

例如，假设这是你的项目结构：

```
com.airsaid.demo.fragment
    FragmentDemo1.kt
    FragmentDemo2.kt
com.airsaid.demo.ui
    ViewDemo1.kt
    ViewDemo2.kt
```

演示列表自动生成的结果就是：

```
|-- fragment
  |-- FragmentDemo1
  |-- FragmentDemo2
|-- ui
  |-- ViewDemo1
  |-- ViewDemo2
```

就是这样，你不需要做任何事情。如果不希望自动通过包名生成，你也可以通过 `path` 属性来手动指定示例的路径。例如：

```kotlin
@Register(path = "android/view")
class DemoFragment : Fragment() {
  ...
}
```

## 使用测试用例

想象一下，你有一个庞大的演示应用程序，此时很难找到一个演示。因此，你可能希望在启动应用程序时直接打开它。

解决方案是使用 `@TestCase` 注解，它可以指定一个测试用例，当你启动应用程序时，它会自动打开这个测试用例。如果指定了多个测试用例的情况，会弹出一个弹框让你选择：

```kotlin
@TestCase
@Register
class DemoFragment : Fragment() {
  ...
}
```

## 使用扩展

我们提供了许多扩展的注解，可以轻松地为示例添加额外的功能。

例如，当你使用 `@SampleSourceCode` 注解时，示例应用会自动生成一个 `ViewPager` 并将你的源代码作为一个列表展示：

```kotlin
@SampleSourceCode
@Register
class DemoFragment : Fragment() {
  ...
}
```

目前，示例框架本身提供有以下扩展注解。或者你也可以自己进行扩展，具体可见示例应用。

### @SampleDocument

显示相关文档。例如：

```kotlin
// 你可以指定一个在 assets 目录的文档：
@SampleDocument("assets://sample_document.md")
// 或者你也可以指定一个在线的文档地址：
// @SampleDocument("https://raw.githubusercontent.com/Airsaid/sample-gradle-plugin/master/README.md")
// 或者你也可以指定一个在任意目录的文档，例如下面的代码指定了与源码同级的文档：
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

显示源码文件。例如：

```kotlin
// 默认情况下，将显示当前类所在包下的所有源码类文件：
@SampleSourceCode
// 或者你也可以指定正则表达式来筛选你想要展示的源码文件：
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

显示输出的测试消息。 例如：

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

显示当前的运行内存情况。例如：

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

自动申请指定的权限。例如：

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

- **app**: 示例应用。
- **sample-api**: 库和插件公用的 API。
- **sample-core**: 核心库。支持基础的动作处理器等核心 API。
- **sample-extension**: 扩展库。基于核心库，提供各类扩展功能。
- **sample-plugin**: GradlePlugin，用于在编译期收集注解信息，以及修改字节码文件。

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
