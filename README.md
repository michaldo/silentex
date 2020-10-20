# silentex

Silentex is a library which helps keep unwanted stacktraces out of test output.
Silentex works with JUnit5 and Logback.

Silentex depends on thread local context - when test is performed in other thread
than a code, it will not work.

Let's assume the code is:
```java
String getHint(int userId) {
  try {
    return hintRepository.findHintForUser(userId);
  } catch (RepositoryException ex) {
    log.error("Failure ", ex);
    return "Keep calm and propagate silentex";
  }
}
```

Let' assume that test make `hintRepository` defective and verifies that
`getHint` returns `"Keep calm..."`.

Side effect is that test output is polluted with stacktrace. A lot of
unwanted stacktraces makes hard to find a problem when **true** exception broke tests.

> silentex does not verify if the exception occurs

## Installation

```xml
<dependency>
  <groupId>io.github.michaldo.silentex</groupId>
  <artifactId>silentex</artifactId>
  <version>1.0</version>
</dependency>
```

## Usage

```java
@Test
@SilentException(RepositoryException.class)
void testHintRepositoryFailure() {
    // given
    when(hintRepository.findHintForUser(1)).thenThrow(RepositoryException.class)
    
    // when then
    assertThat(getHint(1))
      .isEqualTo("Keep calm and propagate silentex");
}
```


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)