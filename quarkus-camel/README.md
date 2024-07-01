# quarkus-amq-db

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-amq-db-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Camel XML IO DSL ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/xml-io-dsl.html)): An XML stack for parsing XML route definitions
- Camel File ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/file.html)): Read and write files
- Camel Core ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/core.html)): Camel core functionality and basic Camel languages: Constant, ExchangeProperty, Header, Ref, Simple and Tokenize
- Camel Bindy ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/bindy.html)): Marshal and unmarshal between POJOs on one side and Comma separated values (CSV), fixed field length or key-value pair (KVP) formats on the other side using Camel Bindy
- Camel Jackson ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jackson.html)): Marshal POJOs to JSON and back using Jackson
- Camel Bean ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/bean.html)): Invoke methods of Java beans
- JDBC Driver - MySQL ([guide](https://quarkus.io/guides/datasource)): Connect to the MySQL database via JDBC
- Camel HTTP ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/http.html)): Send requests to external HTTP servers using Apache HTTP Client 5.x
- Camel FTP ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/ftp.html)): Upload and download files to/from SFTP, FTP or SFTP servers
- Camel Log ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/log.html)): Prints data form the routed message (such as body and headers) to the logger
- Camel Attachments ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/attachments.html)): Support for attachments on Camel messages
- Camel CSV ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/csv.html)): Handle CSV (Comma Separated Values) payloads
- Camel JacksonXML ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jacksonxml.html)): Unmarshal an XML payloads to POJOs and back using XMLMapper extension of Jackson
- Camel ActiveMQ ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/activemq.html)): Send messages to (or consume from) Apache ActiveMQ. This component extends the Camel JMS component
- Camel File Watch ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/file-watch.html)): Get notified about file events in a directory using java.nio.file.WatchService
- Camel SQL ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/sql.html)): Perform SQL queries
