# Note about fundamentals of Spring Framework

## Ref

- https://docs.spring.io/spring/docs/5.1.3.RELEASE/spring-framework-reference/core.html
- https://www.logicbig.com/tutorials/spring-framework/spring-core.html

## Why do we must change dependency proxy mode when use prototype or request Scope dependencies in Singleton bean ?

- https://www.credera.com/blog/technology-insights/open-source-technology-insights/aspect-oriented-programming-in-spring-boot-part-2-spring-jdk-proxies-vs-cglib-vs-aspectj/
- http://cliffmeyers.com/blog/2006/12/29/spring-aop-cglib-or-jdk-dynamic-proxies.html

Why do we need this annotation in prototype and request bean ?

```java
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
```

Refs:

- https://prasanthnath.wordpress.com/2013/03/21/injecting-a-prototype-bean-into-a-singleton-bean/
- https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-sing-prot-interaction
- https://www.baeldung.com/spring-inject-prototype-bean-into-singleton
- https://www.logicbig.com/tutorials/spring-framework/spring-core/scoped-proxy.html

> 1.5.3. Singleton Beans with Prototype-bean Dependencies
  When you use singleton-scoped beans with dependencies on prototype beans, be aware that dependencies are resolved at instantiation time. Thus, if you dependency-inject a prototype-scoped bean into a singleton-scoped bean, a new prototype bean is instantiated and then dependency-injected into the singleton bean. The prototype instance is the sole instance that is ever supplied to the singleton-scoped bean.
  However, suppose you want the singleton-scoped bean to acquire a new instance of the prototype-scoped bean repeatedly at runtime. You cannot dependency-inject a prototype-scoped bean into your singleton bean, because that injection occurs only once, when the Spring container instantiates the singleton bean and resolves and injects its dependencies. If you need a new instance of a prototype bean at runtime more than once, see Method Injection

## proxyMode ScopedProxyMode.TARGET_CLASS vs ScopedProxyMode.INTERFACE

- https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch08s05.html#aop-pfb-proxy-types
- https://stackoverflow.com/a/52622395/4314131
- https://stackoverflow.com/questions/21759684/interfaces-or-target-class-which-proxymode-should-i-choose

> It's not about which is better. They each have their own feature set. Proxying with interfaces means Spring will use JDK proxies which can only take the target bean's interface types.  Proxying with target class means Spring will use CGLIB proxies which can take the target bean's class and interface types. Use them when appropriate.
> 
> See the [javadoc][1].
> 
> Using [`@SessionAttributes`][2] is not an alternative to session scoped beans. Session attributes are just objects, they are not beans. They don't possess the full lifecycle, injection capabilities, proxying behavior that a bean may have.
> 
> 
>   [1]: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/annotation/ScopedProxyMode.html
>   [2]: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/SessionAttributes.html


> 8.5.5 Proxying classes
  
> What if you need to proxy a class, rather than one or more interfaces?
> 
> Imagine that in our example above, there was no Person interface: 
we needed to advise a class called Person that didn't implement any business interface. 
In this case, you can configure Spring to use CGLIB proxying, rather than dynamic proxies.
Simply set the proxyTargetClass property on the ProxyFactoryBean above to true. While it's best to program to interfaces, 
rather than classes, the ability to advise classes that don't implement interfaces can be useful when working with legacy code. (
In general, Spring isn't prescriptive. While it makes it easy to apply good practices, it avoids forcing a particular approach.)
> 
> If you want to, you can force the use of CGLIB in any case, even if you do have interfaces.
> 
> CGLIB proxying works by generating a subclass of the target class at runtime.
Spring configures this generated subclass to delegate method calls to the original target: 
the subclass is used to implement the Decorator pattern, weaving in the advice.
> 
> CGLIB proxying should generally be transparent to users. However, there are some issues to consider:
> 
>     Final methods can't be advised, as they can't be overridden.
> 
>     You'll need the CGLIB 2 binaries on your classpath; dynamic proxies are available with the JDK.
> 
> There's little performance difference between CGLIB proxying and dynamic proxies.
 As of Spring 1.0, dynamic proxies are slightly faster. However, this may change in the future. 
 Performance should not be a decisive consideration in this case.


> Spring will use either dynamic proxy or cglib to implement AOP.
> 
> Cglib is picked if there is no interface, then it will effectively create a subclass of the target class, and override all methods in the target class. With this way all methods could be cut in, except final and static ones.
> 
> In case the target class is with interface, then Spring might use a dynamic proxy using one of the interface, and apprantly this will only affect the methods declared in the interface.
> 
> Before spring-boot 2.0, dynamic proxy is the default strategy. Now Cglib is the default strategy after spring-boot 2.0. 
> 
> It seems to me spring probably take the dynamic proxy approach in your case. You could add **spring.aop.proxy-target-class: true** in your application.yaml to force use Cglib.
> 
> In case you still have issue, it's better to post more complete code snippet showing how the mothods are invoked.


- https://stackoverflow.com/questions/10664182/what-is-the-difference-between-jdk-dynamic-proxy-and-cglib
- https://stackoverflow.com/questions/39945163/proxymode-scopedproxymode-target-class-vs-scopedproxymode-interface
- https://stackoverflow.com/questions/21759684/interfaces-or-target-class-which-proxymode-should-i-choose/43013315#43013315
- https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch07s06.html
- https://stackoverflow.com/questions/45463757/what-is-interface-based-proxying


> Spring AOP uses either JDK dynamic proxies or CGLIB to create the
> proxy for a given target object. (JDK dynamic proxies are preferred
> whenever you have a choice).
> 
> If the target object to be proxied implements at least one interface
> then a JDK dynamic proxy will be used. All of the interfaces
> implemented by the target type will be proxied. If the target object
> does not implement any interfaces then a CGLIB proxy will be created.
> 
> If you want to force the use of CGLIB proxying (for example, to proxy
> every method defined for the target object, not just those implemented
> by its interfaces) you can do so. However, there are some issues to
> consider:
> 
> final methods cannot be advised, as they cannot be overriden.
> 
> You will need the CGLIB 2 binaries on your classpath, whereas dynamic
> proxies are available with the JDK. Spring will automatically warn you
> when it needs CGLIB and the CGLIB library classes are not found on the
> classpath.
> 
> The constructor of your proxied object will be called twice. This is a
> natural consequence of the CGLIB proxy model whereby a subclass is
> generated for each proxied object. For each proxied instance, two
> objects are created: the actual proxied object and an instance of the
> subclass that implements the advice. This behavior is not exhibited
> when using JDK proxies. Usually, calling the constructor of the
> proxied type twice, is not an issue, as there are usually only
> assignments taking place and no real logic is implemented in the
> constructor.



Spring AOP uses either JDK dynamic proxies or CGLIB to create the proxy for a given target object. (JDK dynamic proxies are preferred whenever you have a choice).

If the target object to be proxied implements at least one interface then a JDK dynamic proxy will be used. All of the interfaces implemented by the target type will be proxied. If the target object does not implement any interfaces then a CGLIB proxy will be created.

If you want to force the use of CGLIB proxying (for example, to proxy every method defined for the target object, not just those implemented by its interfaces) you can do so. However, there are some issues to consider:

- final methods cannot be advised, as they cannot be overriden.
- You will need the CGLIB 2 binaries on your classpath, whereas dynamic proxies are available with the JDK. Spring will automatically warn you when it needs CGLIB and the CGLIB library classes are not found on the classpath.
- The constructor of your proxied object will be called twice. This is a natural consequence of the CGLIB proxy model whereby a subclass is generated for each proxied object. For each proxied instance, two objects are created: the actual proxied object and an instance of the subclass that implements the advice. This behavior is not exhibited when using JDK proxies. Usually, calling the constructor of the proxied type twice, is not an issue, as there are usually only assignments taking place and no real logic is implemented in the constructor.


## Example about using ScopedProxyMode.INTERFACES 

- https://www.logicbig.com/tutorials/spring-framework/spring-core/scoped-interface-based-proxy.html

## Do we has any problems when inject singleton beans into prototype bean ?

- https://www.quora.com/Can-singleton-bean-be-injected-into-prototype-bean


## Spring component scan exclude configuration annotated class

- https://stackoverflow.com/questions/18992880/exclude-component-from-componentscan

```java
@Configuration @EnableSpringConfigured
@ComponentScan(basePackages = {"com.example"}, excludeFilters={
  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=Foo.class)})
public class MySpringConfiguration {}
```