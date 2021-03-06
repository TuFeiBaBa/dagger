# Copyright (C) 2017 The Dagger Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

package(default_visibility = ["//visibility:public"])

package_group(
    name = "src",
    packages = ["//..."],
)

load("//tools:javadoc.bzl", "javadoc_library")

py_test(
    name = "maven_sha1_test",
    srcs = ["maven_sha1_test.py"],
    data = ["WORKSPACE"],
)

java_library(
    name = "dagger_with_compiler",
    exported_plugins = ["//java/dagger/internal/codegen:component-codegen"],
    exports = ["//java/dagger:core"],
)

java_library(
    name = "producers_with_compiler",
    exports = [
        ":dagger_with_compiler",
        "//java/dagger/producers",
    ],
)

android_library(
    name = "android",
    exported_plugins = ["//java/dagger/android/processor:plugin"],
    exports = ["//java/dagger/android"],
)

android_library(
    name = "android-support",
    exports = [
        ":android",
        "//java/dagger/android/support",
    ],
)

load("//tools:jarjar.bzl", "jarjar_library")

jarjar_library(
    name = "shaded_compiler",
    rules_file = "shade_rules.txt",
    deps = [
        "//java/dagger/internal/codegen:base",
        "//java/dagger/internal/codegen:binding",
        "//java/dagger/internal/codegen:processor",
        "//java/dagger/internal/codegen:shared-with-spi",
        "//java/dagger/internal/codegen:validation",
        "//java/dagger/internal/codegen:writing",
        "//java/dagger/model:internal-proxies",
        "@com_google_auto_auto_common//jar",
    ],
)

jarjar_library(
    name = "shaded_compiler_src",
    rules_file = "merge_all_rules.txt",
    deps = [
        "//java/dagger/internal/codegen:libbase-src.jar",
        "//java/dagger/internal/codegen:libbinding-src.jar",
        "//java/dagger/internal/codegen:libprocessor-src.jar",
        "//java/dagger/internal/codegen:libshared-with-spi-src.jar",
        "//java/dagger/internal/codegen:libvalidation-src.jar",
        "//java/dagger/internal/codegen:libwriting-src.jar",
    ],
)

jarjar_library(
    name = "shaded_spi",
    rules_file = "shade_rules.txt",
    deps = [
        "//java/dagger/internal/codegen:shared-with-spi",
        "//java/dagger/model",
        "//java/dagger/spi",
        "@com_google_auto_auto_common//jar",
    ],
)

jarjar_library(
    name = "shaded_spi_src",
    rules_file = "merge_all_rules.txt",
    deps = [
        "//java/dagger/internal/codegen:libshared-with-spi-src.jar",
        "//java/dagger/model:libmodel-src.jar",
        "//java/dagger/spi:libspi-src.jar",
    ],
)

javadoc_library(
    name = "spi-javadoc",
    srcs = [
        "//java/dagger/model:model-srcs",
        "//java/dagger/spi:spi-srcs",
    ],
    root_packages = [
        "dagger.model",
        "dagger.spi",
    ],
    deps = [
        "//java/dagger/model",
        "//java/dagger/spi",
    ],
)

jarjar_library(
    name = "shaded_android_processor",
    rules_file = "shade_rules.txt",
    deps = [
        "//java/dagger/android/processor",
        "@com_google_auto_auto_common//jar",
    ],
)

jarjar_library(
    name = "shaded_grpc_server_processor",
    rules_file = "shade_rules.txt",
    deps = [
        "//java/dagger/grpc/server/processor",
        "@com_google_auto_auto_common//jar",
    ],
)

# coalesced javadocs used for the gh-pages site
javadoc_library(
    name = "user-docs",
    srcs = [
        "//java/dagger:javadoc-srcs",
        "//java/dagger/android:android-srcs",
        "//java/dagger/android/support:support-srcs",
        "//java/dagger/grpc/server:javadoc-srcs",
        "//java/dagger/grpc/server/processor:javadoc-srcs",
        "//java/dagger/model:model-srcs",
        "//java/dagger/producers:producers-srcs",
        "//java/dagger/spi:spi-srcs",
    ],
    android_api_level = 26,
    # TODO(ronshapiro): figure out how to specify the version number for release builds
    doctitle = "Dagger Dependency Injection API",
    exclude_packages = [
        "dagger.internal",
        "dagger.producers.internal",
        "dagger.producers.monitoring.internal",
    ],
    root_packages = ["dagger"],
    deps = [
        "//java/dagger:core",
        "//java/dagger/android",
        "//java/dagger/android/support",
        "//java/dagger/grpc/server",
        "//java/dagger/grpc/server/processor",
        "//java/dagger/model",
        "//java/dagger/producers",
        "//java/dagger/spi",
    ],
)
