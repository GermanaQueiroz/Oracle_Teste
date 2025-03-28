pipeline {
    agent {
        label 'docker-host'
    }
    options {
        disableConcurrentBuilds()
        disableResume()
    }

    parameters {
        string name: 'ENVIRONMENT_NAME', trim: true     
        password defaultValue: '', description: 'Password to use for MySQL container - root user', name: 'MYSQL_PASSWORD'
        string name: 'MYSQL_PORT', trim: true  
        choice(name: 'DATABASE_ENGINE', choices: ['mysql', 'postgres', 'oracle'], description: 'Escolha o motor de banco de dados')
        booleanParam(name: 'SKIP_STEP_1', defaultValue: false, description: 'STEP 1 - RE-CREATE DOCKER IMAGE')
    }

    stages {
        stage('Validate Parameters') {
            steps {
                script {
                    // Validar se o valor de MYSQL_PORT está dentro do intervalo válido (1024-65535)
                    if (params.MYSQL_PORT.toInteger() < 1024 || params.MYSQL_PORT.toInteger() > 65535) {
                        error "Port must be between 1024 and 65535"
                    }
                }
            }
        }

        stage('Checkout GIT repository') {
            steps {     
              script {
                git branch: 'master',
                credentialsId: '21f01d09-06da9cc35103',
                url: 'git@mysecret-nonexistent-repo/jenkins.git'
              }
            }
        }

        stage('Create latest Docker image') {
            steps {     
              script {
                if (!params.SKIP_STEP_1){    
                    echo "Building Docker image for $params.DATABASE_ENGINE with name $params.ENVIRONMENT_NAME using port: $params.MYSQL_PORT"
                    sh """
                    sed 's/<PASSWORD>/$params.MYSQL_PASSWORD/g' pipelines/include/create_developer.template > pipelines/include/create_developer.sql
                    """
                    sh """
                    docker build --build-arg DB_TYPE=$params.DATABASE_ENGINE -t $params.ENVIRONMENT_NAME:latest .
                    """
                } else {
                    echo "Skipping STEP1"
                }
              }
            }
        }

        stage('Start new container using latest image and create user') {
            steps {     
              script {
                
                def dateTime = (sh(script: "date +%Y%m%d%H%M%S", returnStdout: true).trim())
                def containerName = "${params.ENVIRONMENT_NAME}_${dateTime}"
                
                if (params.DATABASE_ENGINE == 'mysql') {
                    sh """
                    docker run -itd --name ${containerName} --rm -e MYSQL_ROOT_PASSWORD=$params.MYSQL_PASSWORD -p $params.MYSQL_PORT:3306 $params.ENVIRONMENT_NAME:latest
                    """
                    sh """
                    docker exec ${containerName} /bin/bash -c 'mysql --user="root" --password="$params.MYSQL_PASSWORD" < /docker-entrypoint-initdb.d/create_developer.sql'
                    """
                } else if (params.DATABASE_ENGINE == 'postgres') {
                    sh """
                    docker run -itd --name ${containerName} --rm -e POSTGRES_PASSWORD=$params.MYSQL_PASSWORD -p $params.MYSQL_PORT:5432 $params.ENVIRONMENT_NAME:latest
                    """
                    sh """
                    docker exec ${containerName} /bin/bash -c 'psql --user="postgres" --password="$params.MYSQL_PASSWORD" < /docker-entrypoint-initdb.d/create_developer.sql'
                    """
                } else if (params.DATABASE_ENGINE == 'oracle') {
                    sh """
                    docker run -itd --name ${containerName} --rm -e ORACLE_PASSWORD=$params.MYSQL_PASSWORD -p $params.MYSQL_PORT:1521 $params.ENVIRONMENT_NAME:latest
                    """
                    sh """
                    docker exec ${containerName} /bin/bash -c 'sqlplus / as sysdba @/docker-entrypoint-initdb.d/create_developer.sql'
                    """
                }

                echo "Docker container created: $containerName"
              }
            }
        }
    }
}
