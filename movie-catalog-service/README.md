# Read Me First
This microservice will call two other microservices for movie info and movie rating and then combine them as movie catalog and send as response.

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
* For add application name for Eureka server to display application name and later we'll use these name to call service resources.
* Now need to add @LoadBalance annotation to RestTemplate and after that we can call any service end point wiht calling it's ip and port like bellow:
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId){						
		UserRating ratings = restTemplate.getForObject("http://rating-data-service/ratingsdata/users/"+userId, UserRating.class);		
		return ratings.getUserRating().stream().map(rating-> {
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getMovieName(),"Test",rating.getRating());
			}).collect(Collectors.toList());		
	}
* When we are facing one of microservice giving response very slow then whole application become very slow
* solution for this problem is to set timeouts in restTemplat:
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		// 3000 Milliseconds which is equals to seconds  
		httpComponentsClientHttpRequestFactory.setConnectTimeout(3000);
		return new RestTemplate(httpComponentsClientHttpRequestFactory);
	}
* But above solution will not solve the problem permanently because if frequency of requests are faster than this time out it'll become slow or blast after a while.
* So the permanent solution is to use Circuit Breaker pattern. We should use in every microservice that call an other microservice but it very very important to use this pattern in the service where we are calling more than one microservice.
* In Circuit breaker pattern we have need make microservice smart enough to make circuit break. For this we have list of parameters for set circuit breaker

## Spring cloud Hystrix (Netflix Hystrix Framework)

**When Circuit break:**

1) Timeout for every request to consider the request is failed.
	
	@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000") 

2) How many requests failed (from last n requests).
	
	@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5")

3) Last n requests persentage to break circuit (if they are failed to response).
	
	@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
	
* it means if 50% from last 5 request are failed then make circuit Breaker.

**When Circuit become normal:**

1) How long circuit break waits for trying again.

	@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")

**What should we do when Circuit break**

1) Throw an error [it should be last option]

2) Return a default/hard coded response [better option than throw an error]

3) Cache the last response and send to the request [It's coolest option]

* As I'm using spring cloud so we have to use Netflix Hystrix framework. Even Netflix team also not using Hystrix directly they are using from Spring cloud.

**How to use Spring cloud Hystrix** 
* Add dependency for spring cloud hystrix starter
	
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
	</dependency>
	
* Add @EnableCircuitBreaker to application class.

	@SpringBootApplication
	@EnableEurekaClient
	@EnableCircuitBreaker
	public class MovieCatalogServiceApplication {
		public static void main(String[] args) {
			SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}...
	
* Add @HystrixCommand to specific method that need circuit breaker.
	
	@RequestMapping("/{userId}")
	@HystrixCommand
	public List<CatalogItem> getCatalog(@PathVariable String userId){						
	....		
* In the last configure parameters for Circuit break and fallback mechanism
	
	@HystrixCommand(fallbackMethod = "getFallBackCatalog")
	public List<CatalogItem> getCatalog(@PathVariable String userId){
	...
* Now make one service down Rating service or movie data service then fallback method will called automatically. Then response will be:
	
	[
		{
		"name": "No Movie",
		"description": "",
		"rating": 0
		}
	]
* If any of service down or slow down then it'll response hard coded data but this is not perfect solution so we should make it better to repose with respect to each api like: 
* Method have to divided into two separate methods for each calling microservice (can be failed or slow to make circuit breaker)
* Now every method have his own fall back method. This is better solution then before But it'll not work because it's a proxy class with respect to hystrix.
* Proxy class is a raper class of the instance of api class (Resource class)   
* Hystrix will not work if child methods are in same class so we have to create two seperate service classes and add those method on it:
* Please check RatingService and MovieInfoService in services package.
* Now it's working fine.

## Hystrix Dashboard:
* It's a web application that display what are the different circuit breakers you have in your system.
* What are the circuits are open closed can also display in this dashboard.

#### Setup Hystrix Dashboard:
* Need to add couple of dependencies for hystrix dashbaord.
	
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
	</dependency>
	
	<dependency>
	   <groupId>org.springframework.boot</groupId>
	   <artifactId>spring-boot-starter-actuator</artifactId>
	</dependency>

* Actuator dependency not only for hystrix dashboard, it also provide coulple of others things
* Now have to enable hystrix dashboard from application main class:

	@EnableHystrixDashboard
* Add bellow property into application.properties file:
	
	management.endpoints.web.exposure.include=hystrix.stream
* Now Run movie-catalog-service microservice and then [check this url](http://localhost:8081/hystrix)
* After couple of time movie-catalog-service endpoint please click hystrix moniter button by adding bellow url text field:
	
	http://localhost:8081/actuator/hystrix.stream
* We can either setup in one separate microservice for whole system or every microservice have it's on Hystrix dashbaord.    
* In this dashboard we can show how many circuits are open ,closed and break with time and threads detail as well. 
* Every microservice can have it's on hystrix dashbaord.

### Third way to Handle circuit break is BulkHead Pattern:
* In this pattern we need to create separate thread pools for every method that is calling an other microservice like:

	@HystrixCommand(fallbackMethod = "getFallBackCatalogItem",
			threadPoolKey = "movieInfoPool",
			threadPoolProperties  = {
					@HystrixProperty(name = "coorSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10"),					
					})
	public CatalogItem getCatalogItemByRating_v2(Rating rating) { ... }
	
	@HystrixCommand(fallbackMethod = "getFallBackUserRating",
			threadPoolKey = "ratingServicePool",
			threadPoolProperties  = {
					@HystrixProperty(name = "coorSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10"),					
					})
	public UserRating getUserRating_v2(String userId) { ... }
* Max 20 threads will be execute at a time can be configurable by coorSize.
* And Max Queue thread will be configurable by maxQueueSize.