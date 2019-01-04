# Documents, notes and books about spring web mvc

## Problem

### Two method has same url pattern but has different parameters

- https://stackoverflow.com/questions/15853035/create-two-method-for-same-url-pattern-with-different-arguments

```java
@RequestMapping(value = "/searchUser", params = "userID")
public String searchUserById(@RequestParam long userID, Model model) {
  // ...
}

@RequestMapping(value = "/searchUser", params = "userName")
public ModelAndView searchUserByName(@RequestParam String userName) {
  // ...
```

### Request param and path variable

- https://stackoverflow.com/questions/7021084/how-do-you-receive-a-url-parameter-with-a-spring-controller-mapping

```java
// http://localhost:8080/sitename/controllerLevelMapping/1?someAttr=6
@RequestMapping("/{someID}")
public @ResponseBody int getAttr(@PathVariable(value="someID") String id, 
                                 @RequestParam String someAttr) {
}
```
### Request param

- https://stackoverflow.com/questions/12296642/is-it-possible-to-have-empty-requestparam-values-use-the-defaultvalue

## Documents

- https://docs.spring.io/spring-framework/docs/4.1.x/spring-framework-reference/html/mvc.html#mvc-ann-controller-advice
- https://www.baeldung.com/java-serial-version-uid
- https://www.jubiliation.net/2017/11/java-serialization-deserialization-and.html
