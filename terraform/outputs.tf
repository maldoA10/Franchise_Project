output "connection_string" {
  description = "MongoDB Atlas connection string"
  value       = mongodbatlas_cluster.franchise_cluster.connection_strings[0].standard_srv
  sensitive   = true
}

output "project_id" {
  description = "MongoDB Atlas Project ID"
  value       = mongodbatlas_project.franchise_project.id
}
