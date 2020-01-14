# Read Me First
This Microservice will be Discovery server for registering all the microservices. 


# Getting Started


### Guides
* Have to set bellow properties in application.properties file else we'll face error (Actually By default DiscoveryServer try to register him self as well)
	
	server.port=8761
	eureka.client.register-with-eureka=false
	eureka.client.fetch-registry=false
* Have add Spring cloud netflix eureka server dependency:

	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
	</dependency>
* For all the spring cloud dependencies we need bellow things in properties tag of pom.xml (it vary from spring boot and spring cloud version to version)
	
	<spring-cloud.version>Hoxton.SR1</spring-cloud.version>
* After dependencies need to add DependencyManagement tag:
	
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
*Now the last thing is to add Annotation to main class
	
	@EnableEurekaServer

* Click this [http://localhost:8761/](http://localhost:8761/) to show detail of eureka client microservices.