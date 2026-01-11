# Chubb Capstone — Smart Order Inventory Management 

This repository is a compilation of my frontend and backend repositories in one place.

---

## Links for my code repositories

- Frontend: https://github.com/QuantumSoham/smart_order_inventory_mgmt_system_frontend
- Backend: https://github.com/QuantumSoham/smart_order_inventory_mgmt_system_backend
- Config Server: https://github.com/QuantumSoham/inventory-config-server-repo

---

## Architecture & Diagrams — System Overview
<div align="left">

### System design / architecture
<img width="1386" height="843" alt="system-design-architecture" src="https://github.com/user-attachments/assets/3f0de3e2-0d87-416b-b2fb-69330e775467" />


### CI/CD & Deployment diagram
<img width="2393" height="1358" alt="cicd-deployment-diagram" src="https://github.com/user-attachments/assets/d3078ac6-f902-49aa-b235-fa9aa23cb747" />


## Spring AI and Agent Server Architecture

<img width="6018" height="2658" alt="image" src="https://github.com/user-attachments/assets/399af9e5-98ce-456e-9459-41c746f35569" />

## ER Diagrams 
<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/9395d50f-b452-4e94-838b-a5d7d1e3b514" />

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/f2cc6b8a-09a2-4a1f-a4ea-9f9ea5f876d2" />

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/750210a2-3581-4aad-ab63-7b7d237805a4" />


### Sequence diagram (request flows)
<img width="2847" height="6210" alt="sequence-diagram" src="https://github.com/user-attachments/assets/31f4fb48-62b0-4d42-92ca-610d1147979f" />


</div>

---

## Table of contents

- [About](#about)
- [Features](#features)
- [Tech stack](#tech-stack)
- [Screenshots](#screenshots)
- [Local setup](#local-setup)
- [Development & CI/CD](#development--cicd)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## About

This project implements a smart inventory and order management system with the following goals:
- Track inventory across locations
- Automate reorder suggestions and order creation
- Provide a responsive web UI for inventory and order management
- Demonstrate a production-ready architecture with CI/CD and deployment diagrams

---

## Features

- Product and inventory management
- Orders, suppliers, and purchase workflows
- Notifications for low stock
- Role-based access and simple auth (see backend)
- REST API backend and React (or similar) frontend (see repositories linked above)

---

## Tech stack

- Frontend: (see frontend repo) — likely React / TypeScript / CSS framework
- Backend: (see backend repo) — Node/Express or Python/Flask (see backend repo for exact stack)
- Database: PostgreSQL / MongoDB (configured in backend repo)
- CI/CD: Automated pipelines for tests, builds, and deployment (diagram included above)

---

## Screenshots

(Use these sections to show UI screenshots or important flows. Example:)
![App screenshot 1](images/screenshot1.png)
![App screenshot 2](images/screenshot2.png)

---

## Local setup

1. Clone the repositories:
   - Frontend: `git clone https://github.com/QuantumSoham/smart_order_inventory_mgmt_system_frontend.git`
   - Backend: `git clone https://github.com/QuantumSoham/smart_order_inventory_mgmt_system_backend.git`

2. Follow each repo's README for environment setup, installing dependencies, and running locally.

3. If you want to run both services together:
   - Start the backend service and ensure its API is reachable (default port documented in backend README).
   - Start the frontend and point its API base URL to your running backend.

---

## Development & CI/CD

- CI runs: tests, linting, build steps (see CI/CD pipeline in backend/frontend repos).
- Deployment: diagrams above show the intended deployment topology (cloud provider, containerization, load balancer, database, etc.)
- Add workflows or pipeline YAML into `.github/workflows/` in the frontend/backend repos as needed.

---

## Contributing

Contributions are welcome. Suggested workflow:
1. Fork the repository.
2. Create a new branch: `git checkout -b feat/your-feature`
3. Make changes and add tests where applicable.
4. Open a pull request describing your changes.

Please follow the code style and run linters/tests before opening PRs.

---

## License

Specify your license here (e.g., MIT). Example:

MIT © QuantumSoham

---

## Contact

Project lead: QuantumSoham  
Repository: https://github.com/QuantumSoham/chubb-capstone-inventory-mgmt




