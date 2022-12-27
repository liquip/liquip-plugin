# The Liquip Plugin
[![Gradle Build](https://github.com/liquip/liquip-plugin/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/liquip/liquip-plugin/actions/workflows/gradle-build.yml)
[![GitHub Packages](https://github.com/liquip/liquip-plugin/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/liquip/liquip-plugin/actions/workflows/gradle-publish.yml)

Liquip is a paper-only plugin. It adds additional gameplay-features to Minecraft like the ability to
add "custom" items.
The items are not really new materials but rather already existing items that have uniquie features
and may be re-textured.

## Overview

Here is an overview over the possibilities you have with the plugin:

* Additional crafting table for custom items
* Add custom items
    * Set name
    * Set lore
    * Add features
* Add shaped recipes for custom crafting table

## Structure
The project is divided into several Gradle submodules:
```
.
├── api
├── gui
├── paper-bundled
├── paper-core
└── paper-standalone
```
Each submodule has its own purpose.

The `api` submodule contains the code for the API.

The `gui` submodule contains the *old* code for the inventory gui used in the crafting table.
It will be removed in the future.

The `paper-core` submodules implements the parts of the API that are common between the submodules
`paper-bundled` and `paper-standalone`.

The `paper-bundled` submodule is a minimal implementation of the API without any plugin functionality.
It also does not have an implementation for the crafting system.
The module depends on `api`, `gui` and `paper-core`.

The `paper-standalone` submodule contains the code for the Liquip plugin. It implements the crafting
system and propably every other component of the API.
The module depends on `api`, `gui` and `paper-core`.

## TODO

The following things are yet to be done:

* Add shapeless recipes
* Add recipe book

## Feedback

If you got any suggestions you can share them on our
[discord server](https://discord.gg/WfzeWjBpeY).
