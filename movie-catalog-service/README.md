# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.az.movie-catalog-service' is invalid and this project uses 'com.az.moviecatalogservice' instead.

# Getting Started


### Notes:
* For Spring cloud we should add these thinks in pom else we'll face error in pom.xml
* In properties section add this: (Spring-Cloud.version vary from spring boot version to version)
	
	<spring-cloud.version>Hoxton.SR1</spring-cloud.version>

* Spring could netflix Eureka Client Dependency:

	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency>

* Just after dependencies add this:
	
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
* @EnableEurekaClient on main class is optional now, before it was mandatory up to certain version. Now Just add dependency it will work fine. 
* For add application name for Eureka server to display application name for better understanding.
