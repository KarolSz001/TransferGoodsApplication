"# TransferGoodsApplication" 
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)

## General info
Multi-Module Maven Application with Java Modules , still in progress adding new functionality

## Technologies
* Java - version 12
* gson - version 2.8.4
* maven - version 3.6
* Multi-Module Maven 
* TestUnit junit 4.12
* junit-jupiter

## Setup
download, compile and run, in module main file to compile main-1.0-SNAPSHOT-jar-with-dependencies.jar

## Code Examples
return customerProductsMap.entrySet().stream().flatMap(f -> f.getValue().stream()).map(Product::getCategory).collect(Collectors.groupingBy(g -> g))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().size()
                        ))
                        .entrySet()
                        .stream()
                        .sorted(Comparator.comparing(Map.Entry::getValue))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, HashMap::new));

## Features

To-do list:
- more tests



## Status
Project is: _in_progress_"# TransferGoodsApplication" 
"# TransferGoodsApplication" 
