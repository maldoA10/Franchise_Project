output "connection_string_srv" {
  description = "MongoDB Atlas SRV connection string"
  value       = mongodbatlas_cluster.franchise_cluster.connection_strings[0].standard_srv
  sensitive   = true
}

output "project_id" {
  description = "MongoDB Atlas Project ID"
  value       = mongodbatlas_project.franchise_project.id
}

output "cluster_name" {
  description = "MongoDB Atlas Cluster name"
  value       = mongodbatlas_cluster.franchise_cluster.name
}