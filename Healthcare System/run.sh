#!/bin/bash

# Set database password
export DB_PASSWORD="bece2018"

# Navigate to project directory

# Clean and compile
mvn clean compile

# Run JavaFX application
mvn javafx:run
