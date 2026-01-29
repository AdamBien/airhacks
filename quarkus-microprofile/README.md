# 🚀Quarkus with MicroProfile on BCE

Banking application with account management using the [Open Banking](https://www.openbanking.org.uk) standard. Manages credits with country-agnostic account identification ([IBAN](https://www.iso.org/standard/81090.html), [IFSC](https://www.rbi.org.in/Scripts/bs_viewcontent.aspx?Id=2009), or any routing scheme).

Built with Quarkus, MicroProfile, JAX-RS, and CDI. BCE-structured with system tests in a standalone module.

BCE-structured 👉 [bce.design](https://bce.design)

## Modules

- [service](service/README.md) - Quarkus application module with BCE structure
- [service-st](service-st/README.md) - System tests for the service module
