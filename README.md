# Microservices: Fleet Management System with Telematics Features Example

This project is a demo full-stack fleet management application implemented using a microservices architecture. One of its key features is a simulation module that replaces the real devices typically used in such projects (IoT). While the primary goal of this project was to create a reference for microservices and experiment with the tech stack, it can also be practically applied as a fake implementation for developing and testing an IoT backend before deploying an actual fleet and devices (similar to AWS IoT Device Simulator).

## Live Demo

This project is deployed on AWS and available for a try:

https://main.d3u9lg0x3zo2mp.amplifyapp.com/

<p float="left">
  <img src="https://github.com/user-attachments/assets/6bc83742-7d2a-4cac-aa74-96b8c509b135" width="500" />
  <img src="https://github.com/user-attachments/assets/eedcce78-7da0-46bf-9291-47a4cb6b45d2" width="500" />
</p>

## Runtime Architecture

The project can be easily configured to run in various environments using Spring profiles:

- `default` - locally
- `docker` - within a Docker environment
- `kube` - on Kubernetes
- `aws` - deployed to AWS cloud with native stack

Architecture diagram of the AWS deployment:

![architecture_diagram](https://github.com/user-attachments/assets/c46e5661-0bd2-4267-a915-3cd233dacc9b)

For all profiles except `aws`, the implementation of infrastructure services is independent of AWS as a provider

- OAuth2 provider: Keycloak vs. Amazon Cognito
- API gateway: Spring Cloud API Gateway vs. Amazon API Gateway
- Service discovery: Spring Cloud Eureka vs. AWS Cloud Map
- Load balancer: Spring Cloud LoadBalancer vs. Elastic Load Balancing (ALB)
- Message broker: RabbitMQ Docker container vs. Amazon MQ (RabbitMQ)
- RDBMS: PostgreSQL Docker container vs. Amazon RDS

## Business Logic Overview

This project includes three business microservices (APIs) and a frontend:

**vehicle** - This service is responsible for vehicle operations. When a request to add a vehicle is made, it communicates with the simulation module to attach a device.

**simulation** - This module is built around a simulation engine and includes device API and place API. By extending simulation engine provider classes, it enables access to an API related to continuous GIS space and movement within it at a given speed. Place represents a GIS point in this space. The module generates telemetry data, simulating sensor data collection at specific intervals, and sends it to a broker, where it is consumed by the telematics service.

**telematics** - This service consumes telemetry data from the queue and stores it in a NoSQL database (MongoDB). It immediately sends individual device data (geolocation) linked to vehicles for real-time map visualization via WebSocket. Aggregated data for the entire fleet is streamed to the dashboard at regular intervals via WebSocket. It also offers a REST API for working with the collected telemetry.

**frontend** - The frontend provides two main views: a map and a dashboard. The map combines data from the vehicle and place REST APIs and supplements it with telemetry received via WebSocket to create a near-real-time visualization of fleet movements. The dashboard displays average key metrics for the fleet, including the current average speedometer reading, odometer and fuel consumption dynamics.
