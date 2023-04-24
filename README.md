# JxMetrics3 (JxMetrics v3)

JxMetrics is a module that calculates metrics values for elements within Java source code. 

### Source Code Model 

JxMetrics calculates several metric values for a project, package, class, method, and field and stores such information into the following classes: 

* ProjectMetrics - Stores metric information on a project 
* PackageMetrics - Stores metric information on a package 
* ClassMetrics - Stores metric information on a class 
* MethodMetrics - Stores metrics information on a method 
* FieldMetrics - Stores metrics information on a field 

The measurement metrics are shown [here](<https://github.com/katsuhisamaruyama/jxmetrics/tree/master/org.jtool.jxmetrics/src/main/java/org/jtool/jxmetrics/measurement>).

MetricsManager class provides APIs for calculating metric values. It also exports metric values into an XML file and imports ones from an XML file.

## Requirement

* JDK 11 
* [Eclipse](https://www.eclipse.org/) 2022-09 (4.25.0) and later 

## License 

[Eclipse Public License 1.0 (EPL-1.0)](<https://opensource.org/licenses/eclipse-1.0.php>) 

## Installation

You can download the latest jar file (`jxplatform-3.x.x.jar`) 
from the [Releases](https://github.com/katsuhisamaruyama/jxplatform3/releases/latest) page.

Alternatively, you can build a jar file with the Gradle on your own environment. 

```
git clone https://github.com/katsuhisamaruyama/jxmetrics3/
cd jxmetrics3/org.jtool.jxmetrics
./gradlew jar shadowJar
```
The jar file (`jxmetrics-3.jar`) exists in the 'build/libs' directory. 
Please deploy it in the directory for library files (e.g., 'lib' or 'libs'), 
and specify the directory as the build path and the runtime classpath under your environment.
When using the Eclipse, see the "Build Path" settings of a project.

## Usage

### As a batch-process application

`jxmetrics-3.jar` is an executable jar file.
When you put Java source code (usually expanded Java source files) under the `xxx` folder,
the following command calculates several metric values for the source code and writes the values into an XML file (`xxx-<time>.xml`).

```
java -jar jxmetrics-3.jar [-target target_path] -output output_file [-name name] [-logging on/off]
```
* `-target` - (optional) specifies the path of a target project (default: the current directory) 
* `-output`- (mandatory) specifies the name of a file in which the result of analysis is written 
* `-name` - (optional) specifies the name of a target project 
* `-logging` - (optional) displays log messages (default: on)


### Building an applicatio/plug-in embedding JxMetrics

`MetricsManager` class provides APIs for calculating metric values. It also exports metric values into an XML file and imports ones from an XML file. The following API calls write the measured values into an XML file. 

```java
String target;   // the path of a target project (default: the current directory) 
String output;   // the name of a file in which the result of analysis is written 
String name;     // the name of a target project 
boolean logging; // whether log messages are displayed

MetricsCalculator calculator  = new MetricsCalculator();
List<ProjectMetrics> calculator = getProjectMetrics(target, output, name, logging);
for (ProjectMetrics mproject : mprojects) {
    manager.exportXML(mproject, output);
}
```

The following code imports metrics values from an XML file with a path name.

```
String path;  // The path of an XNL file that contains metrics data

MetricsManager manager = new MetricsManager();
ProjectMetrics mproject = manager.importXML(path);

## Author

[Katsuhisa Maruyama](http://www.fse.cs.ritsumei.ac.jp/~maru/index.html)
