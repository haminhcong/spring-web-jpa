# Some note when use Hystrix with Spring Boot framework

## Problem

### We cannot use HystrixCommand annotation when call it from method which is in same class

Ref:

- https://stackoverflow.com/questions/39838173/spring-hystrix-not-triggered-on-inner-methods

 > Javanica uses the HystrixCommandAspect aspect to detect methods annotated with the HystrixCommand annotation, 
 and it seems that the pointcut defined affects only public methods.
 
 - https://stackoverflow.com/questions/48811903/hystrix-fallback-is-not-getting-called-for-invidiual-item-in-collection
 
 > The reason this is not working is because the method with the @HystrixCommand is called from within the same class.
As discussed in the answers to this question, this is a limitation of Spring's AOP.

Example from ref link:

```java
@Service
public class AggregationService {

    @Autowired
    Service1 service1Client;

    @Autowired
    Service2 service2Client;

    private static final String QUERY = "query";
    private static final Logger log = LogManager.getLogger();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //@HystrixCommand(
    //    fallbackMethod = "emptyResult",
    //    groupKey = "aggregation-service",
    //    commandKey = "aggregate")
    public AggregationResponse aggregate(final AggregationParams params)
            throws ApiException, IOException {

        final String query = queryExplain(params);
        final WrapperQueryBuilder wrappedQuery = QueryBuilders.wrapperQuery(query);
        final SearchResponse aggregationResult = searchAggregation(params, wrappedQuery);

        return toDtoResponse(aggregationResult.getAggregations().get(params.getAggregationField().name().toLowerCase()));
    }

    @HystrixCommand(
        fallbackMethod = "emptyAggregationResult",
        groupKey = "aggregation-service",
        commandKey = "searchAggregation")
    private SearchResponse searchAggregation(final AggregationParams params, final WrapperQueryBuilder query) {

        return ... do something with service 2 ....
    }

    //    @HystrixCommand(
    //        fallbackMethod = "rethrowTimeoutException",
    //        groupKey = "aggregation-service",
    //        commandKey = "query-for-aggregation",
    //        ignoreExceptions = TimeoutException.class)
    private String queryExplain(final AggregationParams params) throws ApiException, IOException {
        final String queryAsString = ... do something with service 1 ....
    }

    private String rethrowTimeoutException(final AggregationParams params, final Throwable e) {
        log.error("On Hystrix fallback because of ", e);
        return null;
    }

    private SearchResponse emptyAggregationResult(final AggregationParams params, final WrapperQueryBuilder query, final Throwable e) {
        log.error("On Hystrix fallback because of ", e);
        return null;
    }

}

```

with above code, when `aggregate` method execute, hystrix config in `searchAggregation` will not has any effect, because these methods are in same class.

If we want hystrix on `searchAggregation` method working, we need move `searchAggregation` method to another class and mark it as public method

