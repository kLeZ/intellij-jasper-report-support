## Intellij Jasper Report Support plugin

[![Build](https://github.com/kLeZ/intellij-jasper-report-support/workflows/Build/badge.svg)](https://github.com/kLeZ/intellij-jasper-report-support/actions/workflows/build.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/22531.svg)](https://plugins.jetbrains.com/plugin/22531)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/22531.svg)](https://plugins.jetbrains.com/plugin/22531)

<!-- Plugin description -->
This plugin enables jasper reports viewing and compiling support for IntelliJ IDEA IDE.
It comes with xml syntax support through the official XSD (updated to the latest version),
and includes a "Compile JR XML file" action that helps you produce a jasper report binary report,
which is useful for performance purposes.
<!-- Plugin description end -->

### Installation & Usage 

1. **Install from Marketplace**
   Open `Settings > Plugins > Marketplace` in your IntelliJ IDEA IDE, search for Jasper Report Support and install the plugin.

1. **Build from source**
Clone the repository and run `buildPlugin` gradle task in project root. After that plugin jar file will be generated at `<PROJECT_ROOT>/build/libs` directory
    ```
    git clone https://github.com/kLeZ/intellij-jasper-report-support.git
    cd intellij-jasper-report-support
    gradle buildPlugin
    ```

### Changelog

Detailed changes for each release are documented in the [release notes](https://github.com/kLeZ/intellij-jasper-report-support/releases).

### Contribution

Contributions, issues, and feature requests are welcome.
Feel free to check [issues page](https://github.com/kLeZ/intellij-jasper-report-support/issues) if you want to contribute.

### License
Copyright © 2023 Alessandro 'kLeZ' Accardo ([kLeZ](https://klez.me))  
Copyright © 2019-2023 Chathura Buddhika ([chathurabuddi](http://www.chathurabuddi.lk))  
This project is [MIT](http://opensource.org/licenses/MIT) licensed.
