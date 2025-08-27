#   Spring Boot Controller Testing - Complete Exercise and Guide

## 📚 **What You'll Learn**
How to create professional, production-ready unit tests for Spring Boot controllers using industry best practices.

## 🎯 **Learning Objectives**
By the end of this lesson, you will be able to:
- Understand the difference between unit testing and integration testing
- Set up MockMvc for testing REST endpoints
- Use Mockito for mocking dependencies
- Write comprehensive tests covering all scenarios
- Follow professional testing patterns and conventions

---

### **Testing Different Parameter Types**

#### **Path Variables**
```java
mockMvc.perform(get("/api/v1/tools/getbyid/{id}", toolId))
    .andExpect(status().isOk());
```

#### **Query Parameters**
```java
mockMvc.perform(delete("/api/v1/tools/deletebyid")
    .param("id", String.valueOf(toolId)))
    .andExpect(status().isNoContent());
```

**Why both?** Real APIs use both patterns - path variables for resource identification, query parameters for optional data.

## 🔧 **Essential Testing Tools & Dependencies**

### **1. Spring Boot Test Starter**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```
**When to use:** Always included in Spring Boot projects for testing support.

### **2. Mockito Extension**
```xml
<!-- Included in spring-boot-starter-test -->
```
**What it does:** Enables mocking and dependency injection in tests.
**When to use:** For unit testing when you need to mock service dependencies.

### **3. JUnit 5 (Jupiter)**
```xml
<!-- Included in spring-boot-starter-test -->
```
**What it does:** Modern testing framework for Java.
**When to use:** For writing and organizing test methods.

---

## 🏗️ **Test Class Structure - Step by Step**

### **Step 1: Basic Class Setup**
```java
@ExtendWith(MockitoExtension.class)  // Enables Mockito
class ToolControllerTest {
    // Test methods will go here
}
```

**Why this annotation?**
- `@ExtendWith(MockitoExtension.class)` tells JUnit to use Mockito for mocking
- This is the modern way (replaces the old `@RunWith`)

### **Step 2: Mock Dependencies**
```java
@Mock
private ToolService toolService;  // Mock the service layer

@InjectMocks
private ToolController toolController;  // Inject mocks into controller
```

**What this does:**
- `@Mock` creates a fake version of `ToolService`
- `@InjectMocks` puts the fake service into the real controller
- This isolates the controller for testing

### **Step 3: Test Infrastructure Setup**
```java
private MockMvc mockMvc;           // For testing HTTP endpoints
private ObjectMapper objectMapper;  // For JSON conversion

@BeforeEach
void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(toolController).build();
    objectMapper = new ObjectMapper();
}
```

**Why this setup?**
- `MockMvc` simulates HTTP requests to your controller
- `ObjectMapper` converts objects to/from JSON
- `@BeforeEach` runs before each test method

---

## 1️⃣ **Writing Your First Test Method and, for this exercise, this is how to format the method names**

### **Test Method Template**
```java
@Test
void methodName_ShouldDoSomething() throws Exception {
    // Arrange - Set up test data and mock behavior - (given) - Build
    // Act - Perform the action (HTTP request) - (when) - Operate
    // Assert - Verify the results - (then) - Check
}
```

### **Example: Testing GET Endpoint**
```java
@Test
void findAll_ShouldReturnAllTools() throws Exception {
    // Arrange
    List<Tool> tools = Arrays.asList(
        new Tool("Hammer", "A heavy tool"),
        new Tool("Screwdriver", "A precision tool")
    );
    when(toolService.getAllTools()).thenReturn(tools);

    // Act & Assert
    mockMvc.perform(get("/api/v1/tools/all"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value("Hammer"))
            .andExpect(jsonPath("$[1].name").value("Screwdriver"));

    verify(toolService).getAllTools();
}
```

---

## 📝 **Test Method Breakdown - The AAA Pattern**

### **1. Arrange (Setup)**
```java
// Arrange
List<Tool> tools = Arrays.asList(
    new Tool("Hammer", "A heavy tool"),
    new Tool("Screwdriver", "A precision tool")
);
when(toolService.getAllTools()).thenReturn(tools);
```
**What you're doing:**
- Creating test data
- Setting up mock behavior ("when X happens, return Y")
- Preparing the test environment

### **2. Act (Execute)**
```java
// Act
mockMvc.perform(get("/api/v1/tools/all"))
```
**What you're doing:**
- Making the actual HTTP request
- Simulating what a real client would do
- Triggering the controller method

### **3. Assert (Verify)**
```java
// Assert
.andExpect(status().isOk())
.andExpect(content().contentType(MediaType.APPLICATION_JSON))
.andExpect(jsonPath("$[0].name").value("Hammer"))
.andExpect(jsonPath("$[1].name").value("Screwdriver"));
```
**What you're doing:**
- Checking HTTP status code
- Verifying response format
- Validating response content

---

## 🔍 **Key Testing Concepts Explained**

### **1. Mocking vs Real Objects**
```java
// ❌ DON'T do this in unit tests
@Autowired
private ToolService realToolService;  // Real service with database

// ✅ DO this in unit tests
@Mock
private ToolService toolService;      // Fake service for testing
```

**Why mock?**
- **Faster tests** (no database calls)
- **Predictable results** (you control what the mock returns)
- **Isolated testing** (only test the controller, not the service)

### **2. HTTP Method Testing**
```java
// GET request
mockMvc.perform(get("/api/v1/tools/all"))

// POST request
mockMvc.perform(post("/api/v1/tools/add")
    .contentType(MediaType.APPLICATION_JSON)
    .content(jsonString))

// PUT request with @RequestParam
mockMvc.perform(put("/api/v1/tools/update")
    .param("id", "1")
    .contentType(MediaType.APPLICATION_JSON)
    .content(jsonString))

// DELETE request
mockMvc.perform(delete("/api/v1/tools/deleteall"))
```

### **3. Response Validation**
```java
// Check HTTP status
.andExpect(status().isOk())           // 200 OK
.andExpect(status().isNoContent())    // 204 No Content
.andExpect(status().isCreated())      // 201 Created

// Check content type
.andExpect(content().contentType(MediaType.APPLICATION_JSON))

// Check JSON content
.andExpect(jsonPath("$.name").value("Hammer"))
.andExpect(jsonPath("$[0].id").value(1))
.andExpect(jsonPath("$").isEmpty())   // Empty array
```

---

## 🎯 **Testing Different Scenarios**

### **1. Success Scenarios**
```java
@Test
void findAll_ShouldReturnAllTools() throws Exception {
    // Test when everything works correctly
    when(toolService.getAllTools()).thenReturn(tools);
    
    mockMvc.perform(get("/api/v1/tools/all"))
            .andExpect(status().isOk());
}
```

### **2. Edge Cases**
```java
@Test
void findAll_WhenNoTools_ShouldReturnEmptyList() throws Exception {
    // Test when no data exists
    when(toolService.getAllTools()).thenReturn(Arrays.asList());
    
    mockMvc.perform(get("/api/v1/tools/all"))
            .andExpect(jsonPath("$").isEmpty());
}
```

### **3. Parameter Handling**
```java
@Test
void deleteById_ShouldDeleteToolById() throws Exception {
    // Test with query parameters
    mockMvc.perform(delete("/api/v1/tools/deletebyid")
            .param("id", "1"))
            .andExpect(status().isNoContent());
}
```

---

## 🚀 **Advanced Testing Techniques**

### **1. JSON Path Expressions**
```java
// $ = root of JSON
.andExpect(jsonPath("$").isArray())           // Root is an array
.andExpect(jsonPath("$").isEmpty())           // Root is empty

// $[0] = first element of array
.andExpect(jsonPath("$[0].name").value("Hammer"))

// $.property = property of root object
.andExpect(jsonPath("$.id").value(1))
.andExpect(jsonPath("$.name").value("Tool"))
```

### **2. Mock Verification**
```java
// Verify the service method was called
verify(toolService).getAllTools();

// Verify with specific parameters
verify(toolService).deleteToolById(1);

// Verify method was called exactly once
verify(toolService, times(1)).getAllTools();

// Verify method was never called
verify(toolService, never()).deleteAllTools();
```

### **3. Exception Testing**
```java
@Test
void updateTool_WhenServiceThrowsException_ShouldReturnError() throws Exception {
    // Arrange
    when(toolService.updateTool(anyInt(), any(Tool.class)))
        .thenThrow(new RuntimeException("Service error"));

    // Act & Assert
    mockMvc.perform(put("/api/v1/tools/update")
            .param("id", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isInternalServerError());
}
```

---

## 📋 **Testing Checklist**

### **Before Writing Tests:**
- [ ] Understand what the controller method does
- [ ] Identify all dependencies (services, repositories)
- [ ] Plan test scenarios (success, failure, edge cases)

### **Writing Tests:**
- [ ] Use descriptive test method names
- [ ] Follow AAA pattern (Arrange, Act, Assert)
- [ ] Mock all external dependencies
- [ ] Test both success and failure scenarios
- [ ] Verify service method calls

### **Test Quality:**
- [ ] Tests are independent (don't rely on each other)
- [ ] Tests are repeatable (same result every time)
- [ ] Tests are fast (under 1 second each)
- [ ] Tests are readable and maintainable

---

## 🎓 **Unit Test**


### **❌ DON'T:**
- Test multiple things in one test method
- Use real databases or external services
- Write tests that depend on each other
- Ignore error scenarios
- Use unclear or generic test names

---

## 🔍 **Common Mistakes to Avoid**

### **1. Testing Implementation Instead of Behavior**
```java
// ❌ DON'T test internal implementation
verify(toolController, times(1)).findAll();

// ✅ DO test the HTTP contract
.andExpect(status().isOk())
.andExpect(jsonPath("$").isArray())
```

### **2. Over-Mocking**
```java
// ❌ DON'T mock everything
@Mock private ToolRepository toolRepository;
@Mock private ToolValidator toolValidator;
@Mock private AuditService auditService;

// ✅ DO mock only what the controller directly uses
@Mock private ToolService toolService;
```

### **3. Testing Framework Instead of Your Code**
```java
// ❌ DON'T test that Spring works
.andExpect(handler().methodName("findAll"))

// ✅ DO test your business logic
.andExpect(jsonPath("$[0].name").value("Hammer"))
```

---

## 🎯 **Your Assignment**

Now it's your turn! Create a test class for a controller with these requirements:

1. **Test all endpoints** (GET, POST, PUT, DELETE)
2. **Cover success scenarios** and edge cases
3. **Use proper mocking** for dependencies
4. **Follow AAA pattern** in every test
5. **Verify service method calls**
6. **Test with meaningful data**

### **Example Controller to Test:**
```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    
    @GetMapping
    public List<Product> getAllProducts() { ... }
    
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) { ... }
    
    @PostMapping
    public Product createProduct(@RequestBody Product product) { ... }
    
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) { ... }
    
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) { ... }
}
```

### **What to Submit:**
- Complete test class with all endpoints tested
- At least 8 test methods
- Both success and edge case scenarios
- Proper mocking and verification
- Clean, readable code following best practices

---

## 🏆 **Success Criteria**

Your test class is successful when:
- ✅ All tests pass
- ✅ All endpoints are covered
- ✅ Edge cases are tested
- ✅ Code follows best practices
- ✅ Tests are readable and maintainable
- ✅ Mocking is used appropriately

---

## 📚 **Additional Resources**

- [Spring Boot Testing Documentation](https://spring.io/guides/gs/testing-web/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [JSONPath Reference](https://github.com/json-path/JsonPath)

---

**Remember:** Good testing is not about quantity, it's about quality. Focus on testing the right things in the right way! 🎯

