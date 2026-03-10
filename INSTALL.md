# Prerequisites
## 1. Install Java 21
Download Java 21 from [Oracle's Website](https://www.oracle.com/java/technologies/downloads/#java21), and follow [Oracle's Java Installation Instructions](https://docs.oracle.com/en/java/javase/21/install/overview-jdk-installation.html).

## 2. (Optional) Install Maven
If you decide to install Hadrian by cloning the repository (harder of the two options), you must install Maven in order to use the project's dependencies and build the project. Download a stable version of Maven from [Apache's Website](https://maven.apache.org/download.cgi), and follow [Apache's Maven Installation Tutorial](https://maven.apache.org/install.html).

# Choose An Installation Method
Hadrian can be accessed by either of two ways, either by downloading and running a compiled release of the application (easier), or by cloning the repository and building the project yourself

## Option 1: Download A Compiled Release
1. Travel to [this repository's releases page](https://github.com/NSX2022/Hadrian/releases)
2. Download the `.jar` file from the most recent official release
3. Open your terminal or command prompt, and run the following command:
	```bash
	java -jar hadrian.jar
	```

## Option 2: Clone Repository
1. Download the repository to have access to it's files and access the application:
	```sh
	git clone https://github.com/NSX2022/Hadrian.git
	```
2. Move to the project folder:
	```bash
	cd Hadrian/
	```
3. Install project dependencies:
	```bash
	mvn install
	```
4. Run the newly created `.jar` file:
	```bash
	java -jar out/artifacts/Hadrian_jar/Hadrian.jar
	```
