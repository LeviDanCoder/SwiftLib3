// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "KotlinApi",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "KotlinApi",
            targets: ["KotlinApi"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "KotlinApi",
            path: "./KotlinApi.xcframework"
        ),
    ]
)
