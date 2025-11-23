# Requests4J

**Requests4J** is a simple, elegant HTTP library for Java, inspired by Python's [requests](https://requests.readthedocs.io/) library.

```java
import cn.jeyor1337.requests4j.Requests;
import cn.jeyor1337.requests4j.Response;
import cn.jeyor1337.requests4j.auth.BasicAuth;

Response response = Requests.get("https://httpbin.org/basic-auth/user/pass",
    BasicAuth.of("user", "pass"));
System.out.println(response.getStatusCode());  // 200
System.out.println(response.getHeader("content-type"));  // application/json
System.out.println(response.getText());
```

Requests4J allows you to send HTTP requests extremely easily. There's no need to manually add query strings to your URLs, or to form-encode your POST data.

## Features

Requests4J is ready for the demands of building robust and reliable HTTP-speaking applications:

- Keep-Alive & Connection Pooling
- Sessions with Cookie Persistence
- Basic Authentication
- Automatic Content Decoding
- Automatic JSON Encoding/Decoding
- Connection Timeouts
- Redirect Handling
- Elegant API similar to Python's requests

## Quick Start

### Making a Request

Making a request with Requests4J is very simple:

```java
import cn.jeyor1337.requests4j.Requests;
import cn.jeyor1337.requests4j.Response;

Response response = Requests.get("https://httpbin.org/get");
```

Now, we have a `Response` object called `response`. We can get all the information we need from this object:

```java
System.out.println(response.getStatusCode());  // 200
System.out.println(response.isOk());  // true
```

### Passing Parameters In URLs

You often want to send some sort of data in the URL's query string. Requests4J allows you to provide these arguments as a `Map`:

```java
import java.util.Map;
import java.util.HashMap;

Map<String, String> params = new HashMap<>();
params.put("key1", "value1");
params.put("key2", "value2");

Response response = Requests.get("https://httpbin.org/get", params);
System.out.println(response.getText());
```

### Response Content

We can read the content of the server's response:

```java
Response response = Requests.get("https://httpbin.org/get");
System.out.println(response.getText());
```

Requests4J will automatically decode content from the server. You can also access the raw bytes:

```java
byte[] content = response.getContent();
```

### JSON Response Content

Requests4J has a built-in JSON decoder:

```java
Response response = Requests.get("https://httpbin.org/get");
Map<String, Object> json = response.json();
System.out.println(json.get("url"));
```

Or with a specific class:

```java
MyData data = response.json(MyData.class);
```

### Making POST Requests

Making POST requests is just as easy:

```java
Response response = Requests.post("https://httpbin.org/post");
```

You can send form-encoded data:

```java
Map<String, String> data = new HashMap<>();
data.put("key1", "value1");
data.put("key2", "value2");

Response response = Requests.post("https://httpbin.org/post", data);
```

Or send JSON:

```java
Map<String, Object> json = new HashMap<>();
json.put("name", "John");
json.put("age", 30);

Response response = Requests.postJson("https://httpbin.org/post", json);
```

### Other HTTP Request Types

Requests4J supports all common HTTP methods:

```java
Response response = Requests.put("https://httpbin.org/put", data);
Response response = Requests.delete("https://httpbin.org/delete");
Response response = Requests.head("https://httpbin.org/get");
Response response = Requests.options("https://httpbin.org/get");
Response response = Requests.patch("https://httpbin.org/patch", data);
```

### Custom Headers

If you need to add HTTP headers to a request, you'll need to use a `Session` or create a `Request` object:

```java
import cn.jeyor1337.requests4j.Session;

try (Session session = Requests.session()) {
    session.addHeader("User-Agent", "MyApp/1.0");
    Response response = session.get("https://httpbin.org/get");
}
```

### Authentication

Requests4J supports Basic Authentication:

```java
import cn.jeyor1337.requests4j.auth.BasicAuth;
import cn.jeyor1337.requests4j.Session;

try (Session session = Requests.session()) {
    session.setAuth(BasicAuth.of("user", "password"));
    Response response = session.get("https://httpbin.org/basic-auth/user/password");
}
```

### Sessions

Session objects allow you to persist certain parameters across requests. They also persist cookies across all requests made from the Session instance:

```java
import cn.jeyor1337.requests4j.Session;

try (Session session = Requests.session()) {
    // First request sets a cookie
    session.get("https://httpbin.org/cookies/set?sessioncookie=123456789");

    // Second request sends the cookie
    Response response = session.get("https://httpbin.org/cookies");
    System.out.println(response.getText());
}
```

Sessions can also be used to provide default data to the request methods:

```java
try (Session session = Requests.session()) {
    session.addHeader("X-Custom-Header", "value");
    session.setAuth(BasicAuth.of("user", "pass"));

    // Both requests will use the header and auth
    session.get("https://httpbin.org/get");
    session.get("https://httpbin.org/headers");
}
```

### Timeouts

You can set timeouts for your requests:

```java
try (Session session = Requests.session()) {
    session.setTimeout(5000);  // 5 seconds
    Response response = session.get("https://httpbin.org/delay/10");
}
```

### Error Handling

All request exceptions inherit from `RequestException`:

```java
import cn.jeyor1337.requests4j.exceptions.*;

try {
    Response response = Requests.get("https://httpbin.org/status/404");
    response.raiseForStatus();  // Throws HTTPError for 4xx/5xx
} catch (HTTPError e) {
    System.out.println("HTTP error occurred: " + e.getMessage());
} catch (ConnectionError e) {
    System.out.println("Connection error occurred: " + e.getMessage());
} catch (Timeout e) {
    System.out.println("Request timed out: " + e.getMessage());
} catch (RequestException e) {
    System.out.println("An error occurred: " + e.getMessage());
}
```

## Complete Example

```java
import cn.jeyor1337.requests4j.*;
import cn.jeyor1337.requests4j.auth.BasicAuth;
import cn.jeyor1337.requests4j.exceptions.*;

import java.util.HashMap;
import java.util.Map;

public class Example {
    public static void main(String[] args) {
        try {
            // Simple GET request
            Response response = Requests.get("https://httpbin.org/get");
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Content: " + response.getText());

            // GET with parameters
            Map<String, String> params = new HashMap<>();
            params.put("name", "John");
            params.put("age", "30");
            response = Requests.get("https://httpbin.org/get", params);

            // POST with JSON
            Map<String, Object> json = new HashMap<>();
            json.put("username", "john_doe");
            json.put("email", "john@example.com");
            response = Requests.postJson("https://httpbin.org/post", json);

            // Parse JSON response
            Map<String, Object> responseData = response.json();
            System.out.println("Response: " + responseData);

            // Using Sessions
            try (Session session = Requests.session()) {
                session.addHeader("X-API-Key", "secret123");
                session.setAuth(BasicAuth.of("user", "password"));
                session.setTimeout(10000);

                Response r1 = session.get("https://httpbin.org/get");
                Response r2 = session.post("https://httpbin.org/post", "data");
            }

        } catch (HTTPError e) {
            System.err.println("HTTP error: " + e.getMessage());
        } catch (ConnectionError e) {
            System.err.println("Connection error: " + e.getMessage());
        } catch (Timeout e) {
            System.err.println("Timeout: " + e.getMessage());
        } catch (RequestException e) {
            System.err.println("Request error: " + e.getMessage());
        }
    }
}
```

## API Reference

### Main Classes

- `Requests` - Static methods for making HTTP requests
- `Session` - Persists settings and cookies across requests
- `Response` - Contains server's response to an HTTP request
- `Request` - User-created request object
- `Auth` - Interface for authentication

### Exception Hierarchy

- `RequestException` - Base exception class
  - `HTTPError` - HTTP error occurred (4xx, 5xx)
  - `ConnectionError` - Connection error occurred
    - `ConnectTimeout` - Connection timeout
  - `Timeout` - Request timed out
    - `ReadTimeout` - Read timeout
  - `TooManyRedirects` - Too many redirects
  - `InvalidURL` - Invalid URL
  - `JSONDecodeError` - JSON decode error

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

This is a Java port inspired by the Python [requests](https://github.com/psf/requests) library by Kenneth Reitz.

## Acknowledgments

- Inspired by Python's [requests](https://requests.readthedocs.io/) library
- Original Python requests library by Kenneth Reitz and contributors

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

If you encounter any issues or have questions, please file an issue on the GitHub repository.
