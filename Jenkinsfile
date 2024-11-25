pipeline {
    agent { 
        label 'Agent_1' 
        }
    stages {
        stage('build') {
            steps {
                echo 'Building the project with Maven'
                sh ''' 
                    mvn --version 
                    mvn clean install 
                    '''
            }
        }
            stage('Deploy to tomcat'){
                steps {
                    echo 'Deploying application to Tomcat'
                    sh ''' cp target/*.war /usr/local/tomcat/webapps/ 
                           catalina.sh run ''' 
                }
            }
    }
}
