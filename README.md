# Equo SDK

The Equo SDK is an open-source set of independent components which allows users to write modern applications, and deploy them across desktop and embedded operating systems, from a single shared codebase.

To render your web-based UIs, Equo SDK uses the high-performance open-source [Equo Chromium](https://github.com/equodev/chromium) browser, which can also be embedded in many existing apps, like Swing applications, JavaFx, or Eclipse RCP.

## Getting Started

If you are interested in creating an Equo app, please visit our [documentation website](https://docs.equo.dev/sdk/2.0.x/getting-started/using.html).

The quickest way to get started is to clone and start using our samples applications, which you can find in this [repo](https://github.com/equodev/sdk-samples/). Both Maven and Gradle are supported.

## Key Features

- Create modern web-based desktop apps with Java/Kotlin and your favorite web framework
- Develop customizable browser-based applications. This provides the ability to create a customized user interface that is similar to a browser (i.e your app may include the address bar, the title bar, and a customizable way to add bookmarks)
- Load existing web applications into a desktop app
- Middleware: intercept incoming browser requests, run custom logic, modify responses
- Asynchronous real-time communication between web UIs and the Java application code
- System tray icons (coming soon)
- Native notifications (coming soon)

## Platforms

Equo currently supports development and distribution on the following platforms:

| Platform           | Versions                                                                                                        |
| :----------------- | :-------------------------------------------------------------------------------------------------------------- |
| Windows            | 7 and above                                                                                                     |
| macOS              | 10.12 and above                                                                                                 |
| Linux              | Ubuntu 16.04 - 18.04, openSUSE 42.3, Red Hat Enterprise Linux 7.4+, Generic Linux                               |

Architectures x86, x86_64, ARM 32/64-bit are supported for Windows and Linux, while x86_64 and ARM 64-bit are supported for macOS.

## Contributing

Contributions to Equo SDK are welcome and highly appreciated. However, before you jump right into it, we would like you to review our [Contribution Guidelines](https://docs.equo.dev/sdk/2.0.x/contributing/contributing.html) to make sure you have a smooth experience contributing to Equo SDK.

## Release Notes

All the release notes for the Equo SDK can be found [here](https://docs.equo.dev/sdk/2.0.x/reference/release-notes/core-sdk.html).

## Licenses

Equo SDK is available under the MIT license.

Equo Chromium, Middleware, and Comm API, are exclusively intended for use in applications developed using the Equo SDK. If you want to embed or integrate Equo Chromium, Middleware, or the Comm API in an existing application (i.e. Eclipse RCP), please [contact us](mailto:support@equo.dev) for a commercial license.

## Support

If you need support for creating an Equo SDK application, migrating from and old legacy app to a modern application, or integrating our Equo Chromium browser into your application, please contact our [support team](mailto:support@equo.dev).
