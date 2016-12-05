# CSVLoader
Scienaptic's kick ass CSV loader.
CSV are an unfortunate part of every dev's life.
This is an effort to ease pain points of our use case.

It can be used to load CSV to 
 - MySQL
 - H2DB [save it to file system]
 - Postgres

## What it does: 
 - create the parser config for the csv : `CsvParserConfig`[link](../src/main/java/com/scienaptic/csvloader/CsvParserConfig)
 - infer the schema of the CSV file in question
   - `CSVLoader` has internal `Column` type semantics
   - inferred [types](../src/main/scala/com/scienaptic/csvloader/FieldTypes) are one of :
     - BOOLEAN
     - CATEGORY
     - FLOAT
     - SHORT_INT
     - INTEGER
     - LONG_INT
     - LOCAL_DATE
     - LOCAL_DATE_TIME
     - LOCAL_TIME
 - for a supported DB, create ddl and insert queries for the inferred types

As of now, only support for MySQL is added.
Coming soon - H2DB and Postgres support. 

## Why H2DB support?
- No need to "install" anything - just dump it into file system, and point a DB visualizer tool to relevant directory.
- Very easy to get started for small datasets.

## Why Postgres ?
- Mature, well-documented rdbms
- has an open source column store [[Citus Community Edition](https://www.citusdata.com/product/community)]    

### Help required
File bugs, issues in the `Issues` tab.


# License
Unless otherwise specified, each and every component is APL 2.0 licensed.
Please go ahead and use it, go nuts, have fun.

Copyright [2016] [Scienaptic Inc.]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
