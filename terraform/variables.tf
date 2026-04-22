variable "mongodb_atlas_public_key" {
  description = "MongoDB Atlas Public API Key"
  type        = string
  sensitive   = true
}

variable "mongodb_atlas_private_key" {
  description = "MongoDB Atlas Private API Key"
  type        = string
  sensitive   = true
}

variable "atlas_org_id" {
  description = "MongoDB Atlas Organization ID"
  type        = string
}

variable "project_name" {
  description = "Name of the MongoDB Atlas project"
  type        = string
  default     = "franchise-api"
}

variable "db_username" {
  description = "Database username"
  type        = string
  default     = "franchise_user"
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}
