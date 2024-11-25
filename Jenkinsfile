pipeline {
    agent { 
        docker { image 'maven:3.9.9-eclipse-temurin-21-alpine' } }
    stages {
        stage('build') {
            steps {
                echo 'Building the project with Maven'
                sh ''' 
                    mvn --version 
                    mvn clean install 
                    '''
            }
            stage('Deploy to tomcat'){
                steps {
                    echo 'Deploying application to Tomcat'
                    sh 'cp target/*.war /usr/local/tomcat/webapps/'
                    sh 'catalina.sh run'
                    
        }
    }
}
