# Read Me First
This microservice will be use to provide movie rating and it's Eureka client.

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
* Need to add @LoadBalanced annotation on RestTemplate, it will help us to discover other microservices from Eureka server:
	* It does bunch of things like don't go directly to service by url fetch service from eureka server in a load balancing way.
	
		@Autowired
		@LoadBalanced
		private RestTemplate restTemplate;
* Now in bellow way we'll call service from eureka server by using RestTemplat.
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId){						
		UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId, UserRating.class);		
		return ratings.getUserRating().stream().map(rating-> {
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getMovieName(),"Test",rating.getRating());
			}).collect(Collectors.toList());		
	}
* Please notice above method I'm not calling by services by ip & port I'm calling by every application name that is registered in Discovery server. 