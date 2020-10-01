# Coding Challenge

This program performs compression and decompression/extraction. There is flexibility to pass maximum size during compression. For example,
if user want to compress file or directory and provided the max size 10MB and if file size is more than provided max size then
the file will be compressed in two different Zip file.

# Technical stacks
- Java 1.8
- Apache maven
- Apache common library

# Build project
We are using maven build manager to package and generate executable jar/application.

To build an executable jar:

1. Install Java 1.8 and Apache maven in your system if not installed
2. Run command to check maven installed: `mvn --version` and `java --version`
3. Clone/copy project source code.
4. Run command to build an executable jar file from project root directory: `mvn clean compile assembly:single`
5. Change directory to `target` where executable jar placed after assembly.
6. See usage section to test the compression.

### Usage
For compression :

``` java -jar Application.jar inputDirectory outputDirectory maxFileSizeInMB [compression_algorithm]```

Note: `[compression_algorithm]` is an optional argument. By default, application using `ZIP` algorithm. For other than
`ZIP` algorithm system will throw an exception.

Example : 

``` java -jar Application.jar src/test/resources/data src/test/resources/compressed/ 5.0 ```

For decompression/extraction :

``` java -jar Application.jar inputDirectory outputDirectory ```

Example :

``` java -jar Application.jar src/test/resources/compressed/ src/test/resources/decompressed/ ```

For validation after decompression:
``` java -jar Application.jar originalWithoutCompressionDirectory compressedOutputDirectory validate ```

Example :

``` java -jar Application.jar src/test/resources/data/ src/test/resources/decompressed/ ``` 
 
### Implementation details

For compressing a directory, this program iterates through all the files and sequentially reads the data, compresses it and writes to the output zip file. It uses a CountingOutputStream to maintain the number of bytes written to the stream. Whenever the size threshold limit is reached, a new zip file is created and written to.

Decompression works similarly, all zipped files are read sequentially in order of when they were created and decompressed file is written to the filesystem. For cases when multiple zip files contain parts of a single file, a mapping of each file's outputstream is maintainted to continue writing to the file's outputstream.

### Test case
To execute test cases and verification

- Run ```mvn clean compile assembly:single``` command which will build the executable jar and run all test cases.
- If you want to verify manually compression/decompression, verify the source directory (`src/test/resources/data`) files with decompressed (`src/test/resources/decompressed/`) directory files.
- or run command using jar: ``` java -jar Application.jar originalWithoutCompressionDirectory compressedOutputDirectory validate ```

- We have provided `UnitTestSuite` test class which will run all test cases

### Future enhancement
Current release/version we are supporting compression algorithm as `ZIP` but in future we can support other compression
algorithms.

To enable new algorithms, 
- user needs to pass one more program argument in existing syntax

  Example :
  
``` java -jar Application.jar src/test/resources/testData src/test/resources/compressedTestDir/ 5.0 <compression_algorithm>```

- For Developer, we have to provide implementation to support new compression algorithm.