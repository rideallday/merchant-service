# Configure Kubernetes provider and connect to the Kubernetes API server
# set `export KUBE_CONFIG_PATH="${HOME}/.kube/config"` for docker desktop k8s
provider "kubernetes" {
  host = "https://kubernetes.docker.internal:6443"
  config_context_auth_info = "docker-desktop"
  config_context_cluster   = "docker-desktop"
}

resource "kubernetes_service" "merchant" {
  metadata {
    name = "merchant"
  }
  spec {
    selector = {
      app = kubernetes_pod.merchant.metadata.0.labels.app
    }
    session_affinity = "ClientIP"
    port {
      port        = 8081
      target_port = 8081
    }

    type = "LoadBalancer"
  }
}

resource "kubernetes_pod" "merchant" {
  metadata {
    name = "merchant"
    labels = {
      app = "merchant"
    }
  }
  spec {
    container {
        image = "merchant"
        name  = "merchant"
        env {
            name  = "SHOPPING_SERVICE_URL_PORT"
            value = "${kubernetes_service.shopping.spec[0].cluster_ip}:${kubernetes_service.shopping.spec[0].port[0].port}"
        }
        image_pull_policy = "IfNotPresent"
          port {
            container_port = 8081
          }
    }
   }
}

resource "kubernetes_service" "shopping" {
  metadata {
    name = "shopping"
  }
  spec {
    selector = {
      app = kubernetes_pod.shopping.metadata.0.labels.app
    }
    session_affinity = "ClientIP"
    port {
      port        = 8082
      target_port = 8082
    }

    type = "LoadBalancer"
  }
}

resource "kubernetes_pod" "shopping" {
  metadata {
    name = "shopping"
    labels = {
      app = "shopping"
    }
  }
  spec {
    container {
        image = "shopping"
        name  = "shopping"
        image_pull_policy = "IfNotPresent"
          port {
            container_port = 8082
          }
    }
  }
}
