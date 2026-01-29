# Project Structure

## Architecture Pattern
BCE (Boundary-Control-Entity) architecture within business components.

## Package Layout
```
ro.tuica.distillery.business/
├── JAXRSConfiguration.java    # JAX-RS application config
├── TuicaBodyWriter.java       # Custom message body writer
├── authenticator/             # Authentication component
│   ├── boundary/              # Principal provider
│   └── entity/                # TuicaPrincipal
├── catalog/                   # Main catalog component
│   ├── boundary/              # TuicasResource (REST), TuicaService (facade)
│   ├── control/               # TuicaQualityManagement (business logic)
│   └── entity/                # Tuica, Strength, StrengthValidator
├── configuration/             # Job configuration
│   └── boundary/              # JobConfigurator
├── distillery/                # Distillation process
│   ├── boundary/              # Burner
│   └── control/               # Distillator
└── monitoring/                # Performance monitoring
    └── boundary/              # PerformanceLogger (interceptor)
```

## BCE Conventions
- **boundary/**: JAX-RS resources, EJB facades, interceptors, CDI producers
- **control/**: Procedural business logic, stateless processing
- **entity/**: JPA entities, value objects, validators

## Resource Paths
- Base: `/resources`
- Tuicas: `/resources/tuicas`
