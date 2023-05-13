# JxMetrics3 (JxMetrics v3)

JxMetrics3 is a module that calculates metrics values for elements within Java source code, 
internally using [JxPlatform3](https://github.com/katsuhisamaruyama/jxplatform3).

### Source Code Model 

JxMetrics3 calculates several metric values for a project, package, class, method, and field and stores such information into the following classes: 

* ProjectMetrics - Stores metric information on a project 
* PackageMetrics - Stores metric information on a package 
* ClassMetrics - Stores metric information on a class 
* MethodMetrics - Stores metrics information on a method 
* FieldMetrics - Stores metrics information on a field 

The measurement metrics for each program element are shown [here](<https://github.com/katsuhisamaruyama/jxmetrics/tree/master/org.jtool.jxmetrics/src/main/java/org/jtool/jxmetrics/measurement>).

## Requirement

* JDK 11 
* [Eclipse](https://www.eclipse.org/) 2022-09 (4.25.0) and later 

## License 

[Eclipse Public License 1.0 (EPL-1.0)](<https://opensource.org/licenses/eclipse-1.0.php>) 

## Installation

You can download the latest jar file (`jxmetrics-3.jar`) 
from the [Releases](https://github.com/katsuhisamaruyama/jxmetrics3/releases/latest) page.

Alternatively, you can build a jar file with the Gradle on your own environment. 

```
git clone https://github.com/katsuhisamaruyama/jxmetrics3/
cd jxmetrics3
./gradlew jar shadowJar
```
The jar file (`jxmetrics-3.jar`) exists in the 'build/libs' directory. 
Please deploy it in the directory for library files (e.g., 'lib' or 'libs'), 
and specify the directory as the build path and the runtime classpath under your environment.
When using the Eclipse, see the "Build Path" settings of a project.

## Usage

### As a batch-process application

`jxmetrics-3.jar` is an executable jar file.
The following command calculates several metric values for the source code.

```
java -jar jxmetrics-3.jar -target target_path -name name -output output_file -logging on/off
```
* `-target` - (optional) specifies the path of a target project (default: the current directory) 
* `-name` - (optional) specifies the name of a target project (default: the last folder as the target path)
* `-output`- (optional) specifies the name of the output file (deault: JX-<project_name>-<time_as_long>.xml)
* `-logging` - (optional) displays log messages (default: on)

### Building an application leveraging JxMetrics

JxMetrics3 provides APIs for calculating metric values and exporting the calculated metric values into an XML file. 

```java
String name;     // the name of a target project 
String target;   // the path of a target project 
boolean logging; // whether log messages are displayed 

MetricsManager manager = new MetricsManager();
MetricsStore mstore = manager.calculate(name, target, logging);

String path;     // the name of a file in which the result of analysis is written 
exportXML(mstore, path);

manager.unbuild();
```

It also imports metric values from an XML file.

```java
String path;   // the path of a file output by JxMetrics3. 
MetricsManager manager = new MetricsManager();
MetricsStore mstore = manager.importXML(path);
```

The following code shows all metric values. 

```java
MetricsManager manager = new MetricsManager();
MetricsStore mstore = manager.calculate(project, target, true);

for (ProjectMetrics mproject: mstore.getProjectMetrics()) {
    for (ClassMetrics mclass : mproject.getClasses()) {
        Map<String, Double> metrics = mclass.getMetricValues()
        for (String sort : metrics.keySet()) {
            double value = metrics.get(sort).doubleValue();
            System.out.print("{" + sort + ":" + value + "}");
        }
    }
}

manager.unbuild();
```

## Author

[Katsuhisa Maruyama](http://www.fse.cs.ritsumei.ac.jp/~maru/index.html)
