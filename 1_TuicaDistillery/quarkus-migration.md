# Quarkus Migration Plan

Migration of TuicaDistillery from Jakarta EE 10 WAR to Quarkus.

## 1. Project Setup

Replace the current `pom.xml` with Quarkus BOM and plugin:

- Parent: `io.quarkus.platform:quarkus-bom` (latest)
- Plugin: `io.quarkus:quarkus-maven-plugin`
- Remove WAR packaging, switch to default JAR
- Remove `failOnMissingWebXml` property
- Set Java 25 source/target via `maven.compiler.release`

### Quarkus Extensions Required

| Extension | Replaces |
|---|---|
| `quarkus-rest` | JAX-RS (`@Path`, `@GET`, `@POST`, `AsyncResponse`, `MessageBodyWriter`) |
| `quarkus-rest-jsonp` | JSON-P (`Json`, `JsonObject`, `JsonArray`) |
| `quarkus-rest-jaxb` | JAXB XML binding (`@XmlRootElement`, `@XmlElement`) |
| `quarkus-hibernate-orm-panache` or `quarkus-hibernate-orm` | JPA (`@Entity`, `EntityManager`, `@NamedQuery`) |
| `quarkus-jdbc-h2` or `quarkus-jdbc-postgresql` | JDBC driver (choose per target DB) |
| `quarkus-scheduler` | EJB timers (`@Schedule`, `@Timeout`, `TimerService`, `ScheduleExpression`) |
| `quarkus-hibernate-validator` | Bean Validation (`@Constraint`, `ConstraintValidator`) |

**Note:** CDI, interceptors, and `@PostConstruct` work out of the box in Quarkus — no extra extension needed.

## 2. EJB Removal

Quarkus does not support EJB. Each EJB annotation must be replaced with a CDI scope.

### 2.1 `@Stateless` → `@ApplicationScoped`

| File | Change |
|---|---|
| `TuicasResource.java` | Remove `@Stateless`, add `@ApplicationScoped` |
| `TuicaService.java` | Remove `@Stateless`, add `@ApplicationScoped`, add `@Transactional` |

`@Transactional` replaces the implicit EJB transaction demarcation in `TuicaService` (boundary layer).

### 2.2 `@Singleton` / `@Startup` → CDI + Scheduler

| File | Change |
|---|---|
| `Burner.java` | Remove `@Singleton`, `@Startup`, `@Timeout`. Add `@ApplicationScoped`. Replace `TimerService` / `ScheduleExpression` / `Timer` with Quarkus `@Scheduled` |

### 2.3 `@Resource ManagedExecutorService` → `@Inject ManagedExecutor`

Replace `jakarta.annotation.Resource` + `jakarta.enterprise.concurrent.ManagedExecutorService` with `@Inject` + `io.smallrye.common.annotation.Blocking` or `org.eclipse.microprofile.context.ManagedExecutor`.

Affected files: `Burner.java`, `TuicaService.java`

## 3. File-by-File Migration

### 3.1 `JAXRSConfiguration.java`

No change needed. Quarkus supports `@ApplicationPath` + `Application` directly.

### 3.2 `TuicaBodyWriter.java`

No change needed. `@Provider` + `MessageBodyWriter` work in Quarkus REST.

### 3.3 `TuicasResource.java`

- Remove `@Stateless`
- Add `@ApplicationScoped`
- `AsyncResponse` / `@Suspended` — replace with Mutiny `Uni<T>` or keep as-is (Quarkus REST supports `AsyncResponse`)
- `@Context UriInfo` — works as-is, alternatively use `@Inject`

### 3.4 `TuicaService.java`

- Remove `@Stateless`
- Add `@ApplicationScoped`
- Add `@Transactional` on class or `store()` method (boundary layer)
- Remove `@Interceptors(PerformanceLogger.class)`, use CDI `@InterceptorBinding` instead
- Replace `@PersistenceContext EntityManager` with `@Inject EntityManager`
- Replace `@Resource ManagedExecutorService` with `@Inject ManagedExecutor`

### 3.5 `Burner.java`

Full rewrite of scheduling:

```java
@ApplicationScoped
public class Burner {

    @Inject
    Distillator d;

    @Scheduled(every = "5s")
    void burn() {
        d.doSomethingExpensive();
    }
}
```

- Remove `TimerService`, `Timer`, `ScheduleExpression`, `@PostConstruct`, `@Timeout`, `@Resource`
- Remove `ManagedExecutorService` (Quarkus scheduler handles threading)

### 3.6 `JobConfigurator.java`

Delete entirely. The `ScheduleExpression` producer is no longer needed — scheduling is configured via `@Scheduled` annotation or `application.properties`.

### 3.7 `Distillator.java`

No change needed. Plain CDI bean.

### 3.8 `PerformanceLogger.java`

- Add a dedicated `@InterceptorBinding` annotation (e.g., `@Logged`)
- Annotate `PerformanceLogger` with `@Interceptor` and `@Logged`
- Replace `@Interceptors(PerformanceLogger.class)` in `TuicaService` with `@Logged`
- Register the interceptor binding (Quarkus auto-discovers `@Interceptor` beans)

### 3.9 `Tuica.java` (Entity)

No change needed. JPA annotations (`@Entity`, `@Id`, `@GeneratedValue`, `@NamedQuery`) and JAXB annotations work as-is.

### 3.10 `Strength.java` / `StrengthValidator.java`

No change needed. Custom `@Constraint` and `ConstraintValidator` work as-is with `quarkus-hibernate-validator`.

### 3.11 `PrincipalProvider.java`

Evaluate necessity. Quarkus injects `SecurityIdentity` directly. If custom `TuicaPrincipal` is still needed, keep the CDI producer but inject `SecurityIdentity` instead of `java.security.Principal`.

### 3.12 `TuicaQualityManagement.java`

- Remove unused `@Resource` import and `ManagedExecutorService` import (currently imported but not used as a field)
- Otherwise no change needed — plain CDI bean

## 4. Configuration Files

### 4.1 Delete

- `src/main/webapp/WEB-INF/beans.xml` — Quarkus discovers beans automatically
- `src/main/resources/META-INF/persistence.xml` — replaced by `application.properties`
- `nb-configuration.xml` — IDE config, not needed

### 4.2 Create `src/main/resources/application.properties`

```properties
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:tuica
quarkus.hibernate-orm.database.generation=drop-and-create
```

## 5. Migration Order

Execute in this sequence — each step should compile before proceeding:

1. **pom.xml** — replace with Quarkus BOM, extensions, and plugin
2. **Delete** `beans.xml`, `persistence.xml` → create `application.properties`
3. **Burner + JobConfigurator** — replace EJB timers with `@Scheduled`, delete `JobConfigurator`
4. **TuicaService** — `@Stateless` → `@ApplicationScoped` + `@Transactional`, fix `EntityManager` injection
5. **TuicasResource** — `@Stateless` → `@ApplicationScoped`
6. **PerformanceLogger** — convert to CDI `@Interceptor` with `@InterceptorBinding`
7. **PrincipalProvider** — evaluate and adapt to Quarkus security model
8. **Verify** — `mvn quarkus:dev` and test all endpoints
