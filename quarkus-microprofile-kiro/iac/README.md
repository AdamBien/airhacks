# Infrastructure as Code

OpenShift deployment using Ansible.

## Prerequisites

```bash
# Install Ansible collections
cd iac/ansible
ansible-galaxy collection install -r requirements.yml

# Login to OpenShift
oc login --token=<token> --server=<cluster-url>
```

## Deploy

```bash
# Development
ansible-playbook -i inventory/dev.yml playbooks/deploy.yml

# Production
IMAGE_TAG=v1.0.0 ansible-playbook -i inventory/prod.yml playbooks/deploy.yml
```

## Build Image

```bash
cd ../service
mvn package -DskipTests
podman build -f src/main/docker/Dockerfile.jvm -t qmp-service .
podman tag qmp-service <registry>/<namespace>/qmp-service:<tag>
podman push <registry>/<namespace>/qmp-service:<tag>
```
